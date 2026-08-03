package sunsetsatellite.retrostorage.util;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerCrafting;

public class InventoryAutocrafting extends ContainerCrafting {
    private final ItemStack[] stackList;
    private final int field_21104_b;

    public InventoryAutocrafting(int i, int j) {
        super(null, i, j);
        int k = i * j;
        this.stackList = new ItemStack[k];
        this.field_21104_b = i;
    }

    @Override
    public int getContainerSize() {
        return this.stackList.length;
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= this.getContainerSize() ? null : this.stackList[i];
    }

    @Override
    public ItemStack getItemStackAt(int i, int j) {
        if (i >= 0 && i < this.field_21104_b) {
            int k = i + j * this.field_21104_b;
            return this.getItem(k);
        } else {
            return null;
        }
    }

    @Override
    public void setSlotContentsAt(int i, int j, ItemStack itemStack) {
        if (i >= 0 && i < this.field_21104_b) {
            int k = i + j * this.field_21104_b;
            this.setItem(k, itemStack);
        }
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if (this.stackList[i] != null) {
            ItemStack itemstack1;
            if (this.stackList[i].stackSize <= j) {
                itemstack1 = this.stackList[i];
                this.stackList[i] = null;
                //this.eventHandler.onCraftMatrixChanged(this);
                return itemstack1;
            } else {
                itemstack1 = this.stackList[i].splitStack(j);
                if (this.stackList[i].stackSize == 0) {
                    this.stackList[i] = null;
                }

                //this.eventHandler.onCraftMatrixChanged(this);
                return itemstack1;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        this.stackList[i] = itemstack;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return true;
    }
}
