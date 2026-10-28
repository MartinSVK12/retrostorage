package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.FluidStackList;
import sunsetsatellite.retrostorage.api.IProcessor;
import sunsetsatellite.retrostorage.util.ProcessingState;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.List;

public class TileEntitySubinterface extends TileEntityNetworkDevice implements IProcessor, IScreenActionListener {
	public Container workingTile;
	public IFluidInventory workingFluidTile;
	public ProcessNode workingNode;
	public CraftingTask workingTask;
	public int id = 1;

	@Override
	public void writeAdditionalData(@NotNull CompoundTag compoundTag) {
		super.writeAdditionalData(compoundTag);
		compoundTag.putInt("id",id);
	}

	@Override
	public void readAdditionalData(@NotNull CompoundTag compoundTag) {
		super.readAdditionalData(compoundTag);
		id = compoundTag.getInteger("id");
	}

	@Override
	public void tick() {
		super.tick();
		if (worldObj != null && worldObj.isClientSide) return;
		if (worldObj != null && network != null && getController() != null) {
			TileEntity tileEntity = Direction.getDirectionFromSide(getBlockMeta()).getTileEntity(worldObj, this);
			if (tileEntity instanceof Container && !(tileEntity instanceof TileEntityAdvInterface)) {
				workingTile = (Container) tileEntity;
			}
			if (tileEntity instanceof IFluidInventory && !(tileEntity instanceof TileEntityAdvInterface)) {
				workingFluidTile = (IFluidInventory) tileEntity;
			}

			if (isInUse() && (workingTile != null || workingFluidTile != null)) {
				for (CraftingProcess.Step step : workingNode.getProcess().steps) {
					if(step.subinterface != getId()) continue;
					if (step.output && step.type == StackType.ITEM && workingTile != null) {
						if(step.slot == -1) {
							for (int i = 0; i < workingTile.getContainerSize(); i++) {
								insertItemFromSlot(step.stack, i);
							}
							continue;
						}
						insertItemFromSlot(step.stack, step.slot);
					} else if ((step.output && step.type == StackType.FLUID && workingFluidTile != null)) {
						if(step.slot == -1) {
							for (int i = 0; i < workingFluidTile.getFluidInventorySize(); i++) {
								insertFluidFromSlot(step.fluidStack, i);
							}
							continue;
						}
						insertFluidFromSlot(step.fluidStack, step.slot);
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

	public void insertFluidFromSlot(FluidStack fluidStack, int slot) {
		FluidStack slotStack = workingFluidTile.getFluidInSlot(slot);
		if (slotStack == null) {
			return;
		}
		if(fluidStack.isFluidEqual(slotStack)){
			FluidStack remainder = workingTask.insertFromProcess(slotStack);
			workingFluidTile.setFluidInSlot(slot, remainder);
		}
	}

	public void insertItemFromSlot(ItemStack stack, int slot) {
		ItemStack slotStack = workingTile.getItem(slot);
		if (slotStack == null) {
			return;
		}
		if(stack.isItemEqual(slotStack)){
			ItemStack remainder = workingTask.insertFromProcess(slotStack);
			workingTile.setItem(slot, remainder);
		}
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public List<NetworkCraftable> getCraftables() {
		return List.of();
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
	public TileEntity getConnectedTile() {
		return (TileEntity) workingTile;
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
			if(step.subinterface != getId()) continue;
			if (!step.output && step.type == StackType.ITEM) {
				int slot = step.slot;
				if(step.slot == -1) {
					for (int i = 0; i < workingTile.getContainerSize(); i++) {
						if (canInsertItem(i, step.stack, items)) {
							slot = i;
							break;
						}
					}
					if(slot == -1) {
						return false;
					}
				}
				ItemStack slotStack = workingTile.getItem(slot);
				ItemStack stepStack = step.stack;
				if(stepStack == null) continue;
				if(slotStack == null || stepStack.isItemEqual(slotStack)){
					ItemStack removed = items.remove(stepStack.itemID, stepStack.getMetadata(), stepStack.stackSize, stepStack.getData(), false, false);
					if (removed == null) {
						return false;
					}
					if (slotStack == null) {
						workingTile.setItem(slot, removed);
					} else {
						workingTile.getItem(slot).stackSize += removed.stackSize;
					}
				}
			}
		}
		return true;
	}

	public boolean canInsertItem(int slot, ItemStack stack, ItemStackList items){
		ItemStack slotStack = workingTile.getItem(slot);
		if (slotStack != null) {
			if(stack == null) return true;
			if (!slotStack.isItemEqual(stack)) {
				return false;
			} else {
				ItemStack testStack = items.get(stack.itemID, stack.getMetadata(), stack.getData());
				if (testStack == null) {
					return false;
				}
				if (testStack.stackSize < stack.stackSize) {
					return false;
				}
				if (slotStack.stackSize + stack.stackSize > slotStack.getMaxStackSize()) {
					return false;
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
			if(step.subinterface != getId()) continue;
			if (!step.output && step.type == StackType.ITEM) {
				int slot = step.slot;
				if(step.slot == -1) {
					for (int i = 0; i < workingTile.getContainerSize(); i++) {
						if (canInsertItem(i, step.stack, items)) {
							slot = i;
							break;
						}
					}
					if(slot == -1) {
						return false;
					}
				}
				if (!canInsertItem(slot, step.stack, items)) {
					can = false;
					break;
				}
			} else if(step.output && step.type == StackType.ITEM){
				int slot = step.slot;
				if(step.slot == -1) {
					for (int i = 0; i < workingTile.getContainerSize(); i++) {
						if (canInsertItem(i, step.stack, items)) {
							slot = i;
							break;
						}
					}
					if(slot == -1) {
						return false;
					}
				}
				ItemStack slotStack = workingTile.getItem(slot);
				ItemStack stepStack = step.stack;
				if(stepStack == null) continue;
				if(slotStack != null && !stepStack.isItemEqual(slotStack)) can = false;
			}
		}
		return can;
	}

	@Override
	public boolean insertFluids(FluidStackList fluids) {
		if(!canInsertFluids(fluids)) return false;
		if(fluids.isEmpty()) return true;
		for (CraftingProcess.Step step : workingNode.getProcess().steps) {
			if(step.subinterface != getId()) continue;
			if (!step.output && step.type == StackType.FLUID) {
				int slot = step.slot;
				if(step.slot == -1) {
					for (int i = 0; i < workingFluidTile.getFluidInventorySize(); i++) {
						if (canInsertFluid(i, step.fluidStack, fluids)) {
							slot = i;
							break;
						}
					}
					if(slot == -1) {
						return false;
					}
				}
				FluidStack slotStack = workingFluidTile.getFluidInSlot(slot);
				FluidStack stepStack = step.fluidStack;
				if(stepStack == null) continue;
				if(slotStack == null || stepStack.isFluidEqual(slotStack)){
					FluidStack removed = fluids.removeById(stepStack.fluid.getFirstId(), stepStack.amount, false);
					if (removed == null) {
						return false;
					}
					if (slotStack == null) {
						workingFluidTile.setFluidInSlot(slot, removed);
					} else {
						workingFluidTile.getFluidInSlot(slot).amount += removed.amount;
					}
				}
			}
		}
		return true;
	}

	public boolean canInsertFluid(int slot, FluidStack stack, FluidStackList items){
		FluidStack slotStack = workingFluidTile.getFluidInSlot(slot);
		if (slotStack != null) {
			if(stack == null) return true;
			if (!slotStack.isFluidEqual(stack)) {
				return false;
			} else {
				FluidStack testStack = items.getById(stack.fluid.getFirstId());
				if (testStack == null) {
					return false;
				}
				if (testStack.amount < stack.amount) {
					return false;
				}
				if (slotStack.amount + stack.amount > workingFluidTile.getFluidCapacityForSlot(slot)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean canInsertFluids(FluidStackList fluids) {
		if(fluids.isEmpty()) return true;
		boolean can = true;
		if (!isInUse() || workingFluidTile == null) return false;
		for (CraftingProcess.Step step : workingNode.getProcess().steps) {
			if(step.subinterface != getId()) continue;
			if (!step.output && step.type == StackType.FLUID) {
				int slot = step.slot;
				if(step.slot == -1) {
					for (int i = 0; i < workingFluidTile.getFluidInventorySize(); i++) {
						if (canInsertFluid(i, step.fluidStack, fluids)) {
							slot = i;
							break;
						}
					}
					if(slot == -1) {
						return false;
					}
				}
				if (!canInsertFluid(slot, step.fluidStack, fluids)) {
					can = false;
					break;
				}
			} else if(step.output && step.type == StackType.FLUID){
				int slot = step.slot;
				if(step.slot == -1) {
					for (int i = 0; i < workingFluidTile.getFluidInventorySize(); i++) {
						if (canInsertFluid(i, step.fluidStack, fluids)) {
							slot = i;
							break;
						}
					}
					if(slot == -1) {
						return false;
					}
				}
				FluidStack slotStack = workingFluidTile.getFluidInSlot(slot);
				FluidStack stepStack = step.fluidStack;
				if(stepStack == null) continue;
				if(slotStack != null && !stepStack.isFluidEqual(slotStack)) can = false;
			}
		}
		return can;
	}

	@Override
	public void buttonClicked(int id, int button, int channel) {
		if (id == 0) {
			if(this.id == 1){
				this.id = -1;
			} else {
				this.id--;
			}

		}
		if (id == 1) {
			if(this.id == -1){
				this.id = 1;
			} else {
				this.id++;
			}
		}
	}
}
