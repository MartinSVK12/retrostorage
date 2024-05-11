

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

    public TileEntityRequestTerminal() {}

    public void tick()
    {
        if(network != null && network.drive != null){
            this.pages = ((network.getAvailableRecipes().size() + network.getAvailableProcesses().size())/36)+1;
        } else {
            page = 1;
            pages = 1;
        }
    }


    public int page = 1;
    public int pages = 1;
}
