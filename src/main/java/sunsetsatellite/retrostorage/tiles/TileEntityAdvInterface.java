package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.FluidStackList;
import sunsetsatellite.retrostorage.items.ItemAdvRecipeDisc;
import sunsetsatellite.retrostorage.util.*;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntityAdvInterface extends TileEntityNetworkDevice
        implements Container, IProcessor {

    private ItemStack[] contents;
    public HashMap<Direction, TileEntity> connectedTiles = new HashMap<>();
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
    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
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
        CompoundTag.put("Items", listTag);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
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
    public void tick() {
        super.tick();
        if (worldObj != null && worldObj.isClientSide) return;
        if (worldObj != null && network != null && getController() != null) {
            ArrayList<Class<?>> tiles = new ArrayList<>();
            tiles.add(Container.class);
            tiles.add(IFluidInventory.class);
            connectedTiles = getConnectedTileEntity(tiles);
            TileEntity tileEntity = Direction.getDirectionFromSide(getBlockMeta()).getTileEntity(worldObj, this);
            int i = 1;
            if (tileEntity instanceof Container && !(tileEntity instanceof TileEntityAdvInterface)) {
                workingTile = (Container) tileEntity;
            }
            if (tileEntity instanceof IFluidInventory && !(tileEntity instanceof TileEntityAdvInterface)) {
                workingFluidTile = (IFluidInventory) tileEntity;
            }
            /*for (TileEntity tile : connectedTiles.values()) {

                if ((tile instanceof Container || tile instanceof IFluidInventory) && !(tile instanceof TileEntityAdvInterface)) {
                    break;
                }

                i++;
            }*/
            if (i >= 6) {
                workingTile = null;
            }

            if (isInUse() && (workingTile != null || workingFluidTile != null)) {
                for (CraftingProcess.Step step : workingNode.getProcess().steps) {
                    if (step.output && step.type == StackType.ITEM && workingTile != null) {
                        ItemStack slotStack = workingTile.getItem(step.slot);
                        ItemStack stepStack = step.stack;
                        if (slotStack == null) {
                            continue;
                        }
                        workingTile.setItem(step.slot, null);
                        workingTask.insertFromProcess(slotStack);
                    } else if ((step.output && step.type == StackType.FLUID && workingFluidTile != null)) {
                        FluidStack slotStack = workingFluidTile.getFluidInSlot(step.slot);
                        FluidStack stepStack = step.fluidStack;
                        if (slotStack == null) {
                            continue;
                        }
                        workingFluidTile.setFluidInSlot(step.slot, null);
                        workingTask.insertFromProcess(slotStack);
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

    public ArrayList<CraftingProcess> getProcesses() {
        ArrayList<CraftingProcess> processes = new ArrayList<>();
        for (ItemStack stack : contents) {
            if (stack != null && stack.getItem() instanceof ItemAdvRecipeDisc && stack.getData().containsKey("disc")) {
                processes.add(new CraftingProcess(stack.getData().getCompound("disc")));
            }
        }
        return processes;
    }

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
    public Container getConnectedTile() {
        return workingTile;
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
                ItemStack slotStack = workingTile.getItem(step.slot);
                ItemStack stepStack = step.stack;
                if(stepStack == null) continue;
                ItemStack removed = items.remove(stepStack.itemID, stepStack.getMetadata(), stepStack.stackSize, stepStack.getData(), false, false);
                if (removed == null) {
                    return false;
                }
                if (slotStack == null) {
                    workingTile.setItem(step.slot, removed);
                } else {
                    workingTile.getItem(step.slot).stackSize += removed.stackSize;
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
                ItemStack slotStack = workingTile.getItem(step.slot);
                ItemStack stepStack = step.stack;
                if (slotStack != null) {
                    if(stepStack == null) continue;
                    if (!slotStack.isItemEqual(stepStack)) {
                        can = false;
                        break;
                    } else {
                        ItemStack testStack = items.get(stepStack.itemID, stepStack.getMetadata(), stepStack.getData());
                        if (testStack == null) {
                            can = false;
                            break;
                        }
                        if (testStack.stackSize < stepStack.stackSize) {
                            can = false;
                            break;
                        }
                        if (slotStack.stackSize + stepStack.stackSize > slotStack.getMaxStackSize()) {
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
        if(!canInsertFluids(items)) return false;
        if(items.isEmpty()) return true;
        for (CraftingProcess.Step step : workingNode.getProcess().steps) {
            if (!step.output && step.type == StackType.FLUID) {
                FluidStack slotStack = workingFluidTile.getFluidInSlot(step.slot);
                FluidStack stepStack = step.fluidStack;
                if(stepStack == null) continue;
                FluidStack removed = items.removeById(stepStack.fluid.getFirstId(), stepStack.amount, false);
                if (removed == null) {
                    return false;
                }
                if (slotStack == null) {
                    workingFluidTile.setFluidInSlot(step.slot, removed);
                } else {
                    workingFluidTile.getFluidInSlot(step.slot).amount += removed.amount;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canInsertFluids(FluidStackList items) {
        if(items.isEmpty()) return true;
        boolean can = true;
        if (!isInUse() || workingFluidTile == null) return false;
        for (CraftingProcess.Step step : workingNode.getProcess().steps) {
            if (!step.output && step.type == StackType.FLUID) {
                FluidStack slotStack = workingFluidTile.getFluidInSlot(step.slot);
                FluidStack stepStack = step.fluidStack;
                if (slotStack != null) {
                    if(stepStack == null) continue;
                    if (!slotStack.isFluidEqual(stepStack)) {
                        can = false;
                        break;
                    } else {
                        FluidStack testStack = items.getById(stepStack.fluid.getFirstId());
                        if (testStack == null) {
                            can = false;
                            break;
                        }
                        if (testStack.amount < stepStack.amount) {
                            can = false;
                            break;
                        }
                        if (slotStack.amount + stepStack.amount > workingFluidTile.getFluidCapacityForSlot(step.slot)) {
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

