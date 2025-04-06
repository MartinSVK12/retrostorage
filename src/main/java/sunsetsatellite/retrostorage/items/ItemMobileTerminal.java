package sunsetsatellite.retrostorage.items;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.retrostorage.ReSItems;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalFluidTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;

import static sunsetsatellite.retrostorage.RetroStorage.key;

public class ItemMobileTerminal extends Item implements ICustomDescription {

    public ItemMobileTerminal(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack itemstack, Player entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        TileEntity tile = world.getTileEntity(blockX, blockY, blockZ);
        if (tile != null) {
            if ((tile.getClass() == TileEntityDigitalTerminal.class && this == ReSItems.mobileTerminal)){ //|| (tile.getClass() == TileEntityRequestTerminal.class && this == RetroStorage.mobileRequestTerminal) || (tile.getClass() == TileEntityDigitalFluidTerminal.class && this == RetroStorage.mobileFluidTerminal)) {
                CompoundTag positionNBT = (new CompoundTag());
                positionNBT.putInt("x", blockX);
                positionNBT.putInt("y", blockY);
                positionNBT.putInt("z", blockZ);
                itemstack.getData().putCompound("position", positionNBT);
                entityplayer.sendTranslatedChatMessage("action.retrostorage.terminalBound");
            }
        } else {
            if (entityplayer.isSneaking()) {
                itemstack.getData().getValue().remove("position");
                entityplayer.sendTranslatedChatMessage("action.retrostorage.terminalUnbound");
            }
        }
        return true;
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
        CompoundTag positionNBT = itemstack.getData().getCompound("position");
        TileEntity tile = world.getTileEntity(positionNBT.getInteger("x"), positionNBT.getInteger("y"), positionNBT.getInteger("z"));
        if (itemstack.getItem().equals(ReSItems.mobileTerminal)) {
            if (tile != null) {
                Catalyst.displayGui(entityplayer, tile, key("gui/digital_terminal"));
            }
        }else if (itemstack.getItem().equals(ReSItems.mobileFluidTerminal)) {
            if (tile != null) {
                Catalyst.displayGui(entityplayer, tile, key("gui/digital_fluid_terminal"));
            }
        } else if (itemstack.getItem().equals(ReSItems.mobileRequestTerminal)) {
            if (tile != null) {
                Catalyst.displayGui(entityplayer, tile, key("gui/request_terminal"));
            }
        }

        return super.onUseItem(itemstack, world, entityplayer);
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
