package sunsetsatellite.retrostorage.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import sunsetsatellite.retrostorage.block.entity.DigitalChestBlockEntity;
import sunsetsatellite.retrostorage.item.StorageDiscItem;

public class SlotDigitalChest extends SlotVariable{
    public SlotDigitalChest(Inventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if(this.slotIndex == 0 && stack != null && stack.getItem() instanceof StorageDiscItem){
            return true;
        }
        ItemStack discStack = this.inventory.getStack(0);
        if(discStack != null && discStack.getItem() instanceof StorageDiscItem disc) {
            if (inventory instanceof DigitalChestBlockEntity chest) {
                int slotsUsed = chest.getAmountOfUsedSlots();
                return slotsUsed <= disc.getMaxStackCapacity();
            }
        }
        return false;
    }

    @Override
    public void onCrafted(ItemStack stack) {
        if(stack.getItem() instanceof StorageDiscItem && slotIndex == 0 && inventory instanceof DigitalChestBlockEntity chest){
            DiscNbtManipulator.saveDisc(stack,chest);
            DiscNbtManipulator.clearInventory(chest);
        } else if(inventory instanceof DigitalChestBlockEntity chest){
            DiscNbtManipulator.saveDisc(stack,chest);
        }
        super.onCrafted(stack);
    }

    @Override
    public void setStack(ItemStack stack) {
        super.setStack(stack);
        if(inventory instanceof DigitalChestBlockEntity chest){
            if(stack != null && stack.getItem() instanceof StorageDiscItem && slotIndex == 0){
                DiscNbtManipulator.loadDisc(stack,chest);
            } else if(inventory.getStack(0) != null && inventory.getStack(0).getItem() instanceof StorageDiscItem){
                DiscNbtManipulator.saveDisc(inventory.getStack(0),chest);
            }
        }
    }
}
