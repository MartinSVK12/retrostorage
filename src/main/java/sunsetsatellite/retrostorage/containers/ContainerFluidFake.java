package sunsetsatellite.retrostorage.containers;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryPlayer;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.ContainerFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;

import java.util.List;

public class ContainerFluidFake extends ContainerFluid {

    public ContainerFluidFake(IInventory inv, IFluidInventory fluidInv) {
        super(inv, null);
        this.inv = fluidInv;
    }

    //prevent super tile field from being accessible from this class
    public Void tile = null;
    public IFluidInventory inv;

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
            FluidStack fluidStack = fluidSlots.get(slotID).getFluidStack();
            if (fluidStack != null) {
                slot.putStack(null);
            } else {
                if (inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof ItemBucket) {
                    ItemBucket bucket = (ItemBucket) inventoryPlayer.getHeldItemStack().getItem();
                    BlockFluid fluid = CatalystFluids.FLUIDS.findFluidsWithFilledContainer(bucket).get(0);
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
                    List<BlockFluid> fluids = CatalystFluids.FLUIDS.findFluidsWithAnyContainer((Item) item);
                    if (fluids != null && !fluids.isEmpty()) {
                        if (inv.getAllowedFluidsForSlot(slotID).isEmpty()
                                || inv.getAllowedFluidsForSlot(slotID).stream().anyMatch(fluids::contains)
                                || (slot.getFluidStack() != null && CatalystFluids.FLUIDS.findContainers(slot.getFluidStack().liquid).contains(item))
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
    }
}
