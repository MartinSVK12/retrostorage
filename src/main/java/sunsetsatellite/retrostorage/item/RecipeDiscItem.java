package sunsetsatellite.retrostorage.item;


import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;
import sunsetsatellite.retrostorage.RetroStorage;

public class RecipeDiscItem  extends TemplateItem implements CustomTooltipProvider {

    public RecipeDiscItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public String[] getTooltip(ItemStack itemStack, String s) {
        ItemStack result = RetroStorage.findRecipeResultFromNBT(itemStack.getStationNbt().getCompound("recipe"));
        if (result != null) {
            return new String[]{s,Formatting.LIGHT_PURPLE + "Output: " + result.count + "x " + TranslationStorage.getInstance().getClientTranslation(result.getTranslationKey()) + Formatting.WHITE};
        }
        return new String[]{s,Formatting.GRAY + "Empty" + Formatting.WHITE};
    }
}
