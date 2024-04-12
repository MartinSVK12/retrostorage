package sunsetsatellite.retrostorage.util;

import net.minecraft.recipe.CraftingRecipe;
import net.modificationstation.stationapi.api.registry.Registries;

import java.util.ArrayList;

public class RecipeTask extends Task {

    public final CraftingRecipe recipe;

    public RecipeTask(CraftingRecipe recipe, Task parent, ArrayList<Task> requires) {
        this.recipe = recipe;
        this.parent = parent;
        this.requires = requires != null ? requires : new ArrayList<>();
        if(parent != null){
            parent.requires.add(this);
        }
    }

    @Override
    public ArrayList<Task> getSubtasks(DigitalNetwork network) {
        return null;
    }
}
