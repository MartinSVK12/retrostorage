//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sunsetsatellite.retrostorage.util;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

public class AutocraftingInventory extends CraftingInventory {
    private ItemStack[] stacks;
    private int width;

    public AutocraftingInventory(int width, int height) {
        super(null, width, height);
        int var4 = width * height;
        this.stacks = new ItemStack[var4];
        this.width = width;
    }

    @Override
    public int size() {
        return this.stacks.length;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= this.size() ? null : this.stacks[slot];
    }

    @Override
    public ItemStack getStack(int x, int y) {
        if (x >= 0 && x < this.width) {
            int var3 = x + y * this.width;
            return this.getStack(var3);
        } else {
            return null;
        }
    }

    @Override
    public String getName() {
        return "Autocrafting";
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (this.stacks[slot] != null) {
            if (this.stacks[slot].count <= amount) {
                ItemStack var4 = this.stacks[slot];
                this.stacks[slot] = null;
                return var4;
            } else {
                ItemStack var3 = this.stacks[slot].split(amount);
                if (this.stacks[slot].count == 0) {
                    this.stacks[slot] = null;
                }

                return var3;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.stacks[slot] = stack;

    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
