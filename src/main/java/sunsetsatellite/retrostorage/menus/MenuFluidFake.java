package sunsetsatellite.retrostorage.menus;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemBucket;
import net.minecraft.core.item.ItemStack;

import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.CatalystFluids;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;

import java.util.List;

public class MenuFluidFake extends MenuFluid {

    public MenuFluidFake(ContainerInventory inv, IFluidInventory fluidInv) {
        super(fluidInv);
        this.inv = fluidInv;
    }

    //prevent super tile field from being accessible from this class
    public Void tile = null;
    public IFluidInventory inv;

    @Override
    public FluidStack clickFluidSlot(int slotID, int button, boolean shift, boolean control, Player entityplayer) {
        if (inv == null) {
            return null;
        }
        if (slotID == -999) {
            return null;
        }
        SlotFluid slot = fluidSlots.get(slotID);
        ContainerInventory inventory = entityplayer.inventory;
        if (slot != null) {
            FluidStack fluidStack = fluidSlots.get(slotID).getFluidStack();
            if (fluidStack != null) {
                slot.putStack(null);
            } else {
                if (inventory.getHeldItemStack() != null && inventory.getHeldItemStack().getItem() instanceof IItemFluidContainer) {
                    ItemStack stack = inventory.getHeldItemStack();
                    IItemFluidContainer item = (IItemFluidContainer) inventory.getHeldItemStack().getItem();
                    if (slot.getFluidStack() == null) {
                        FluidStack currentFluid = item.getCurrentFluid(stack);
                        if(currentFluid != null) {
                            if (slot.isFluidValid(currentFluid.fluid)) {
                                if (item.canDrain(inventory.getHeldItemStack())) {
                                    slot.putStack(new FluidStack(currentFluid.fluid, 1));
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
