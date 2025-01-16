package sunsetsatellite.retrostorage.item;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.ArrayList;

public class AdvRecipeDiscItem extends TemplateItem implements CustomTooltipProvider {

    public AdvRecipeDiscItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public String[] getTooltip(ItemStack stack, String s) {
        StringBuilder text = new StringBuilder();
        if (!stack.getStationNbt().getCompound("disc").values().isEmpty()) {
            text.append(Formatting.LIGHT_PURPLE).append(stack.getStationNbt().getCompound("disc").getCompound("tasks").values().size()).append(" steps.").append("\n");
        } else if (stack.getStationNbt().getCompound("disc").values().isEmpty()) {
            text.append(Formatting.GRAY).append("Empty");
        }
        NbtCompound tasksNBT = stack.getStationNbt().getCompound("disc").getCompound("tasks");
        ArrayList<NbtCompound> tasks = new ArrayList<>();
        for (Object value : tasksNBT.values()) {
            tasks.add((NbtCompound) value);
        }
        ItemStack output = RetroStorage.getFirstOutputOfProcess(tasks);
        if (output != null) {
            String name = TranslationStorage.getInstance().getClientTranslation(output.getTranslationKey());
            text.append(Formatting.LIGHT_PURPLE).append("Output: ").append(output.count).append("x ").append(name);
        }
        return new String[]{s,text.toString()};
    }
}
