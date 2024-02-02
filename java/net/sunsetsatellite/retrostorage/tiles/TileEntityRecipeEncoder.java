package net.sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import net.sunsetsatellite.retrostorage.mod_RetroStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntityRecipeEncoder extends TileEntityNetworkDevice
    implements IInventory
{
    public Map<Integer,Boolean> useMeta = new HashMap<>();

    public TileEntityRecipeEncoder() {
        contents = new ItemStack[10];
        for (int i = 0; i < 9; i++) {
            useMeta.put(i,true);
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
            } else
            {
                continue;
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

    @Override
    public ItemStack getStackInSlotOnClosing(int par1) {
        return null;
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

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    public String getInvName()
    {
        return "Recipe Encoder";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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
                contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);//new ItemStack(nbttagcompound1);
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
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    private ItemStack[] contents;

    public void encodeDisc() {
        ItemStack recipeDisc = getStackInSlot(9);
        if (recipeDisc != null) {
            if (recipeDisc.getItem() instanceof ItemRecipeDisc) {
                ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
                for(int i = 0;i<9;i++) {
                    ItemStack item = getStackInSlot(i);
                    if(item != null) {
                        item = item.copy();
                        item.stackSize = 1;
                        itemList.add(i, item);
                        if(!useMeta.get(i)){
                            item.setItemDamage(-1);
                            mod_RetroStorage.LOGGER.info("disabled meta for item in slot "+i);
                        }
                    } else {
                        itemList.add(i, null);
                    }
                }
                NBTTagCompound nbt = mod_RetroStorage.itemsArrayToNBT(itemList);//DiscManipulator.convertRecipeToNBT(itemList);
                //RetroStorage.printCompound(nbt);
                recipeDisc.stackTagCompound = new NBTTagCompound();
                recipeDisc.stackTagCompound.setCompoundTag("recipe",nbt);
                //ItemStack result = mod_RetroStorage.findRecipeResultFromNBT(nbt);
                //RetroStorage.LOGGER.info(String.valueOf(result));
                /*if(result != null && result.itemID != 0 && result.stackSize != 0){
                    String itemName = StringTranslate.getInstance().translateKey(result.getItemName() + ".name");
                    recipeDisc.tag.setString("name","Recipe Disc: "+result.stackSize+"x "+itemName);
                    recipeDisc.tag.setBoolean("overrideName",true);
                } else {
                    recipeDisc.tag.setString("name","");
                    recipeDisc.tag.setBoolean("overrideName",false);
                }*/
                mod_RetroStorage.LOGGER.info("Encoded!");
            }
        }
    }
}
