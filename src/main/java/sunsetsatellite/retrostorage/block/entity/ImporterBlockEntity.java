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
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public class ImporterBlockEntity extends NetworkDeviceBlockEntity implements ScreenActionListener {

    public Filter filter = new Filter(9, 0);
    public InventoryWrapper wrapper = new InventoryWrapper(filter);

    public TickTimer workTimer = new TickTimer(this, this::work, 10, true);
    public int slot = -1;
    public boolean isWhitelist = true;
    public boolean enabled = true;
    public BlockEntity connectedTile;

    public boolean matchesFilter(ItemStack stack) {
        if (stack == null) return false;
        boolean contains = wrapper.contains(stack.itemId, stack.getDamage(), stack.getStationNbt());
        return contains == isWhitelist;
    }

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
                if (connectedTile instanceof Inventory inv) {
                    if (slot == -1) {
                        here:
                        for (int i = 0; i < inv.size(); i++) {
                            ItemStack stack = inv.getStack(i);
                            if (matchesFilter(stack)) {
                                for (CraftingTask task : con.getCurrentTasks()) {
                                    ItemStack leftovers = task.insertFromProcess(stack);
                                    if (leftovers == stack) continue;
                                    inv.setStack(i, leftovers);
                                    break here;
                                }
                                ItemStack leftovers = con.addItemToNetwork(stack);
                                inv.setStack(i, leftovers);
                                break;
                            }
                        }
                    } else {
                        if (slot >= inv.size()) {
                            return;
                        }
                        ItemStack stack = inv.getStack(slot);
                        if (matchesFilter(stack)) {
                            for (CraftingTask currentTask : con.getCurrentTasks()) {
                                ItemStack leftovers = currentTask.insertFromProcess(stack);
                                if (leftovers == stack) continue;
                                inv.setStack(slot, leftovers);
                                return;
                            }
                            ItemStack leftovers = con.addItemToNetwork(stack);
                            inv.setStack(slot, leftovers);
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
        return "container.retrostorage.importer";
    }
}
