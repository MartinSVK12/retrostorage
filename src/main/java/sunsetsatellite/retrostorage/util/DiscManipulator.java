package sunsetsatellite.retrostorage.util;





import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscManipulator {

    public static void saveDisc(ItemStack disc, IInventory inv, int page) {
        saveDisc(disc, inv);
    }


    public static void saveDisc(ItemStack disc, IInventory inv){
        if(disc == null || inv == null){
            return;
        }
        CompoundTag discNBT = disc.getData().getCompound("disc");
        for(int i = 1; i < inv.getSizeInventory();i++){
            ItemStack item = inv.getStackInSlot(i);
            CompoundTag itemNBT = new CompoundTag();
            if(item != null){
                itemNBT.putByte("Count", (byte)item.stackSize);
                itemNBT.putShort("id", (short)item.itemID);
                itemNBT.putShort("Damage", (short)item.getMetadata());
                itemNBT.putByte("Expanded", (byte)1);
                itemNBT.putInt("Version", 19133);
                itemNBT.putCompound("Data", item.getData());
                discNBT.putCompound(String.valueOf(i),itemNBT);
            } else {
                discNBT.getValue().remove(String.valueOf(i));
            }
        }
        //System.out.printf("Data: %s%n",discNBT.toStringExtended());
        disc.getData().putCompound("disc",discNBT);
    }

    /*public static void saveDisc(TileEntityDigitalController controller){
        ItemStack disc = controller.network_disc;
        IInventory inv = controller.network_inv;
        if(disc == null || !controller.isActive() && !controller.clearing){
            return;
        }
        CompoundTag discNBT = disc.getData();
        for(int i = 1; i < inv.getSizeInventory();i++){
            ItemStack item = inv.getStackInSlot(i);
            CompoundTag itemNBT = new CompoundTag();
            if(item != null){
                itemNBT.putByte("Count", (byte)item.stackSize);
                itemNBT.putShort("id", (short)item.itemID);
                itemNBT.putShort("Damage", (short)item.getMetadata());
                itemNBT.putCompound("Data", item.getData());
                discNBT.putCompound(String.valueOf(i),itemNBT);
            } else {
                discNBT.removeTag(String.valueOf(i));
            }
        }
        //System.out.printf("Data: %s%n",discNBT.toStringExtended());
        disc.getData() = discNBT;
    }*/

    public static void loadDisc(ItemStack disc, IInventory inv, int page){
        //System.out.printf("Loading contents of page %d of disc %s to inventory %s%n",page,disc.toString(),inv.toString());
        AtomicInteger i = new AtomicInteger();
        Collection<?> values = disc.getData().getCompound("disc").getValues();
        values.forEach((V)->{
            if(i.get() < 37) {
                if(V instanceof CompoundTag){
                    String K = ((Tag<?>) V).getTagName();
                    ItemStack itemStack = ItemStack.readItemStackFromNbt((CompoundTag) V);
                    if(itemStack.getItem() != null){
                        inv.setInventorySlotContents((Integer.parseInt(K) * page), itemStack);
                    }
                    i.getAndIncrement();
                }
            }
        });

    }

    public static void loadDisc(ItemStack disc, IInventory inv){
        Collection<?> values = disc.getData().getCompound("disc").getValues();
        values.forEach((V)->{
            if(V instanceof CompoundTag) {
                String K = ((Tag<?>) V).getTagName();
                ItemStack itemStack = ItemStack.readItemStackFromNbt((CompoundTag) V);
                if(itemStack == null) return;
                if(itemStack.getItem() != null) {
                    if (inv instanceof TileEntityDigitalChest && Integer.parseInt(K) == 0) {
                        inv.setInventorySlotContents(Integer.parseInt(K) + 1, itemStack);
                    } else if (inv instanceof TileEntityDigitalChest && Integer.parseInt(K) >= inv.getSizeInventory()) {
                    } else {
                        inv.setInventorySlotContents(Integer.parseInt(K), itemStack);
                    }
                }
            }
        });
		/*for(int i = 0; i <= discNBT.size();i++){
			System.out.println(i);
			if(discNBT.containsKey(String.valueOf(i))){
				System.out.println(i+"*");
				ItemStack item = new ItemStack(discNBT.getCompound(String.valueOf(i)));
				inv.setInventorySlotContents(i,item);
			}
		}*/

    }

    public static void clearDigitalInv(IInventory inv){
        //System.out.printf("Clearing digital inventory %s%n",inv.toString());
        Arrays.fill(((InventoryDigital)inv).inventoryContents,null);
    }

    public static ArrayList<CompoundTag> getProcessesFromDisc(ItemStack disc){
        CompoundTag tasksNBT = disc.getData().getCompound("disc").getCompound("tasks");
        ArrayList<CompoundTag> values = new ArrayList<>();
        for (Tag<?> value : tasksNBT.getValues()) {
            values.add((CompoundTag) value);
        }
        return new ArrayList<>(values);
    }

}
