package sunsetsatellite.retrostorage.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ProcessTask extends Task {

    public final CraftingProcess process;
    public final ItemStack outputStack;

    public ProcessTask(CraftingProcess process, Task parent, ArrayList<Task> requires) {
        ItemStack output = null;
        this.process = process;
        for (CraftingProcess.Step step : process.steps) {
            if(step.output){
                output = step.stack;
            }
        }
        outputStack = output;
        this.parent = parent;
        this.requires = requires != null ? requires : new ArrayList<>();
        if(parent != null) {
            parent.requires.add(this);
        }
    }

    @Override
    public ArrayList<Task> getSubtasks(DigitalNetwork network) {
        return null;
    }
}
