package sunsetsatellite.retrostorage.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Formatting;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.screen.CraftingProcessScreen;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;

import java.util.ArrayList;
import java.util.List;

public class AdvRecipeDiscItem extends TemplateItem implements CustomTooltipProvider {
    public AdvRecipeDiscItem(String identifier) {
        super(RetroStorage.NAMESPACE.id(identifier));
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack itemStack, String s) {
        List<String> list = new ArrayList<>();
        list.add(s);

        NbtCompound disc = itemStack.getStationNbt().getCompound("disc");
        NbtCompound tasks = disc.getCompound("tasks");
        if (!disc.values().isEmpty()) {
            list.add(Formatting.LIGHT_PURPLE + String.valueOf(tasks.values().size()) + " steps.");
        } else {
            list.add(Formatting.GRAY + "Empty" + Formatting.WHITE);
        }

        ArrayList<NbtCompound> tags = new ArrayList<>();
        for (Object value : tasks.values()) {
            tags.add((NbtCompound) value);
        }
        ItemStack output = RetroStorage.getFirstOutputOfProcess(tags);
        if (output != null) {
            String name = TranslationStorage.getInstance().getClientTranslation(output.getTranslationKey());
            list.add(Formatting.LIGHT_PURPLE + "Output: " + output.count + "x " + name);
        }

        return list.toArray(new String[0]);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        Minecraft.INSTANCE.setScreen(new CraftingProcessScreen(new CraftingProcess(stack.getStationNbt().getCompound("disc"))));
        return super.use(stack, world, user);
    }
}
