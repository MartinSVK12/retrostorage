package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemAdvRecipeDisc;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.ProcessTask;
import sunsetsatellite.retrostorage.util.Task;
import sunsetsatellite.sunsetutils.util.TickTimer;

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
        /*if(!Objects.equals(CompoundTag.getCompound("processing"), new CompoundTag())){
            processing = new ItemStack(CompoundTag.getCompound("processing"));
            CompoundTag tasks = CompoundTag.getCompound("tasks");
            tasks.func_28110_c().forEach((key)->{
                processTasks.add(tasks.getCompound((String) key));
            });
        }*/
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
        /*if(processing != null) {
            CompoundTag compound = new CompoundTag();
            CompoundTag tasks = new CompoundTag();
            processing.writeToNBT(compound);
            CompoundTag.putCompound("processing", compound);
            processTasks.forEach((nbt)->{
                tasks.putCompound(nbt.getKey(),nbt);
            });
            CompoundTag.putCompound("tasks",tasks);
            //CompoundTag.putInt("processingAmount", processingAmount);
        }*/
        CompoundTag.put("Items", listTag);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getInventorySlotContainItem(int itemID, int itemDamage) {
        for(int i2 = 0; i2 < this.contents.length; ++i2) {
            if(this.contents[i2] != null && this.contents[i2].itemID == itemID && this.contents[i2].getMetadata() == itemDamage) {
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
        connectedTiles = getConnectedTileEntity(tiles);
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
                        RetroStorage.LOGGER.debug(this+" Accepted task "+ request +".");
                    }
                }
            } else {
                fulfillRequest();
            }
        }
    }

    public void fulfillRequest(){
        if(request != null && request.tasks.size() > 0){
            ArrayList<CompoundTag> tasks = request.tasks;
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
            RetroStorage.LOGGER.debug(this+" Task fulfilled!");
            this.request.completed = true;
            this.request.processor = null;
            network.requestQueue.remove(request);
            this.request = null;
        }
    }

    public void runTask(CompoundTag task){
        if(network != null){
            boolean isOutput = task.getBoolean("isOutput");
            int slot = task.getInteger("slot");
            ItemStack stack = ItemStack.readItemStackFromNbt(task.getCompound("stack"));
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
                                int networkSlot = network.inventory.getInventorySlotContainItem(stack.itemID,stack.getMetadata());
                                int networkAmount = network.inventory.getItemCount(stack.itemID,stack.getMetadata());
                                if(networkSlot != -1) {
                                    if (networkAmount >= stack.stackSize) {
                                        network.inventory.decrStackSize(networkSlot, stack.stackSize);
                                        DiscManipulator.saveDisc(network.drive.virtualDisc, network.inventory);
                                        slotStack.stackSize += stack.stackSize;
                                        finishTask(task);
                                    } else {
                                        RetroStorage.LOGGER.debug("Not enough resources for task "+request+", attempting to create subtasks..");
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
                                    RetroStorage.LOGGER.debug("Not enough resources for task "+request+", attempting to create subtasks..");
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
                        int networkSlot = network.inventory.getInventorySlotContainItem(stack.itemID,stack.getMetadata());
                        int networkAmount = network.inventory.getItemCount(stack.itemID,stack.getMetadata());
                        if(networkSlot != -1){
                            if(networkAmount >= stack.stackSize) {
                                network.inventory.decrStackSize(networkSlot, stack.stackSize);
                                DiscManipulator.saveDisc(network.drive.virtualDisc, network.inventory);
                                inv.setInventorySlotContents(slot, stack.copy());
                                finishTask(task);
                            } else {
                                RetroStorage.LOGGER.debug("Not enough resources for task "+request+", attempting to create subtasks..");
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
                            RetroStorage.LOGGER.debug("Not enough resources for task "+request+", attempting to create subtasks..");
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


    public void finishTask(CompoundTag task){
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Task "+task.getInteger("id")+" succeeded!");
        RetroStorage.LOGGER.debug(this+" "+"Step "+task.getInteger("id")+" succeeded!");
        request.tasks.remove(task);
    }

    public void failTask(CompoundTag task, String reason, boolean fatal){
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage((fatal ? "FATAL! " : "")+"Task "+task.getInteger("id")+" failed: "+reason+"!");
        RetroStorage.LOGGER.debug(this+" "+(fatal ? "FATAL! " : "")+"Step "+task.getInteger("id")+" failed: "+reason+"!");
        if(fatal){
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Processing failed!");
            if(request.attempts > 0){
                request.attempts--;
            } else {
                RetroStorage.LOGGER.debug("Too many failed attempts, cancelling task!");
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
        if(!stack.isEmpty()){
            Task t = stack.pop();
            if (t instanceof ProcessTask) {
                if (t.processor == null) {
                    if (!t.completed) {
                        if (t.requirementsMet()) {
                            ArrayList<ProcessTask> processTasks = new ArrayList<>();
                            for (ArrayList<CompoundTag> process : getProcesses()) {
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
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                acceptNextTask();
            }
        }
        return false;
    }

    public ArrayList<ArrayList<CompoundTag>> getProcesses(){
        ArrayList<ArrayList<CompoundTag>> processes = new ArrayList<>();
        for (ItemStack stack : contents){
            if(stack != null && stack.getItem() instanceof ItemAdvRecipeDisc){
                processes.add(DiscManipulator.getProcessesFromDisc(stack));
            }
        }
        return processes;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private ItemStack[] contents;
    public TickTimer workTimer;
    public HashMap<String,TileEntity> connectedTiles = new HashMap<>();
    public TileEntity workingTile;
    public ArrayDeque<Task> stack;
    public ProcessTask request;
}

