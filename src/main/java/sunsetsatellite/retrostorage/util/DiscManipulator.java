package sunsetsatellite.retrostorage.util;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.items.ItemFluidStorageDisc;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;

import java.util.*;
import java.util.stream.Collectors;

public class DiscManipulator {

    public static @UnmodifiableView List<ItemStack> viewDisc(ItemStack disc) {
        ArrayList<ItemStack> result = new ArrayList<>();
        if (disc == null || !(disc.getItem() instanceof ItemStorageDisc)) {
            return Collections.emptyList();
        }

        Collection<?> values = disc.getData().getCompound("Disc").getValues();
        values.forEach((V) -> {
            if (V instanceof CompoundTag) {
                String K = ((Tag<?>) V).getTagName();
                ItemStack itemStack = readUnlimitedStackFromNbt((CompoundTag) V);
                if (itemStack == null) return;
                if (itemStack.getItem() != null) {
                    result.add(itemStack);
                }
            }
        });

        return Collections.unmodifiableList(result);
    }

    public static @UnmodifiableView List<FluidStack> viewFluidDisc(ItemStack disc) {
        ArrayList<FluidStack> result = new ArrayList<>();
        if (disc == null || !(disc.getItem() instanceof ItemFluidStorageDisc)) {
            return Collections.emptyList();
        }

        Collection<?> values = disc.getData().getCompound("Disc").getValues();
        values.forEach((V) -> {
            if (V instanceof CompoundTag) {
                FluidStack fluidStack = new FluidStack((CompoundTag) V);
                if (fluidStack.fluid != null) {
                    result.add(fluidStack);
                }
            }
        });

        return Collections.unmodifiableList(result);
    }

