package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.RecipeTask;
import sunsetsatellite.retrostorage.util.Task;
import sunsetsatellite.sunsetutils.util.TickTimer;

import java.util.*;

public class TileEntityAssembler extends TileEntityNetworkDevice
    implements IInventory
{
    public TileEntityAssembler() {
        contents = new ItemStack[9];
        this.workTimer = new TickTimer(this,"work",10,true);
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

    public void readFromNBT(CompoundTag CompoundTag)
    {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < listTag.tagCount(); i++)
        {
            CompoundTag CompoundTag1 = (CompoundTag)listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if(j < contents.length)
            {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }

    }

    public void writeToNBT(CompoundTag CompoundTag)
    {
        super.writeToNBT(CompoundTag);
        ListTag listTag = new ListTag();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte)i);
                contents[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }

        CompoundTag.put("Items", listTag);
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
        return entityplayer.distanceToSqr((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    @Override
    public void updateEntity() {
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(TileEntityChest.class);
        connectedTiles = getConnectedTileEntity(tiles);
    }

    public void work(){
        if(network != null){
            if(task == null){
                if(stack == null || stack.isEmpty()){
                    stack = network.requestQueue.clone();
                }
                if(!network.requestQueue.isEmpty()){
                    boolean result = acceptNextTask();
                    if(result){
                        RetroStorage.LOGGER.debug(this+" Accepted task "+task+".");
                    }
                }
            } else {
                fulfillRequest();
            }
        }
    }

    public void fulfillRequest(){
        if(task != null){
            ArrayList<ItemStack> inputs = RetroStorage.condenseItemList(RetroStorage.getRecipeItems(task.recipe));
            if (network.inventory.hasItems(inputs)) {
                if (network.inventory.removeItems(inputs)) {
                    ItemStack result = task.recipe.getRecipeOutput().copy();
                    result.getData().setName("Data");
                    if (result.stackSize == 0) {
                        result.stackSize = 1;
                    }
                    if (network.inventory.addItemStackToInventory(result)) {
                        RetroStorage.LOGGER.debug(this + " Task fulfilled.");
                        task.completed = true;
                        network.requestQueue.remove(task);
                        if (task.parent == null && task.requirementsMet()) {
                            RetroStorage.LOGGER.debug(this + " Request fulfilled.");
                        }
                        task = null;
                    } else {
                        failTask(task,"Failed to add items to the network.");
                    }
                } else {
                    failTask(task,"Failed to remove items from the network.");
                }
            } else {
                if(task.attempts <= 0){
                    RetroStorage.LOGGER.error("Too many failed attempts, cancelling task!");
                    network.requestQueue.clone().forEach((V)->{
                        if(V.requires.contains(task) || V.parent == task){
                            network.requestQueue.remove(V);
                        }
                    });
                    network.requestQueue.remove(task);
                    task.processor = null;
                    task = null;
                    return;
                }
                RetroStorage.LOGGER.debug("Not enough resources for task "+task+", attempting to create subtasks..");
                ArrayList<Task> subtasks = network.getSubtask(task);
                if(subtasks == null){
                    RetroStorage.LOGGER.error("No subtasks could be created for "+task+"!");
                    network.requestQueue.remove(task);
                    task.processor = null;
                    task = null;
                } else {
                    if(subtasks.isEmpty()){
                        task.attempts -= 1;
                    }
                    for (Task subtask : subtasks) {
                        if(task != null){
                            task.requires.add(subtask);
                        }
                        network.requestQueue.addFirst(subtask);
                        if(task != null){
                            task.processor = null;
                            task = null;
                        }
                    }
                }
            }
        }
    }

    public void failTask(Task task, String message){
        RetroStorage.LOGGER.error(this + " Failed to fulfill task " + task + ": "+message);
        network.requestQueue.remove(task);
        this.task.processor = null;
        this.task = null;
    }

    public ArrayList<IRecipe> getRecipes(){
        ArrayList<IRecipe> recipes = new ArrayList<>();
        for (ItemStack stack : contents) {
            if (stack != null && stack.getItem() == RetroStorage.recipeDisc) {
                IRecipe recipe = RetroStorage.findRecipeFromNBT(stack.getData().getCompound("recipe"));
                if (recipe != null) {
                    recipes.add(recipe);
                }
            }
        }
        return !recipes.isEmpty() ? recipes : null;
    }

    public boolean acceptNextTask() {
        if(!stack.isEmpty()){
            Task t = stack.pop();
            boolean success = false;
            if (t instanceof RecipeTask) {
                if (t.processor == null) {
                    if (!t.completed) {
                        if (t.requirementsMet()) {
                            for (IRecipe aRecipe : network.getAvailableRecipes()) {
                                if (aRecipe.equals(((RecipeTask) t).recipe)) {
                                    success = true;
                                    break;
                                }
                            }
                            if (success) {
                                this.task = (RecipeTask) t;
                                t.processor = this;
                                return true;
                            } else {
                                //RetroStorage.LOGGER.error(this+" Network doesn't know how to handle task! "+t);
                                return false;
                            }
                        } else {
                            //RetroStorage.LOGGER.error(this+" Not all requirements met for tasks! "+t);
                            return false;
                        }
                    } else {
                        //RetroStorage.LOGGER.error(this+ " Task is already completed! "+t);
                        return false;
                    }
                } else {
                    //RetroStorage.LOGGER.error(this + " Task is taken already! "+t);
                    return false;
                }
            } else {
                acceptNextTask();
            }
        }
        return false;
    }


    private ItemStack[] contents;
    public RecipeTask task;
    public TickTimer workTimer;
    public ArrayDeque<Task> stack;
    public HashMap<String, TileEntity> connectedTiles = new HashMap<>();
}
