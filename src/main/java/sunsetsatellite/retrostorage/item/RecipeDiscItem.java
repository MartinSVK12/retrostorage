package sunsetsatellite.retrostorage.item;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;
import java.util.List;

public class RecipeDiscItem extends TemplateItem implements CustomTooltipProvider {
    public RecipeDiscItem(String identifier) {
        super(RetroStorage.NAMESPACE.id(identifier));
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack itemStack, String s) {
        List<String> list = new ArrayList<>();
        list.add(s);
        ItemStack result = RetroStorage.findRecipeResultFromNBT(itemStack.getStationNbt().getCompound("recipe"));
        if (result != null) {
            list.add(Formatting.LIGHT_PURPLE + "Output: " + result.count + "x " + TranslationStorage.getInstance().getClientTranslation(result.getTranslationKey()) + Formatting.WHITE);
        } else {
            list.add(Formatting.GRAY + "Empty" + Formatting.WHITE);
        }
        return list.toArray(new String[0]);
    }
}
