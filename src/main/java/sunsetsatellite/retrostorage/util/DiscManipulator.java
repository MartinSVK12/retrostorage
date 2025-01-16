package sunsetsatellite.retrostorage.util;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.teamterminus.machineessentials.MachineEssentials;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import org.jetbrains.annotations.UnmodifiableView;
import sunsetsatellite.retrostorage.item.FluidStorageDiscItem;
import sunsetsatellite.retrostorage.item.StorageDiscItem;

import java.util.*;
import java.util.stream.Collectors;

public class DiscManipulator {

    public static @UnmodifiableView List<ItemStack> viewDisc(ItemStack disc) {
        ArrayList<ItemStack> result = new ArrayList<>();
        if (disc == null || !(disc.getItem() instanceof StorageDiscItem)) {
            return Collections.emptyList();
        }

        Collection<?> values = disc.getStationNbt().getCompound("Disc").values();
        values.forEach((V) -> {
            if (V instanceof NbtCompound) {
                String K = ((NbtCompound) V).getKey();
                ItemStack itemStack = readUnlimitedStackFromNbt((NbtCompound) V);
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
        if (disc == null || !(disc.getItem() instanceof FluidStorageDiscItem)) {
            return Collections.emptyList();
        }

        Collection<?> values = disc.getStationNbt().getCompound("Disc").values();
        values.forEach((V) -> {
            if (V instanceof NbtCompound) {
                FluidStack fluidStack = new FluidStack((NbtCompound) V);
                if (fluidStack.fluid != null) {
                    result.add(fluidStack);
                }
            }
        });

        return Collections.unmodifiableList(result);
    }

    /*public static void serializeStacks(NbtCompound tag, List<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack item = stacks.get(i);
            NbtCompound itemNBT = new NbtCompound();
            if (item != null) {
                itemNBT.putInt("Count", item.count);
                itemNBT.putShort("id", (short) item.itemId);
                itemNBT.putShort("Damage", (short) item.getDamage());
                itemNBT.putByte("Expanded", (byte) 1);
                itemNBT.putInt("Version", 19133);
                itemNBT.putCompound("Data", item.getStationNbt());
                tag.putCompound(String.valueOf(i), itemNBT);
            } else {
                tag.getValue().remove(String.valueOf(i));
            }
        }
    }*/

    public static boolean canSaveAllToDiscs(List<ItemStack> discs, List<ItemStack> list) {
        int itemAmount = list.stream().filter(Objects::nonNull).filter((S) -> S.getItem() != null).mapToInt((S) -> S.count).sum();
        int stackAmount = MachineEssentials.condenseItemList(list).size();

        int maxItemCapacity = discs.stream().filter(Objects::nonNull).map(ItemStack::getItem).filter(item -> item instanceof StorageDiscItem).mapToInt(item -> ((StorageDiscItem) item).getMaxItemCapacity()).sum();
        int maxStackCapacity = discs.stream().filter(Objects::nonNull).map(ItemStack::getItem).filter(item -> item instanceof StorageDiscItem).mapToInt(item -> ((StorageDiscItem) item).getMaxStackCapacity()).sum();

        return itemAmount <= maxItemCapacity && stackAmount <= maxStackCapacity;
    }

   public static boolean canSaveAllToFluidDiscs(List<ItemStack> discs, List<FluidStack> list) {
        int itemAmount = list.stream().filter(Objects::nonNull).filter((S) -> S.fluid != null).mapToInt((S) -> S.amount).sum();
        int stackAmount = MachineEssentials.condenseFluidList(list).size();

        int maxItemCapacity = discs.stream().filter(Objects::nonNull).map(ItemStack::getItem).filter(item -> item instanceof FluidStorageDiscItem).mapToInt(item -> ((FluidStorageDiscItem) item).getMaxItemCapacity()).sum();
        int maxStackCapacity = discs.stream().filter(Objects::nonNull).map(ItemStack::getItem).filter(item -> item instanceof FluidStorageDiscItem).mapToInt(item -> ((FluidStorageDiscItem) item).getMaxStackCapacity()).sum();

        return itemAmount <= maxItemCapacity && stackAmount <= maxStackCapacity;
    }

    public static void saveToDiscs(List<ItemStack> discs, List<ItemStack> stacks) {
        if(!canSaveAllToDiscs(discs, stacks)) return;
        ArrayList<ItemStack> mutableStacks = MachineEssentials.condenseItemList(stacks);

        discs = discs.stream().filter(Objects::nonNull).filter((S)-> S.getItem() instanceof StorageDiscItem).collect(Collectors.toList());

        for (ItemStack discStack : discs) {
            StorageDiscItem disc = (StorageDiscItem) discStack.getItem();
            int maxItemCapacity = disc.getMaxItemCapacity();
            int maxStackCapacity = disc.getMaxStackCapacity();
            NbtCompound tag = new NbtCompound();

            int itemAmount = 0;
            int stackAmount = 0;

            ListIterator<ItemStack> iter = mutableStacks.listIterator();
            int i = 0;
            while (iter.hasNext()) {
                ItemStack stack = iter.next();
                if (itemAmount >= maxItemCapacity || stackAmount >= maxStackCapacity) break;
                itemAmount += stack.count;
                stackAmount += 1;
                NbtCompound itemNBT = new NbtCompound();
                stack.writeNbt(itemNBT);
                itemNBT.putInt("ExtendedCount",stack.count);
                tag.put(String.valueOf(i), itemNBT);
                i++;
                iter.remove();
            }

            discStack.getStationNbt().put("Disc", tag);
        }

    }

    public static void saveToFluidDiscs(List<ItemStack> discs, List<FluidStack> stacks) {
        if(!canSaveAllToFluidDiscs(discs, stacks)) return;
        ArrayList<FluidStack> mutableStacks = MachineEssentials.condenseFluidList(stacks);

        discs = discs.stream().filter(Objects::nonNull).filter((S)-> S.getItem() instanceof FluidStorageDiscItem).collect(Collectors.toList());

        for (ItemStack discStack : discs) {
            FluidStorageDiscItem disc = (FluidStorageDiscItem) discStack.getItem();
            int maxItemCapacity = disc.getMaxItemCapacity();
            int maxStackCapacity = disc.getMaxStackCapacity();
            NbtCompound tag = new NbtCompound();

            int itemAmount = 0;
            int stackAmount = 0;

            ListIterator<FluidStack> iter = mutableStacks.listIterator();
            int i = 0;
            while (iter.hasNext()) {
                FluidStack stack = iter.next();
                if (itemAmount >= maxItemCapacity || stackAmount >= maxStackCapacity) break;
                itemAmount += stack.amount;
                stackAmount += 1;
                NbtCompound itemNBT = new NbtCompound();
                stack.writeNbt(itemNBT);
                tag.put(String.valueOf(i), itemNBT);
                i++;
                iter.remove();
            }

            discStack.getStationNbt().put("Disc", tag);
        }
    }

    public static ItemStack readUnlimitedStackFromNbt(NbtCompound tag) {
        ItemStack stack = new ItemStack(0, 0, 0);
        stack.readNbt(tag);
        stack.count = tag.getInt("ExtendedCount");
        return stack.getItem() != null && stack.count > 0 ? stack : null;
    }

    public static @UnmodifiableView List<ItemStack> viewDiscs(ArrayList<ItemStack> discsUsed) {
        ArrayList<ItemStack> result = new ArrayList<>();
        for (ItemStack disc : discsUsed) {
            if (disc == null || !(disc.getItem() instanceof StorageDiscItem)) {
                continue;
            }
            result.addAll(viewDisc(disc));
        }
        return Collections.unmodifiableList(result);
    }

    public static @UnmodifiableView List<FluidStack> viewFluidDiscs(ArrayList<ItemStack> discsUsed) {
        ArrayList<FluidStack> result = new ArrayList<>();
        for (ItemStack disc : discsUsed) {
            if (disc == null || !(disc.getItem() instanceof FluidStorageDiscItem)) {
                continue;
            }
            result.addAll(viewFluidDisc(disc));
        }
        return Collections.unmodifiableList(result);
    }
}
