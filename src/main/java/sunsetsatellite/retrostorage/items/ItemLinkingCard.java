package sunsetsatellite.retrostorage.items;

import net.minecraft.src.*;
import sunsetsatellite.retrostorage.tiles.TileEntityWirelessLink;

public class ItemLinkingCard extends Item {

    public ItemLinkingCard(int i) {
        super(i);

    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        return itemstack;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, double heightPlaced)
    {
        TileEntityWirelessLink link = (TileEntityWirelessLink) world.getBlockTileEntity(i, j, k);
        if (link != null) {
            if(link.remoteLink != null){
                link.remoteLink.remoteLink = null;
                link.remoteLink = null;
                entityplayer.addChatMessage("action.retrostorage.linkBroken");
                return true;
            }
            NBTTagCompound data = itemstack.tag.getCompoundTag("position");
            if (!(data.hasKey("x") && data.hasKey("y") && data.hasKey("z"))){
                NBTTagCompound positionNBT = (new NBTTagCompound());
                positionNBT.setInteger("x",i);
                positionNBT.setInteger("y",j);
                positionNBT.setInteger("z",k);
                itemstack.tag.setCompoundTag("position",positionNBT);
                entityplayer.addChatMessage("action.retrostorage.cardBound");
            } else {
                TileEntity tile = world.getBlockTileEntity(data.getInteger("x"),data.getInteger("y"),data.getInteger("z"));
                if(tile instanceof TileEntityWirelessLink){
                    link.remoteLink = (TileEntityWirelessLink) tile;
                    link.remoteLink.remoteLink = link;
                    entityplayer.addChatMessage("action.retrostorage.linkEstablished");
                } else {
                    entityplayer.addChatMessage("action.retrostorage.linkInvalidBlock");
                }

            }
        } else {
            if (entityplayer.isSneaking()) {
                itemstack.tag.setCompoundTag("position",new NBTTagCompound());
                entityplayer.addChatMessage("action.retrostorage.cardUnbound");
            }
        }
        return true;
    }

}
