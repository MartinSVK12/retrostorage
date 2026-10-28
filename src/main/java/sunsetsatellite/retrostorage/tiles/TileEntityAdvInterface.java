package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.FluidStackList;
import sunsetsatellite.retrostorage.api.IProcessor;
import sunsetsatellite.retrostorage.items.ItemAdvRecipeDisc;
import sunsetsatellite.retrostorage.util.*;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.*;
import java.util.stream.Collectors;

public class TileEntityAdvInterface extends TileEntityNetworkDevice
        implements Container, IProcessor {

    private ItemStack[] contents;
    public HashMap<Direction, TileEntity> connectedTiles = new HashMap<>();
	public Set<Vec3i> subintPositions = new HashSet<>();
	public HashMap<Integer, IProcessor> subinterfaces = new HashMap<>();
    public Container workingTile;
    public IFluidInventory workingFluidTile;
    public ProcessNode workingNode;
    public CraftingTask workingTask;

    public TileEntityAdvInterface() {
        contents = new ItemStack[10];
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
        return "container.retrostorage.advInterface";
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void readAdditionalData(@NonNull CompoundTag compoundTag) {

        ListTag listTag = compoundTag.getList("Items");
        contents = new ItemStack[getContainerSize()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag CompoundTag1 = (CompoundTag) listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if (j >= 0 && j < contents.length) {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }
		ListTag list2Tag = compoundTag.getList("SubInterfaces");
		for (int i = 0; i < list2Tag.tagCount(); i++) {
			CompoundTag tag = (CompoundTag) list2Tag.tagAt(i);
			subintPositions.add(new Vec3i(tag));
		}
    }

    @Override
    public void writeAdditionalData(@NonNull CompoundTag compoundTag) {

		ListTag subIntTag = new ListTag();
		for (Vec3i pos : subintPositions) {
			CompoundTag tag = new CompoundTag();
			pos.writeToNBT(tag);
			subIntTag.addTag(tag);
		}
		compoundTag.put("SubInterfaces", subIntTag);
        ListTag listTag = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                contents[i].writeToNBT(CompoundTag1);
				listTag.addTag(CompoundTag1);
            }
        }
        compoundTag.put("Items", listTag);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
        if (worldObj != null && network != null && getController() != null) {
			if(subintPositions.size() != subinterfaces.size()){
				subinterfaces.clear();
				subintPositions.forEach((pos) -> {
					TileEntity tile = worldObj.getTileEntity(pos.tilePos());
					if(tile instanceof TileEntitySubinterface subint) {
						subinterfaces.put(subint.getId(), subint);
					}
				});
			}
            ArrayList<Class<?>> tiles = new ArrayList<>();
            tiles.add(Container.class);
            tiles.add(IFluidInventory.class);
            connectedTiles = getConnectedTileEntity(tiles);
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
                        ItemStack slotStack = workingTile.getItem(step.slot);
                        ItemStack stepStack = step.stack;
                        if (slotStack == null) {
                            continue;
                        }
                        if(stepStack.isItemEqual(slotStack)){
                            ItemStack remainder = workingTask.insertFromProcess(slotStack);
                            workingTile.setItem(step.slot, remainder);
                        }
                    } else if ((step.output && step.type == StackType.FLUID && workingFluidTile != null)) {
                        FluidStack slotStack = workingFluidTile.getFluidInSlot(step.slot);
                        FluidStack stepStack = step.fluidStack;
                        if (slotStack == null) {
                            continue;
                        }
                        if(stepStack.isFluidEqual(slotStack)){
                            FluidStack remainder = workingTask.insertFromProcess(slotStack);
                            workingFluidTile.setFluidInSlot(step.slot, remainder);
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

    public ArrayList<CraftingProcess> getProcesses() {
        ArrayList<CraftingProcess> processes = new ArrayList<>();
        for (ItemStack stack : contents) {
            if (stack != null && stack.getItem() instanceof ItemAdvRecipeDisc && stack.getData().containsKey("disc")) {
                processes.add(new CraftingProcess(stack.getData().getCompound("disc")));
            }
        }
        return processes;
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
	public int getId() {
		return 0;
	}

	@Override
    public List<NetworkCraftable> getCraftables() {
        return getProcesses().stream().map(NetworkCraftable::new).collect(Collectors.toList());
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
			if(step.subinterface != getId()) {
				if(!subinterfaces.containsKey(step.subinterface)){
					return false;
				}
				subinterfaces.get(step.subinterface).setFocus(workingNode, workingTask);
				boolean b = subinterfaces.get(step.subinterface).insertItems(items);
				if(!b) return false;
				continue;
			}
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
			if(step.subinterface != getId()) {
				if(!subinterfaces.containsKey(step.subinterface)){
					return false;
				}
				subinterfaces.get(step.subinterface).setFocus(workingNode, workingTask);
				can = subinterfaces.get(step.subinterface).canInsertItems(items);
				if(!can) break;
				continue;
			}
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
			if(step.subinterface != getId()) {
				if(!subinterfaces.containsKey(step.subinterface)){
					return false;
				}
				subinterfaces.get(step.subinterface).setFocus(workingNode, workingTask);
				boolean b = subinterfaces.get(step.subinterface).insertFluids(fluids);
				if(!b) return false;
				continue;
			}
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
			if(step.subinterface != getId()) {
				if(!subinterfaces.containsKey(step.subinterface)){
					return false;
				}
				subinterfaces.get(step.subinterface).setFocus(workingNode, workingTask);
				can = subinterfaces.get(step.subinterface).canInsertFluids(fluids);
				if(!can) break;
				continue;
			}
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
}

