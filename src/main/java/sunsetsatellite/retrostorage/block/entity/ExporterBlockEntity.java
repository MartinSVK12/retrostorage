package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.math.Direction;
import sunsetsatellite.retrostorage.util.InventoryWrapper;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.TickTimer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ExporterBlockEntity extends NetworkDeviceBlockEntity
        implements Inventory {

    public ExporterBlockEntity() {
        contents = new ItemStack[9];
        this.workTimer = new TickTimer(this, this::work, 10, true);
    }

    public int size() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (ItemStack stack : contents) {
            if (stack != null) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getStack(int i) {
        return contents[i];
    }

    public ItemStack removeStack(int i, int j) {
        if (contents[i] != null) {
            if (contents[i].count <= j) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].split(j);
            if (contents[i].count == 0) {
                contents[i] = null;
            }
            markDirty();
            return itemstack1;
        } else {
            return null;
        }
    }

    public void setStack(int i, ItemStack itemstack) {
        contents[i] = itemstack;
        if (itemstack != null && itemstack.count > getMaxCountPerStack()) {
            itemstack.count = getMaxCountPerStack();
        }
        markDirty();

    }

    public int containsItem(int itemId, int itemDamage) {
        for (int i2 = 0; i2 < this.contents.length; ++i2) {
            if (this.contents[i2] != null && this.contents[i2].itemId == itemId && this.contents[i2].getDamage() == itemDamage) {
                return i2;
            }
        }

        return -1;
    }

    public void markDirty() {

    }

    public String getName() {
        return "Exporter";
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        NbtList listTag = tag.getList("Items");
        isWhitelist = tag.getBoolean("isWhitelist");
        enabled = tag.getBoolean("enabled");
        slot = tag.getInt("workSlot");
        contents = new ItemStack[size()];
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound tag1 = (NbtCompound) listTag.get(i);
            int j = tag.getByte("Slot") & 0xff;
            if (j < contents.length) {
                contents[j] = new ItemStack(tag1);
            }
        }

    }

    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        NbtList listTag = new NbtList();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                NbtCompound tag1 = new NbtCompound();
                tag1.putByte("Slot", (byte) i);
                contents[i].writeNbt(tag1);
                listTag.add(tag1);
            }
        }

        tag.putInt("workSlot", slot);
        tag.putBoolean("isWhitelist", isWhitelist);
        tag.putBoolean("enabled", enabled);
        tag.put("Items", listTag);

    }

    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity entityplayer) {
        return super.canPlayerUse(entityplayer);
    }



    @Override
    public void tick() {
        super.tick();
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(Inventory.class);
        connectedTiles = getConnectedBlockEntity(tiles);
    }

    public void work() {
        NetworkController controller = getController();
        if(controller != null && enabled){
            for (BlockEntity tile : connectedTiles.values()) {
                if (tile != null && !(tile instanceof NetworkDeviceBlockEntity)) {
                    InventoryWrapper wrapper = new InventoryWrapper((Inventory) tile);
                    if(slot == -1){
                        Arrays.stream(contents).filter(Objects::nonNull).forEach((S)->{
                            Optional<ItemStack> stack = Optional.ofNullable(controller.removeItemFromNetwork(S.itemId, S.getDamage(), null, Math.min(S.count,S.getMaxCount())));
                            AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                            stack.ifPresent(S2 -> leftovers.set(Optional.ofNullable(wrapper.add(S2))));
                            leftovers.get().ifPresent(controller::addItemToNetwork);
                        });
                    } else {
                        ItemStack invStack = wrapper.get(slot);
                        if(invStack == null){
                            Arrays.stream(contents).filter(Objects::nonNull).findAny().ifPresent((S)->{
                                Optional<ItemStack> stack = Optional.ofNullable(controller.removeItemFromNetwork(S.itemId, S.getDamage(), null, Math.min(S.count,S.getMaxCount())));
                                AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                                stack.ifPresent(S2 -> leftovers.set(Optional.ofNullable(wrapper.add(slot,S2))));
                                leftovers.get().ifPresent(controller::addItemToNetwork);
                            });
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
    public HashMap<Direction, BlockEntity> connectedTiles = new HashMap<>();
}
