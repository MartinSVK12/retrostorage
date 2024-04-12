package sunsetsatellite.retrostorage.util;

import net.minecraft.item.ItemStack;

import java.util.List;

public class CraftingProcess {

    public final String name;
    public final List<Step> steps;

    public CraftingProcess(String name, List<Step> steps) {
        this.name = name;
        this.steps = steps;
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
