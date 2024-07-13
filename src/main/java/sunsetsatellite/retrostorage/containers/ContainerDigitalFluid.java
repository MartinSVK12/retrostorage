package sunsetsatellite.retrostorage.containers;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemBucketEmpty;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryPlayer;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.ContainerFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.retrostorage.util.InventoryFluidDigital;

import java.util.List;

public class ContainerDigitalFluid extends ContainerFluid {

    public ContainerDigitalFluid(IInventory inv, InventoryFluidDigital fluidInv) {
        super(inv, null);
        this.inv = fluidInv;
    }

    //prevent super tile field from being accessible from this class
    public Void tile = null;
    public InventoryFluidDigital inv;

    @Override
    public FluidStack clickFluidSlot(int slotID, int button, boolean shift, boolean control, EntityPlayer entityplayer) {
        if (inv == null) {
            return null;
        }
        if (slotID == -999) {
            return null;
        }
        SlotFluid slot = fluidSlots.get(slotID);
        InventoryPlayer inventoryPlayer = entityplayer.inventory;
        if (slot != null) {
            if (slot.getFluidStack() != null && slot.getFluidStack().amount >= 1000) {
                //extract fluid into bucket
                if (inventoryPlayer.getHeldItemStack() != null
                        && inventoryPlayer.getHeldItemStack().getItem() instanceof ItemBucketEmpty
                        && CatalystFluids.FLUIDS.findEmptyContainers(slot.getFluidStack().liquid).contains(inventoryPlayer.getHeldItemStack().getItem())) {

                    Item item = CatalystFluids.FLUIDS.findFilledContainersWithContainer(slot.getFluidStack().liquid, inventoryPlayer.getHeldItemStack().getItem()).get(0);
                    if (item != null) {
                        ItemStack stack = new ItemStack(item, 1);
                        if (inventoryPlayer.getHeldItemStack().stackSize > 1) {
                            boolean isInvFull = true;
                            for (int i = 0; i < inventoryPlayer.mainInventory.length; ++i) {
                                if (inventoryPlayer.mainInventory[i] == null) {
                                    isInvFull = false;
                                    break;
                                }
                            }
                            if (isInvFull) {
                                return fluidSlots.get(slotID).getFluidStack();
                            }
                            inventoryPlayer.insertItem(stack, false);
                            inventoryPlayer.getHeldItemStack().stackSize--;
                        } else {
                            inventoryPlayer.setHeldItemStack(stack);
                        }
                        inv.get(slot.slotIndex).amount -= 1000;
                        if (inv.get(slot.slotIndex).amount <= 0) {
                            inv.remove(slot.slotIndex, false);
                        }
                        slot.onPickupFromSlot(slot.getFluidStack());
                        slot.onSlotChanged();
                        return fluidSlots.get(slotID).getFluidStack();
                    }
                }
            }
            //insert fluid from bucket
            if (inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof ItemBucket) {
                ItemBucket bucket = (ItemBucket) inventoryPlayer.getHeldItemStack().getItem();
                List<BlockFluid> fluids = CatalystFluids.FLUIDS.findFluidsWithFilledContainer(bucket);
                if(!fluids.isEmpty()){
                    BlockFluid fluid = fluids.get(0);
                    if (slot.getFluidStack() == null) {
                        if (inv.getAllowedFluidsForSlot(slotID).isEmpty() || inv.getAllowedFluidsForSlot(slotID).contains(fluid)) {
                            if (slot.isFluidValid(fluid)) {
                                inventoryPlayer.setHeldItemStack(new ItemStack(bucket.getContainerItem(), 1));
                                slot.putStack(new FluidStack(fluid, 1000));
                                slot.onSlotChanged();
                            }
                        }
                    } else if (slot.getFluidStack() != null && slot.getFluidStack().getLiquid() == fluid) {
                        if (slot.getFluidStack().amount + 1000 <= inv.getFluidCapacityForSlot(slot.slotIndex)) {
                            if (inv.getAllowedFluidsForSlot(slotID).isEmpty() || inv.getAllowedFluidsForSlot(slotID).contains(fluid)) {
                                if (slot.isFluidValid(fluid)) {
                                    inventoryPlayer.setHeldItemStack(new ItemStack(bucket.getContainerItem(), 1));
                                    slot.getFluidStack().amount += 1000;
                                    slot.onSlotChanged();
                                }
                            }
                        }
                    }
                }
            }
            //I/O from custom fluid container items
            if (inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer) {
                IItemFluidContainer item = (IItemFluidContainer) inventoryPlayer.getHeldItemStack().getItem();
                List<BlockFluid> fluids = CatalystFluids.FLUIDS.findFluidsWithAnyContainer((Item) item);
                if (fluids != null && !fluids.isEmpty()) {
                    if (inv.getAllowedFluidsForSlot(slotID).isEmpty()
                            || inv.getAllowedFluidsForSlot(slotID).stream().anyMatch(fluids::contains)
                            || (slot.getFluidStack() != null && CatalystFluids.FLUIDS.findContainers(slot.getFluidStack().liquid).contains(item))
                            && slot.isAnyFluidValid(fluids)) {
                        //drain
                        if (item.canDrain(inventoryPlayer.getHeldItemStack())) {
                            if (inv.getFluidInSlot(slot.slotIndex) == null) {
                                item.drain(inventoryPlayer.getHeldItemStack(), slot.slotIndex, inv);
                                slot.onSlotChanged();
                            } else if (inv.getFluidInSlot(slot.slotIndex).amount < inv.getFluidCapacityForSlot(slot.slotIndex)) {
                                item.drain(inventoryPlayer.getHeldItemStack(), slot.slotIndex, inv);
                                slot.onSlotChanged();
                            } else if (inv.getFluidInSlot(slot.slotIndex).amount >= inv.getFluidCapacityForSlot(slot.slotIndex)) {
                                if (item.canFill(inventoryPlayer.getHeldItemStack())) {
                                    ItemStack stack = item.fill(slot.getFluidStack(), inventoryPlayer.getHeldItemStack(), inv);
                                    if (stack != null) {
                                        inventoryPlayer.setHeldItemStack(stack);
                                        inventoryPlayer.onInventoryChanged();
                                    }
                                    slot.onSlotChanged();
                                }
                            }
                        } else if (item.canFill(inventoryPlayer.getHeldItemStack())) { //fill
                            ItemStack stack = item.fill(slot.getFluidStack(), inventoryPlayer.getHeldItemStack(), inv);
                            if (stack != null) {
                                inventoryPlayer.setHeldItemStack(stack);
                            }
                            slot.onSlotChanged();
                        }
                        if (inv.get(slot.slotIndex) != null && inv.get(slot.slotIndex).amount <= 0) {
                            inv.remove(slot.slotIndex, false);
                        }
                    }
                }
            }
            slot.onSlotChanged();
            updateInventory();
            return fluidSlots.get(slotID).getFluidStack();
        }
        return null;
    }
}
