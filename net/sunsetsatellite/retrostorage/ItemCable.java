package net.sunsetsatellite.retrostorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.mod_RetroStorage;

public class ItemCable extends Item {

	public ItemCable(int i) {
		super(i);
		
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
		int pos = world.getBlockId(i, j, k);
		BlockCable cable = (BlockCable) mod_RetroStorage.cable;
		//System.out.println(world.canBlockBePlacedAt(pos, i, j, k, false, l));
		if(world.canBlockBePlacedAt(pos, i, j, k, true, l) && world.setBlockWithNotify(cable.blockID, j, k, l))
        {
            itemstack.stackSize--;
            return true;
        } else
        {
            return false;
        }
    }

}
