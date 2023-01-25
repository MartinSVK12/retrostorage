//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sunsetsatellite.retrostorage.util;

import net.minecraft.src.*;

public class InventoryAutocrafting extends InventoryCrafting {
    private ItemStack[] stackList;
    private int field_21104_b;
    private Container eventHandler;

    public InventoryAutocrafting(int i, int j) {
        super(null,i,j);
        int k = i * j;
        this.stackList = new ItemStack[k];
        this.field_21104_b = i;
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return i >= this.getSizeInventory() ? null : this.stackList[i];
    }

    @Override
    public ItemStack getItemStackAt(int i, int j) {
        if (i >= 0 && i < this.field_21104_b) {
            int k = i + j * this.field_21104_b;
            return this.getStackInSlot(k);
        } else {
            return null;
        }
    }

    @Override
    public void setSlotContentsAt(int i, int j, ItemStack itemStack) {
        if (i >= 0 && i < this.field_21104_b) {
            int k = i + j * this.field_21104_b;
            this.setInventorySlotContents(k, itemStack);
        }
    }

    @Override
    public String getInvName() {
        return "Crafting";
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
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
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.stackList[i] = itemstack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void onInventoryChanged() {
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }
}
