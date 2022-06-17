package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class ItemTerminal extends Item {

	public ItemTerminal(int i) {
		super(i);
		
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		NBTTagCompound data = itemstack.getItemData();
		if (data.hasKey("x") && data.hasKey("y") && data.hasKey("z")){
			TileEntityDigitalTerminal TileEntityDigitalTerminal = (TileEntityDigitalTerminal)world.getBlockTileEntity(data.getInteger("x"), data.getInteger("y"), data.getInteger("z"));
			if (TileEntityDigitalTerminal != null) {
				ModLoader.OpenGUI(entityplayer, new GuiDigitalTerminal(entityplayer.inventory, TileEntityDigitalTerminal));
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
			TileEntityDigitalTerminal TileEntityDigitalTerminal = (TileEntityDigitalTerminal)world.getBlockTileEntity(i, j, k);
			if (TileEntityDigitalTerminal != null) {
				NBTTagCompound positionNBT = (new NBTTagCompound());
				positionNBT.setInteger("x",i);
				positionNBT.setInteger("y",j);
				positionNBT.setInteger("z",k);
				itemstack.setItemData(positionNBT);
				entityplayer.addChatMessage("Terminal bound to block at "+String.valueOf(i)+", "+String.valueOf(j)+", "+String.valueOf(k)+"!");
				//ModLoader.OpenGUI(entityplayer, new GuiDigitalTerminal(entityplayer.inventory, TileEntityDigitalTerminal));
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
