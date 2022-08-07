package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapelessRecipes implements IRecipe {
	private final ItemStack recipeOutput;
	private final List recipeItems;

	public ShapelessRecipes(ItemStack itemStack1, List list2) {
		this.recipeOutput = itemStack1;
		this.recipeItems = list2;
	}

	public ItemStack getRecipeOutput() {
		return this.recipeOutput;
	}

	public ArrayList<?> getRecipeItems() {
		ArrayList arraylist = new ArrayList(recipeItems);
		arraylist.ensureCapacity(9);
		for(int i = arraylist.size();i<9;i++){
			arraylist.add(i,null);
		}
		return arraylist;
	}

	public boolean matchesArray(ArrayList<ItemStack> list) {
		//System.out.println(list);
		if (list.isEmpty()){
			return false;
		}
		ArrayList<ItemStack> arraylist = (ArrayList<ItemStack>) getRecipeItems();
		//System.out.println(arraylist);
		if (list.size() != arraylist.size()){
			return false;
		}
		int n = 0;
		for(int j = 0; j < list.size(); j++)
		{
			ItemStack itemstack = list.get(j);//inventorycrafting.func_21103_b(j, i);
			for (int i = 0;i < arraylist.size();i++) {
				ItemStack itemstack1 = arraylist.get(i);
				if (itemstack1 == null && itemstack == null) {
					//System.out.println("Matching (null)");
					n++;
					arraylist.remove(i);
					continue;
				} else if(itemstack != null && itemstack1 != null) {
					//System.out.println(itemstack.toString() + " vs " + itemstack1.toString());
					if(itemstack.itemID != itemstack1.itemID || (itemstack1.getItemDamage() != -1 && itemstack.getItemDamage() != itemstack1.getItemDamage()))
					{
						continue;
					} else {
						//System.out.println("Matching");
						n++;
						arraylist.remove(i);
					}
				} else {
					continue;
				}
			}

		}
		//System.out.println("n: "+n);
		//System.out.println(getRecipeItems().size());
		if (n == getRecipeItems().size()) {
			return true;
		} else {
			return false;
		}
	}


	public boolean matches(InventoryCrafting inventoryCrafting1) {
		ArrayList arrayList2 = new ArrayList(this.recipeItems);

		for(int i3 = 0; i3 < 3; ++i3) {
			for(int i4 = 0; i4 < 3; ++i4) {
				ItemStack itemStack5 = inventoryCrafting1.func_21103_b(i4, i3);
				if(itemStack5 != null) {
					boolean z6 = false;
					Iterator iterator7 = arrayList2.iterator();

					while(iterator7.hasNext()) {
						ItemStack itemStack8 = (ItemStack)iterator7.next();
						if(itemStack5.itemID == itemStack8.itemID && (itemStack8.getItemDamage() == -1 || itemStack5.getItemDamage() == itemStack8.getItemDamage())) {
							z6 = true;
							arrayList2.remove(itemStack8);
							break;
						}
					}

					if(!z6) {
						return false;
					}
				}
			}
		}

		return arrayList2.isEmpty();
	}

	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting1) {
		return this.recipeOutput.copy();
	}

	public int getRecipeSize() {
		return this.recipeItems.size();
	}
}
