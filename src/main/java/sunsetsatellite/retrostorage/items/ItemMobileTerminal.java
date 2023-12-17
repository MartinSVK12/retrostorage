package sunsetsatellite.retrostorage.items;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.gui.GuiDigitalTerminal;
import sunsetsatellite.retrostorage.gui.GuiRequestTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;

public class ItemMobileTerminal extends Item implements ICustomDescription {
    public ItemMobileTerminal(int i) {
        super(i);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        TileEntity tile = world.getBlockTileEntity(blockX,blockY,blockZ);
        if(tile != null){
            if((tile.getClass() == TileEntityDigitalTerminal.class && this == RetroStorage.mobileTerminal) || (tile.getClass() == TileEntityRequestTerminal.class && this == RetroStorage.mobileRequestTerminal)){
                CompoundTag positionNBT = (new CompoundTag());
                positionNBT.putInt("x",blockX);
                positionNBT.putInt("y",blockY);
                positionNBT.putInt("z",blockZ);
                itemstack.getData().putCompound("position",positionNBT);
                entityplayer.addChatMessage("action.retrostorage.terminalBound");
            }
        } else {
            if(entityplayer.isSneaking()){
                itemstack.getData().getValue().remove("position");
                entityplayer.addChatMessage("action.retrostorage.terminalUnbound");
            }
        }
        return true;
        //return super.onItemUse(itemstack, entityplayer, world, i, j, k, l, heightPlaced);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        CompoundTag positionNBT = itemstack.getData().getCompound("position");
        TileEntity tile = world.getBlockTileEntity(positionNBT.getInteger("x"),positionNBT.getInteger("y"),positionNBT.getInteger("z"));
        if(itemstack.getItem() == RetroStorage.mobileTerminal){
            if(tile != null){
                ((IOpenGUI) entityplayer).displayGUI(new GuiDigitalTerminal(entityplayer.inventory, (TileEntityDigitalTerminal) tile));
            }
        } else if(itemstack.getItem() == RetroStorage.mobileRequestTerminal){
            if(tile != null){
                ((IOpenGUI) entityplayer).displayGUI(new GuiRequestTerminal(entityplayer, (TileEntityRequestTerminal) tile));
            }
        }

        return super.onItemRightClick(itemstack, world, entityplayer);
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        CompoundTag pos = itemStack.getData().getCompoundOrDefault("position",null);
        if(pos != null){
            return TextFormatting.MAGENTA+ "Bound to X: "+pos.getInteger("x")+" Y: "+pos.getInteger("y")+" Z: "+pos.getInteger("z")+"!";
        }
        return TextFormatting.GRAY+"Unbound.";
    }
}
