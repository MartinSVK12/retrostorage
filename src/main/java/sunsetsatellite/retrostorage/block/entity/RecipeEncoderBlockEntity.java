package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.CraftingRecipe;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.item.RecipeDiscItem;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.util.ArrayList;

public class RecipeEncoderBlockEntity extends BlockEntity implements Inventory {
    public RecipeEncoderBlockEntity() {
        contents = new ItemStack[10];
    }

    public int size() {
        return contents.length;
    }

    public boolean isEmpty() {
        for (int i = 0; i < size() - 1; i++) {
            if (getStack(i) != null) {
                return false;
            } else {
                continue;
            }
        }
        return true;
    }

    public ItemStack getStack(int i) {
        return contents[i];
    }

    public ItemStack removeStack(int i, int j) {
        if (contents[i] != null) {
            if (contents[i].count <= j) {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].split(j);
            if (contents[i].count == 0) {
                contents[i] = null;
            }
            markDirty();
            return itemstack1;
        } else {
            return null;
        }
    }

    public void setStack(int i, ItemStack itemstack) {
        contents[i] = itemstack;
        if (itemstack != null && itemstack.count > getMaxCountPerStack()) {
            itemstack.count = getMaxCountPerStack();
        }
        markDirty();
    }

    public void markDirty() {
        super.markDirty();
    }

    public String getName() {
        return "Recipe Encoder";
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        NbtList listTag = tag.getList("Items");
        contents = new ItemStack[size()];
        for (int i = 0; i < listTag.size(); i++) {
            NbtCompound tag1 = (NbtCompound) listTag.get(i);
            int j = tag1.getByte("Slot") & 0xff;
            if (j >= 0 && j < contents.length) {
                contents[j] = new ItemStack(tag1);
            }
        }
    }

    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        NbtList listTag = new NbtList();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                NbtCompound tag1 = new NbtCompound();
                tag1.putByte("Slot", (byte) i);
                contents[i].writeNbt(tag1);
                listTag.add(tag1);
            }
        }

        tag.put("Items", listTag);
    }
    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(x, y, z) != this) {
            return false;
        }
        return player.getSquaredDistance((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D) <= 64D;
    }

    private ItemStack[] contents;

    public void encodeDisc() {
        ItemStack recipeDisc = getStack(9);
        if (recipeDisc != null) {
            if (recipeDisc.getItem() instanceof RecipeDiscItem) {
                ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
                for (int i = 0; i < 9; i++) {
                    ItemStack item = getStack(i);
                    if (item != null) {
                        item = item.copy();
                        item.count = 1;
                        itemList.add(i, item);
                    } else {
                        itemList.add(i, null);
                    }
                }
                NbtCompound nbt = RetroStorage.itemsArrayToNBT(itemList);
                recipeDisc.getStationNbt().put("recipe", nbt);
            }
        }
    }

    public void encodeDisc(CraftingRecipe recipe) {
        ItemStack recipeDisc = getStack(9);
        if (recipeDisc != null) {
            if (recipeDisc.getItem() instanceof RecipeDiscItem) {
                NbtCompound nbt = RetroStorage.itemsArrayToNBT(RetroStorage.getRecipeItems(new NetworkCraftable(recipe)));
                recipeDisc.getStationNbt().put("recipe", nbt);
            }
        }
    }
}
