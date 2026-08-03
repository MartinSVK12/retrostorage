package sunsetsatellite.retrostorage.items;


import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.screens.ScreenCraftingProcess;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.ArrayList;

public class ItemAdvRecipeDisc extends Item implements ICustomDescription {


    public ItemAdvRecipeDisc(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "";
    }

	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		if(!EnvironmentHelper.isMultiplayerServer()){
			Minecraft.getMinecraft().displayScreen(new ScreenCraftingProcess(new CraftingProcess(stack.getData().getCompound("disc"))));
		}
		return super.onUse(stack, world, player);
	}

    @Override
    public String getPersistentDescription(ItemStack stack) {
        StringBuilder text = new StringBuilder();
        if (!stack.getData().getCompound("disc").getValues().isEmpty()) {
            text.append(TextFormatting.MAGENTA).append(stack.getData().getCompound("disc").getCompound("tasks").getValues().size()).append(" steps.").append("\n");
        } else if (stack.getData().getCompound("disc").getValues().isEmpty()) {
            text.append(TextFormatting.GRAY).append("Empty");
        }
        CompoundTag tasksNBT = stack.getData().getCompound("disc").getCompound("tasks");
        ArrayList<CompoundTag> tasks = new ArrayList<>();
        for (Tag<?> value : tasksNBT.getValues()) {
            tasks.add((CompoundTag) value);
        }
        ItemStack output = RetroStorage.getFirstOutputOfProcess(tasks);
        if (output != null) {
            String name = I18n.getInstance().translateKey(output.getItemKey() + ".name");
            text.append(TextFormatting.MAGENTA).append("Output: ").append(output.stackSize).append("x ").append(name);
        }
        return text.toString();
    }

    /*@Override
    public CompoundTag getDefaultTag() {
        CompoundTag nbt = new CompoundTag();
        nbt.putCompound("disc",new CompoundTag());
        nbt.putBoolean("overrideColor",true);
        nbt.putByte("color", (byte) 0x2);
        return nbt;
    }*/
}
