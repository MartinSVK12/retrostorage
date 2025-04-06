package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.util.INetworkController;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityImporter extends TileEntityNetworkDevice
        implements Container {
    public TileEntityImporter() {
        contents = new ItemStack[9];
        this.workTimer = new TickTimer(this, this::work, 10, true);
    }

    @Override
    public int getContainerSize() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize() - 1; i++) {
            if (getItem(i) != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return contents[i];
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if (contents[i] != null) {
            if (contents[i].stackSize <= j) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                setChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if (contents[i].stackSize == 0) {
                contents[i] = null;
            }
            setChanged();
            return itemstack1;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        contents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getMaxStackSize()) {
            itemstack.stackSize = getMaxStackSize();
        }
        setChanged();

    }

    @Override
    public String getNameTranslationKey() {
        return "container.retrostorage.itemImporter";
    }

    public int getInventorySlotContainItem(int itemID, int itemDamage) {
        for (int i2 = 0; i2 < this.contents.length; ++i2) {
            if (this.contents[i2] != null && this.contents[i2].itemID == itemID && this.contents[i2].getMetadata() == itemDamage) {
                return i2;
            }
        }

        return -1;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
        isWhitelist = CompoundTag.getBoolean("isWhitelist");
        enabled = CompoundTag.getBoolean("enabled");
        slot = CompoundTag.getInteger("workSlot");
        contents = new ItemStack[getContainerSize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag CompoundTag1 = (CompoundTag) listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if (j >= 0 && j < contents.length) {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }

    }

    @Override
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

        CompoundTag.putInt("workSlot", slot);
        CompoundTag.putBoolean("isWhitelist", isWhitelist);
        CompoundTag.putBoolean("enabled", enabled);
        CompoundTag.put("Items", listTag);

    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        if (worldObj.getTileEntity(x, y, z) != this) {
            return false;
        }
        return entityplayer.distanceToSqr((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }

    @Override
    public void sortContainer() {

    }

    @Override
    public void tick() {
        if (worldObj != null && worldObj.isClientSide) return;
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(Container.class);
        connectedTiles = getConnectedTileEntity(tiles);
    }

    public boolean matchesFilter(ItemStack stack){
        if(stack == null) return false;
        return (getInventorySlotContainItem(stack.itemID,stack.getMetadata()) != -1 && isWhitelist) || (getInventorySlotContainItem(stack.itemID, stack.getMetadata()) == -1 && !isWhitelist);
    }

    public void work() {
        INetworkController controller = getController();
        if(controller != null && enabled){
            for (TileEntity tile : connectedTiles.values()) {
                if (tile != null && !(tile instanceof TileEntityNetworkDevice)) {
                    Container inv = (Container) tile;
                    if(slot == -1){
                        here:
                        for (int i = 0; i < inv.getContainerSize(); i++) {
                            ItemStack stack = inv.getItem(i);
                            if(matchesFilter(stack)){
                                for (CraftingTask currentTask : controller.getCurrentTasks()) {
                                    ItemStack leftovers = currentTask.insertFromProcess(stack);
                                    if(leftovers == stack) continue;
                                    inv.setItem(i, leftovers);
                                    break here;
                                }
                                ItemStack leftovers = controller.addItemToNetwork(stack);
                                inv.setItem(i, leftovers);
                                break;
                            }
                        }
                    } else {
                        if (slot >= inv.getContainerSize()) {
                            return;
                        }
                        ItemStack stack = inv.getItem(slot);
                        if(matchesFilter(stack)){
                            for (CraftingTask currentTask : controller.getCurrentTasks()) {
                                ItemStack leftovers = currentTask.insertFromProcess(stack);
                                if(leftovers == stack) continue;
                                inv.setItem(slot, leftovers);
                                return;
                            }
                            ItemStack leftovers = controller.addItemToNetwork(stack);
                            inv.setItem(slot, leftovers);
                        }
                    }
                }
            }
        }
    }

    private ItemStack[] contents;
    public TickTimer workTimer;
    public int slot = -1;
    public boolean isWhitelist = true;
    public boolean enabled = true;
    public HashMap<Direction, TileEntity> connectedTiles = new HashMap<>();
}
