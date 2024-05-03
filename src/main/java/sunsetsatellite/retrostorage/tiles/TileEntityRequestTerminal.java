

package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;

import net.minecraft.core.crafting.legacy.recipe.IRecipe;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityRequestTerminal extends TileEntityNetworkDevice {

    public TileEntityRequestTerminal()
    {
        /*contents = new ItemStack[37];
        recipeContents = new Object[37];
        saveTimer = new TickTimer(this,this::save,40,true);*/
    }

    /*public void save(){
        if(network != null){
            if(getStackAmount() == 0){
                int i = 1;
                ArrayList<RecipeEntryCrafting<?,?>> recipes = network.getAvailableRecipes();
                ArrayList<ArrayList<CompoundTag>> processes = network.getAvailableProcesses();
                ArrayList<Object> allCraftables = new ArrayList<>();
                allCraftables.addAll(recipes);
                allCraftables.addAll(processes);
                List<Object> pageCraftables = allCraftables.subList(((page-1)*36),Math.min(allCraftables.size(),page*36));
                for (Object craftable : pageCraftables) {
                    if(craftable instanceof RecipeEntryCrafting<?,?>){
                        setInventorySlotContents(i, (ItemStack) ((RecipeEntryCrafting<?,?>)craftable).getOutput());
                        recipeContents[i] = craftable;
                        i++;
                    } else if (craftable instanceof ArrayList) {
                        setInventorySlotContents(i, RetroStorage.getMainOutputOfProcess((ArrayList<CompoundTag>) craftable));
                        recipeContents[i] = craftable;
                        i++;
                    }
                }
            } else {
                Arrays.fill(contents, null);
                Arrays.fill(recipeContents,null);
                save();
            }
        } else {
            Arrays.fill(contents, null);
            Arrays.fill(recipeContents,null);
        }
    }*/

    public void tick()
    {
        if(network != null && network.drive != null){
            this.pages = ((network.getAvailableRecipes().size())/36)+1;
        }
    }


    public int page = 1;
    public int pages = 1;
}
