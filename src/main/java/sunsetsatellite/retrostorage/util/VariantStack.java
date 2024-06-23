package sunsetsatellite.retrostorage.util;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

public class VariantStack {
    private final ItemStack itemStack;
    private final FluidStack fluidStack;
    private final StackType type;

    public VariantStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        this.fluidStack = null;
        this.type = StackType.ITEM;
    }

    public VariantStack(@NotNull FluidStack fluidStack) {
        this.itemStack = null;
        this.fluidStack = fluidStack;
        this.type = StackType.FLUID;
    }

    public StackType getType() {
        return type;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public FluidStack getFluid() {
        return fluidStack;
    }

    public ItemStack forceGetItem(){
        switch (type) {
            case ITEM:
                return itemStack;
            case FLUID:
                return new ItemStack(fluidStack.liquid,fluidStack.amount);
        }
        return null;
    }

    public VariantStack copy() {
        switch (type) {
            case ITEM:
                return new VariantStack(itemStack.copy());
            case FLUID:
                return new VariantStack(fluidStack.copy());
        }
        return null;
    }
}
