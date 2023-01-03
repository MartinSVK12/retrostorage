package net.sunsetsatellite.retrostorage;

import ic2.TileEntityElectricMachine;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Objects;

public class TileEntityAdvInterface extends TileEntityInNetworkWithInv {

	public TileEntityAdvInterface() {
		 contents = new ItemStack[10];
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
        return "Adv. Item Interface";
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
        if(!Objects.equals(nbttagcompound.getCompoundTag("processing"), new NBTTagCompound())){
            processing = new ItemStack(nbttagcompound.getCompoundTag("processing"));
            NBTTagCompound tasks = nbttagcompound.getCompoundTag("tasks");
            tasks.getKeys().forEach((key)->{
                processTasks.add(tasks.getCompoundTag((String) key));
            });
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
        if(processing != null) {
            NBTTagCompound compound = new NBTTagCompound();
            NBTTagCompound tasks = new NBTTagCompound();
            processing.writeToNBT(compound);
            nbttagcompound.setCompoundTag("processing", compound);
            processTasks.forEach((nbt)->{
                tasks.setCompoundTag(nbt.getKey(),nbt);
            });
            nbttagcompound.setCompoundTag("tasks",tasks);
            //nbttagcompound.setInteger("processingAmount", processingAmount);
        }
        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getInventorySlotContainItem(int itemID, int itemDamage) {
        for(int i2 = 0; i2 < this.contents.length; ++i2) {
            if(this.contents[i2] != null && this.contents[i2].itemID == itemID && this.contents[i2].getItemDamage() == itemDamage) {
                return i2;
            }
        }

        return -1;
    }

    public void updateEntity(){
        TileEntity tile = findTileEntityAroundBlock(IInventory.class,this.getClass());
        attachedTileEntity = tile;
        //System.out.println(tile);
        if(processing == null){
            processTasks.clear();
        }
        if(processing != null && processTasks.size() == 0){
            finishProcessing();
        }
        requestSearchTicks++;
        if(requestSearchTicks >= requestSearchMaxTicks){
            requestSearchTicks = 0;
            acceptRequest();
            if(processTasks.size() > 0){
                int i = 0;
                while(processTasks.get(i).getBoolean("isOutput")){
                    if(processTasks.size()-1 == i){
                        break;
                    }
                    i++;
                }
                runTask(processTasks.get(i));
            }
        }
    }

    public void acceptRequest(){
        //System.out.println("Accepting request..");
        if(controller != null && controller.isActive() && controller.network_disc != null && controller.network_disc.getItem() instanceof ItemStorageDisc) {
            if(attachedTileEntity != null){
                if (!controller.assemblyQueue.isEmpty()) {
                    ItemStack item = controller.assemblyQueue.peekFirst();
                    NBTTagCompound tasks = item.getItemData().getCompoundTag("tasks");
                    if (processing == null && getInventorySlotContainItem(item.itemID, item.getItemDamage()) != -1) {
                        System.out.println("Accepted request: "+item.getItemData().getString("processName"));
                        processing = item;
                        controller.assemblyQueue.remove(item);
                        for(int i = 0;i<tasks.size();i++){
                            NBTTagCompound task = tasks.getCompoundTag("task"+i);
                            processTasks.add(task);
                        }
                    } else if(processing.isItemEqual(item) && processing.getItemData().equals(item.getItemData())) {
                        System.out.println("Accepted request: "+item.getItemData().getString("processName"));
                        //processing = item;
                        controller.assemblyQueue.remove(item);
                        for(int i = 0;i<tasks.size();i++){
                            NBTTagCompound task = tasks.getCompoundTag("task"+i);
                            processTasks.add(task);
                        }
                    }
                    //else {System.out.println("Already processing!");}
                } //else {System.out.println("No requests..");}
            } //else {System.out.println("Interface not attached.");};
        } //else {System.out.println("Network offline.");}
    }

    public void runTask(NBTTagCompound task){
        //System.out.println("Running task: "+task.getInteger("id"));
        boolean isOutput = task.getBoolean("isOutput");
        int slot = task.getInteger("slot");
        ItemStack stack = new ItemStack(task.getCompoundTag("stack"));
        if(attachedTileEntity == null){
            failTask(task,"Interface not attached",true);
            return;
        }
        if(!(attachedTileEntity instanceof IInventory)){
            failTask(task,"Incompatible attachment",true);
            return;
        }
        if(controller == null || controller.network_inv == null){
            return;
        }
        IInventory inv = ((IInventory) attachedTileEntity);
        if(!isOutput){
            if(inv.getSizeInventory() > slot){
                ItemStack slotStack = ((IInventory) attachedTileEntity).getStackInSlot(slot);
                if(slotStack != null){
                    if(slotStack.isItemEqual(stack) && slotStack.getItemData().equals(stack.getItemData())){
                        if(slotStack.stackSize + stack.stackSize <= 64){
                            int networkSlot = controller.network_inv.getInventorySlotContainItem(stack.itemID,stack.getItemDamage());
                            int networkAmount = controller.network_inv.getItemCount(stack.itemID,stack.getItemDamage());
                            if(networkSlot != -1){
                                if(networkAmount >= stack.stackSize){
                                    controller.network_inv.decrStackSize(networkSlot,stack.stackSize);
                                    DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                                    slotStack.stackSize += stack.stackSize;
                                    finishTask(task);
                                } else {
                                    failTask(task, "Not enough resources", true);
                                }
                            } else {
                                failTask(task, "Not enough resources",true);
                            }
                        } else {
                            failTask(task, "Slot full",false);
                        }
                    } else {
                        failTask(task, "Slot already occupied",false);
                    }
                } else {
                    int networkSlot = controller.network_inv.getInventorySlotContainItem(stack.itemID,stack.getItemDamage());
                    int networkAmount = controller.network_inv.getItemCount(stack.itemID,stack.getItemDamage());
                    if(networkSlot != -1){
                        if(networkAmount >= stack.stackSize) {
                            controller.network_inv.decrStackSize(networkSlot, stack.stackSize);
                            DiscManipulator.saveDisc(controller.network_disc, controller.network_inv);
                            inv.setInventorySlotContents(slot, stack.copy());
                            finishTask(task);
                        } else {
                            failTask(task, "Not enough resources", true);
                        }
                    } else {
                        failTask(task, "Not enough resources",true);
                    }
                }
            } else {
                failTask(task, "Invalid slot",true);
            }
        } else {
            if(inv.getSizeInventory() > slot) {
                ItemStack slotStack = ((IInventory) attachedTileEntity).getStackInSlot(slot);
                if (slotStack != null) {
                    if(slotStack.stackSize == stack.stackSize ){
                        if(slotStack.isItemEqual(stack) && slotStack.getItemData().equals(stack.getItemData())){
                            if (controller.network_inv.addItemStackToInventory(slotStack.copy())) {
                                inv.setInventorySlotContents(slot, null);
                                DiscManipulator.saveDisc(controller);
                                finishTask(task);
                            } else {failTask(task,"Failed to add to network",false);}
                        } else {failTask(task,"Stack mismatch",false);}
                    }
                } //else {failTask(task, "Empty slot",false);}
            } else {failTask(task, "Invalid slot",true);}
        }
    }

    public void cancelProcessing(){
        processing = null;
        processTasks.clear();
    }

    public void finishProcessing(){
        ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Processing successful!");
        processing = null;
    }


    public void finishTask(NBTTagCompound task){
        ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Task "+task.getInteger("id")+" succeeded!");
        System.out.println("Task "+task.getInteger("id")+" succeeded!");
        processTasks.remove(task);
    }

    public void failTask(NBTTagCompound task, String reason, boolean fatal){
        ModLoader.getMinecraftInstance().thePlayer.addChatMessage((fatal ? "FATAL! " : "")+"Task "+task.getInteger("id")+" failed: "+reason+"!");
        System.out.println((fatal ? "FATAL! " : "")+"Task "+task.getInteger("id")+" failed: "+reason+"!");
        if(fatal){
            ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Processing failed!");
            processTasks.remove(task);
            processing = null;
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
	
	private ItemStack[] contents;
    private ItemStack processing = null;
    private ArrayList<NBTTagCompound> processTasks = new ArrayList<>();
    //private int processingAmount = 0;
    private int requestSearchTicks = 0;
    private int requestSearchMaxTicks = 20;
    private TileEntity attachedTileEntity = null;
}
