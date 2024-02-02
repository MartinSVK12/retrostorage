package sunsetsatellite.retrostorage.util;



import com.mojang.nbt.CompoundTag;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;

public class RecipeTask extends Task{
    public RecipeEntryCrafting<?,?> recipe;

    public RecipeTask(RecipeEntryCrafting<?,?> recipe, Task parent, ArrayList<Task> requires) {
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

    @Override
    public ArrayList<Task> getSubtasks(DigitalNetwork network) {
        RecipeEntryCrafting<?,?> recipe = this.recipe;
        ArrayList<Task> subtasks = new ArrayList<>();
        if(recipe != null) {
            ArrayList<ItemStack> inputs = RetroStorage.condenseItemList(RetroStorage.getRecipeItems(recipe));
            ArrayList<ItemStack> missing = network.inventory.hasItemsReturnMissing(inputs);
            if(missing.isEmpty()){
                RetroStorage.LOGGER.debug("No subtasks needed, all items available.");
                return null;
            } else {
                RetroStorage.LOGGER.debug(String.format("Missing %d different items (%s) for %s, creating subtasks..",missing.size(),missing,RetroStorage.recipeToString(recipe)));
                for(ItemStack missingStack : missing) {
                    ArrayList<RecipeEntryCrafting<?,?>> allRecipes = RetroStorage.findRecipesByOutput(missingStack);
                    ArrayList<ArrayList<CompoundTag>> allProcesses = RetroStorage.findProcessesByOutput(missingStack,network);
                    if (!allRecipes.isEmpty()) {
                        ArrayList<RecipeEntryCrafting<?,?>> knownRecipes = network.getAvailableRecipes();
                        allRecipes.retainAll(knownRecipes);
                        if (!allRecipes.isEmpty()) {
                            RecipeEntryCrafting<?,?> subtaskRecipe = allRecipes.get(0);
                            RecipeTask subtask = new RecipeTask(subtaskRecipe,this,null);
                            subtasks.add(subtask);
                        } else if(!allProcesses.isEmpty()) {
                            ArrayList<CompoundTag> subtaskProcess = allProcesses.get(0);
                            ProcessTask subtask = new ProcessTask(subtaskProcess,this,null);
                            subtasks.add(subtask);
                        } else {
                            RetroStorage.LOGGER.error(String.format("Failed to create subtask: Network doesn't know how to craft or process %s!",missingStack));
                        }
                    } else if(!allProcesses.isEmpty()) {
                        ArrayList<CompoundTag> subtaskProcess = allProcesses.get(0);
                        ProcessTask subtask = new ProcessTask(subtaskProcess,this,null);
                        subtasks.add(subtask);
                    } else {
                        RetroStorage.LOGGER.error(String.format("Failed to create subtask: %s can't be crafted nor processed.",missingStack));
                    }
                }
                RetroStorage.LOGGER.debug(String.format("Got %d subtasks.",subtasks.size()));
                return subtasks;
            }
        }
        return null;
    }
}
