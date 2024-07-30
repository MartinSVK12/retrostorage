package sunsetsatellite.retrostorage.containers;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.util.helper.MathHelper;

public abstract class ContainerDigital extends Container {

    @Override
    public ItemStack clickInventorySlot(InventoryAction action, int[] args, EntityPlayer player) {
        if(action.requireCreative()) {
            if(player.getGamemode().consumeBlocks()) {
                System.out.println("Player "+player.username+" used a creative inventory action but is not in creative mode!");
                return null;
            }
        }

        InventoryPlayer inventory = player.inventory;

        //Drop Item
        if(action == InventoryAction.DROP_HELD_SINGLE || action == InventoryAction.DROP_HELD_STACK) {
            if(inventory.getHeldItemStack() != null) {
                if(action == InventoryAction.DROP_HELD_STACK) {
                    player.dropPlayerItem(inventory.getHeldItemStack());
                    inventory.setHeldItemStack(null);
                }
                if(action == InventoryAction.DROP_HELD_SINGLE) {
                    player.dropPlayerItem(inventory.getHeldItemStack().splitStack(1));
                    if(inventory.getHeldItemStack().stackSize == 0) {
                        inventory.setHeldItemStack(null);
                    }
                }
            }
            onCraftMatrixChanged(inventory);
            return null;
        }

        if(action == InventoryAction.PICKUP_SIMILAR || action == InventoryAction.DRAG_ITEMS_ALL || action == InventoryAction.DRAG_ITEMS_ONE || action == InventoryAction.CREATIVE_DRAG) {
            ItemStack itemStack = inventory.getHeldItemStack();
            if(itemStack == null) {
                return null;
            }
            ItemStack controlStack = itemStack.copy();
            if(action == InventoryAction.PICKUP_SIMILAR) {
                pickupSimilarItems(player);
            }else {
                dragItemsAcrossSlots(player, action, args);
            }
            onCraftMatrixChanged(player.inventory);
            return controlStack;
        }

        if(args == null || args.length == 0) {
            return null;
        }
        int slotId = args[0];
        Slot slot = getSlot(slotId);
        if(slot == null) {
            onCraftMatrixChanged(inventory);
            return null;
        }

        //Used in multiplayer to check if the item the player clicked is the same item on the server
        ItemStack controlStack = null;
        ItemStack stackInSlot = slot.getStack();
        Item itemInSlot = stackInSlot != null ? stackInSlot.getItem() : null;

        if(stackInSlot != null) {
            controlStack = stackInSlot.copy();
        }

        if(action == InventoryAction.INTERACT_SLOT || action == InventoryAction.INTERACT_GRABBED) {
            ItemStack grabbedItem = player.inventory.getHeldItemStack();

            Item interactItem;
            if(action == InventoryAction.INTERACT_SLOT) {
                if(stackInSlot == null) {
                    return null;
                }

                interactItem = itemInSlot;
            }else {
                interactItem = player.inventory.getHeldItemStack().getItem();
            }

            if(!interactItem.hasInventoryInteraction() || !slot.allowItemInteraction()) {
                return controlStack;
            }

            ItemStack result = interactItem.onInventoryInteract(player, slot, stackInSlot, action == InventoryAction.INTERACT_GRABBED);
            if(result != null && result.stackSize <= 0) {
                result = null;
            }
            slot.putStack(result);

            grabbedItem = player.inventory.getHeldItemStack();
            if(grabbedItem != null && grabbedItem.stackSize <= 0) {
                player.inventory.setHeldItemStack(null);
            }

            onCraftMatrixChanged(inventory);
            return controlStack;
        }

        if(action == InventoryAction.EQUIP_ARMOR) {
            handleArmorEquip(slot, player);
            onCraftMatrixChanged(inventory);
            return controlStack;
        }

        if(action == InventoryAction.HOTBAR_ITEM_SWAP) {
            handleHotbarSwap(args, player);
            onCraftMatrixChanged(inventory);
            return controlStack;
        }

        if(action == InventoryAction.MOVE_STACK || action == InventoryAction.MOVE_SINGLE_ITEM || action == InventoryAction.MOVE_SIMILAR || action == InventoryAction.MOVE_ALL) {
            int target = args.length > 1 ? args[1] : 0;
            handleItemMove(action, slot, target, player);
            onCraftMatrixChanged(player.inventory);
            return controlStack;
        }

        if(action == InventoryAction.SORT) {
            if(player.world.isClientSide) {
                return null;
            }
            handleSort(args, player);
            onCraftMatrixChanged(inventory);
            return controlStack;
        }

        //Regular Press
        slot.onSlotChanged();
        ItemStack stackInHand = inventory.getHeldItemStack();

        if(action == InventoryAction.DROP) {
            if(stackInSlot == null) {
                return null;
            }
            int amount = args.length > 1 ? args[1] : 1;
            amount = Math.min(amount, stackInSlot.stackSize);
            ItemStack dropStack = slot.decrStackSize(amount);
            if(stackInSlot.stackSize <= 0) {
                slot.putStack(null);
            }
            slot.onPickupFromSlot(dropStack);
            player.dropPlayerItem(dropStack);
            onCraftMatrixChanged(inventory);
            return controlStack;
        }

        //Clicked Slot With Item
        if(action == InventoryAction.CREATIVE_GRAB || action == InventoryAction.CREATIVE_MOVE || action == InventoryAction.CREATIVE_DELETE) {
            if(action == InventoryAction.CREATIVE_DELETE) {
                int count = args.length > 1 ? args[1] : 1;

                for(int i=0; i < count; i++) {
                    Slot slot1 = getSlot(slotId + i);
                    if(slot1 != null) {
                        slot1.putStack(null);
                    }
                }
            }else {
                int amount = args.length > 1 ? args[1] : 0;
                if(stackInSlot != null) {
                    amount = MathHelper.clamp(amount, 0, stackInSlot.getMaxStackSize());
                }else {
                    amount = 0;
                }
                if(action == InventoryAction.CREATIVE_GRAB) {
                    ItemStack stack;
                    if(amount > 0) {
                        stack = slot.getStack().copy();
                        stack.stackSize = amount;
                    }else {
                        stack = null;
                    }
                    inventory.setHeldItemStack(stack);
                }
                if(action == InventoryAction.CREATIVE_MOVE) {
                    if(amount > 0) {
                        ItemStack stack = slot.getStack().copy();
                        stack.stackSize = amount;
                        player.inventory.insertItem(stack, false);
                    }
                }
            }
            onCraftMatrixChanged(player.inventory);
            return controlStack;
        }

        //Clicked Empty Slot
        if(stackInSlot == null) {
            //Slot is Empty
            if(stackInHand != null && slot.canPutStackInSlot(stackInHand)) {
                int i1 = action != InventoryAction.CLICK_LEFT ? 1 : stackInHand.stackSize;
                if(i1 > slot.getSlotStackLimit()) {
                    i1 = slot.getSlotStackLimit();
                }
                slot.putStack(stackInHand.splitStack(i1));
            }
        } else if(stackInHand == null) {
            //Not Holding Anything
            int j1 = action != InventoryAction.CLICK_LEFT ? (stackInSlot.stackSize + 1) / 2 : stackInSlot.stackSize;
            ItemStack itemstack5 = slot.decrStackSize(j1);
            inventory.setHeldItemStack(itemstack5);
            slot.onPickupFromSlot(inventory.getHeldItemStack());
        } else if(slot.canPutStackInSlot(stackInHand)) {
            //Can Put Stack in Slot
            if(!stackInSlot.canStackWith(stackInHand)) {
                //Insert Item
                if(stackInHand.stackSize <= slot.getSlotStackLimit()) {
                    slot.putStack(stackInHand);
                    inventory.setHeldItemStack(null);
                }
            } else {
                //Combine Stacks
                int splitSize = action != InventoryAction.CLICK_LEFT ? 1 : stackInHand.stackSize;
                if(splitSize > slot.getSlotStackLimit() - stackInSlot.stackSize) {
                    splitSize = slot.getSlotStackLimit() - stackInSlot.stackSize;
                }
                if(splitSize > stackInHand.getMaxStackSize() - stackInSlot.stackSize) {
                    splitSize = stackInHand.getMaxStackSize() - stackInSlot.stackSize;
                }
                stackInHand.splitStack(splitSize);
                stackInSlot.stackSize += splitSize;
            }
        } else if(stackInSlot.canStackWith(stackInHand)) {
            //Pickup Item
            if(stackInSlot.stackSize + stackInHand.stackSize <= stackInHand.getMaxStackSize()) {
                slot.putStack(null);

                //This is currently used for maps, and for Crafting Statistics / Achievements
                //This might modify the stack
                slot.onPickupFromSlot(stackInSlot);

                if(stackInSlot.canStackWith(stackInHand) && stackInHand.stackSize + stackInSlot.stackSize <= stackInHand.getMaxStackSize()) {
                    stackInHand.stackSize += stackInSlot.stackSize;
                }else {
                    //If the item gets modified and can no longer be stacked with the currently held item, drop it
                    player.dropPlayerItem(stackInSlot);
                }
            }
        }

        if(inventory.getHeldItemStack() != null && inventory.getHeldItemStack().stackSize <= 0) {
            inventory.setHeldItemStack(null);
        }

        onCraftMatrixChanged(player.inventory);

        return controlStack;
    }
}
