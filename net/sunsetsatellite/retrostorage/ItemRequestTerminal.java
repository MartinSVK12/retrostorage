package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class ItemRequestTerminal extends Item {

	public ItemRequestTerminal(int i) {
		super(i);
		
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		NBTTagCompound data = itemstack.getItemData();
		if (data.hasKey("x") && data.hasKey("y") && data.hasKey("z")){
			TileEntityRequestTerminal TileEntityRequestTerminal = (TileEntityRequestTerminal)world.getBlockTileEntity(data.getInteger("x"), data.getInteger("y"), data.getInteger("z"));
			if (TileEntityRequestTerminal != null) {
				ModLoader.OpenGUI(entityplayer, new GuiRequestTerminal(entityplayer.inventory, TileEntityRequestTerminal));
			}
		} else {
			entityplayer.addChatMessage("Terminal not bound to any block!");
		}
		return itemstack;
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
		if(world.multiplayerWorld)
		{
			return true;
		} else
		{
			TileEntityRequestTerminal TileEntityRequestTerminal = (TileEntityRequestTerminal)world.getBlockTileEntity(i, j, k);
			if (TileEntityRequestTerminal != null) {
				NBTTagCompound positionNBT = (new NBTTagCompound());
				positionNBT.setInteger("x",i);
				positionNBT.setInteger("y",j);
				positionNBT.setInteger("z",k);
				itemstack.setItemData(positionNBT);
				entityplayer.addChatMessage("Terminal bound to block at "+String.valueOf(i)+", "+String.valueOf(j)+", "+String.valueOf(k)+"!");
				//ModLoader.OpenGUI(entityplayer, new GuiDigitalTerminal(entityplayer.inventory, TileEntityRequestTerminal));
			} else {
				if (entityplayer.isSneaking()) {
					itemstack.setItemData(new NBTTagCompound());
					entityplayer.addChatMessage("Terminal unbound!");
				}
			}
			return true;
		}
    }

}
