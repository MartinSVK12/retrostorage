package sunsetsatellite.retrostorage.tiles;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.core.util.IScreenActionListener;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemAdvRecipeDisc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class TileEntityProcessProgrammer extends TileEntity
        implements Container, IScreenActionListener {

    public TileEntityProcessProgrammer() {
        contents = new ItemStack[3];
    }

    public final Filter filter = new Filter();

    @Override
    public void buttonClicked(int id, int button, int channel) {
        if (id == 6) {
            isCurrentOutput = !isCurrentOutput;
        }
        switch (id) {
            case 0:
                saveProcess();
                break;
            case 1:
                clearDisc();
                isCurrentOutput = false;
                currentSlot = 0;
                currentTask = 0;
                currentProcessName = "New Process";
                break;
            case 2:
                currentTask++;
                break;
            case 3:
                if (currentTask > 0) currentTask--;
                break;
            case 4:
                currentSlot++;
                break;
            case 5:
                if (currentSlot > 0) currentSlot--;
                break;
            case 7:
                setTask(selectedType);
                break;
            case 8:
                selectedType = "item";
                break;
            case 9:
                selectedType = "fluid";
                break;
        }
    }

    public static class Filter implements IFluidInventory {
        public FluidStack[] fluidContents = new FluidStack[1];


        @Override
        public FluidStack insertFluid(int slot, FluidStack fluidStack) {
            FluidStack stack = fluidContents[slot];
            FluidStack split = fluidStack.splitStack(Math.min(fluidStack.amount, getRemainingCapacity(slot)));
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
        public ArrayList<Fluid> getAllowedFluidsForSlot(int slot) {
            ArrayList<Fluid> allFluids = (ArrayList<Fluid>) Fluid.fluidMap.values();
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
            ArrayList<Fluid> allFluids = (ArrayList<Fluid>) Fluid.fluidMap.values();;
            allFluids.removeIf(RetroStorage.DISALLOWED_FLUIDS::contains);
            if (allFluids.contains(fluid.fluid)) {
                this.fluidContents[slot] = fluid;
                this.onFluidInventoryChanged();
            }

        }

        @Override
        public int getFluidInventorySize() {
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

    @Override
    public int getContainerSize() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize() - 1; i++) {
            if (getItem(i) != null) {
                return false;
            } else {
                continue;
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
        return "container.retrostorage.processProgrammer";
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        currentProcessName = tag.getString("ProcessName");
        ListTag listTag = tag.getList("Items");
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
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);
        ListTag listTag = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte) i);
                contents[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }
        tag.putString("ProcessName", currentProcessName);
        tag.put("Items", listTag);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    public void setTask(String selected) {
        if (Objects.equals(selected, "item")) {
            if (getItem(0) != null) {
                HashMap<String, Object> task = new HashMap<>();
                task.put("slot", currentSlot);
                task.put("stack", getItem(0).copy());
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
        if (getItem(1) != null && getItem(1).getItem() instanceof ItemAdvRecipeDisc) {
            ItemStack disc = getItem(1);
            disc.getData().putCompound("disc", new CompoundTag());
            disc.getData().putString("name", "");
            disc.getData().putBoolean("overrideName", false);
        }
        tasks.clear();
    }

    public void saveProcess() {
        if (getItem(1) != null && getItem(1).getItem() instanceof ItemAdvRecipeDisc) {
            CompoundTag data = new CompoundTag();
            CompoundTag taskData = new CompoundTag();
            tasks.forEach((K, V) -> {
                CompoundTag task = new CompoundTag();
                task.putInt("id", K);
                task.putString("type", (String) V.get("type"));
                task.putInt("slot", (Integer) V.get("slot"));
                task.putBoolean("isOutput", (Boolean) V.get("isOutput"));
                CompoundTag stack = new CompoundTag();
                if (V.get("type") == "item") {
                    ((ItemStack) V.get("stack")).writeToNBT(stack);
                } else if (V.get("type") == "fluid") {
                    ((FluidStack) V.get("stack")).writeToNBT(stack);
                }
                task.putCompound("stack", stack);
                taskData.putCompound("task" + K, task);
            });
            getItem(1).getData().putString("name", "Adv. Recipe Disc: " + currentProcessName);
            getItem(1).getData().putBoolean("overrideName", true);
            data.putCompound("tasks", taskData);
            data.putString("processName", currentProcessName);
            getItem(1).getData().putCompound("disc", data);
        }
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        if (worldObj.getTileEntity(x, y, z) != this) {
            return false;
        }
        return entityplayer.distanceToSqr((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }

    @Override
    public void sortContainer() {

    }

    public int currentTask = 0;
    public int currentSlot = 0;
    public boolean isCurrentOutput = false;
    public String currentProcessName = "";
    public String selectedType;
    public HashMap<Integer, HashMap<String, Object>> tasks = new HashMap<Integer, HashMap<String, Object>>();
    private ItemStack[] contents;

}

