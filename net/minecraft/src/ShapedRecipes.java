// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;

// Referenced classes of package net.minecraft.src:
//            IRecipe, ItemStack, InventoryCrafting

public class ShapedRecipes
    implements IRecipe
{

    public ShapedRecipes(int i, int j, ItemStack aitemstack[], ItemStack itemstack)
    {
        recipeOutputItemID = itemstack.itemID;
        recipeWidth = i;
        recipeHeight = j;
        recipeItems = aitemstack;
        recipeOutput = itemstack;
    }

    public ItemStack getRecipeOutput()
    {
        return recipeOutput;
    }
    
    public ArrayList<?> getRecipeItems() {
    	ArrayList arraylist = new ArrayList(Arrays.asList(recipeItems));
    	arraylist.ensureCapacity(9);
    	for(int i = 0;i<9;i++) {
    		if(arraylist.size() < 9) {
    			arraylist.add(null);
    		}
    	}
    	return arraylist;
    }
    
    public ArrayList<?> applyWidthHeightToRecipeArray(ArrayList<?>list){
    	if (recipeWidth == 3 && recipeHeight == 3) {
    		return list;
    	}
    	ArrayList correctArraylist = new ArrayList();
    	correctArraylist.ensureCapacity(9);
    	for(int x = 0;x<9;x++) {
    		correctArraylist.add(null);
    	}
    	int index = 0;
    	int shifted_index = 0;
		for(int i = 0;i<list.size();i++) {
			if (i > recipeWidth*recipeHeight) {
				break;
			}
			if (shifted_index > 8) {
				correctArraylist.set(i,list.get(i));
				continue;
			}
			if(list.get(i) == null) {
				correctArraylist.set(shifted_index,list.get(i));
				shifted_index++;
				index++;
				continue;
			}
			if(index>(recipeWidth-1)) {
				shifted_index += 3-recipeWidth;
				index = 0;
			}
			correctArraylist.set(shifted_index,list.get(i));
			shifted_index++;
			index++;
		}
		return correctArraylist;
    }

    public boolean matches(InventoryCrafting inventorycrafting)
    {
        for(int i = 0; i <= 3 - recipeWidth; i++)
        {
            for(int j = 0; j <= 3 - recipeHeight; j++)
            {
                if(func_21137_a(inventorycrafting, i, j, true))
                {
                    return true;
                }
                if(func_21137_a(inventorycrafting, i, j, false))
                {
                    return true;
                }
            }

        }

        return false;
    }
    
    public boolean matchesArray(ArrayList<ItemStack> list) {
    	ArrayList<ItemStack> recipeList = (ArrayList<ItemStack>) getRecipeItems();
    	recipeList = (ArrayList<ItemStack>) applyWidthHeightToRecipeArray(recipeList);
    	/*if(recipeOutput.getItem() == Item.shovelSteel) {
    		System.out.println(recipeList.toString() + " vs " + list.toString());
    	}*/
    	for(int i = 0;i < list.size();i++) {
    		ItemStack itemstack1 = list.get(i);
    		ItemStack itemstack = recipeList.get(i);
    		 if(itemstack1 == null && itemstack == null)
             {
                 continue;
             }
             if(itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
             {
                 return false;
             }
             //System.out.println(itemstack.toString() + " vs " + itemstack1.toString());
             if(itemstack.itemID != itemstack1.itemID)
             {
                 return false;
             }
             if(itemstack.getItemDamage() != -1 && itemstack.getItemDamage() != itemstack1.getItemDamage())
             {
                 return false;
             }
    	}

        return true;
    }

    private boolean func_21137_a(InventoryCrafting inventorycrafting, int i, int j, boolean flag)
    {
        for(int k = 0; k < 3; k++)
        {
            for(int l = 0; l < 3; l++)
            {
                int i1 = k - i;
                int j1 = l - j;
                ItemStack itemstack = null;
                if(i1 >= 0 && j1 >= 0 && i1 < recipeWidth && j1 < recipeHeight)
                {
                    if(flag)
                    {
                        itemstack = recipeItems[(recipeWidth - i1 - 1) + j1 * recipeWidth];
                    } else
                    {
                        itemstack = recipeItems[i1 + j1 * recipeWidth];
                    }
                }
                ItemStack itemstack1 = inventorycrafting.func_21103_b(k, l);
                if(itemstack1 == null && itemstack == null)
                {
                    continue;
                }
                if(itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
                {
                    return false;
                }
                if(itemstack.itemID != itemstack1.itemID)
                {
                    return false;
                }
                if(itemstack.getItemDamage() != -1 && itemstack.getItemDamage() != itemstack1.getItemDamage())
                {
                    return false;
                }
            }

        }

        return true;
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
    {
        return new ItemStack(recipeOutput.itemID, recipeOutput.stackSize, recipeOutput.getItemDamage());
    }

    public int getRecipeSize()
    {
        return recipeWidth * recipeHeight;
    }

    private int recipeWidth;
    private int recipeHeight;
    private ItemStack recipeItems[];
    private ItemStack recipeOutput;
    public final int recipeOutputItemID;
}
