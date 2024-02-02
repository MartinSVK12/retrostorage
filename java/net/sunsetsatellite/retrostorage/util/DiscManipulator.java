package net.sunsetsatellite.retrostorage.util;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscManipulator {

    public static void saveDisc(ItemStack disc, IInventory inv, int page) {
        saveDisc(disc, inv);
    }


    public static void saveDisc(ItemStack disc, IInventory inv){
        if(disc == null || inv == null){
            return;
        }
        NBTTagCompound discNBT = disc.stackTagCompound.getCompoundTag("disc");
        for(int i = 1; i < inv.getSizeInventory();i++){
            ItemStack item = inv.getStackInSlot(i);
            NBTTagCompound itemNBT = new NBTTagCompound();
            if(item != null){
                itemNBT.setByte("Count", (byte)item.stackSize);
                itemNBT.setShort("id", (short)item.itemID);
                itemNBT.setShort("Damage", (short)item.getItemDamage());
                if(item.hasTagCompound()){
                    itemNBT.setCompoundTag("Data", item.stackTagCompound);
                }
                discNBT.setCompoundTag(String.valueOf(i),itemNBT);
            } else {
                NBTHelper.removeTag(discNBT,String.valueOf(i));
            }
        }
        //System.out.printf("Data: %s%n",discNBT.toStringExtended());
        disc.stackTagCompound.setCompoundTag("disc",discNBT);
    }

    /*public static void saveDisc(TileEntityDigitalController controller){
        ItemStack disc = controller.network_disc;
        IInventory inv = controller.network_inv;
        if(disc == null || !controller.isActive() && !controller.clearing){
            return;
        }
        NBTTagCompound discNBT = disc.tag;
        for(int i = 1; i < inv.getSizeInventory();i++){
            ItemStack item = inv.getStackInSlot(i);
            NBTTagCompound itemNBT = new NBTTagCompound();
            if(item != null){
                itemNBT.setByte("Count", (byte)item.stackSize);
                itemNBT.setShort("id", (short)item.itemID);
                itemNBT.setShort("Damage", (short)item.getMetadata());
                itemNBT.setCompoundTag("Data", item.tag);
                discNBT.setCompoundTag(String.valueOf(i),itemNBT);
            } else {
                discNBT.removeTag(String.valueOf(i));
            }
        }
        //System.out.printf("Data: %s%n",discNBT.toStringExtended());
        disc.tag = discNBT;
    }*/

    public static void loadDisc(ItemStack disc, IInventory inv, int page){
        //System.out.printf("Loading contents of page %d of disc %s to inventory %s%n",page,disc.toString(),inv.toString());
        AtomicInteger i = new AtomicInteger();
        Collection<?> values = disc.stackTagCompound.getCompoundTag("disc").getTags();
        values.forEach((V)->{
            if(i.get() < 37) {
                if(V instanceof NBTTagCompound){
                    String K = ((NBTBase) V).getName();
                    ItemStack itemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) V);//new ItemStack((NBTTagCompound) V);
                    if(itemStack.getItem() != null){
                        inv.setInventorySlotContents((Integer.parseInt(K) * page), itemStack);
                    }
                    i.getAndIncrement();
                }
            }
        });

    }

    public static void loadDisc(ItemStack disc, IInventory inv){
        Collection<?> values = disc.stackTagCompound.getCompoundTag("disc").getTags();
        values.forEach((V)->{
            if(V instanceof NBTTagCompound) {
                String K = ((NBTBase) V).getName();
                ItemStack itemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) V);//new ItemStack((NBTTagCompound) V);
                if(itemStack.getItem() != null) {
                    inv.setInventorySlotContents(Integer.parseInt(K), itemStack);
                    /*if (inv instanceof TileEntityDigitalChest && Integer.parseInt(K) == 0) {
                        inv.setInventorySlotContents(Integer.parseInt(K) + 1, itemStack);
                    } else if (inv instanceof TileEntityDigitalChest && Integer.parseInt(K) >= inv.getSizeInventory()) {
                    } else {

                    }*/
                }
            }
        });
		/*for(int i = 0; i <= discNBT.size();i++){
			System.out.println(i);
			if(discNBT.hasKey(String.valueOf(i))){
				System.out.println(i+"*");
				ItemStack item = new ItemStack(discNBT.getCompoundTag(String.valueOf(i)));
				inv.setInventorySlotContents(i,item);
			}
		}*/

    }

    public static void clearDigitalInv(IInventory inv){
        //System.out.printf("Clearing digital inventory %s%n",inv.toString());
        Arrays.fill(((InventoryDigital)inv).inventoryContents,null);
    }

    public static ArrayList<NBTTagCompound> getProcessesFromDisc(ItemStack disc){
        NBTTagCompound tasksNBT = disc.stackTagCompound.getCompoundTag("disc").getCompoundTag("tasks");
        ArrayList<NBTTagCompound> tasks = new ArrayList<>(tasksNBT.getTags());
        return tasks;
    }

}
