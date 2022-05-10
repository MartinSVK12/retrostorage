package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.Random;

public class BlockTesting extends Block {

	public BlockTesting(int i, Material material) {
		super(i, material);
		
	}

	public BlockTesting(int i, int j, Material material) {
		super(i, j, material);
    }
    
    
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
		if(entityplayer.getCurrentEquippedItem() != null) {
			if (entityplayer.inventory.getStackInSlot(0) != null) {
				entityplayer.addChatMessage(
						String.valueOf(entityplayer.getCurrentEquippedItem().getItemData().equals(entityplayer.inventory.getStackInSlot(0).getItemData()))
				);
			}
		}
		//entityplayer.addChatMessage("Block activated!");
		/*CraftingManager crafter = CraftingManager.getInstance();
		if(entityplayer.getCurrentEquippedItem() != null) {
			if (entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.recipeDisc) {
				ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(entityplayer.getCurrentEquippedItem().getItemData());
				entityplayer.addChatMessage("R: "+recipe.toString());
				ItemStack output = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
				if (output != null) {
					entityplayer.addChatMessage("O: "+output.toString());
					int s = 0;
					for(int i1 = 0;i1<recipe.size();i1++) {
						if (entityplayer.inventory.getStackInSlot(0) != null) {
							if (entityplayer.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc) {
								if (recipe.get(i1) != null) {
									if(DiscManipulator.testDecreaseItemAmountOnDisc(entityplayer.inventory.getStackInSlot(0),(ItemStack) recipe.get(i1))) {
										s++;
									}
								} else {
									s++;
								}
							}
						}
						
					}
					System.out.println(s);
					if (s == recipe.size()){
						for(int i2 = 0;i2<recipe.size();i2++) {
							if (entityplayer.inventory.getStackInSlot(0) != null) {
								if (entityplayer.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc) {
									if (recipe.get(i2) != null) {
										DiscManipulator.decreaseItemAmountOnDisc(entityplayer.inventory.getStackInSlot(0),(ItemStack) recipe.get(i2));
									}
								}
							
							}
						}
						System.out.println(output.stackSize);
						System.out.println("dropped");
						//entityplayer.entityDropItem(output, 0);
					}
				}else {
					entityplayer.addChatMessage("O: null");
				}
				
			}else if (entityplayer.getCurrentEquippedItem().getItem() instanceof ItemStorageDisc) {
				System.out.println(DiscManipulator.decreaseItemAmountOnDisc(entityplayer.getCurrentEquippedItem(), new ItemStack(Block.stone,1)));
			}
		}*/

		/*List<?> list = crafter.getRecipeList();
		for (int y = 0;y < list.size();y++) {
			IRecipe r = (IRecipe) list.get(y);
			ArrayList<?> items = r.getRecipeItems();
			for (int x = 0;x < items.size();x++) {
				ItemStack item = (ItemStack) items.get(x);
				if (item != null) {
					System.out.println(x + ": " + item.getItemName());
				}else {
					System.out.println(x + ": null");
				}
				
			}
	    	System.out.println("r: "+((IRecipe) r).getRecipeOutput().getItemName());
		}*/
		
    	return true;
		/*if(world.multiplayerWorld)
        {
            return true;
        } else
        {
            entityplayer.displayWorkbenchGUI(i, j, k);
            return true;
        }*/
    }
    
    public int idDropped(int i, Random random)
    {
        return mod_RetroStorage.testingBlock.blockID;
    }
	
	
}
