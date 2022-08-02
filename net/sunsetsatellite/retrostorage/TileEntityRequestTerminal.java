// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemStack, NBTTagCompound, 
//            NBTTagList, World, EntityPlayer

public class TileEntityRequestTerminal extends TileEntityInNetworkWithInv
{

    public TileEntityRequestTerminal()
    {
        contents = new ItemStack[39];
    }

    public int getSizeInventory()
    {
        return contents.length;
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
    
    /*public void withdrawItem(Slot slot) {
    	if(slot.slotNumber == 1 || slot.slotNumber == 2) {
    		return;
    	}
    	DiscManipulator.outputItemFromDisc(network_disc, slot.getStack(), (TileEntityInNetworkWithInv) this, (TileEntityInNetworkWithInv) network_drive);
    	network_drive.updateDiscs();
    	//System.out.println((new StringBuilder().append("Slot ").append(slot).append(" want to withdraw item.")).toString());
    	
    }*/
    public void requestItemCrafting(Slot slot) {
        EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
        if(slot.getStack() != null){
            ModLoader.OpenGUI(entityplayer, new GuiAssemblyRequest(entityplayer.inventory, this, slot.getStack()));
        }
        /*entityplayer.addChatMessage("Requesting: "+slot.getStack().stackSize+"x "+StringTranslate.getInstance().translateNamedKey(slot.getStack().getItemName()));
        controller.assemblyQueue.add(slot.getStack().getItem());*/
    }
    /*public void requestItemCrafting(Slot slot) {
    	if(slot.slotNumber == 0 || slot.slotNumber == 1 || slot.slotNumber == 2) {
    		return;
    	}
    	//System.out.println(network_asm == null);
    	CraftingManager crafter = CraftingManager.getInstance();
    	if(network_asm != null) {
            TileEntityInNetworkWithInv handler = (TileEntityInNetworkWithInv) network_asm.itemAssembly.get(slot.getStack()).get(0);
    		int handlerSlot = (int) network_asm.itemAssembly.get(slot.getStack()).get(1);
            if (processing){
                ModLoader.getMinecraftInstance().thePlayer.addChatMessage("An item is already being processed, try again later.");
                return;
            }
            ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Requesting: "+slot.getStack().stackSize+"x "+StringTranslate.getInstance().translateNamedKey(slot.getStack().getItemName()));
            World world = ModLoader.getMinecraftInstance().theWorld;
            System.out.println("Requesting: "+slot.getStack().getItemName()+" from "+handler.toString()+" via "+network_asm.toString()+" using slot "+handlerSlot);
			if (handler instanceof TileEntityAssembler) {
                if (handler.getStackInSlot(handlerSlot) != null) {
                    if (handler.getStackInSlot(handlerSlot).getItem() == mod_RetroStorage.recipeDisc) {
                        ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(handler.getStackInSlot(handlerSlot).getItemData());
                        ItemStack output = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
                        //System.out.println(output.toString());
                        if (output != null && output.stackSize != 0 && !processing) {
                            processing = true;
                            int s = 0;
                            for (int i1 = 0; i1 < recipe.size(); i1++) {
                                if (network_disc != null) {
                                    if (network_disc.getItem() instanceof ItemStorageDisc) {
                                        if (recipe.get(i1) != null) {
                                            if (DiscManipulator.testDecreaseItemAmountOnDisc(network_disc, (ItemStack) recipe.get(i1))) {
                                                s++;
                                            }
                                        } else {
                                            s++;
                                        }
                                    }
                                }

                            }
                            //System.out.println(s);
                            if (s == recipe.size()) {
                                if(getStackInSlot(0) == null || getStackInSlot(0).stackSize < 64) {
                                    for (Object o : recipe) {
                                        if (network_disc != null) {
                                            if (network_disc.getItem() instanceof ItemStorageDisc) {
                                                if (o != null) {
                                                    DiscManipulator.decreaseItemAmountOnDisc(network_disc, (ItemStack) o);
                                                    network_drive.updateDiscs();
                                                    if (((ItemStack) o).getItem().hasContainerItem()){
                                                        //System.out.println("returning container item");
                                                        ItemStack container_item = new ItemStack(((ItemStack) o).getItem().getContainerItem());
                                                        //System.out.println(container_item);
                                                        if (network_disc != null) {
                                                            if (network_disc.getItem() instanceof ItemStorageDisc) {
                                                                if(DiscManipulator.getMaxPartitions(network_drive) > 0) {
                                                                    if(DiscManipulator.getFirstAvailablePartition(network_disc, network_drive) != -1) {
                                                                        DiscManipulator.addStackToPartitionedDisc(container_item,network_disc,DiscManipulator.getFirstAvailablePartition(network_disc, network_drive));
                                                                        network_drive.updateDiscs();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }

                                    if (getStackInSlot(0) == null) {
                                        output.stackSize = output.stackSize == 0 ? 1 : output.stackSize;
                                        setInventorySlotContents(0, output.copy());
                                    } else if (getStackInSlot(0).isItemEqual(output)) {
                                        output.stackSize = output.stackSize == 0 ? 1 : output.stackSize;
                                        ItemStack is = output.copy();
                                        is.stackSize += getStackInSlot(0).stackSize;
                                        setInventorySlotContents(0, is);
                                    } else if (getStackInSlot(0) != null){
                                        setInventorySlotContents(2, output.copy());
                                    } else if (getStackInSlot(0) != null && getStackInSlot(2) != null){
                                        EntityItem entityitem = new EntityItem(world, (float)xCoord, (float)yCoord, (float)zCoord, output.copy());
                                        float f3 = 0.05F;
                                        entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                                        entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                                        entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                                        world.entityJoinedWorld(entityitem);
                                    }
                                    }
                                    ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Request successful!");
                                    processing = false;
                                    //System.out.println(network_disc.getItemData().toStringExtended());
                            } else {
                                processing = false;
                                ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Request failed!");
                            }
                                //System.out.println(output.stackSize);
                                //System.out.println("dropped");
                                //
                        }
                    }
                }
            } else if(handler instanceof TileEntityInterface){
                TileEntity tile = handler.findTileEntityAroundBlock();
                if (handler.getStackInSlot(handlerSlot) != null) {
                    if (handler.getStackInSlot(handlerSlot).getItem() == mod_RetroStorage.recipeDisc) {

                    }else{
                        if (tile instanceof TileEntityChest) {
                            if (network_disc != null) {
                                if (network_disc.getItem() instanceof ItemStorageDisc) {
                                    ItemStack handlerItem = handler.getStackInSlot(handlerSlot);
                                    if (DiscManipulator.testDecreaseItemAmountOnDisc(network_disc, handlerItem)) {
                                        int tileSlot = -1;
                                        boolean add = false;
                                        for(int i = 0; i < ((TileEntityChest) tile).getSizeInventory(); i++) {
                                            ItemStack item = ((TileEntityChest) tile).getStackInSlot(i);
                                            if (item != null) {
                                                //System.out.println(item.toString());
                                                if (item.itemID == handlerItem.itemID && item.getItemDamage() == handlerItem.getItemDamage()){
                                                    //System.out.println("equal");
                                                    if(item.stackSize <= 64-handlerItem.stackSize){
                                                        //System.out.println("enough");
                                                        add = true;
                                                        break;
                                                    } else {
                                                        continue;
                                                    }
                                                } else {
                                                    continue;
                                                }
                                            } else {
                                                tileSlot = i;
                                                break;
                                            }
                                        }
                                        if (tileSlot != -1){
                                            DiscManipulator.decreaseItemAmountOnDisc(network_disc, handlerItem);
                                            if (add){
                                                //System.out.println("add");
                                                ItemStack item = ((TileEntityChest) tile).getStackInSlot(tileSlot);
                                                //System.out.println(item == null);
                                                if(item != null) {
                                                    item.stackSize += handlerItem.stackSize;
                                                    ((TileEntityChest) tile).setInventorySlotContents(tileSlot, item);
                                                } else {
                                                    ((TileEntityChest) tile).setInventorySlotContents(tileSlot, item);
                                                }
                                            } else {
                                                ((TileEntityChest) tile).setInventorySlotContents(tileSlot, handlerItem.copy());
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (tile instanceof TileEntityFurnace){
                            if (network_disc != null) {
                                if (network_disc.getItem() instanceof ItemStorageDisc) {
                                    ItemStack handlerItem = handler.getStackInSlot(handlerSlot);
                                    if (DiscManipulator.testDecreaseItemAmountOnDisc(network_disc, handlerItem)) {
                                        ItemStack item = ((TileEntityFurnace) tile).getStackInSlot(0);
                                        boolean add = false;
                                        //System.out.println(item == null);
                                        if (item != null) {
                                            //System.out.println(item.toString());
                                            if (item.itemID == handlerItem.itemID && item.getItemDamage() == handlerItem.getItemDamage()) {
                                                //System.out.println("equal");
                                                if (item.stackSize <= 64 - handlerItem.stackSize) {
                                                   // System.out.println("enough");
                                                    add = true;
                                                } else {
                                                    return;
                                                }
                                            } else {
                                                return;
                                            }
                                            DiscManipulator.decreaseItemAmountOnDisc(network_disc, handlerItem);
                                            if (add){
                                                if(item != null) {
                                                    item.stackSize += handlerItem.stackSize;
                                                    ((TileEntityFurnace) tile).setInventorySlotContents(0, item);
                                                } else {
                                                    ((TileEntityFurnace) tile).setInventorySlotContents(0, item);
                                                }
                                            } else {
                                                ((TileEntityFurnace) tile).setInventorySlotContents(0, handlerItem.copy());
                                            }
                                        } else {
                                            DiscManipulator.decreaseItemAmountOnDisc(network_disc, handlerItem);
                                            ((TileEntityFurnace) tile).setInventorySlotContents(0, handlerItem.copy());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
		}
    	//network_drive.updateDiscs();
    }*/
    
    public void updateEntity()
    {
    	connectDrive();
    	setInventorySlotContents(1, network_disc);
    	if(network.size() > 0) {
            for (Map.Entry<ArrayList<Integer>, HashMap<String, Object>> element : network.entrySet()) {
                ArrayList<Integer> pos = element.getKey();
                TileEntity tile = (TileEntity) worldObj.getBlockTileEntity(pos.get(0), pos.get(1), pos.get(2));
                if (tile != null) {
                    if (tile instanceof TileEntityDigitalController) {
                        network_asm = (TileEntityDigitalController) tile;
                        if (network_disc != null) {
                            DiscManipulator.readItemAssembly(page, this, network_asm);
                            break;
                        }
                    } else {
                        network_asm = null;
                    }
                } else {
                    network_asm = null;
                }
            }
    	}
    	if(network_asm == null) {
    		for(int i = 3;i <= 38;i++) {
				setInventorySlotContents(i, null);
			}
    	}
    	/*if (getStackInSlot(2) != null) {
    		if (network_disc != null) {
    			if (network_disc.getItem() instanceof ItemStorageDisc) {
    				if(DiscManipulator.getMaxPartitions(network_drive) > 0) {
    					if(DiscManipulator.getFirstAvailablePartition(network_disc, network_drive) != -1) {
    						DiscManipulator.addStackToPartitionedDisc(getStackInSlot(2),network_disc,DiscManipulator.getFirstAvailablePartition(network_disc, network_drive));
    						setInventorySlotContents(2, null);
    						//network_drive.updateDiscs();
    					}
    				}
    			}
			}
    	}*/
    	
    	
    	
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
        return "Digital Terminal";
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

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 8000D;
    }

    private ItemStack contents[];
    private TileEntityDigitalController network_asm = null;
    public int page;
    public int pages;
    private boolean processing = false;
}
