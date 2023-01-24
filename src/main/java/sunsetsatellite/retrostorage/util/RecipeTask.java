package sunsetsatellite.retrostorage.util;

import net.minecraft.src.IRecipe;

import java.util.ArrayList;

public class RecipeTask extends Task{
    public IRecipe recipe;

    public RecipeTask(IRecipe recipe, Task parent, ArrayList<Task> requires) {
        this.recipe = recipe;
        this.parent = parent;
        this.requires = requires != null ? requires : new ArrayList<>();
    }
}
