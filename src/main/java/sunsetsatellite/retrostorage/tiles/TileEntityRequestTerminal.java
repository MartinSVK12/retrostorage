

package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;

import net.minecraft.core.crafting.legacy.recipe.IRecipe;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityRequestTerminal extends TileEntityNetworkDevice
    implements IInventory
{

    public TileEntityRequestTerminal()
    {
        contents = new ItemStack[37];
        recipeContents = new Object[37];
        saveTimer = new TickTimer(this,this::save,40,true);
    }

    public int getSizeInventory()
    {
        return 37;
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

    public void save(){
        if(network != null){
            if(getStackAmount() == 0){
                int i = 1;
                ArrayList<RecipeEntryCrafting<?,?>> recipes = network.getAvailableRecipes();
                ArrayList<ArrayList<CompoundTag>> processes = network.getAvailableProcesses();
                ArrayList<Object> allCraftables = new ArrayList<>();
                allCraftables.addAll(recipes);
                allCraftables.addAll(processes);
                List<Object> pageCraftables = allCraftables.subList(((page-1)*36),Math.min(allCraftables.size(),page*36));
                for (Object craftable : pageCraftables) {
                    if(craftable instanceof RecipeEntryCrafting<?,?>){
                        setInventorySlotContents(i, (ItemStack) ((RecipeEntryCrafting<?,?>)craftable).getOutput());
                        recipeContents[i] = craftable;
                        i++;
                    } else if (craftable instanceof ArrayList) {
                        setInventorySlotContents(i, RetroStorage.getMainOutputOfProcess((ArrayList<CompoundTag>) craftable));
                        recipeContents[i] = craftable;
                        i++;
                    }
                }
            } else {
                Arrays.fill(contents, null);
                Arrays.fill(recipeContents,null);
                save();
            }
        } else {
            Arrays.fill(contents, null);
            Arrays.fill(recipeContents,null);
        }
    }

    public void tick()
    {
        saveTimer.tick();
        if(network != null && network.drive != null){
            setInventorySlotContents(0, network.drive.virtualDisc);
            this.pages = ((network.getAvailableRecipes().size()+network.getAvailableProcesses().size())/36)+1;
        } else {
            contents = new ItemStack[37];
        }
    }

    public int getFirstOccupiedStack(){
        for(int i1 = 1; i1 < this.contents.length; ++i1) {
            if(this.contents[i1] != null) {
                return i1;
            }
        }

        return -1;
    }

    public int getStackAmount(){
        int i2 = 0;
        for(int i1 = 1; i1 < this.contents.length; ++i1) {
            if(this.contents[i1] != null) {
                i2++;
            }
        }

        return i2;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        /*if(i == 0 && itemstack == null){
            DiscManipulator.clearDigitalInv(this);
        }*/
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
        return "Digital Terminal";
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
            if(j >= 0 && j < contents.length)
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

    public int getAmountOfUsedSlots(){
        int j = 0;
        if(network != null && network.drive != null){
            j = network.drive.virtualDisc.getData().getCompound("disc").getValues().size();

        }
        return j;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(x, y, z) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 8000D;
    }

    @Override
    public void sortInventory() {

    }

    private ItemStack[] contents;
    public Object[] recipeContents;
    private TickTimer saveTimer;
    public int page = 1;
    public int pages = 1;
}
