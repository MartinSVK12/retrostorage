package net.sunsetsatellite.retrostorage.items;

import net.minecraft.src.IRecipe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;
import net.sunsetsatellite.retrostorage.mod_RetroStorage;
import net.sunsetsatellite.retrostorage.util.RecipeTask;

import java.util.List;

public class ItemRecipeDisc extends ItemReS{
    public ItemRecipeDisc(int id) {
        super(id);
    }

    @Override
    public void addInformation(ItemStack itemStack, List list) {
        if(itemStack.hasTagCompound()){
            IRecipe recipe = mod_RetroStorage.findRecipeFromNBT(itemStack.stackTagCompound.getCompoundTag("recipe"));
            if(recipe != null){
                StringTranslate trans = StringTranslate.getInstance();
                String name = trans.translateKey(recipe.getRecipeOutput().getItem().getItemNameIS(recipe.getRecipeOutput()) + ".name");
                list.add("Makes: "+recipe.getRecipeOutput().stackSize+"x "+name);
            }
        } else {
            list.add("No recipe.");
        }


    }
}
