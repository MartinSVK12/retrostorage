package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.DiscManipulator;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityRedstoneEmitter extends TileEntityNetworkDevice implements IInventory {

    private ItemStack[] contents;
    public boolean isActive = false;
    public int mode = 0;
    public int amount = 0;
    public boolean useMeta = true;
    public boolean useData = false;
    public TickTimer workTimer = new TickTimer(this,"work",100, true);

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

    public ItemStack decrStackSize(int i, int j) {
        if (contents[i] != null) {
            if (contents[i].stackSize <= j) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if (contents[i].stackSize == 0) {
                contents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        } else {
            return null;
        }
    }

    public void work(){
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(TileEntityAssembler.class);
        list.add(TileEntityAdvInterface.class);
        HashMap<String, TileEntity> map = getConnectedTileEntity(list);
        map.forEach((K,V)->{
            if(V != null){
                connectedTile = V;
            }
        });
        if(connectedTile != null && network != null && isActive){
            if(connectedTile instanceof TileEntityAssembler){
                ItemStack stack = ((TileEntityAssembler)connectedTile).getStackInSlot(asmSlot);
                if(stack != null){
                    if(stack.getItem() == RetroStorage.recipeDisc){
                        RecipeEntryCrafting<?,?> recipe = RetroStorage.findRecipeFromNBT(stack.getData().getCompound("recipe"));
                        if(recipe != null){
                            network.requestCrafting(recipe);
                        }
                    }
                }
            } else if (connectedTile instanceof TileEntityAdvInterface) {
                if(((TileEntityAdvInterface) connectedTile).request == null) {
                    ItemStack stack = ((TileEntityAdvInterface) connectedTile).getStackInSlot(asmSlot);
                    if (stack != null) {
                        if (stack.getItem() == RetroStorage.advRecipeDisc) {
                            ArrayList<CompoundTag> tasks = DiscManipulator.getProcessesFromDisc(stack);
                            if (!tasks.isEmpty()) {
                                network.requestProcessing(tasks);
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void tick() {
        workTimer.tick();
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        worldObj.notifyBlocksOfNeighborChange(x,y,z,isActive ? 15 : 0);
        if(network != null){
            if(getStackInSlot(0) != null){
                int id = getStackInSlot(0).itemID;
                int dmg = getStackInSlot(0).getMaxDamage();
                int count = 0;
                if(useMeta){
                    count = network.inventory.getItemCount(id,dmg);
                } else {
                    count = network.inventory.getItemCount(id);
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
        super.tick();
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
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        if(worldObj.getBlockTileEntity(x, y, z) != this)
        {
            return false;
        }
        return entityPlayer.distanceToSqr((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
    }

    @Override
    public void sortInventory() {

    }

    public String getInvName()
    {
        return "Redstone Emitter";
    }


    public void readFromNBT(CompoundTag nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getList("Items");
        contents = new ItemStack[getSizeInventory()];
        isActive = nbttagcompound.getBoolean("isActive");
        mode = nbttagcompound.getInteger("mode");
        amount = nbttagcompound.getInteger("checkAmount");
        useMeta = nbttagcompound.getBoolean("useMeta");
        asmSlot = nbttagcompound.getInteger("asmSlot");
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < contents.length)
            {
                contents[j] = ItemStack.readItemStackFromNbt(nbttagcompound1);
            }
        }
        super.readFromNBT(nbttagcompound);
    }


    public void writeToNBT(CompoundTag nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {
                CompoundTag nbttagcompound1 = new CompoundTag();
                nbttagcompound1.putByte("Slot", (byte)i);
                contents[i].writeToNBT(nbttagcompound1);
                nbttaglist.addTag(nbttagcompound1);
            }
        }
        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putBoolean("isActive",isActive);
        nbttagcompound.putInt("checkAmount",amount);
        nbttagcompound.putInt("mode",mode);
        nbttagcompound.putBoolean("useMeta",useMeta);
        nbttagcompound.putInt("asmSlot",asmSlot);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public TileEntity connectedTile;
    public int asmSlot = 0;

}
