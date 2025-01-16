package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;
import net.teamterminus.machineessentials.fluid.core.FluidTypeRegistry;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.item.AdvRecipeDiscItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class ProcessProgrammerBlockEntity extends BlockEntity implements Inventory {

    public ProcessProgrammerBlockEntity() {
        contents = new ItemStack[3];
    }

    public final Filter filter = new Filter();

    public static class Filter implements FluidInventory {
        public FluidStack[] fluidContents = new FluidStack[1];


        @Override
        public FluidStack insertFluid(int slot, FluidStack fluidStack) {
            FluidStack stack = fluidContents[slot];
            FluidStack split = fluidStack.split(Math.min(fluidStack.amount, getRemainingCapacity(slot)));
            if (stack != null && split.amount > 0) {
                fluidContents[slot].amount += split.amount;
            } else {
                fluidContents[slot] = split;
            }
            return fluidStack;
        }

        @Override
        public int getRemainingCapacity(int slot) {
            return 1;
        }

        @Override
        public boolean canInsertFluid(int slot, FluidStack fluidStack) {
            if (getFluidInSlot(slot) != null) if (!getFluidInSlot(slot).isFluidEqual(fluidStack)) return false;
            return Math.min(fluidStack.amount, getRemainingCapacity(slot)) > 0;
        }

        @Override
        public FluidStack getFluidInSlot(int slot) {
            if (this.fluidContents.length == 0) return null;
            if (this.fluidContents[slot] == null || this.fluidContents[slot].fluid == null || this.fluidContents[slot].amount == 0) {
                this.fluidContents[slot] = null;
            }
            return fluidContents[slot];
        }

        @Override
        public int getFluidCapacityForSlot(int slot) {
            return 1;
        }

        @Override
        public ArrayList<FluidType> getAllowedFluidsForSlot(int slot) {
            ArrayList<FluidType> allFluids = FluidTypeRegistry.getAll();
            allFluids.removeIf(RetroStorage.DISALLOWED_FLUIDS::contains);
            return allFluids;
        }

        @Override
        public void setFluidInSlot(int slot, FluidStack fluid) {
            if (fluid == null || fluid.amount == 0 || fluid.fluid == null) {
                this.fluidContents[slot] = null;
                this.onFluidInventoryChanged();
                return;
            }
            ArrayList<FluidType> allFluids = FluidTypeRegistry.getAll();
            allFluids.removeIf(RetroStorage.DISALLOWED_FLUIDS::contains);
            if (allFluids.contains(fluid.fluid)) {
                this.fluidContents[slot] = fluid;
                this.onFluidInventoryChanged();
            }

        }

        @Override
        public int size() {
            return fluidContents.length;
        }

        @Override
        public void onFluidInventoryChanged() {

        }

        @Override
        public int getTransferSpeed() {
            return 0;
        }

        public boolean hasFluid(FluidStack fluidStack) {
            if (fluidStack == null) return false;
            return Arrays.stream(fluidContents).anyMatch((F) -> F != null && F.isFluidEqual(fluidStack));
        }
    }

    public int size() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < size() - 1; i++) {
            if (getStack(i) != null) {
                return false;
            } else {
                continue;
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
        return "Process Programmer";
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

    public void tick() {
    }

    public void setTask(String selected) {
        if (Objects.equals(selected, "item")) {
            if (getStack(0) != null) {
                HashMap<String, Object> task = new HashMap<>();
                task.put("slot", currentSlot);
                task.put("stack", getStack(0).copy());
                task.put("isOutput", isCurrentOutput);
                task.put("type", "item");
                tasks.put(currentTask, task);
            } else {
                tasks.remove(currentTask);
            }
        } else if (Objects.equals(selected, "fluid")) {
            if (filter.getFluidInSlot(0) != null) {
                HashMap<String, Object> task = new HashMap<>();
                task.put("slot", currentSlot);
                task.put("stack", filter.getFluidInSlot(0).copy());
                task.put("isOutput", isCurrentOutput);
                task.put("type", "fluid");
                tasks.put(currentTask, task);
            } else {
                tasks.remove(currentTask);
            }
        }

        //System.out.println(tasks);
    }

    public void clearDisc() {
        if (getStack(1) != null && getStack(1).getItem() instanceof AdvRecipeDiscItem) {
            ItemStack disc = getStack(1);
            disc.getStationNbt().put("disc", new NbtCompound());
            disc.getStationNbt().putString("name", "");
            disc.getStationNbt().putBoolean("overrideName", false);
        }
        tasks.clear();
    }

    public void saveProcess() {
        if (getStack(1) != null && getStack(1).getItem() instanceof AdvRecipeDiscItem) {
            NbtCompound data = new NbtCompound();
            NbtCompound taskData = new NbtCompound();
            tasks.forEach((K, V) -> {
                NbtCompound task = new NbtCompound();
                task.putInt("id", K);
                task.putString("type", (String) V.get("type"));
                task.putInt("slot", (Integer) V.get("slot"));
                task.putBoolean("isOutput", (Boolean) V.get("isOutput"));
                NbtCompound stack = new NbtCompound();
                if (V.get("type") == "item") {
                    ((ItemStack) V.get("stack")).writeNbt(stack);
                } else if (V.get("type") == "fluid") {
                    ((FluidStack) V.get("stack")).writeNbt(stack);
                }
                task.put("stack", stack);
                taskData.put("task" + K, task);
            });
            getStack(1).getStationNbt().putString("name", "Adv. Recipe Disc: " + currentProcessName);
            getStack(1).getStationNbt().putBoolean("overrideName", true);
            data.put("tasks", taskData);
            data.putString("processName", currentProcessName);
            getStack(1).getStationNbt().put("disc", data);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        }
        return player.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }

    public int currentTask = 0;
    public int currentSlot = 0;
    public boolean isCurrentOutput = false;
    public String currentProcessName = "";
    public HashMap<Integer, HashMap<String, Object>> tasks = new HashMap<Integer, HashMap<String, Object>>();
    private ItemStack[] contents;

}

