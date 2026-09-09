package sunsetsatellite.retrostorage.api.impl.bemi;

import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.EmiRecipeHandler;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiShapedRecipe;
import dev.emi.emi.recipe.EmiShapelessRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.menus.MenuRecipeEncoder;
import sunsetsatellite.retrostorage.mp.PacketQuickRecipeEncode;
import sunsetsatellite.retrostorage.screens.ScreenRecipeEncoder;
import sunsetsatellite.retrostorage.tiles.TileEntityRecipeEncoder;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.stream.Collectors;

public class EncoderRecipeFiller implements EmiRecipeHandler<MenuRecipeEncoder> {
	@Override
	public EmiPlayerInventory getInventory(ScreenContainerAbstract screen) {
		return new EmiPlayerInventory(screen.inventorySlots.slots.stream().map(Slot::getItemStack).map(EmiStack::of).collect(Collectors.toList()));
	}

	@Override
	public boolean supportsRecipe(EmiRecipe recipe) {
		return recipe instanceof EmiShapedRecipe || recipe instanceof EmiShapelessRecipe;
	}

	@Override
	public boolean canCraft(EmiRecipe recipe, EmiCraftContext<MenuRecipeEncoder> context) {
		return true;
	}

	@Override
	public boolean craft(EmiRecipe recipe, EmiCraftContext<MenuRecipeEncoder> context) {
		Minecraft.getMinecraft().displayScreen(context.getScreen());
		TileEntityRecipeEncoder tile = ((ScreenRecipeEncoder) context.getScreen()).tile;
		if (recipe instanceof EmiShapedRecipe || recipe instanceof EmiShapelessRecipe) {
			if(recipe.getId() != null){
				if(tile.worldObj != null && tile.worldObj.isClientSide){
					NetworkHandler.sendToServer(new PacketQuickRecipeEncode(tile.tilePos.x, tile.tilePos.y, tile.tilePos.z, recipe.getId().toString()));
				} else {
					try {
						tile.encodeDisc((RecipeEntryCrafting<?, ItemStack>) Registries.RECIPES.getRecipeFromKey(recipe.getId().toString()).recipe);
					} catch (IllegalArgumentException | ClassCastException ignored) {}
				}
			}
		}
		return true;
	}
}
