package sunsetsatellite.retrostorage.util;


import net.minecraft.item.ItemStack;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
        return switch (type) {
            case ITEM -> Objects.requireNonNull(itemStack);
            case FLUID -> new ItemStack(Objects.requireNonNull(fluidStack).fluid.flowing(), fluidStack.amount, 0);
        };
    }

    public VariantStack copy() {
        return switch (type) {
            case ITEM -> new VariantStack(Objects.requireNonNull(itemStack).copy());
            case FLUID -> new VariantStack(Objects.requireNonNull(fluidStack).copy());
        };
    }
}
