package sunsetsatellite.retrostorage.util;




import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;

public class ProcessTask extends Task{
    public ArrayList<CompoundTag> tasks;
    public ItemStack output;

    public ProcessTask(ArrayList<CompoundTag> tasks, Task parent, ArrayList<Task> requires) {
        this.tasks = tasks;
        this.parent = parent;
        if(parent != null){
            parent.requires.add(this);
        }
        this.requires = requires != null ? requires : new ArrayList<>();
        for(CompoundTag task : tasks){
            boolean isOutput = task.getBoolean("isOutput");
            if(isOutput){
                output = ItemStack.readItemStackFromNbt(task.getCompound("stack"));
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "ProcessTask{" +
                "tasks=" + tasks +
                ", output=" + output +
                ", processor=" + processor +
                ", attempts=" + attempts +
                ", reqMet="+requirementsMet() +
                '}';
    }
}
