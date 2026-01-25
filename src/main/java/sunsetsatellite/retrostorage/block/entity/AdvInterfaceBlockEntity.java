package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.block.BlockEntityInit;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.danygames2014.nyalib.fluid.block.FluidHandler;
import net.danygames2014.nyalib.item.block.ManagedItemHandlerWithInventory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.FluidInventoryWrapper;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.InventoryWrapper;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.api.Processor;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.item.AdvRecipeDiscItem;
import sunsetsatellite.retrostorage.util.ProcessingState;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.modificationstation.stationapi.api.state.property.Properties.HORIZONTAL_FACING;

public class AdvInterfaceBlockEntity extends NetworkDeviceBlockEntity implements ManagedItemHandlerWithInventory, BlockEntityInit, Processor {

    public HashMap<Direction, BlockEntity> connectedTiles = new HashMap<>();
    public InventoryWrapper workingTile;
    public FluidInventoryWrapper workingFluidTile;
    public ProcessNode workingNode;
    public CraftingTask workingTask;

    public AdvInterfaceBlockEntity() {
        for (int i = 0; i < 9; i++) {
            addItemSlot();
        }
    }

    @Override
    public void init(BlockState blockState) {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return canUse(player);
    }

    @Override
    public String getName() {
        return "container.retrostorage.advInterface";
    }

    @Override
    public void tick() {
        super.tick();
        if (world != null && world.isRemote) return;
        if (getController() != null) {
            int side = world.getBlockState(x, y, z).get(HORIZONTAL_FACING).getOpposite().getId();
            BlockEntity be = Direction.getDirectionFromSide(side).getTileEntity(world, this);
            if (!(be instanceof AdvInterfaceBlockEntity)) {
                if (be instanceof Inventory inventory) {
                    workingTile = new InventoryWrapper(inventory);
                }
                if (be instanceof FluidHandler fluidHandler) {
                    workingFluidTile = new FluidInventoryWrapper(fluidHandler);
                }
            }

            if (isInUse() && (workingTile != null || workingFluidTile != null)) {
                for (CraftingProcess.Step step : workingNode.getProcess().steps) {
                    if (step.output && step.type == StackType.ITEM && workingTile != null) {
                        if(step.slot != -1){
                            ItemStack slotStack = workingTile.connected.getStack(step.slot);
                            ItemStack stepStack = step.stack;
                            if (slotStack == null) {
                                continue;
                            }
                            if(stepStack.isItemEqual(slotStack)){
                                ItemStack remainder = workingTask.insertFromProcess(slotStack);
                                workingTile.connected.setStack(step.slot, remainder);
                            }
                        } else {

                        }

                    } else if ((step.output && step.type == StackType.FLUID && workingFluidTile != null)) {
                        if(step.slot != -1) {
                            FluidStack slotStack = workingFluidTile.connected.getFluid(step.slot, null);
                            FluidStack stepStack = step.fluidStack;
                            if (slotStack == null) {
                                continue;
                            }
                            if(stepStack.isFluidEqual(slotStack)){
                                FluidStack remainder = workingTask.insertFromProcess(slotStack);
                                workingFluidTile.connected.setFluid(step.slot, remainder, null);
                            }
                        } else {

                        }
                    }
                }
            }

            if (isInUse() && workingNode.getState() == ProcessingState.FINISHED) {
                workingTask.processor = null;
                workingNode = null;
                workingTask = null;
            }
        }
    }

    @Override
    public List<NetworkCraftable> getCraftables() {
        return getProcesses().stream().map(NetworkCraftable::new).toList();
    }

    public ArrayList<CraftingProcess> getProcesses() {
        ArrayList<CraftingProcess> processes = new ArrayList<>();
        for (ItemStack stack : getInventory(null)) {
            if (stack != null && stack.getItem() instanceof AdvRecipeDiscItem && stack.getStationNbt().contains("disc")) {
                processes.add(new CraftingProcess(stack.getStationNbt().getCompound("disc")));
            }
        }
        return processes;
    }

    @Override
    public boolean isInUse() {
        return workingNode != null && workingTask != null;
    }

    @Override
    public void setFocus(ProcessNode node, CraftingTask task) {
        workingNode = node;
        workingTask = task;
        if (task != null) {
            task.processor = this;
        }
    }

