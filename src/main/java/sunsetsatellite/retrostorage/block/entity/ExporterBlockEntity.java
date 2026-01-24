package sunsetsatellite.retrostorage.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.ScreenActionListener;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.util.Filter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public class ExporterBlockEntity extends NetworkDeviceBlockEntity implements ScreenActionListener {

    public Filter filter = new Filter(9, 0);

    public TickTimer workTimer = new TickTimer(this, this::work, 10, true);
    public int slot = -1;
    public boolean isWhitelist = true;
    public boolean enabled = true;
    public BlockEntity connectedTile;

    @Override
    public void tick() {
        super.tick();
        int side = world.getBlockState(x, y, z).get(HORIZONTAL_FACING).getOpposite().getId();
        connectedTile = Direction.getDirectionFromSide(side).getTileEntity(world, this);
        workTimer.tick();
    }

    public void work() {
        NetworkController con = getController();
        if (con != null && enabled) {
            if (connectedTile != null && !(connectedTile instanceof NetworkDeviceBlockEntity)) {
                if (connectedTile instanceof Inventory inventory) {
                    InventoryWrapper inv = new InventoryWrapper(inventory);
                    if (slot == -1) {
                        Arrays.stream(filter.getInventory(null)).filter(Objects::nonNull).forEach((S) -> {
                            Optional<ItemStack> stack = Optional.ofNullable(con.removeItemFromNetwork(S.itemId, S.getDamage(), null, Math.min(S.count, S.getMaxCount())));
                            AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                            stack.ifPresent(S2 -> leftovers.set(Optional.ofNullable(inv.add(S2))));
                            leftovers.get().ifPresent(con::addItemToNetwork);
                        });
                    } else {
                        ItemStack invStack = inv.get(slot);
                        if (invStack == null) {
                            Arrays.stream(filter.getInventory(null)).filter(Objects::nonNull).findAny().ifPresent((S) -> {
                                Optional<ItemStack> stack = Optional.ofNullable(con.removeItemFromNetwork(S.itemId, S.getDamage(), null, Math.min(S.count, S.getMaxCount())));
                                AtomicReference<Optional<ItemStack>> leftovers = new AtomicReference<>(Optional.empty());
                                stack.ifPresent(S2 -> leftovers.set(Optional.ofNullable(inv.add(slot, S2))));
                                leftovers.get().ifPresent(con::addItemToNetwork);
                            });
                        }
                    }
                }
            }
        }
    }

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
            isWhitelist = !isWhitelist;
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        isWhitelist = nbt.getBoolean("isWhitelist");
        enabled = nbt.getBoolean("enabled");
        slot = nbt.getInt("workSlot");
        filter.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        filter.writeNbt(nbt);
        nbt.putInt("workSlot", slot);
        nbt.putBoolean("isWhitelist", isWhitelist);
        nbt.putBoolean("enabled", enabled);
    }

    @Override
    public String getName() {
        return "container.retrostorage.exporter";
    }
}
