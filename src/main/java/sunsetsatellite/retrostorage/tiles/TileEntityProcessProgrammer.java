package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemAdvRecipeDisc;

import java.util.HashMap;

public class TileEntityProcessProgrammer extends TileEntity
    implements IInventory
{

    public TileEntityProcessProgrammer() {
        contents = new ItemStack[2];
    }

    public int getSizeInventory()
    {
        return contents.length;
    }

    public boolean isEmpty() {
        for(int i = 0; i < getSizeInventory()-1; i++) {
            if(getStackInSlot(i) != null) {
                return false;
            } else
            {
                continue;
            }
        }
        return true;
    }

    public ItemStack getStackInSlot(int i)
    {
        return contents[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if(contents[i] != null)
        {
            if(contents[i].stackSize <= j)
            {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if(contents[i].stackSize == 0)
            {
                contents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        contents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }

    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Process Programmer";
    }

    public void readFromNBT(CompoundTag CompoundTag)
    {
        super.readFromNBT(CompoundTag);
        ListTag listTag = CompoundTag.getList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < listTag.tagCount(); i++)
        {
            CompoundTag CompoundTag1 = (CompoundTag)listTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }
    }

    public void writeToNBT(CompoundTag CompoundTag)
    {
        super.writeToNBT(CompoundTag);
        ListTag listTag = new ListTag();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte)i);
                contents[i].writeToNBT(CompoundTag1);
                listTag.addTag(CompoundTag1);
            }
        }

        CompoundTag.put("Items", listTag);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void updateEntity()
    {
        return;
    }

    public void setTask() {
        if(getStackInSlot(0) != null){
            HashMap<String,Object> task = new HashMap<>();
            task.put("slot",currentSlot);
            task.put("stack",getStackInSlot(0).copy());
            task.put("isOutput",isCurrentOutput);
            tasks.put(currentTask,task);
        } else {
            tasks.remove(currentTask);
        }
        //System.out.println(tasks);
    }

    public void clearDisc() {
        if(getStackInSlot(1) != null && getStackInSlot(1).getItem() instanceof ItemAdvRecipeDisc){
            ItemStack disc = getStackInSlot(1);
            disc.getData().putCompound("disc",new CompoundTag());
            disc.getData().putString("name","");
            disc.getData().putBoolean("overrideName",false);
        }
        tasks.clear();
    }

    public void saveProcess() {
        if(getStackInSlot(1) != null && getStackInSlot(1).getItem() instanceof ItemAdvRecipeDisc){
            CompoundTag data = new CompoundTag();
            CompoundTag taskData = new CompoundTag();
            tasks.forEach((K,V) -> {
                CompoundTag task = new CompoundTag();
                task.putInt("id",K);
                task.putInt("slot", (Integer) V.get("slot"));
                task.putBoolean("isOutput", (Boolean) V.get("isOutput"));
                CompoundTag stack = new CompoundTag();
                ((ItemStack)V.get("stack")).writeToNBT(stack);
                task.putCompound("stack",stack);
                taskData.putCompound("task"+K,task);
            });
            getStackInSlot(1).getData().putString("name","Adv. Recipe Disc: "+currentProcessName);
            getStackInSlot(1).getData().putBoolean("overrideName",true);
            data.putCompound("tasks",taskData);
            data.putString("processName",currentProcessName);
            getStackInSlot(1).getData().putCompound("disc",data);
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    public int currentTask = 0;
    public int currentSlot = 0;
    public boolean isCurrentOutput = false;
    public String currentProcessName = "";
    public HashMap<Integer, HashMap<String, Object>> tasks = new HashMap<Integer, HashMap<String, Object>>();
    private ItemStack[] contents;

}