    public static void serializeStacks(CompoundTag tag, List<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack item = stacks.get(i);
            CompoundTag itemNBT = new CompoundTag();
            if (item != null) {
                item.writeToNBT(itemNBT);
                itemNBT.putInt("Count", item.stackSize);
                tag.putCompound(String.valueOf(i), itemNBT);
            } else {
                tag.getValue().remove(String.valueOf(i));
            }
        }
    }

    public static void serializeFluidStacks(CompoundTag tag, List<FluidStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            FluidStack fluid = stacks.get(i);
            CompoundTag fluidNbt = new CompoundTag();
            if (fluid != null) {
                fluid.writeToNBT(fluidNbt);
                tag.putCompound(String.valueOf(i), fluidNbt);
            } else {
                tag.getValue().remove(String.valueOf(i));
            }
        }
    }

    public static boolean canSaveAllToDiscs(List<ItemStack> discs, List<ItemStack> list) {
        int itemAmount = list.stream().filter(Objects::nonNull).filter((S) -> S.getItem() != null).mapToInt((S) -> S.stackSize).sum();
        int stackAmount = Catalyst.condenseItemList(list).size();

        int maxItemCapacity = discs.stream().filter(Objects::nonNull).map(ItemStack::getItem).filter(item -> item instanceof ItemStorageDisc).mapToInt(item -> ((ItemStorageDisc) item).getMaxItemCapacity()).sum();
        int maxStackCapacity = discs.stream().filter(Objects::nonNull).map(ItemStack::getItem).filter(item -> item instanceof ItemStorageDisc).mapToInt(item -> ((ItemStorageDisc) item).getMaxStackCapacity()).sum();

        return itemAmount <= maxItemCapacity && stackAmount <= maxStackCapacity;
    }

    public static boolean canSaveAllToFluidDiscs(List<ItemStack> discs, List<FluidStack> list) {
        int itemAmount = list.stream().filter(Objects::nonNull).filter((S) -> S.fluid != null).mapToInt((S) -> S.amount).sum();
        int stackAmount = RetroStorage.condenseFluidList(list).size();

        int maxItemCapacity = discs.stream().filter(Objects::nonNull).map(ItemStack::getItem).filter(item -> item instanceof ItemFluidStorageDisc).mapToInt(item -> ((ItemFluidStorageDisc) item).getMaxItemCapacity()).sum();
        int maxStackCapacity = discs.stream().filter(Objects::nonNull).map(ItemStack::getItem).filter(item -> item instanceof ItemFluidStorageDisc).mapToInt(item -> ((ItemFluidStorageDisc) item).getMaxStackCapacity()).sum();

        return itemAmount <= maxItemCapacity && stackAmount <= maxStackCapacity;
    }

    public static void saveToDiscs(List<ItemStack> discs, List<ItemStack> stacks) {
        if(!canSaveAllToDiscs(discs, stacks)) return;
        ArrayList<ItemStack> mutableStacks = Catalyst.condenseItemList(stacks);

        discs = discs.stream().filter(Objects::nonNull).filter((S)-> S.getItem() instanceof ItemStorageDisc).collect(Collectors.toList());

        for (ItemStack discStack : discs) {
            ItemStorageDisc disc = (ItemStorageDisc) discStack.getItem();
            int maxItemCapacity = disc.getMaxItemCapacity();
            int maxStackCapacity = disc.getMaxStackCapacity();
            CompoundTag tag = new CompoundTag();

            int itemAmount = 0;
            int stackAmount = 0;

            ListIterator<ItemStack> iter = mutableStacks.listIterator();
            int i = 0;
            while (iter.hasNext()) {
                ItemStack stack = iter.next();
                if (itemAmount >= maxItemCapacity || stackAmount >= maxStackCapacity) break;
                itemAmount += stack.stackSize;
                stackAmount += 1;
                CompoundTag itemNBT = new CompoundTag();
                stack.writeToNBT(itemNBT);
                itemNBT.putInt("Count", stack.stackSize);
                tag.putCompound(String.valueOf(i), itemNBT);
                i++;
                iter.remove();
            }

            discStack.getData().putCompound("Disc", tag);
        }

    }

    public static void saveToFluidDiscs(List<ItemStack> discs, List<FluidStack> stacks) {
        if(!canSaveAllToFluidDiscs(discs, stacks)) return;
        ArrayList<FluidStack> mutableStacks = RetroStorage.condenseFluidList(stacks);

        discs = discs.stream().filter(Objects::nonNull).filter((S)-> S.getItem() instanceof ItemFluidStorageDisc).collect(Collectors.toList());

        for (ItemStack discStack : discs) {
            ItemFluidStorageDisc disc = (ItemFluidStorageDisc) discStack.getItem();
            int maxItemCapacity = disc.getMaxItemCapacity();
            int maxStackCapacity = disc.getMaxStackCapacity();
            CompoundTag tag = new CompoundTag();

            int itemAmount = 0;
            int stackAmount = 0;

            ListIterator<FluidStack> iter = mutableStacks.listIterator();
            int i = 0;
            while (iter.hasNext()) {
                FluidStack stack = iter.next();
                if (itemAmount >= maxItemCapacity || stackAmount >= maxStackCapacity) break;
                itemAmount += stack.amount;
                stackAmount += 1;
                CompoundTag itemNBT = new CompoundTag();
                stack.writeToNBT(itemNBT);
                tag.putCompound(String.valueOf(i), itemNBT);
                i++;
                iter.remove();
            }

            discStack.getData().putCompound("Disc", tag);
        }

    }

    public static ItemStack readUnlimitedStackFromNbt(CompoundTag tag) {
        ItemStack stack = new ItemStack(0, 0, 0, new CompoundTag());
        stack.readFromNBT(tag);
        stack.stackSize = tag.getInteger("Count");
        return stack.getItem() != null && stack.stackSize > 0 ? stack : null;
    }

    public static @UnmodifiableView List<ItemStack> viewDiscs(ArrayList<ItemStack> discsUsed) {
        ArrayList<ItemStack> result = new ArrayList<>();
        for (ItemStack disc : discsUsed) {
            if (disc == null || !(disc.getItem() instanceof ItemStorageDisc)) {
                continue;
            }
            result.addAll(viewDisc(disc));
        }
        return Collections.unmodifiableList(result);
    }

    public static @UnmodifiableView List<FluidStack> viewFluidDiscs(ArrayList<ItemStack> discsUsed) {
        ArrayList<FluidStack> result = new ArrayList<>();
        for (ItemStack disc : discsUsed) {
            if (disc == null || !(disc.getItem() instanceof ItemFluidStorageDisc)) {
                continue;
            }
            result.addAll(viewFluidDisc(disc));
        }
        return Collections.unmodifiableList(result);
    }
}
