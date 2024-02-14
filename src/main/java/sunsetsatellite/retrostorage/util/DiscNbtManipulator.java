package sunsetsatellite.retrostorage.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import sunsetsatellite.retrostorage.block.entity.DigitalChestBlockEntity;
import sunsetsatellite.retrostorage.item.StorageDiscItem;

import java.util.Arrays;
import java.util.Collection;

public class DiscNbtManipulator {

    public static void clearInventory(DigitalInventoryBase inventory) {
        if(inventory instanceof DigitalChestBlockEntity){
            ItemStack disc = inventory.getContents()[0];
            Arrays.fill(inventory.getContents(), null);
            inventory.getContents()[0] = disc;
        } else {
            Arrays.fill(inventory.getContents(), null);
        }

    }

    public static void saveDisc(ItemStack disc, DigitalInventoryBase inventory){
        if (disc == null || !(disc.getItem() instanceof StorageDiscItem) || inventory == null) {
            return;
        }
        NbtCompound discNbt = new NbtCompound();
        for (int id = inventory instanceof DigitalChestBlockEntity ? 1 : 0; id < inventory.getContents().length; id++) {
            if (inventory.getContents()[id] != null) {
                NbtCompound itemNbt = new NbtCompound();
                inventory.getContents()[id].writeNbt(itemNbt);
                discNbt.put(String.valueOf(id), itemNbt);
            }
        }
        disc.getStationNbt().put("contents", discNbt);
    }
    public static void loadDisc(ItemStack disc, DigitalInventoryBase inventory){
        if (disc == null || !(disc.getItem() instanceof StorageDiscItem) || inventory == null) {
            return;
        }
        NbtCompound discNbt = disc.getStationNbt().getCompound("contents");
        for (Object itemNbt : discNbt.values()) {
            if(itemNbt instanceof NbtCompound) {
                int id = Integer.parseInt(((NbtCompound) itemNbt).getKey());
                if(id >= inventory.size()){
                    continue;
                }
                inventory.getContents()[id] = new ItemStack((NbtCompound) itemNbt);
            }
        }
    }
}
