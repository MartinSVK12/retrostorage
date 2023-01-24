package sunsetsatellite.retrostorage.util;

import net.minecraft.src.IRecipe;
import net.minecraft.src.NBTTagCompound;

import java.util.ArrayList;

public class ProcessTask extends Task{
    NBTTagCompound task;

    public ProcessTask(NBTTagCompound task, Task parent, ArrayList<Task> requires) {
        this.task = task;
        this.parent = parent;
        this.requires = requires;
    }
}
