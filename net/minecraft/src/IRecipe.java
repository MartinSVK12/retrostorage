package net.minecraft.src;

import java.util.ArrayList;

public interface IRecipe {
	boolean matches(InventoryCrafting inventoryCrafting1);

	ItemStack getCraftingResult(InventoryCrafting inventoryCrafting1);

	int getRecipeSize();

	ItemStack getRecipeOutput();

	boolean matchesArray(ArrayList<ItemStack> list);
}
