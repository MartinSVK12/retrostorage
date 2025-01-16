package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;
import sunsetsatellite.retrostorage.item.AdvRecipeDiscItem;
import sunsetsatellite.retrostorage.util.*;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AdvInterfaceBlockEntity extends NetworkDeviceBlockEntity
        implements Inventory, Processor {

    private ItemStack[] contents;
    public HashMap<Direction, BlockEntity> connectedTiles = new HashMap<>();
    public Inventory workingTile;
    public FluidInventory workingFluidTile;
    public ProcessNode workingNode;
    public CraftingTask workingTask;

    public AdvInterfaceBlockEntity() {
        contents = new ItemStack[10];
    }

    public int size() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < size() - 1; i++) {
            if (getStack(i) != null) {
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

    public void markDirty() {
        super.markDirty();
    }

    public String getName() {
        return "Adv. Item Interface";
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        NbtList listTag = tag.getList("Items");
        contents = new ItemStack[size()];
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound tag1 = (NbtCompound) listTag.get(i);
            int j = tag1.getByte("Slot") & 0xff;
            if (j >= 0 && j < contents.length) {
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
        tag.put("Items", listTag);
    }

    public int getMaxCountPerStack() {
        return 64;
    }

    public int containsItem(int itemId, int itemDamage) {
        for (int i2 = 0; i2 < this.contents.length; ++i2) {
            if (this.contents[i2] != null && this.contents[i2].itemId == itemId && this.contents[i2].getDamage() == itemDamage) {
                return i2;
            }
        }

        return -1;
    }

    @Override
    public void tick() {
        super.tick();
        if (network != null && getController() != null) {
            ArrayList<Class<?>> tiles = new ArrayList<>();
            tiles.add(Inventory.class);
            tiles.add(FluidInventory.class);
            connectedTiles = getConnectedBlockEntity(tiles);
            int i = 0;
            for (BlockEntity tile : connectedTiles.values()) {
                if (tile instanceof Inventory && !(tile instanceof AdvInterfaceBlockEntity)) {
                    workingTile = (Inventory) tile;
                }
                if (tile instanceof FluidInventory && !(tile instanceof AdvInterfaceBlockEntity)) {
                    workingFluidTile = (FluidInventory) tile;
                }
                if ((tile instanceof Inventory || tile instanceof FluidInventory) && !(tile instanceof AdvInterfaceBlockEntity)) {
                    break;
                }

                i++;
            }
            if (i >= 6) {
                workingTile = null;
            }

            if (isInUse() && (workingTile != null || workingFluidTile != null)) {
                for (CraftingProcess.Step step : workingNode.getProcess().steps) {
                    if (step.output && step.type == StackType.ITEM && workingTile != null) {
                        ItemStack slotStack = workingTile.getStack(step.slot);
                        ItemStack stepStack = step.stack;
                        if (slotStack == null) {
                            continue;
                        }
                        workingTile.setStack(step.slot, null);
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
            if (stack != null && stack.getItem() instanceof AdvRecipeDiscItem && stack.getStationNbt().contains("disc")) {
                processes.add(new CraftingProcess(stack.getStationNbt().getCompound("disc")));
            }
        }
        return processes;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity entityplayer) {
        return super.canPlayerUse(entityplayer);
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
    public Inventory getConnectedTile() {
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
                ItemStack slotStack = workingTile.getStack(step.slot);
                ItemStack stepStack = step.stack;
                if(stepStack == null) continue;
                ItemStack removed = items.remove(stepStack.itemId, stepStack.getDamage(), stepStack.count, stepStack.getStationNbt(), false, false);
                if (removed == null) {
                    return false;
                }
                if (slotStack == null) {
                    workingTile.setStack(step.slot, removed);
                } else {
                    workingTile.getStack(step.slot).count += removed.count;
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
                ItemStack slotStack = workingTile.getStack(step.slot);
                ItemStack stepStack = step.stack;
                if (slotStack != null) {
                    if(stepStack == null) continue;
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
        if(!canInsertFluids(items)) return false;
        if(items.isEmpty()) return true;
        for (CraftingProcess.Step step : workingNode.getProcess().steps) {
            if (!step.output && step.type == StackType.FLUID) {
                FluidStack slotStack = workingFluidTile.getFluidInSlot(step.slot);
                FluidStack stepStack = step.fluidStack;
                if(stepStack == null) continue;
                FluidStack removed = items.removeById(stepStack.fluid.blockId(), stepStack.amount, false);
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
                        FluidStack testStack = items.get(stepStack.fluid.blockId());
                        if (testStack == null) {
                            can = false;
                            break;
                        }
                        if (testStack.fluid.blockId() < stepStack.fluid.blockId()) {
                            can = false;
                            break;
                        }
                        if (slotStack.fluid.blockId() + stepStack.fluid.blockId() > workingFluidTile.getFluidCapacityForSlot(step.slot)) {
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

