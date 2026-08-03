package sunsetsatellite.retrostorage.items;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.retrostorage.ReSItems;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalFluidTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;

import static sunsetsatellite.retrostorage.RetroStorage.key;

public class ItemMobileTerminal extends Item implements ICustomDescription {

    public ItemMobileTerminal(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		TileEntity tile = world.getTileEntity(blockPos);
		if (tile != null) {
			if ((tile.getClass() == TileEntityDigitalTerminal.class && this == ReSItems.mobileTerminal) || (tile.getClass() == TileEntityRequestTerminal.class && this == ReSItems.mobileRequestTerminal) || (tile.getClass() == TileEntityDigitalFluidTerminal.class && this == ReSItems.mobileFluidTerminal)) {
				CompoundTag positionNBT = (new CompoundTag());
				positionNBT.putInt("x", blockPos.x());
				positionNBT.putInt("y", blockPos.y());
				positionNBT.putInt("z", blockPos.z());
				stack.getData().putCompound("position", positionNBT);
				player.sendTranslatedChatMessage("action.retrostorage.terminalBound");
			}
		} else {
			if (player.isSneaking()) {
				stack.getData().getValue().remove("position");
				player.sendTranslatedChatMessage("action.retrostorage.terminalUnbound");
			}
		}
		return true;
	}

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		CompoundTag positionNBT = stack.getData().getCompound("position");
		TileEntity tile = world.getTileEntity(new TilePos(positionNBT.getInteger("x"), positionNBT.getInteger("y"), positionNBT.getInteger("z")));
		if (stack.getItem().equals(ReSItems.mobileTerminal)) {
			if (tile instanceof TileEntityDigitalTerminal) {
				Catalyst.displayGui(player, tile, key("gui/digital_terminal"));
			}
		}else if (stack.getItem().equals(ReSItems.mobileFluidTerminal)) {
			if (tile instanceof TileEntityDigitalFluidTerminal) {
				Catalyst.displayGui(player, tile, key("gui/digital_fluid_terminal"));
			}
		} else if (stack.getItem().equals(ReSItems.mobileRequestTerminal)) {
			if (tile instanceof TileEntityRequestTerminal) {
				Catalyst.displayGui(player, tile, key("gui/request_terminal"));
			}
		}

		return super.onUse(stack, world, player);
	}

    @Override
    public String getDescription(ItemStack itemStack) {
        CompoundTag pos = itemStack.getData().getCompoundOrDefault("position", null);
        if (pos != null) {
            return TextFormatting.MAGENTA + "Bound to X: " + pos.getInteger("x") + " Y: " + pos.getInteger("y") + " Z: " + pos.getInteger("z") + "!";
        }
        return TextFormatting.GRAY + "Unbound.";
    }
}
