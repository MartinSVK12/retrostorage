package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class ItemLinkingCard extends Item {

	public ItemLinkingCard(int i) {
		super(i);
		
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		return itemstack;
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
		if(world.multiplayerWorld)
		{
			return true;
		} else
		{
			TileEntityWirelessLink link = (TileEntityWirelessLink) world.getBlockTileEntity(i, j, k);
			if (link != null) {
				if(link.remoteLink != null){
					link.remoteLink.remoteLink = null;
					link.remoteLink = null;
					entityplayer.addChatMessage("Link broken!");
					return true;
				}
				NBTTagCompound data = itemstack.getItemData();
				if (!(data.hasKey("x") && data.hasKey("y") && data.hasKey("z"))){
					NBTTagCompound positionNBT = (new NBTTagCompound());
					positionNBT.setInteger("x",i);
					positionNBT.setInteger("y",j);
					positionNBT.setInteger("z",k);
					itemstack.setItemData(positionNBT);
					entityplayer.addChatMessage("Card bound to block at "+String.valueOf(i)+", "+String.valueOf(j)+", "+String.valueOf(k)+"!");
				} else {
					TileEntity tile = world.getBlockTileEntity(data.getInteger("x"),data.getInteger("y"),data.getInteger("z"));
					if(tile instanceof TileEntityWirelessLink){
						link.remoteLink = (TileEntityWirelessLink) tile;
						link.remoteLink.remoteLink = link;
						entityplayer.addChatMessage("Link established!");
					} else {
						entityplayer.addChatMessage("Link failed! Card points to invalid block.");
					}

				}
			} else {
				if (entityplayer.isSneaking()) {
					itemstack.setItemData(new NBTTagCompound());
					entityplayer.addChatMessage("Card unbound!");
				}
			}
			return true;
		}
    }

}
