package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TileEntityAssembler extends TileEntityInNetworkWithInv {

	public TileEntityAssembler() {
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
    	if(i != 9) {
    		for(int j = 0;j < contents.length;j++) {
        		if (contents[j] == null) { 
        			contents[j] = itemstack;
        			break;
        		}
        	}
    	} else {
    		contents[i] = itemstack;
    	}
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
        return "Assembler";
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
        requestSearchTicks += 1;
        if(requestSearchTicks >= requestSearchMaxTicks){
            requestSearchTicks = 0;
            if(controller != null && controller.isActive()){
                setInventorySlotContents(9, controller.network_disc);
                if(!controller.assemblyQueue.isEmpty() && !processing){
                    Item item = controller.assemblyQueue.peekFirst();
                    CraftingManager crafter = CraftingManager.getInstance();
                    for(int i = 0;i < 9;i++){
                        if(getStackInSlot(i) != null && getStackInSlot(i).getItem() == mod_RetroStorage.recipeDisc){
                            ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(getStackInSlot(i).getItemData());
                            ItemStack output = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
                            if(output != null && output.getItem() == item){
                                craftItem(recipe,output);
                                return;
                            }
                        }
                    }
                /*ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(getStackInSlot(handlerSlot).getItemData());
                ItemStack output = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);*/
                }
            }
        }
        if(controller == null || !controller.isActive()) {
            setInventorySlotContents(9,null);
        }
    }

    private boolean craftItem(ArrayList<?> recipe, ItemStack output){
        if(controller == null || !controller.isActive()) {
            return false;
        }
        if(!controller.itemAssembly.containsKey(output)){
            ModLoader.getMinecraftInstance().thePlayer.addChatMessage("ERROR: Attempted to craft an unrecognized recipe.");
            controller.assemblyQueue.remove(output.getItem());
            return false;
        }
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Crafting: "+StringTranslate.getInstance().translateNamedKey(output.getItemName()));
        controller.assemblyQueue.remove(output.getItem());
        processing = true;
        HashMap<ArrayList<Integer>, Integer> requirements = new HashMap<ArrayList<Integer>, Integer>();
        for (Object value : recipe) {
            if (value != null) {
                ArrayList<Integer> item = new ArrayList<>();
                item.add(((ItemStack)value).itemID);
                item.add(((ItemStack)value).getItemDamage());
                if (!requirements.containsKey(item)){
                    requirements.put(item, 1);
                } else {
                    requirements.replace(item,requirements.get(item),requirements.get(item)+1);
                }
                /*if (!requirements.containsKey(((ItemStack) value).itemID)) {
                    requirements.put(((ItemStack) value).itemID, 1);
                } else {
                    requirements.replace(((ItemStack) value).itemID, requirements.get(((ItemStack) value).itemID), requirements.get(((ItemStack) value).itemID) + 1);
                }*/
            }
        }
        int s = 0;
        for (Map.Entry<ArrayList<Integer>, Integer> i1 : requirements.entrySet()) {
            if (controller.network_disc != null) {
                if (controller.network_disc.getItem() instanceof ItemStorageDisc) {
                    if (requirements.get(i1.getKey()) != null) {
                        int count = controller.network_inv.getItemCount(i1.getKey().get(0),i1.getKey().get(1));
                        if(count >= requirements.get(i1.getKey())){
                            s++;
                        }
                    } else {
                        s++;
                    }
                }
            }
        }
        if (s == requirements.size()) {
            for (Object o : recipe) {
                if(o != null){
                    int slot = controller.network_inv.getInventorySlotContainItem(((ItemStack)o).itemID, ((ItemStack)o).getItemDamage());
                    if(slot != -1){
                        ItemStack itemCopy = controller.network_inv.getStackInSlot(slot).copy();
                        controller.network_inv.decrStackSize(slot,1);
                        DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                        if(itemCopy.getItem().hasContainerItem()){
                            if(controller.network_inv.getFirstEmptyStack() != -1){
                                controller.network_inv.addItemStackToInventory(new ItemStack(itemCopy.getItem().getContainerItem(),1));
                                DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
                            } else {
                                World world = ModLoader.getMinecraftInstance().theWorld;
                                EntityItem entityitem = new EntityItem(world, (float)xCoord, (float)yCoord, (float)zCoord, new ItemStack(getStackInSlot(slot).getItem().getContainerItem(),1));
                                float f3 = 0.05F;
                                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                                world.entityJoinedWorld(entityitem);
                            }
                        }
                    }
                }
            }
            if(controller.network_inv.getFirstEmptyStack() != -1){
                controller.network_inv.addItemStackToInventory(output.copy());
                DiscManipulator.saveDisc(controller.network_disc,controller.network_inv);
            } else {
                World world = ModLoader.getMinecraftInstance().theWorld;
                EntityItem entityitem = new EntityItem(world, (float)xCoord, (float)yCoord, (float)zCoord, output.copy());
                float f3 = 0.05F;
                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            }
            processing = false;
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Crafting successful!");
            return true;
        }
        processing = false;
        ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Crafting failed!");
        return false;
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
    private boolean processing = false;
    private int requestSearchTicks = 0;
    private int requestSearchMaxTicks = 20;
}
