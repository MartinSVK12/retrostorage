package sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.util.RecipeTask;
import sunsetsatellite.retrostorage.util.Task;
import sunsetsatellite.retrostorage.util.TickTimer;

import java.util.*;

public class TileEntityAssembler extends TileEntityNetworkDevice
    implements IInventory
{
    public TileEntityAssembler() {
        contents = new ItemStack[10];
        try {
            this.workTimer = new TickTimer(this,this.getClass().getMethod("work"),60,true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSizeInventory()
    {
        return contents.length;
    }

    public boolean isEmpty() {
        for(int i = 0; i < getSizeInventory()-1; i++) {
            if(getStackInSlot(i) != null) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getStackInSlot(int i)
    {
        return contents[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if(contents[i] != null)
        {
            if(contents[i].stackSize <= j)
            {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if(contents[i].stackSize == 0)
            {
                contents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        contents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();

    }

    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Assembler";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = new ItemStack(nbttagcompound1);
            }
        }

    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    @Override
    public void updateEntity() {
        workTimer.tick();
        ArrayList<Class<? extends TileEntity>> tiles = new ArrayList<>();
        tiles.add(TileEntityChest.class);
        connectedTiles = getConnectedTileEntity(tiles);
    }

    public void work(){
        if(network != null){
            if(task == null){
                if(requestCopy == null || requestCopy.size() == 0){
                    requestCopy = network.requestQueue.clone();
                }
                //RetroStorage.LOGGER.info(this+" Accepted task: "+acceptNextTask());
            } else {
                fulfillRequest();
            }
        }
    }

    public void fulfillRequest(){
        if(task != null){
            ArrayList<ItemStack> inputs = null;
            if(task.recipe instanceof RecipeShapeless){
                inputs = new ArrayList<>(((RecipeShapeless) task.recipe).recipeItems);
            }
            if(task.recipe instanceof RecipeShaped){
                inputs = new ArrayList<>();
                Collections.addAll(inputs, ((RecipeShaped) task.recipe).recipeItems);
            }
            if (inputs != null) {
                inputs.removeIf(Objects::isNull);
            }
            if(inputs != null){
                if(network.inventory.hasItems(inputs)){
                    if(network.inventory.removeItems(inputs)){
                        ItemStack result = task.recipe.getRecipeOutput().copy();
                        if(result.stackSize == 0){result.stackSize = 1;}
                        if(result != null){
                            if(network.inventory.addItemStackToInventory(result)){
                                RetroStorage.LOGGER.info(this+" Task fulfilled.");
                                task.completed = true;
                                if(task.parent == null && task.requirementsMet()){
                                    network.requestQueue.remove(task);
                                }
                                task = null;
                            } else {
                                RetroStorage.LOGGER.error(this+" Failed to fulfill task: Failed to add items to network.");
                                if(task.parent == null && task.requirementsMet()){
                                    network.requestQueue.remove(task);
                                }
                                task = null;
                            }
                        } else {
                            RetroStorage.LOGGER.warn(this+" Task produced no result.");
                            if(task.parent == null && task.requirementsMet()){
                                network.requestQueue.remove(task);
                            }
                            task = null;
                        }
                    } else {
                        RetroStorage.LOGGER.error(this+" Failed to fulfill task: failed to remove items from network.");
                        if(task.parent == null && task.requirementsMet()){
                            network.requestQueue.remove(task);
                        }
                        task = null;
                    }
                } else {
                    RetroStorage.LOGGER.error(this+" Failed to fulfill task: Not enough resources!");
                    if(task.parent == null && task.requirementsMet()){
                        network.requestQueue.remove(task);
                    }
                    task = null;
                }
            } else {
                RetroStorage.LOGGER.warn(this+" Task had no inputs.");
                if(task.parent == null && task.requirementsMet()){
                    network.requestQueue.remove(task);
                }
                task = null;
            }
        }
    }

    public boolean acceptNextTask(){
        if(requestCopy.size() > 0){
            Task t = requestCopy.peek();
            if(t instanceof RecipeTask && t.processor == null && t.requirementsMet()){
                boolean success = false;
                for (int i = 0; i < 8; i++) {
                    ItemStack recipeDisc = contents[i];
                    if(recipeDisc != null && recipeDisc.getItem() instanceof ItemRecipeDisc){
                        IRecipe recipe = RetroStorage.findRecipeFromNBT(recipeDisc.tag.getCompoundTag("recipe"));
                        if(recipe.equals(((RecipeTask) t).recipe)){
                            success = true;
                            break;
                        }
                    }
                }
                if(success) {
                    task = (RecipeTask) t;
                    t.processor = this;
                    requestCopy.remove(t);
                    return true;
                } else {
                    requestCopy.remove(t);
                    return false;
                }
            } else {
                requestCopy.remove(t);
                return false;
            }
        }
        return false;
    }

    private ItemStack[] contents;
    public RecipeTask task;
    public TickTimer workTimer;
    public ArrayDeque<Task> requestCopy;
    public HashMap<String,TileEntity> connectedTiles = new HashMap<>();
}
