package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.RetroStorage;

public class TileEntityDigitalTerminal extends TileEntityNetworkDevice implements IInventory {

    public TileEntityDigitalTerminal() {
        contents = new ItemStack[10];
    }

    public int getSizeInventory() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < getSizeInventory() - 1; i++) {
            if (getStackInSlot(i) != null) {
                return false;
            } else {
                continue;
            }
        }
        return true;
    }

    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
        contents = new ItemStack[getSizeInventory()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag CompoundTag1 = (CompoundTag) listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if (j >= 0 && j < contents.length) {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }
    }

    public void writeToNBT(CompoundTag CompoundTag) {
        super.writeToNBT(CompoundTag);
        ListTag listTag = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                contents[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }

        CompoundTag.put("Items", listTag);
    }


    private ItemStack[] contents;

    public void tick() {
        super.tick();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

    }

    @Override
    public String getInvName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 0;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == RetroStorage.mobileTerminal){
            return true;
        }
        return super.canInteractWith(entityplayer);
    }

    @Override
    public void sortInventory() {

    }

    public int page = 0;
    public int pages = 0;
}
