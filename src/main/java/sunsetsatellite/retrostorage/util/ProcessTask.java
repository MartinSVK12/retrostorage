package sunsetsatellite.retrostorage.util;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

import java.util.ArrayList;

public class ProcessTask extends Task{
    public ArrayList<NBTTagCompound> tasks;
    public ItemStack output;

    public ProcessTask(ArrayList<NBTTagCompound> tasks, Task parent, ArrayList<Task> requires) {
        this.tasks = tasks;
        this.parent = parent;
        if(parent != null){
            parent.requires.add(this);
        }
        this.requires = requires != null ? requires : new ArrayList<>();
        for(NBTTagCompound task : tasks){
            boolean isOutput = task.getBoolean("isOutput");
            if(isOutput){
                output = new ItemStack(task.getCompoundTag("stack"));
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
