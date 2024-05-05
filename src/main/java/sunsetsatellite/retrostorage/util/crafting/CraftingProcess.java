package sunsetsatellite.retrostorage.util.crafting;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CraftingProcess {

    public final String name;
    public final List<Step> steps;
    public final ItemStack mainOutput;

    public CraftingProcess(String name, List<Step> steps, ItemStack mainOutput) {
        this.name = name;
        this.steps = steps;
        this.mainOutput = mainOutput;
    }

    public CraftingProcess(CompoundTag tag){
        ItemStack output;
        this.name = tag.getString("processName");
        CompoundTag tasks = tag.getCompound("tasks");
        List<Step> unsortedSteps = new ArrayList<>();
        for (Tag<?> value : tasks.getValues()) {
            CompoundTag taskTag = ((CompoundTag) value);
            Step step = new Step(taskTag.getInteger("slot"), taskTag.getInteger("id"), taskTag.getBoolean("isOutput"),ItemStack.readItemStackFromNbt(taskTag.getCompound("stack")));
            unsortedSteps.add(step);
        }
        output = null;
        steps = unsortedSteps.stream().sorted(Comparator.comparingInt(s -> s.id)).collect(Collectors.toList());
        for (Step step : steps) {
            if(step.output){
                output = step.stack;
                break;
            }
        }
        mainOutput = output;
    }

    public static final class Step {
        public final int slot;
        public final int id;
        public final boolean output;
        public final ItemStack stack;

        public Step(int slot, int id, boolean output, ItemStack stack) {
            this.slot = slot;
            this.id = id;
            this.output = output;
            this.stack = stack;
        }
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
