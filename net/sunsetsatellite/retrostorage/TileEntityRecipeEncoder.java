package net.sunsetsatellite.retrostorage;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import net.minecraft.src.*;
import org.lwjgl.Sys;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityRecipeEncoder extends TileEntityDigitalContainer
	implements IInventory{

	public TileEntityRecipeEncoder() {
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
        return "Recipe Encoder";
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
        facing = nbttagcompound.getInteger("facing");
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
        nbttagcompound.setInteger("facing",facing);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

	public void updateEntity()
    {
		return;
    }
	
	public void encodeDisc() {
        if(!debug){
            ItemStack recipeDisc = getStackInSlot(9);
            if (recipeDisc != null) {
                if (recipeDisc.getItem() instanceof ItemRecipeDisc) {
                    ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
                    for(int i = 0;i<9;i++) {
                        ItemStack item = getStackInSlot(i);
                        if(item != null) {
                            item = item.copy();
                            item.stackSize = 1;
                            itemList.add(i, item);
                        } else {
                            itemList.add(i, null);
                        }
                    }
                    //System.out.println(itemList.toString());
                    NBTTagCompound nbt = DiscManipulator.convertRecipeToNBT(itemList);
                    recipeDisc.setItemData(nbt);
                    //System.out.println(recipeDisc.getItemData().toString());
                }
            }
        } else {
            /*//plan A (hard coded ids, recipe will be wack if they're changed)
            StringBuilder s = new StringBuilder("ModLoader.AddRecipe(new ItemStack(");
            ItemStack output = getStackInSlot(9);
            s.append(output.itemID).append(",1,").append(output.getItemDamage()).append(")");
            s.append(",\"123\",\"456\",\"789\"");
            for(int i = 0;i<9;i++){
                ItemStack item = getStackInSlot(i);
                if(item != null){
                    s.append(",'").append(i).append("',").append("new ItemStack(").append(item.itemID).append(",1,").append(item.getItemDamage()).append(")");
                }
            }
            s.append(");");
            System.out.println(s);*/

            //plan B (reflection)
            StringBuilder s = new StringBuilder("ModLoader.AddRecipe(new ItemStack(");
            ItemStack output = getStackInSlot(9);
            Object item;
            if(output.itemID > 255){
                item = output.getItem();
                s.append(getItemFieldName((Item) item));
                s.append(",1,");
                s.append(output.getItemDamage());
                s.append(")");
                s.append(",\"123\",\"456\",\"789\"");
                for(int i = 0;i<9;i++){
                    ItemStack itemstack = getStackInSlot(i);
                    if(itemstack != null && itemstack.getItemDamage() == 0){
                        Object item2;
                        if(itemstack.itemID > 255) {
                            item2 = itemstack.getItem();
                            s.append(",'").append(i+1).append("',").append(getItemFieldName((Item) item2));
                        }
                        if(itemstack.itemID < 256) {
                            item2 = Block.blocksList[itemstack.itemID];
                            s.append(",'").append(i+1).append("',").append(getBlockFieldName((Block) item2));
                        }
                    } else if(itemstack != null && itemstack.getItemDamage() > 0){
                        Object item2;
                        if(itemstack.itemID < 256) {
                            item2 = Block.blocksList[itemstack.itemID];
                            s.append(",'").append(i+1).append("',new ItemStack(").append(getBlockFieldName((Block) item2)).append(",1,").append(itemstack.getItemDamage()).append(")");
                        }
                        if(itemstack.itemID > 255) {
                            item2 = itemstack.getItem();
                            s.append(",'").append(i+1).append("',new ItemStack(").append(getItemFieldName((Item) item2)).append(",1,").append(itemstack.getItemDamage()).append(")");
                        }
                    }
                }

            } else {
                item = Block.blocksList[output.itemID];
                s.append(getBlockFieldName((Block)item));
                s.append(",1,");
                s.append(output.getItemDamage());
                s.append(")");
                s.append(",\"123\",\"456\",\"789\"");
                for(int i = 0;i<9;i++){
                    ItemStack itemstack = getStackInSlot(i);
                    if(itemstack != null && itemstack.getItemDamage() == 0){
                        Object item2;
                        if(itemstack.itemID < 256) {
                            item2 = Block.blocksList[itemstack.itemID];
                            s.append(",'").append(i+1).append("',").append(getBlockFieldName((Block) item2));
                        }
                        if(itemstack.itemID > 255) {
                            item2 = itemstack.getItem();
                            s.append(",'").append(i+1).append("',").append(getItemFieldName((Item) item2));
                        }
                    } else if(itemstack != null && itemstack.getItemDamage() > 0){
                        Object item2;
                        if(itemstack.itemID < 256) {
                            item2 = Block.blocksList[itemstack.itemID];
                            s.append(",'").append(i+1).append("',new ItemStack(").append(getBlockFieldName((Block) item2)).append(",1,").append(itemstack.getItemDamage()).append(")");
                        }
                        if(itemstack.itemID > 255) {
                            item2 = itemstack.getItem();
                            s.append(",'").append(i+1).append("',new ItemStack(").append(getItemFieldName((Item) item2)).append(",1,").append(itemstack.getItemDamage()).append(")");
                        }
                    }
                }
            }
            s.append(");");
            System.out.println(s);
        }
	}

    private String getItemFieldName(Item item){
        try{
            ArrayList<Field> fields = new ArrayList<>(Arrays.asList(Item.class.getDeclaredFields()));
            //fields.addAll(Arrays.asList(mod_RetroStorage.class.getDeclaredFields()));
            for (Field field : fields) {
                if(field.getType().isAssignableFrom(Item.class) && Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Item fieldItem = (Item) field.get(null);
                    if(fieldItem.equals(item)){
                        return "Item."+field.getName();
                    }
                }
            }
            fields = new ArrayList<>(Arrays.asList(mod_RetroStorage.class.getDeclaredFields()));
            for (Field field : fields) {
                if(field.getType().isAssignableFrom(Item.class) && Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Item fieldItem = (Item) field.get(null);
                    if(fieldItem.equals(item)){
                        return "mod_RetroStorage."+field.getName();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private String getBlockFieldName(Block item){
        try{
            ArrayList<Field> fields = new ArrayList<>(Arrays.asList(Block.class.getDeclaredFields()));
            //fields.addAll(Arrays.asList(mod_RetroStorage.class.getDeclaredFields()));
            for (Field field : fields) {
                if(field.getType().isAssignableFrom(Block.class) && Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Block fieldItem = (Block) field.get(null);
                    if(fieldItem.equals(item)){
                        return "Block."+field.getName();
                    }
                }
            }
            fields = new ArrayList<>(Arrays.asList(mod_RetroStorage.class.getDeclaredFields()));
            for (Field field : fields) {
                if(field.getType().isAssignableFrom(Block.class) && Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Block fieldItem = (Block) field.get(null);
                    if(fieldItem.equals(item)){
                        return "mod_RetroStorage."+field.getName();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
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
    private boolean debug = false;
}
