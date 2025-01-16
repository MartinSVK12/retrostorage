package sunsetsatellite.retrostorage.util.crafting;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.VariantStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CraftingProcess {

    public final String name;
    public final List<Step> steps;

    public CraftingProcess(String name, List<Step> steps) {
        this.name = name;
        this.steps = steps;
    }

    public CraftingProcess(NbtCompound tag) {
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
        public final StackType type;
        public final int slot;
        public final int id;
        public final boolean output;
        public final ItemStack stack;
        public final FluidStack fluidStack;

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
    }

    public List<ItemStack> getItemOutputs(){
        return steps.stream().filter((S)->S.output && S.type == StackType.ITEM).map((S)->S.stack.copy()).collect(Collectors.toList());
    }

    public List<FluidStack> getFluidOutputs(){
        return steps.stream().filter((S)->S.output && S.type == StackType.FLUID).map((S)->S.fluidStack.copy()).collect(Collectors.toList());
    }

    public List<VariantStack> getAllOutputs(){
        return steps.stream().filter((S)->S.output).map((S)->S.type == StackType.ITEM ? new VariantStack(S.stack.copy()) : new VariantStack(S.fluidStack.copy())).collect(Collectors.toList());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CraftingProcess)) return false;
        CraftingProcess process = (CraftingProcess) o;

        return Objects.equals(name, process.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
