package sunsetsatellite.retrostorage.screen.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.teamterminus.machineessentials.fluid.core.FluidScreenHandler;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;

public class FluidFakeScreenHandler extends FluidScreenHandler {

    public FluidFakeScreenHandler(Inventory inv, FluidInventory fluidInv) {
        this.inv = fluidInv;
    }

    //prevent super tile field from being accessible from this class
    public Void tile = null;
    public FluidInventory inv;

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    /*@Override
    public FluidStack clickFluidSlot(int slotID, int button, boolean shift, boolean control, PlayerEntity entityplayer) {
        if (inv == null) {
            return null;
        }
        if (slotID == -999) {
            return null;
        }
        SlotFluid slot = fluidSlots.get(slotID);
        PlayerInventory inventoryPlayer = entityplayer.inventory;
        if (slot != null) {
            FluidStack fluidStack = fluidSlots.get(slotID).getFluidStack();
            if (fluidStack != null) {
                slot.putStack(null);
            } else {
                if (inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof ItemBucket) {
                    ItemBucket bucket = (ItemBucket) inventoryPlayer.getHeldItemStack().getItem();
                    BlockFluid fluid = CatalystFluids.CONTAINERS.findFluidsWithFilledContainer(bucket).get(0);
                    if (slot.getFluidStack() == null) {
                        if (inv.getAllowedFluidsForSlot(slotID).isEmpty() || inv.getAllowedFluidsForSlot(slotID).contains(fluid)) {
                            if (slot.isFluidValid(fluid)) {
                                slot.putStack(new FluidStack(fluid, 1));
                                slot.onSlotChanged();
                            }
                        }
                    }
                }
                if (inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer) {
                    ItemStack stack = inventoryPlayer.getHeldItemStack();
                    IItemFluidContainer item = (IItemFluidContainer) inventoryPlayer.getHeldItemStack().getItem();
                    List<BlockFluid> fluids = CatalystFluids.CONTAINERS.findFluidsWithAnyContainer((Item) item);
                    if (fluids != null && !fluids.isEmpty()) {
                        if (inv.getAllowedFluidsForSlot(slotID).isEmpty()
                                || inv.getAllowedFluidsForSlot(slotID).stream().anyMatch(fluids::contains)
                                || (slot.getFluidStack() != null && CatalystFluids.CONTAINERS.findContainers(slot.getFluidStack().liquid).contains(item))
                                && slot.isAnyFluidValid(fluids)) {
                            if (item.canDrain(inventoryPlayer.getHeldItemStack())) {
                                BlockFluid itemFluid = item.getCurrentFluid(stack).getLiquid();
                                if (itemFluid != null) {
                                    slot.putStack(new FluidStack(itemFluid, 1));
                                    slot.onSlotChanged();
                                }
                            }
                        }
                    }
                }
            }
            return fluidSlots.get(slotID).getFluidStack();
        }
        return null;
    }*/
}
