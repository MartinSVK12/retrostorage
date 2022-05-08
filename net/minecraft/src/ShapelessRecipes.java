// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            IRecipe, InventoryCrafting, ItemStack

public class ShapelessRecipes
    implements IRecipe
{

    public ShapelessRecipes(ItemStack itemstack, List list)
    {
        recipeOutput = itemstack;
        recipeItems = list;
    }

    public ItemStack getRecipeOutput()
    {
        return recipeOutput;
    }
    
    public ArrayList<?> getRecipeItems() {
    	ArrayList arraylist = new ArrayList(recipeItems);
    	return arraylist;
    }

    public boolean matches(InventoryCrafting inventorycrafting)
    {
        ArrayList arraylist = new ArrayList(recipeItems);
        int i = 0;
        do
        {
            if(i >= 3)
            {
                break;
            }
            for(int j = 0; j < 3; j++)
            {
                ItemStack itemstack = inventorycrafting.func_21103_b(j, i);
                if(itemstack == null)
                {
                    continue;
                }
                boolean flag = false;
                Iterator iterator = arraylist.iterator();
                do
                {
                    if(!iterator.hasNext())
                    {
                        break;
                    }
                    ItemStack itemstack1 = (ItemStack)iterator.next();
                    if(itemstack.itemID != itemstack1.itemID || itemstack1.getItemDamage() != -1 && itemstack.getItemDamage() != itemstack1.getItemDamage())
                    {
                        continue;
                    }
                    flag = true;
                    arraylist.remove(itemstack1);
                    break;
                } while(true);
                if(!flag)
                {
                    return false;
                }
            }

            i++;
        } while(true);
        return arraylist.isEmpty();
    }
    
    public boolean matchesArray(ArrayList<ItemStack> list) {
    	 ArrayList<ItemStack> arraylist = (ArrayList<ItemStack>) getRecipeItems();
    	 int n = 0;
	     for(int j = 0; j < list.size(); j++)
	     {
	         ItemStack itemstack = list.get(j);//inventorycrafting.func_21103_b(j, i);
	         if (itemstack == null) {
	        	 continue;
	         }
	         for (int i = 0;i < arraylist.size();i++) {
	        	 ItemStack itemstack1 = arraylist.get(i);
	        	 if (itemstack1 == null) {
	        		 continue;
	        	 }
	        	 //System.out.println(itemstack.toString() + " vs " + itemstack1.toString());
	        	 if(itemstack.itemID != itemstack1.itemID || itemstack1.getItemDamage() != -1 && itemstack.getItemDamage() != itemstack1.getItemDamage())
                 {
                     continue;
                 } else {
                	 n++;
                	 arraylist.remove(i);
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

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
    {
        return recipeOutput.copy();
    }

    public int getRecipeSize()
    {
        return recipeItems.size();
    }

    private final ItemStack recipeOutput;
    private final List recipeItems;
}
