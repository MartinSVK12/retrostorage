package sunsetsatellite.retrostorage.api.impl.tmb.fillers;

import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.retrostorage.mp.PacketQuickRecipeEncode;
import sunsetsatellite.retrostorage.screens.ScreenRecipeEncoder;
import sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;
import turing.tmb.api.RecipeFiller;
import turing.tmb.api.recipe.IRecipeTranslator;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;
import java.util.List;

public class EncoderRecipeFiller implements RecipeFiller<RecipeEntryCrafting<?, ItemStack>, ScreenRecipeEncoder> {

    @Override
    public void fillRecipe(IRecipeTranslator<RecipeEntryCrafting<?,ItemStack>> translator, ScreenRecipeEncoder screen, boolean maximum) {
        TileEntityRecipeEncoder tile = screen.tile;
        if (translator.getOriginal() instanceof RecipeEntryCraftingShaped || translator.getOriginal() instanceof RecipeEntryCraftingShapeless) {
            if(tile.worldObj != null && tile.worldObj.isClientSide){
                NetworkHandler.sendToServer(new PacketQuickRecipeEncode(tile.tilePos.x, tile.tilePos.y, tile.tilePos.z, translator.getOriginal().toString()));
            } else {
                tile.encodeDisc(translator.getOriginal());
            }

        }
    }

    @Override
    public List<Class<? extends RecipeEntryBase<?, ?, ?>>> getSupportedRecipes() {
        ArrayList<Class<? extends RecipeEntryBase<?, ?, ?>>> list = new ArrayList<>();
        list.add(RecipeEntryCraftingShaped.class);
        list.add(RecipeEntryCraftingShapeless.class);
        return list;
    }
}
