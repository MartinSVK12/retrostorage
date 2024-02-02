package net.sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.items.ItemAdvRecipeDisc;
import net.sunsetsatellite.retrostorage.mod_RetroStorage;
import net.sunsetsatellite.retrostorage.util.*;
import net.sunsetsatellite.retrostorage.util.Direction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityAdvInterface extends TileEntityNetworkDevice
    implements IInventory {

    public TileEntityAdvInterface() {
        contents = new ItemStack[10];
        this.workTimer = new TickTimer(this,"work",10,true);
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

    @Override
    public ItemStack getStackInSlotOnClosing(int par1) {
        return null;
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

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

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
                contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);//new ItemStack(nbttagcompound1);
            }
        }
        /*if(!Objects.equals(nbttagcompound.getCompoundTag("processing"), new NBTTagCompound())){
            processing = new ItemStack(nbttagcompound.getCompoundTag("processing"));
            NBTTagCompound tasks = nbttagcompound.getCompoundTag("tasks");
            tasks.func_28110_c().forEach((key)->{
                processTasks.add(tasks.getCompoundTag((String) key));
            });
        }*/
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
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        /*if(processing != null) {
            NBTTagCompound compound = new NBTTagCompound();
            NBTTagCompound tasks = new NBTTagCompound();
            processing.writeToNBT(compound);
            nbttagcompound.setCompoundTag("processing", compound);
            processTasks.forEach((nbt)->{
                tasks.setCompoundTag(nbt.getKey(),nbt);
            });
            nbttagcompound.setCompoundTag("tasks",tasks);
            //nbttagcompound.setInteger("processingAmount", processingAmount);
        }*/
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

    @Override
    public void updateEntity() {
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(IInventory.class);
        connectedTiles = getConnectedTileEntities(tiles);
        int i = 0;
        for (TileEntity tile : connectedTiles.values()) {
            if(tile != null){
                workingTile = tile;
                break;
            }
            i++;
        }
        if(i >= 6){
            workingTile = null;
        }
    }

    public void work(){
        if(network != null){
            if(request == null){
                if(stack == null || stack.size() == 0){
                    stack = network.requestQueue.clone();
                }
                if(network.requestQueue.size() > 0){
                    boolean result = acceptNextTask();
                    if(result){
                        mod_RetroStorage.LOGGER.finest(this+" Accepted task "+ request +".");
                    }
                }
            } else {
                fulfillRequest();
            }
        }
    }

    public void fulfillRequest(){
        if(request != null && request.tasks.size() > 0){
            ArrayList<NBTTagCompound> tasks = request.tasks;
            int i = 0;
            while(tasks.get(i).getBoolean("isOutput")){
                if(tasks.size()-1 == i){
                    break;
                }
                i++;
            }
            runTask(tasks.get(i));
        }
        if(request != null && request.tasks.size() == 0){
            mod_RetroStorage.LOGGER.finest(this+" Task fulfilled!");
            this.request.completed = true;
            this.request.processor = null;
            network.requestQueue.remove(request);
            this.request = null;
        }
    }

    public void runTask(NBTTagCompound task){
        if(network != null){
            boolean isOutput = task.getBoolean("isOutput");
            int slot = task.getInteger("slot");
            ItemStack stack = ItemStack.loadItemStackFromNBT(task.getCompoundTag("stack"));//new ItemStack(task.getCompoundTag("stack"));
            if(workingTile == null){
                failTask(task,"Interface not attached",true);
                return;
            }
            IInventory inv = ((IInventory) workingTile);
            if(!isOutput) {
                if (inv.getSizeInventory() > slot) {
                    ItemStack slotStack = ((IInventory) workingTile).getStackInSlot(slot);
                    if(slotStack != null) {
                        if (slotStack.isItemEqual(stack)) {
                            if (slotStack.stackSize + stack.stackSize <= 64) {
                                int networkSlot = network.inventory.getInventorySlotContainItem(stack.itemID,stack.getItemDamage());
                                int networkAmount = network.inventory.getItemCount(stack.itemID,stack.getItemDamage());
                                if(networkSlot != -1) {
                                    if (networkAmount >= stack.stackSize) {
                                        network.inventory.decrStackSize(networkSlot, stack.stackSize);
                                        DiscManipulator.saveDisc(network.drive.virtualDisc, network.inventory);
                                        slotStack.stackSize += stack.stackSize;
                                        finishTask(task);
                                    } else {
                                        mod_RetroStorage.LOGGER.finest("Not enough resources for task "+request+", attempting to create subtasks..");
                                        ArrayList<Task> subtasks = network.getSubtask(request);
                                        if(subtasks == null){
                                            failTask(task, "Not enough resources and no subtasks could be made", true);
                                        } else {
                                            if(subtasks.size() == 0){
                                                failTask(task, "Not enough resources and no subtasks could be made", true);
                                            }
                                            for (Task subtask : subtasks) {
                                                if(request != null){
                                                    request.requires.add(subtask);
                                                    request.processor = null;
                                                    request = null;
                                                }
                                                network.requestQueue.addFirst(subtask);
                                                if(request != null){
                                                    request.processor = null;
                                                    request = null;
                                                }
                                            }
                                        }

                                    }
                                } else {
                                    mod_RetroStorage.LOGGER.finest("Not enough resources for task "+request+", attempting to create subtasks..");
                                    ArrayList<Task> subtasks = network.getSubtask(request);
                                    if(subtasks == null){
                                        failTask(task, "Not enough resources and no subtasks could be made", true);
                                    } else {
                                        if(subtasks.size() == 0){
                                            failTask(task, "Not enough resources and no subtasks could be made", true);
                                        }
                                        for (Task subtask : subtasks) {
                                            if(request != null){
                                                request.requires.add(subtask);
                                                request.processor = null;
                                                request = null;
                                            }
                                            network.requestQueue.addFirst(subtask);
                                            if(request != null){
                                                request.processor = null;
                                                request = null;
                                            }
                                        }
                                    }
                                }
                            } else {
                                failTask(task, "Slot full",false);
                            }
                        } else {
                            failTask(task, "Slot already occupied",false);
                        }
                    } else {
                        int networkSlot = network.inventory.getInventorySlotContainItem(stack.itemID,stack.getItemDamage());
                        int networkAmount = network.inventory.getItemCount(stack.itemID,stack.getItemDamage());
                        if(networkSlot != -1){
                            if(networkAmount >= stack.stackSize) {
                                network.inventory.decrStackSize(networkSlot, stack.stackSize);
                                DiscManipulator.saveDisc(network.drive.virtualDisc, network.inventory);
                                inv.setInventorySlotContents(slot, stack.copy());
                                finishTask(task);
                            } else {
                                mod_RetroStorage.LOGGER.finest("Not enough resources for task "+request+", attempting to create subtasks..");
                                ArrayList<Task> subtasks = network.getSubtask(request);
                                if(subtasks == null){
                                    failTask(task, "Not enough resources and no subtasks could be made", true);
                                } else {
                                    if(subtasks.size() == 0){
                                        failTask(task, "Not enough resources and no subtasks could be made", true);
                                    }
                                    for (Task subtask : subtasks) {
                                        if(request != null){
                                            request.requires.add(subtask);
                                            request.processor = null;
                                            request = null;
                                        }
                                        network.requestQueue.addFirst(subtask);
                                        if(request != null){
                                            request.processor = null;
                                            request = null;
                                        }
                                    }
                                }
                            }
                        } else {
                            mod_RetroStorage.LOGGER.finest("Not enough resources for task "+request+", attempting to create subtasks..");
                            ArrayList<Task> subtasks = network.getSubtask(request);
                            if(subtasks == null){
                                failTask(task, "Not enough resources and no subtasks could be made", true);
                            } else {
                                if(subtasks.size() == 0){
                                    failTask(task, "Not enough resources and no subtasks could be made", true);
                                }
                                for (Task subtask : subtasks) {
                                    if(request != null){
                                        request.requires.add(subtask);
                                        request.processor = null;
                                        request = null;
                                    }
                                    network.requestQueue.addFirst(subtask);
                                    if(request != null){
                                        request.processor = null;
                                        request = null;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    failTask(task, "Invalid slot",true);
                }
            } else {
                if(inv.getSizeInventory() > slot) {
                    ItemStack slotStack = ((IInventory) workingTile).getStackInSlot(slot);
                    if (slotStack != null) {
                        if(slotStack.stackSize == stack.stackSize ){
                            if(slotStack.isItemEqual(stack)){
                                if (network.inventory.addItemStackToInventory(slotStack.copy())) {
                                    inv.setInventorySlotContents(slot, null);
                                    DiscManipulator.saveDisc(network.drive.virtualDisc,network.inventory);
                                    finishTask(task);
                                } else {failTask(task,"Failed to add to network",false);}
                            } else {failTask(task,"Stack mismatch",false);}
                        }
                    } //else {failTask(task, "Empty slot",false);}
                } else {failTask(task, "Invalid slot",true);}
            }
        }
    }


    public void finishTask(NBTTagCompound task){
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Task "+task.getInteger("id")+" succeeded!");
        mod_RetroStorage.LOGGER.finest(this+" "+"Step "+task.getInteger("id")+" succeeded!");
        request.tasks.remove(task);
    }

    public void failTask(NBTTagCompound task, String reason, boolean fatal){
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage((fatal ? "FATAL! " : "")+"Task "+task.getInteger("id")+" failed: "+reason+"!");
        mod_RetroStorage.LOGGER.finest(this+" "+(fatal ? "FATAL! " : "")+"Step "+task.getInteger("id")+" failed: "+reason+"!");
        if(fatal){
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Processing failed!");
            if(request.attempts > 0){
                request.attempts--;
            } else {
                mod_RetroStorage.LOGGER.finest("Too many failed attempts, cancelling task!");
                network.requestQueue.clone().forEach((V)->{
                    if(V.requires.contains(request) || V.parent == request){
                        network.requestQueue.remove(V);
                    }
                });
                network.requestQueue.remove(request);
                request.processor = null;
                request = null;
                return;
            }

            request.processor = null;
            request = null;
        }
    }

    public void cancelProcessing(){
        request.processor = null;
        request = null;
    }

    public boolean acceptNextTask() {
        if(stack.size() > 0){
            Task t = stack.pop();
            if (t instanceof ProcessTask) {
                if (t.processor == null) {
                    if (!t.completed) {
                        if (t.requirementsMet()) {
                            ArrayList<ProcessTask> processTasks = new ArrayList<>();
                            for (ArrayList<NBTTagCompound> process : getProcesses()) {
                                processTasks.add(new ProcessTask(process,null,null));
                            }
                            boolean found = processTasks.stream().anyMatch((T)-> T.output.isItemEqual(((ProcessTask) t).output));
                            if(found){
                                this.request = (ProcessTask) t;
                                t.processor = this;
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            //mod_RetroStorage.LOGGER.error(this+" Not all requirements met for tasks! "+t);
                            return false;
                        }
                    } else {
                        //mod_RetroStorage.LOGGER.error(this+ " Task is already completed! "+t);
                        return false;
                    }
                } else {
                    //mod_RetroStorage.LOGGER.error(this + " Task is taken already! "+t);
                    return false;
                }
            } else {
                acceptNextTask();
            }
        }
        return false;
    }

    public ArrayList<ArrayList<NBTTagCompound>> getProcesses(){
        ArrayList<ArrayList<NBTTagCompound>> processes = new ArrayList<>();
        for (ItemStack stack : contents){
            if(stack != null && stack.getItem() instanceof ItemAdvRecipeDisc){
                processes.add(DiscManipulator.getProcessesFromDisc(stack));
            }
        }
        return processes;
    }

    private ItemStack[] contents;
    public TickTimer workTimer;
    public HashMap<Direction, TileEntity> connectedTiles = new HashMap<>();
    public TileEntity workingTile;
    public ArrayDeque<Task> stack;
    public ProcessTask request;
}

