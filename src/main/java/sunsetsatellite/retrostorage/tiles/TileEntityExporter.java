package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.retrostorage.api.INetworkController;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class TileEntityExporter extends TileEntityNetworkDevice
        implements Container, IScreenActionListener {

    public TileEntityExporter() {
        contents = new ItemStack[9];
        this.workTimer = new TickTimer(this, this::work, 10, true);
    }

    @Override
    public int getContainerSize() {
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

    @Override
    public void setItem(int i, ItemStack itemstack) {
        contents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getMaxStackSize()) {
            itemstack.stackSize = getMaxStackSize();
        }
        setChanged();

    }

    @Override
    public String getNameTranslationKey() {
        return "container.retrostorage.itemExporter";
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
    public void readAdditionalData(@NonNull CompoundTag CompoundTag) {

        ListTag listTag = CompoundTag.getList("Items");
        isStocking = CompoundTag.getBoolean("isStocking");
        enabled = CompoundTag.getBoolean("enabled");
        slot = CompoundTag.getInteger("workSlot");
        contents = new ItemStack[getContainerSize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag CompoundTag1 = (CompoundTag) listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if (j < contents.length) {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }

    }

    @Override
    public void writeAdditionalData(@NonNull CompoundTag CompoundTag) {

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
        CompoundTag.putBoolean("isStocking", isStocking);
        CompoundTag.putBoolean("enabled", enabled);
        CompoundTag.put("Items", listTag);

    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

	@Override
    public boolean stillValid(@NonNull Player entityplayer) {
        if (worldObj.getTileEntity(tilePos) != this) {
            return false;
        }
        return entityplayer.distanceToSqr((double) tilePos.x + 0.5D, (double) tilePos.y + 0.5D, (double) tilePos.z + 0.5D) <= 64D;
    }

    @Override
    public void sort() {

    }

    @Override
    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(Container.class);
        connectedTiles = getConnectedTileEntity(tiles);
    }

    public void work() {
        INetworkController controller = getController();
        if(controller != null && enabled){
            for (TileEntity tile : connectedTiles.values()) {
                if (tile != null && !(tile instanceof TileEntityNetworkDevice)) {
                    InventoryWrapper wrapper = new InventoryWrapper((Container) tile);
                    if(slot == -1){
                        Arrays.stream(contents).filter(Objects::nonNull).forEach((S)->{
                            Optional<ItemStack> stack = Optional.ofNullable(controller.removeItemFromNetwork(S.itemID, S.getMetadata(), null, Math.min(S.stackSize,S.getMaxStackSize(wrapper.connected))));
                            AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                            stack.ifPresent(S2 -> leftovers.set(Optional.ofNullable(wrapper.add(S2))));
                            leftovers.get().ifPresent(controller::addItemToNetwork);
                        });
                    } else {
                        ItemStack invStack = wrapper.get(slot);
                        Arrays.stream(contents).filter(Objects::nonNull).findAny().ifPresent((S)->{
                            if(!isStocking || (invStack == null || invStack.stackSize < S.stackSize)){
                                Optional<ItemStack> stack;
                                if(isStocking && invStack != null){
                                    stack = Optional.ofNullable(controller.removeItemFromNetwork(S.itemID, S.getMetadata(), null, Math.min(S.stackSize - invStack.stackSize, S.getMaxStackSize(wrapper.connected))));
                                } else {
                                    stack = Optional.ofNullable(controller.removeItemFromNetwork(S.itemID, S.getMetadata(), null, Math.min(S.stackSize,S.getMaxStackSize(wrapper.connected))));
                                }
                                AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                                stack.ifPresent(S2 -> leftovers.set(Optional.ofNullable(wrapper.add(slot,S2))));
                                leftovers.get().ifPresent(controller::addItemToNetwork);
                            }
                        });
                    }
                }

            }
        }
    }

    private ItemStack[] contents;
    public TickTimer workTimer;
    public int slot = -1;
    public boolean isStocking = true;
    public boolean enabled = true;
    public HashMap<Direction, TileEntity> connectedTiles = new HashMap<>();

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (id == 0) {
            if (slot >= 0) {
                slot--;
            }
        }
        if (id == 1) {
            slot++;
        }
        if (id == 2) {
            isStocking = !isStocking;
        }
    }
}
