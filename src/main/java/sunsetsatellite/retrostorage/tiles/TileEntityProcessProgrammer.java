package sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.*;
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

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = new ItemStack(nbttagcompound1);
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
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
            disc.tag.setCompoundTag("disc",new NBTTagCompound());
            disc.tag.setString("name","");
            disc.tag.setBoolean("overrideName",false);
        }
        tasks.clear();
    }

    public void saveProcess() {
        if(getStackInSlot(1) != null && getStackInSlot(1).getItem() instanceof ItemAdvRecipeDisc){
            NBTTagCompound data = new NBTTagCompound();
            NBTTagCompound taskData = new NBTTagCompound();
            tasks.forEach((K,V) -> {
                NBTTagCompound task = new NBTTagCompound();
                task.setInteger("id",K);
                task.setInteger("slot", (Integer) V.get("slot"));
                task.setBoolean("isOutput", (Boolean) V.get("isOutput"));
                NBTTagCompound stack = new NBTTagCompound();
                ((ItemStack)V.get("stack")).writeToNBT(stack);
                task.setCompoundTag("stack",stack);
                taskData.setCompoundTag("task"+K,task);
            });
            getStackInSlot(1).tag.setString("name","Adv. Recipe Disc: "+currentProcessName);
            getStackInSlot(1).tag.setBoolean("overrideName",true);
            data.setCompoundTag("tasks",taskData);
            data.setString("processName",currentProcessName);
            getStackInSlot(1).tag.setCompoundTag("disc",data);
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    public int currentTask = 0;
    public int currentSlot = 0;
    public boolean isCurrentOutput = false;
    public String currentProcessName = "";
    public HashMap<Integer, HashMap<String, Object>> tasks = new HashMap<Integer, HashMap<String, Object>>();
    private ItemStack[] contents;

}