    @Override
    public BlockEntity getConnectedTile() {
        return (BlockEntity) workingTile.connected;
    }

    @Override
    public ProcessNode getWorkingNode() {
        return workingNode;
    }

    @Override
    public CraftingTask getWorkingTask() {
        return workingTask;
    }

    @Override
    public boolean insertItems(ItemStackList items) {
        if (!isInUse() || workingTile == null) return false;
        if (!canInsertItems(items)) return false;
        for (CraftingProcess.Step step : workingNode.getProcess().steps) {
            if (!step.output && step.type == StackType.ITEM) {
                ItemStack slotStack = workingTile.connected.getStack(step.slot);
                ItemStack stepStack = step.stack;
                if (stepStack == null) continue;
                if(slotStack == null || stepStack.isItemEqual(slotStack)){
                    ItemStack removed = items.remove(stepStack.itemId, stepStack.getDamage(), stepStack.count, stepStack.getStationNbt(), false, false);
                    if (removed == null) {
                        return false;
                    }
                    if (slotStack == null) {
                        workingTile.connected.setStack(step.slot, removed);
                    } else {
                        workingTile.connected.getStack(step.slot).count += removed.count;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean canInsertItems(ItemStackList items) {
        boolean can = true;
        if (!isInUse() || workingTile == null) return false;
        for (CraftingProcess.Step step : workingNode.getProcess().steps) {
            if (!step.output && step.type == StackType.ITEM) {
                if(step.slot == -1) return false;
                ItemStack slotStack = workingTile.get(step.slot);
                ItemStack stepStack = step.stack;
                if (slotStack != null) {
                    if (stepStack == null) continue;
                    if (!slotStack.isItemEqual(stepStack)) {
                        can = false;
                        break;
                    } else {
                        ItemStack testStack = items.get(stepStack.itemId, stepStack.getDamage(), stepStack.getStationNbt());
                        if (testStack == null) {
                            can = false;
                            break;
                        }
                        if (testStack.count < stepStack.count) {
                            can = false;
                            break;
                        }
                        if (slotStack.count + stepStack.count > slotStack.getMaxCount()) {
                            can = false;
                            break;
                        }
                    }
                }
            }
        }
        return can;
    }

    @Override
    public boolean insertFluids(FluidStackList items) {
        if (!canInsertFluids(items)) return false;
        if (items.isEmpty()) return true;
        for (CraftingProcess.Step step : workingNode.getProcess().steps) {
            if (!step.output && step.type == StackType.FLUID) {
                if(step.slot == -1) return false;
                FluidStack slotStack = workingFluidTile.connected.getFluid(step.slot, null);
                FluidStack stepStack = step.fluidStack;
                if (stepStack == null) continue;
                FluidStack removed = items.removeById(stepStack.fluid.getFlowingBlock().id, stepStack.amount, false);
                if (removed == null) {
                    return false;
                }
                if(slotStack == null || stepStack.isFluidEqual(slotStack)){
                    if (slotStack == null) {
                        workingFluidTile.connected.setFluid(step.slot, removed, null);
                    } else {
                        workingFluidTile.connected.getFluid(step.slot, null).amount += removed.amount;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean canInsertFluids(FluidStackList items) {
        if (items.isEmpty()) return true;
        boolean can = true;
        if (!isInUse() || workingFluidTile == null) return false;
        for (CraftingProcess.Step step : workingNode.getProcess().steps) {
            if (!step.output && step.type == StackType.FLUID) {
                if(step.slot == -1) return false;
                FluidStack slotStack = workingFluidTile.connected.getFluid(step.slot, null);
                FluidStack stepStack = step.fluidStack;
                if (slotStack != null) {
                    if (stepStack == null) continue;
                    if (!slotStack.isFluidEqual(stepStack)) {
                        can = false;
                        break;
                    } else {
                        FluidStack testStack = items.getById(stepStack.fluid.getFlowingBlock().id);
                        if (testStack == null) {
                            can = false;
                            break;
                        }
                        if (testStack.amount < stepStack.amount) {
                            can = false;
                            break;
                        }
                        if (slotStack.amount + stepStack.amount > workingFluidTile.connected.getFluidCapacity(step.slot, null)) {
                            can = false;
                            break;
                        }
                    }
                }
            }
        }
        return can;
    }
}
