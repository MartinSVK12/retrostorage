package sunsetsatellite.retrostorage.util;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalChest;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;

public class SlotDigital extends Slot{
    public int variableIndex = 0;
    public final int slotIndex;

    public SlotDigital(IInventory iInventory1, int id, int x, int y) {
        super( iInventory1, id, x, y);
        variableIndex = id;
        slotIndex = id;
    }

    public boolean canPutStackInSlot(ItemStack itemstack)
    {
        if(inventory instanceof InventoryPortable){
            return !((InventoryPortable) inventory).owner.isItemEqual(itemstack);
        }
        if(this.slotIndex == 0 && itemstack != null && itemstack.getItem() instanceof ItemStorageDisc){
            return true;
        }
        ItemStack discStack = this.inventory.getStackInSlot(0);
        if(discStack != null && discStack.getItem() instanceof ItemStorageDisc){
            if(inventory instanceof TileEntityDigitalChest){
                int slotsUsed = ((TileEntityDigitalChest) inventory).getAmountOfUsedSlots();
                return slotsUsed <= ((ItemStorageDisc) discStack.getItem()).getMaxStackCapacity();
            } else if (inventory instanceof TileEntityDigitalTerminal){
                if(((TileEntityDigitalTerminal) inventory).network.drive == null){
                    return false;
                }
                int slotsUsed = ((TileEntityDigitalTerminal) inventory).getAmountOfUsedSlots();
                if(((TileEntityDigitalTerminal) inventory).network.drive == null){
                    return false;
                } else {
                    return slotsUsed < ((TileEntityDigitalTerminal) inventory).network.drive.virtualDriveMaxStacks;
                }

            }
            return true;
        }
        return false;
    }

    public void onPickupFromSlot(ItemStack itemStack1) {
        if(inventory instanceof TileEntityNetworkDevice) {
            if (itemStack1 != null) {
                System.out.println(itemStack1 +" picked up from slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
            }
            DiscManipulator.saveDisc(((TileEntityNetworkDevice) inventory).network.drive.virtualDisc,((TileEntityNetworkDevice) inventory).network.inventory);
            super.onPickupFromSlot(itemStack1);
        } else if(inventory instanceof InventoryPortable){
            DiscManipulator.saveDisc(((InventoryPortable)inventory).owner,inventory);
            super.onPickupFromSlot(itemStack1);
        }
    }

    public ItemStack decrStackSize(int i1) {
        return this.inventory.decrStackSize(this.variableIndex, i1);
    }

    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.variableIndex);
    }

    public void putStack(ItemStack itemStack1) {
        if(inventory instanceof TileEntityNetworkDevice) {
            if (itemStack1 != null) {
                System.out.println(itemStack1 +" inserted into slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
            }
            this.onSlotChanged();
            this.inventory.setInventorySlotContents(this.variableIndex, itemStack1);
            DiscManipulator.saveDisc(((TileEntityNetworkDevice) inventory).network.drive.virtualDisc,((TileEntityNetworkDevice) inventory).network.inventory);
        } else if (inventory instanceof InventoryPortable){
            this.onSlotChanged();
            this.inventory.setInventorySlotContents(this.variableIndex, itemStack1);
            DiscManipulator.saveDisc(((InventoryPortable)this.inventory).owner,this.inventory);
        }
    }
}
