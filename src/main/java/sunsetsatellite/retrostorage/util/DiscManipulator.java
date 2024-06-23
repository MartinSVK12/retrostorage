package sunsetsatellite.retrostorage.util;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;

import java.util.Collection;

public class DiscManipulator {

    public static void saveDisc(ItemStack disc, IDigitalInventory inv) {
        if (disc == null || inv == null) {
            return;
        }
        CompoundTag discNBT = new CompoundTag();
        for (int i = 0; i < inv.sizeStacks(); i++) {
            ItemStack item = inv.get(i);
            CompoundTag itemNBT = new CompoundTag();
            if (item != null) {
                itemNBT.putInt("Count", item.stackSize);
                itemNBT.putShort("id", (short) item.itemID);
                itemNBT.putShort("Damage", (short) item.getMetadata());
                itemNBT.putByte("Expanded", (byte) 1);
                itemNBT.putInt("Version", 19133);
                itemNBT.putCompound("Data", item.getData());
                discNBT.putCompound(String.valueOf(i), itemNBT);
            } else {
                discNBT.getValue().remove(String.valueOf(i));
            }
        }
        disc.getData().putCompound("Disc", discNBT);
    }

    public static void saveDisc(ItemStack disc, IDigitalFluidInventory inv) {
        if (disc == null || inv == null) {
            return;
        }
        CompoundTag discNBT = new CompoundTag();
        for (int i = 0; i < inv.sizeStacks(); i++) {
            FluidStack fluidStack = inv.get(i);
            CompoundTag fluidNbt = new CompoundTag();
            if (fluidStack != null) {
                fluidStack.writeToNBT(fluidNbt);
                discNBT.putCompound(String.valueOf(i), fluidNbt);
            } else {
                discNBT.getValue().remove(String.valueOf(i));
            }
        }
        disc.getData().putCompound("Disc", discNBT);
    }

    public static void loadDisc(ItemStack disc, IDigitalInventory inv) {
        if (disc == null || inv == null) {
            return;
        }

        Collection<?> values = disc.getData().getCompound("Disc").getValues();
        values.forEach((V) -> {
            if (V instanceof CompoundTag) {
                String K = ((Tag<?>) V).getTagName();
                ItemStack itemStack = readUnlimitedStackFromNbt((CompoundTag) V);
                if (itemStack == null) return;
                if (itemStack.getItem() != null) {
                    inv.add(itemStack);
                }
            }
        });
    }

    public static void loadDisc(ItemStack disc, IDigitalFluidInventory inv) {
        if (disc == null || inv == null) {
            return;
        }

        Collection<?> values = disc.getData().getCompound("Disc").getValues();
        values.forEach((V) -> {
            if (V instanceof CompoundTag) {
                String K = ((Tag<?>) V).getTagName();
                FluidStack fluidStack = new FluidStack((CompoundTag) V);
                if (fluidStack.getLiquid() != null) {
                    inv.add(fluidStack);
                }
            }
        });
    }

    public static ItemStack readUnlimitedStackFromNbt(CompoundTag tag) {
        ItemStack stack = new ItemStack(0, 0, 0, new CompoundTag());
        stack.readFromNBT(tag);
        stack.stackSize = tag.getInteger("Count");
        return stack.getItem() != null && stack.stackSize > 0 ? stack : null;
    }

}
