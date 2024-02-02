package net.sunsetsatellite.retrostorage.items;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.tiles.TileEntityWirelessLink;

public class ItemLinkingCard extends ItemReS {

    public ItemLinkingCard(int i) {
        super(i);

    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        TileEntityWirelessLink link = (TileEntityWirelessLink) world.getBlockTileEntity(i, j, k);
        if (link != null) {
            if(link.remoteLink != null){
                link.remoteLink.remoteLink = null;
                link.remoteLink = null;
                entityplayer.addChatMessage("action.retrostorage.linkBroken");
                return true;
            }
            if(!itemstack.hasTagCompound()){
                itemstack.stackTagCompound = new NBTTagCompound();
            }
            NBTTagCompound data = itemstack.stackTagCompound.getCompoundTag("position");
            if (!(data.hasKey("x") && data.hasKey("y") && data.hasKey("z"))){
                NBTTagCompound positionNBT = (new NBTTagCompound());
                positionNBT.setInteger("x",i);
                positionNBT.setInteger("y",j);
                positionNBT.setInteger("z",k);
                itemstack.stackTagCompound.setCompoundTag("position",positionNBT);
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
                itemstack.stackTagCompound.setCompoundTag("position",new NBTTagCompound());
                entityplayer.addChatMessage("action.retrostorage.cardUnbound");
            }
        }
        return true;
    }

}
