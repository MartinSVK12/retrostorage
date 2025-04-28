package sunsetsatellite.retrostorage.util.crafting;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
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

    public CraftingProcess(CompoundTag tag) {
        readFromNBT(tag);
    }

    public void writeToNBT(CompoundTag tag) {
        tag.putString("processName", name);
        CompoundTag stepsTag = new CompoundTag();
        for (Step step : steps) {
            CompoundTag stepTag = new CompoundTag();
            step.writeToNBT(stepTag);
            stepsTag.put("task"+step.id, stepTag);
        }
        tag.put("tasks", stepsTag);
    }

    public void readFromNBT(CompoundTag tag) {
        this.name = tag.getString("processName");
        CompoundTag tasks = tag.getCompound("tasks");
        List<Step> unsortedSteps = new ArrayList<>();
        for (Tag<?> value : tasks.getValues()) {
            CompoundTag taskTag = ((CompoundTag) value);
            Step step = null;
            if (Objects.equals(taskTag.getString("type"), "item")) {
                step = new Step(taskTag.getInteger("slot"), taskTag.getInteger("id"), taskTag.getBoolean("isOutput"), ItemStack.readItemStackFromNbt(taskTag.getCompound("stack")));
            } else if (Objects.equals(taskTag.getString("type"), "fluid")) {
                step = new Step(taskTag.getInteger("slot"), taskTag.getInteger("id"), taskTag.getBoolean("isOutput"), new FluidStack(taskTag.getCompound("stack")));
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

        public void writeToNBT(CompoundTag tag) {
            tag.putString("type", type.name().toLowerCase());
            tag.putInt("slot", slot);
            tag.putInt("id", id);
            tag.putBoolean("isOutput",output);
            CompoundTag stackTag = new CompoundTag();
            if(stack != null){
                stack.writeToNBT(stackTag);
                tag.put("stack",stackTag);

            }
            if(fluidStack != null){
                fluidStack.writeToNBT(stackTag);
                tag.put("stack",stackTag);
            }
        }

        public void readFromNBT(CompoundTag tag) {
            type = tag.getString("type").equals("item") ? StackType.ITEM : StackType.FLUID;
            slot = tag.getInteger("slot");
            id = tag.getInteger("id");
            output = tag.getBoolean("isOutput");
            if(type == StackType.ITEM) {
                stack = ItemStack.readItemStackFromNbt(tag.getCompound("stack"));
            } else {
                fluidStack = new FluidStack(tag.getCompound("stack"));
            }
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
