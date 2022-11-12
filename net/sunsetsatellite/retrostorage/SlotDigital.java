package net.sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class SlotDigital extends Slot {
    public int variableIndex = 0;

    public SlotDigital(IInventory iInventory1, int i2, int i3, int i4) {
        super( iInventory1, i2, i3, i4);
        variableIndex = i2;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        if(this.slotIndex == 0 && itemstack != null && itemstack.getItem() instanceof  ItemStorageDisc){
            return true;
        }
        ItemStack discStack = this.inventory.getStackInSlot(0);
        if(discStack != null && discStack.getItem() instanceof ItemStorageDisc){
            if(inventory instanceof TileEntityDigitalChest){
                int slotsUsed = ((TileEntityDigitalChest) inventory).getAmountOfUsedSlots();
                return slotsUsed <= ((ItemStorageDisc) discStack.getItem()).getMaxStackCapacity();
            } else if (inventory instanceof  TileEntityDigitalTerminal){
                if(((TileEntityDigitalTerminal) inventory).controller.network_drive == null){
                    return false;
                }
                int slotsUsed = ((TileEntityDigitalTerminal) inventory).getAmountOfUsedSlots();
                if(((TileEntityDigitalTerminal) inventory).controller.network_drive == null){
                    return false;
                } else {
                    return slotsUsed < ((TileEntityDiscDrive)((TileEntityDigitalTerminal) inventory).controller.network_drive).virtualDriveMaxStacks;
                }

            }
            return true;
        }
        return false;
    }

    public void onPickupFromSlot(ItemStack itemStack1) {
        if(inventory instanceof TileEntityInNetwork){
            if(this.slotIndex == 0 && itemStack1 != null && itemStack1.getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(itemStack1,((TileEntityInNetwork) inventory).controller.network_inv, ((TileEntityDigitalContainer) inventory).page);
                DiscManipulator.clearDigitalInv(((TileEntityInNetwork) inventory).controller.network_inv);
            } else if(this.slotIndex != 0 && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(this.inventory.getStackInSlot(0),((TileEntityInNetwork) inventory).controller.network_inv,((TileEntityDigitalContainer)inventory).page);
            }
            //System.out.println((itemStack1 != null ? itemStack1.toString() : "null") +" picked up from slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
            super.onPickupFromSlot(itemStack1);
        } else if(inventory instanceof TileEntityDigitalChest) {
            if(this.slotIndex == 0 && itemStack1 != null && itemStack1.getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(itemStack1,this.inventory, ((TileEntityDigitalContainer) inventory).page);
                DiscManipulator.clearDigitalInv(this.inventory);
            } else if(this.slotIndex != 0 && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(this.inventory.getStackInSlot(0),this.inventory,((TileEntityDigitalContainer)inventory).page);
            }
            //System.out.println((itemStack1 != null ? itemStack1.toString() : "null") +" picked up from slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
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
        if(inventory instanceof TileEntityInNetwork){
            if(itemStack1 != null){
                //System.out.println(itemStack1 +" inserted into slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
            }
            this.onSlotChanged();
            this.inventory.setInventorySlotContents(this.variableIndex, itemStack1);
//            super.putStack(itemStack1);
            if(this.slotIndex == 0 && itemStack1.getItem() instanceof ItemStorageDisc){
                DiscManipulator.clearDigitalInv(((TileEntityInNetwork) inventory).controller.network_inv);
                DiscManipulator.loadDisc(itemStack1,((TileEntityInNetwork) inventory).controller.network_inv,((TileEntityDigitalContainer)inventory).page);
            } else if(this.slotIndex != 0 && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(this.inventory.getStackInSlot(0),((TileEntityInNetwork) inventory).controller.network_inv,((TileEntityDigitalContainer)inventory).page);
            }
        } else if(inventory instanceof TileEntityDigitalChest) {
            if(itemStack1 != null) {
                //System.out.println(itemStack1 +" inserted into slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
            }
            this.onSlotChanged();
            this.inventory.setInventorySlotContents(this.variableIndex, itemStack1);
//            super.putStack(itemStack1);
            if(this.slotIndex == 0 && itemStack1.getItem() instanceof ItemStorageDisc){
                DiscManipulator.clearDigitalInv(this.inventory);
                DiscManipulator.loadDisc(itemStack1,this.inventory,((TileEntityDigitalContainer)inventory).page);
            } else if(this.slotIndex != 0 && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(this.inventory.getStackInSlot(0),this.inventory,((TileEntityDigitalContainer)inventory).page);
            }
        }
    }
}
