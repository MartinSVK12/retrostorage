package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;

import java.util.ArrayList;

public class TileEntityRecipeEncoder extends TileEntity
    implements IInventory
{
    public TileEntityRecipeEncoder() {
        contents = new ItemStack[10];
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
        return "Recipe Encoder";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
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
                    } else {
                        itemList.add(i, null);
                    }
                }
                CompoundTag nbt = RetroStorage.itemsArrayToNBT(itemList);//DiscManipulator.convertRecipeToNBT(itemList);
                //RetroStorage.printCompound(nbt);
                recipeDisc.getData().putCompound("recipe",nbt);
                ItemStack result = RetroStorage.findRecipeResultFromNBT(nbt);
                //RetroStorage.LOGGER.debug(String.valueOf(result));
                if(result != null && result.itemID != 0 && result.stackSize != 0){
                    String itemName = I18n.getInstance().translateKey(result.getItemName() + ".name");
                    recipeDisc.getData().putString("name","Recipe Disc: "+result.stackSize+"x "+itemName);
                    recipeDisc.getData().putBoolean("overrideName",true);
                } else {
                    recipeDisc.getData().putString("name","");
                    recipeDisc.getData().putBoolean("overrideName",false);
                }
                RetroStorage.LOGGER.debug("Encoded!");
            }
        }
    }
}
