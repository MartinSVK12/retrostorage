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
        }
        /*if(inventory instanceof TileEntityNetworkDevice){
            if(this.slotIndex == 0 && itemStack1 != null && itemStack1.getItem() instanceof ItemStorageDisc){
                int page;
                try {
                    page = (int) inventory.getClass().getField("page").get(inventory);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                DiscManipulator.saveDisc(itemStack1,((TileEntityNetworkDevice) inventory).network.inventory, page);
                DiscManipulator.clearDigitalInv(((TileEntityNetworkDevice) inventory).network.inventory);
            } else if(this.slotIndex != 0 && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                int page;
                try {
                    page = (int) inventory.getClass().getField("page").get(inventory);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                DiscManipulator.saveDisc(this.inventory.getStackInSlot(0),((TileEntityNetworkDevice) inventory).network.inventory,page);
            }
            //System.out.println((itemStack1 != null ? itemStack1.toString() : "null") +" picked up from slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
            super.onPickupFromSlot(itemStack1);
        } else if(inventory instanceof TileEntityDigitalChest) {
            if(this.slotIndex == 0 && itemStack1 != null && itemStack1.getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(itemStack1,this.inventory, ((TileEntityDigitalChest) inventory).page);
                DiscManipulator.clearDigitalInv(this.inventory);
            } else if(this.slotIndex != 0 && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(this.inventory.getStackInSlot(0),this.inventory,((TileEntityDigitalChest)inventory).page);
            }
            //System.out.println((itemStack1 != null ? itemStack1.toString() : "null") +" picked up from slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
            super.onPickupFromSlot(itemStack1);
        }*/

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
        }
        /*if(inventory instanceof TileEntityNetworkDevice){
            if(itemStack1 != null){
                //System.out.println(itemStack1 +" inserted into slot "+ this +" (index "+ this.slotIndex +"/"+ this.variableIndex +")");
            }
            this.onSlotChanged();
            this.inventory.setInventorySlotContents(this.variableIndex, itemStack1);
//            super.putStack(itemStack1);
            if(this.slotIndex == 0 && itemStack1.getItem() instanceof ItemStorageDisc){
                DiscManipulator.clearDigitalInv(((TileEntityNetworkDevice) inventory).network.inventory);
                int page;
                try {
                    page = (int) inventory.getClass().getField("page").get(inventory);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                DiscManipulator.loadDisc(itemStack1,((TileEntityNetworkDevice) inventory).network.inventory,page);
            } else if(this.slotIndex != 0 && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                int page;
                try {
                    page = (int) inventory.getClass().getField("page").get(inventory);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                DiscManipulator.saveDisc(this.inventory.getStackInSlot(0),((TileEntityNetworkDevice) inventory).network.inventory,page);
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
                DiscManipulator.loadDisc(itemStack1,this.inventory,((TileEntityDigitalChest)inventory).page);
            } else if(this.slotIndex != 0 && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(0).getItem() instanceof ItemStorageDisc){
                DiscManipulator.saveDisc(this.inventory.getStackInSlot(0),this.inventory,((TileEntityDigitalChest)inventory).page);
            }
        }*/
    }
}
