package sunsetsatellite.retrostorage.util.crafting;

import net.minecraft.core.item.ItemStack;

import java.util.List;

public class CraftingProcess {

    public final String name;
    public final List<Step> steps;
    public final ItemStack mainOutput;

    public CraftingProcess(String name, List<Step> steps, ItemStack mainOutput) {
        this.name = name;
        this.steps = steps;
        this.mainOutput = mainOutput;
    }

    public static final class Step {
        public final int slot;
        public final boolean output;
        public final ItemStack stack;

        public Step(int slot, boolean output, ItemStack stack) {
            this.slot = slot;
            this.output = output;
            this.stack = stack;
        }
    }
}
