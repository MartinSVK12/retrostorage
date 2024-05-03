package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.IProcessor;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityAssembler extends TileEntityNetworkDevice
    implements IInventory, IProcessor
{
    public TileEntityAssembler() {
        contents = new ItemStack[9];
        this.workTimer = new TickTimer(this,this::work,10,true);
    }

    public int getSizeInventory()
    {
        return contents.length;
    }

    public boolean isEmpty() {
        for(int i = 0; i < getSizeInventory()-1; i++) {
            if(getStackInSlot(i) != null) {
                return false;
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
            if(network != null) {
                RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(getStackInSlot(i).getData().getCompound("recipe"));
                if(recipe != null) {
                    network.knownCraftables.remove(new NetworkCraftable(recipe));
                }
            }
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
        if(network != null) {
            if (itemstack == null) {
                RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(getStackInSlot(i).getData().getCompound("recipe"));
                if(recipe != null) {
                    network.knownCraftables.remove(new NetworkCraftable(recipe));
                }
            } else {
                RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(itemstack.getData().getCompound("recipe"));
                if(recipe != null){
                    network.knownCraftables.add(new NetworkCraftable(recipe));
                }
            }
        }
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
        return "Assembler";
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
            if(j < contents.length)
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

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(x, y, z) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
    }

    @Override
    public void sortInventory() {

    }

    @Override
    public void tick() {
        workTimer.tick();
        ArrayList<Class<?>> tiles = new ArrayList<>();
        tiles.add(TileEntityChest.class);
        connectedTiles = getConnectedTileEntity(tiles);
    }

    public void work(){
        if(network != null){
            if(task == null){
                if(stack == null || stack.isEmpty()){
                    stack = network.requestQueue.clone();
                }
                if(!network.requestQueue.isEmpty()){
                    acceptNextTask();
                }
            } else {
                fulfillRequest();
            }
        }
    }


    public void fulfillRequest(){
        if(task != null){
            if(task.update()){
                network.requestQueue.remove(task);
                task = null;
            }
        }
    }

    public ArrayList<RecipeEntryCrafting<?,ItemStack>> getRecipes(){
        ArrayList<RecipeEntryCrafting<?,ItemStack>> recipes = new ArrayList<>();
        for (ItemStack stack : contents) {
            if (stack != null && stack.getItem() == RetroStorage.recipeDisc) {
                RecipeEntryCrafting<?,ItemStack> recipe = RetroStorage.findRecipeFromNBT(stack.getData().getCompound("recipe"));
                if (recipe != null) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    public boolean acceptNextTask() {
        if(!stack.isEmpty()){
            CraftingTask t = stack.pop();
            if(t.processor == null){
                t.start(this);
                task = t;
                return true;
            }
        }
        return false;
    }


    private ItemStack[] contents;
    public CraftingTask task;
    public TickTimer workTimer;
    public ArrayDeque<CraftingTask> stack;
    public HashMap<String, TileEntity> connectedTiles = new HashMap<>();

    @Override
    public CraftingTask getCurrentTask() {
        return task;
    }

    public void cancelTask() {
        if(task != null){
            task.onCancelled();
            task = null;
        }
    }
}
