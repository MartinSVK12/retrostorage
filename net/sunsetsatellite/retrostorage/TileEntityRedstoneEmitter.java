package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Random;

public class TileEntityRedstoneEmitter extends TileEntityInNetworkWithInv {

    private ItemStack[] contents;
    public boolean isActive = false;
    public int mode = 0;
    public int amount = 0;
    public boolean useMeta = true;
    public boolean useData = false;

    public TileEntityRedstoneEmitter()
    {
        contents = new ItemStack[1];
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

    @Override
    public void updateEntity() {
        sendRecipeTicks++;
        if(sendRecipeTicks >= sendRecipeMaxTicks){
            sendRecipeTicks = 0;
        }
        connectedTile = findTileEntityAroundBlock(TileEntityAssembler.class);
        if(connectedTile instanceof TileEntityAssembler){
            if(sendRecipeTicks == 0 && controller != null && isActive) {
                if (((TileEntityAssembler) connectedTile).getStackInSlot(asmSlot) != null && ((TileEntityAssembler) connectedTile).getStackInSlot(asmSlot).getItem() == mod_RetroStorage.recipeDisc) {
                    ItemStack recipeDisc = ((TileEntityAssembler) connectedTile).getStackInSlot(asmSlot);
                    CraftingManager crafter = CraftingManager.getInstance();
                    ArrayList<?> recipe = DiscManipulator.convertRecipeToArray(recipeDisc.getItemData());
                    ItemStack item = crafter.findMatchingRecipeFromArray((ArrayList<ItemStack>) recipe);
                    if(item != null) {
                        DiscManipulator.addCraftRequest(item, 1, controller);
                    }
                }
            }
        }
        World world = ModLoader.getMinecraftInstance().theWorld;
        world.markBlocksDirty(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
        world.notifyBlocksOfNeighborChange(xCoord,yCoord,zCoord,isActive ? 15 : 0);
        if(controller != null){
            if(getStackInSlot(0) != null){
                int id = getStackInSlot(0).itemID;
                int dmg = getStackInSlot(0).getItemDamage();
                int count = 0;
                if(useMeta){
                    count = controller.network_inv.getItemCount(id,dmg);
                } else {
                    count = controller.network_inv.getItemCount(id);
                }
                switch (mode){
                    case 0:
                        isActive = count == amount;
                        break;
                    case 1:
                        isActive = count != amount;
                        break;
                    case 2:
                        isActive = count > amount;
                        break;
                    case 3:
                        isActive = count < amount;
                        break;
                    case 4:
                        isActive = count >= amount;
                        break;
                    case 5:
                        isActive = count <= amount;
                        break;
                }
            } else {
                isActive = false;
            }
        } else {
            isActive = false;
        }
        super.updateEntity();
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
        return "Redstone Emitter";
    }


    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        contents = new ItemStack[getSizeInventory()];
        isActive = nbttagcompound.getBoolean("isActive");
        mode = nbttagcompound.getInteger("mode");
        amount = nbttagcompound.getInteger("checkAmount");
        useMeta = nbttagcompound.getBoolean("useMeta");
        asmSlot = nbttagcompound.getInteger("asmSlot");
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = new ItemStack(nbttagcompound1);
            }
        }
        super.readFromNBT(nbttagcompound);
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
        nbttagcompound.setBoolean("isActive",isActive);
        nbttagcompound.setInteger("checkAmount",amount);
        nbttagcompound.setInteger("mode",mode);
        nbttagcompound.setBoolean("useMeta",useMeta);
        nbttagcompound.setInteger("asmSlot",asmSlot);
    }


    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public TileEntity connectedTile;
    public int asmSlot = 0;
    public int sendRecipeTicks = 0;
    public int sendRecipeMaxTicks = 100;

}
