package sunsetsatellite.retrostorage.util;


import net.minecraft.core.crafting.recipe.IRecipe;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;

public class RecipeTask extends Task{
    public IRecipe recipe;

    public RecipeTask(IRecipe recipe, Task parent, ArrayList<Task> requires) {
        this.recipe = recipe;
        this.parent = parent;
        this.requires = requires != null ? requires : new ArrayList<>();
        if(parent != null){
            parent.requires.add(this);
        }
    }

    @Override
    public String toString() {
        /*String p = "null";
        if(parent instanceof RecipeTask){
            p = ((RecipeTask) parent).toStringOnlyRecipe();
        }*/
        return "RecipeTask{" +
                "attempts=" + attempts +
                ", recipe=" + RetroStorage.recipeToString(recipe) +
                ", parent=" + ((parent instanceof RecipeTask) ? RetroStorage.recipeToString(((RecipeTask) parent).recipe) : "null") +
                ", requires=" + ((requires == null || requires.isEmpty()) ? "no" : "yes") +
                ", reqMet="+requirementsMet()+
                ", completed=" + completed +
                ", processor=" + processor +
                '}';
    }

    public String toStringOnlyRecipe(){
        return "RecipeTask{" +
                "recipe=" + RetroStorage.recipeToString(recipe) +
                ")";
    }
}
