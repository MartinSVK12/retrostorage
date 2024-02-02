package sunsetsatellite.retrostorage.util;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
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

    @Override
    public ArrayList<Task> getSubtasks(DigitalNetwork network) {
        ArrayList<Task> subtasks = new ArrayList<>();
        ArrayList<CompoundTag> steps = tasks;
        if(steps != null){
            ArrayList<ItemStack> inputs = new ArrayList<>();
            for(CompoundTag step : steps){
                ItemStack stack = ItemStack.readItemStackFromNbt(step.getCompound("stack"));
                if(stack == null) continue;
                if(!step.getBoolean("isOutput")){
                    inputs.add(stack);
                }
            }
            inputs = RetroStorage.condenseItemList(inputs);
            ArrayList<ItemStack> missing = network.inventory.hasItemsReturnMissing(inputs);
            if(missing.isEmpty()){
                RetroStorage.LOGGER.debug("No subtasks needed, all items available.");
                return null;
            } else {
                RetroStorage.LOGGER.debug(String.format("Missing %d different items (%s), creating subtasks..",missing.size(),missing));
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
