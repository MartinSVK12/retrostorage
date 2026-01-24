package sunsetsatellite.retrostorage.util.crafting;


import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.VariantStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CraftingProcess {

    public String name;
    public List<Step> steps;

    public CraftingProcess(String name, List<Step> steps) {
        this.name = name;
        this.steps = steps;
    }

    public CraftingProcess(NbtCompound tag) {
        readFromNBT(tag);
    }

    public void writeToNBT(NbtCompound tag) {
        tag.putString("processName", name);
        NbtCompound stepsTag = new NbtCompound();
        for (Step step : steps) {
            NbtCompound stepTag = new NbtCompound();
            step.writeToNBT(stepTag);
            stepsTag.put("task" + step.id, stepTag);
        }
        tag.put("tasks", stepsTag);
    }

    public void readFromNBT(NbtCompound tag) {
        this.name = tag.getString("processName");
        NbtCompound tasks = tag.getCompound("tasks");
        List<Step> unsortedSteps = new ArrayList<>();
        for (Object value : tasks.values()) {
            NbtCompound taskTag = ((NbtCompound) value);
            Step step = null;
            if (Objects.equals(taskTag.getString("type"), "item")) {
                step = new Step(taskTag.getInt("slot"), taskTag.getInt("id"), taskTag.getBoolean("isOutput"), new ItemStack(taskTag.getCompound("stack")));
            } else if (Objects.equals(taskTag.getString("type"), "fluid")) {
                step = new Step(taskTag.getInt("slot"), taskTag.getInt("id"), taskTag.getBoolean("isOutput"), new FluidStack(taskTag.getCompound("stack")));
            }
            if (step != null) unsortedSteps.add(step);
        }
        steps = unsortedSteps.stream().sorted(Comparator.comparingInt(s -> s.id)).collect(Collectors.toList());
    }

    public static final class Step {
        public StackType type;
        public int slot;
        public int id;
        public boolean output;
        public ItemStack stack;
        public FluidStack fluidStack;

        public Step(int slot, int id, boolean output, ItemStack stack) {
            this.type = StackType.ITEM;
            this.slot = slot;
            this.id = id;
            this.output = output;
            this.stack = stack;
            this.fluidStack = null;
        }

        public Step(int slot, int id, boolean output, FluidStack stack) {
            this.type = StackType.FLUID;
            this.slot = slot;
            this.id = id;
            this.output = output;
            this.fluidStack = stack;
            this.stack = null;
        }

        public void writeToNBT(NbtCompound tag) {
            tag.putString("type", type.name().toLowerCase());
            tag.putInt("slot", slot);
            tag.putInt("id", id);
            tag.putBoolean("isOutput", output);
            NbtCompound stackTag = new NbtCompound();
            if (stack != null) {
                stack.writeNbt(stackTag);
                tag.put("stack", stackTag);

            }
            if (fluidStack != null) {
                fluidStack.writeNbt(stackTag);
                tag.put("stack", stackTag);
            }
        }

        public void readFromNBT(NbtCompound tag) {
            type = tag.getString("type").equals("item") ? StackType.ITEM : StackType.FLUID;
            slot = tag.getInt("slot");
            id = tag.getInt("id");
            output = tag.getBoolean("isOutput");
            if (type == StackType.ITEM) {
                stack = new ItemStack(tag.getCompound("stack"));
            } else {
                fluidStack = new FluidStack(tag.getCompound("stack"));
            }
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Step step)) return false;

            boolean header = slot == step.slot
                    && id == step.id
                    && output == step.output
                    && type == step.type;

            header = header && (stack == null ? step.stack == null : stack.isItemEqual(step.stack));
            header = header && (fluidStack == null ? step.fluidStack == null : fluidStack.isFluidEqual(step.fluidStack));

            return header;
        }

        @Override
        public int hashCode() {
            int result = Objects.hashCode(type);
            result = 31 * result + slot;
            result = 31 * result + id;
            result = 31 * result + Boolean.hashCode(output);
            result = 31 * result + Objects.hashCode(stack);
            result = 31 * result + Objects.hashCode(fluidStack);
            return result;
        }
    }

    public List<ItemStack> getItemOutputs() {
        return steps.stream().filter((S) -> S.output && S.type == StackType.ITEM).map((S) -> S.stack.copy()).collect(Collectors.toList());
    }

    public List<FluidStack> getFluidOutputs() {
        return steps.stream().filter((S) -> S.output && S.type == StackType.FLUID).map((S) -> S.fluidStack.copy()).collect(Collectors.toList());
    }

    public List<VariantStack> getAllOutputs() {
        return steps.stream().filter((S) -> S.output).map((S) -> S.type == StackType.ITEM ? new VariantStack(S.stack.copy()) : new VariantStack(S.fluidStack.copy())).collect(Collectors.toList());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CraftingProcess)) return false;
        if(o instanceof CraftingProcess otherProcess){
            return steps.stream().allMatch(step -> otherProcess.steps.stream().anyMatch(step::equals)) && Objects.equals(name, otherProcess.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
