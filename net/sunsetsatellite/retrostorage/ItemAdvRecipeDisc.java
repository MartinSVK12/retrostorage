package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.HashMap;

public class ItemAdvRecipeDisc extends Item
    implements IDataItem
{

    public ItemAdvRecipeDisc(int i)
    {
        super(i);
    }
    
    
    public Item setMaxStackCapacity(int i) {
    	return this;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        NBTTagCompound data = itemstack.getItemData();
        if(data.size() != 0 && data.hasKey("processName") && data.hasKey("tasks")){
            System.out.println("processName: "+data.getString("processName"));
            System.out.println("tasks: "+data.getCompoundTag("tasks").toString());
            NBTTagCompound taskData = data.getCompoundTag("tasks");
            mod_RetroStorage.printCompound(taskData);
        }
        //entityplayer.addChatMessage(itemstack.getItemData().toStringExtended());
        return super.onItemRightClick(itemstack, world, entityplayer);
    }

    @Override
    public String getDescription(ItemStack stack) {
        if(stack.getItemData().hasKey("processName")){
            return "Processes: "+stack.getItemData().getString("processName");
        } else {
            return "";
        }

    }
}
