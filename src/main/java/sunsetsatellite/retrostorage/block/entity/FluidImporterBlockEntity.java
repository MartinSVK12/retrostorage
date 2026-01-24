package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.block.FluidHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.ScreenActionListener;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.io.FluidInventoryWrapper;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.util.Filter;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public class FluidImporterBlockEntity extends NetworkDeviceBlockEntity implements ScreenActionListener {

    public Filter filter = new Filter(0, 9);
    public FluidInventoryWrapper wrapper = new FluidInventoryWrapper(filter);

    public TickTimer workTimer = new TickTimer(this, this::work, 10, true);
    public int slot = -1;
    public boolean isWhitelist = true;
    public boolean enabled = true;
    public BlockEntity connectedTile;

    public boolean matchesFilter(FluidStack stack) {
        if (stack == null) return false;
        boolean contains = wrapper.contains(stack.fluid.getFlowingBlock().id);
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
                if (connectedTile instanceof FluidHandler inv) {
                    if (slot == -1) {
                        here:
                        for (int i = 0; i < inv.getFluids(null).length; i++) {
                            FluidStack stack = inv.getFluid(i, null);
                            if (matchesFilter(stack)) {
                                for (CraftingTask task : con.getCurrentTasks()) {
                                    FluidStack leftovers = task.insertFromProcess(stack);
                                    if (leftovers == stack) continue;
                                    inv.setFluid(i, leftovers, null);
                                    break here;
                                }
                                FluidStack leftovers = con.addFluidToNetwork(stack);
                                inv.setFluid(i, leftovers, null);
                                break;
                            }
                        }
                    } else {
                        if (slot >= inv.getFluids(null).length) {
                            return;
                        }
                        FluidStack stack = inv.getFluid(slot, null);
                        if (matchesFilter(stack)) {
                            for (CraftingTask currentTask : con.getCurrentTasks()) {
                                FluidStack leftovers = currentTask.insertFromProcess(stack);
                                if (leftovers == stack) continue;
                                inv.setFluid(slot, leftovers, null);
                                return;
                            }
                            FluidStack leftovers = con.addFluidToNetwork(stack);
                            inv.setFluid(slot, leftovers, null);
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
        return "container.retrostorage.fluidImporter";
    }
}
