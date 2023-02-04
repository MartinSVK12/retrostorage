package sunsetsatellite.retrostorage.items;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.containers.ContainerPlayerExtra;
import sunsetsatellite.retrostorage.gui.GuiDigitalTerminal;
import sunsetsatellite.retrostorage.gui.GuiPlayerExtra;
import sunsetsatellite.retrostorage.gui.GuiRequestTerminal;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.InventoryPortable;

public class ItemMobileTerminal extends Item {
    public ItemMobileTerminal(int i) {
        super(i);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, double heightPlaced) {
        TileEntity tile = world.getBlockTileEntity(i,j,k);
        if(tile != null){
            if((tile.getClass() == TileEntityDigitalTerminal.class && this == RetroStorage.mobileTerminal) || (tile.getClass() == TileEntityRequestTerminal.class && this == RetroStorage.mobileRequestTerminal)){
                NBTTagCompound positionNBT = (new NBTTagCompound());
                positionNBT.setInteger("x",i);
                positionNBT.setInteger("y",j);
                positionNBT.setInteger("z",k);
                itemstack.tag.setCompoundTag("position",positionNBT);
                entityplayer.addChatMessage("action.retrostorage.terminalBound");
            }
        }
        return true;
        //return super.onItemUse(itemstack, entityplayer, world, i, j, k, l, heightPlaced);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        NBTTagCompound positionNBT = itemstack.tag.getCompoundTag("position");
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
}
