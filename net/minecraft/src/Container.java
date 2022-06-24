package net.minecraft.src;

import net.sunsetsatellite.retrostorage.ContainerDigitalChest;
import net.sunsetsatellite.retrostorage.ContainerDigitalTerminal;
import net.sunsetsatellite.retrostorage.ContainerRequestTerminal;
import net.sunsetsatellite.retrostorage.SlotViewOnly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Container {
	public List field_20123_d = new ArrayList();
	public List slots = new ArrayList();
	public int windowId = 0;
	private short field_20917_a = 0;
	protected List field_20121_g = new ArrayList();
	private Set field_20918_b = new HashSet();

	protected void addSlot(Slot slot1) {
		slot1.slotNumber = this.slots.size();
		this.slots.add(slot1);
		this.field_20123_d.add((Object)null);
	}

	public void updateCraftingResults() {
		for(int i1 = 0; i1 < this.slots.size(); ++i1) {
			ItemStack itemStack2 = ((Slot)this.slots.get(i1)).getStack();
			ItemStack itemStack3 = (ItemStack)this.field_20123_d.get(i1);
			if(!ItemStack.areItemStacksEqual(itemStack3, itemStack2)) {
				itemStack3 = itemStack2 == null ? null : itemStack2.copy();
				this.field_20123_d.set(i1, itemStack3);

				for(int i4 = 0; i4 < this.field_20121_g.size(); ++i4) {
					((ICrafting)this.field_20121_g.get(i4)).func_20159_a(this, i1, itemStack3);
				}
			}
		}

	}

	public Slot getSlot(int i1) {
		return (Slot)this.slots.get(i1);
	}

	public ItemStack getStackInSlot(int i1) {
		Slot slot2 = (Slot)this.slots.get(i1);
		return slot2 != null ? slot2.getStack() : null;
	}

	//Specific for digital slots (SlotDigital.java, used in the Digital Chest), this is the first way i thought of doing this and idk any better
	public void withdrawItem(Slot slot) {
	}

	//specific for Request Terminal & Assembler
	public void requestItemCrafting(Slot slot) {
	}

    public ItemStack func_27280_a(int i, int j, boolean flag, EntityPlayer entityplayer)
    {
        ItemStack itemstack = null;
        if(j == 0 || j == 1)
        {
            InventoryPlayer inventoryplayer = entityplayer.inventory;
            if(i == -999)
            {
                if(inventoryplayer.getItemStack() != null && i == -999)
                {
                    if(j == 0)
                    {
                        entityplayer.dropPlayerItem(inventoryplayer.getItemStack());
                        inventoryplayer.setItemStack(null);
                    }
                    if(j == 1)
                    {
                        entityplayer.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));
                        if(inventoryplayer.getItemStack().stackSize == 0)
                        {
                            inventoryplayer.setItemStack(null);
                        }
                    }
                }
            } else
            if(flag)
            {
                ItemStack itemstack1 = getStackInSlot(i);
                if(itemstack1 != null)
                {
                    int k = itemstack1.stackSize;
                    itemstack = itemstack1.copy();
                    Slot slot1 = (Slot)slots.get(i);
                    if(slot1 != null && slot1.getStack() != null)
                    {
                        int l = slot1.getStack().stackSize;
                        if(l < k)
                        {
                            func_27280_a(i, j, flag, entityplayer);
                        }
                    }
                }
            } else
            {
                Slot slot = (Slot)slots.get(i);
                if(slot != null)
                {
                    slot.onSlotChanged();
                    ItemStack itemstack2 = slot.getStack();
                    ItemStack itemstack3 = inventoryplayer.getItemStack();
                    if(itemstack2 != null)
                    {
                        itemstack = itemstack2.copy();
                    }
                    if(itemstack2 == null)
                    {
                        if(itemstack3 != null && slot.isItemValid(itemstack3))
                        {
                            int i1 = j != 0 ? 1 : itemstack3.stackSize;
                            if(i1 > slot.getSlotStackLimit())
                            {
                                i1 = slot.getSlotStackLimit();
                            }
                            slot.putStack(itemstack3.splitStack(i1));
                            if(itemstack3.stackSize == 0)
                            {
                                inventoryplayer.setItemStack(null);
                            }
                        }
                    } else
                    if(itemstack3 == null)
                    {
                    	if(slot.canTakeFromSlot()) {
                    		int j1 = j != 0 ? (itemstack2.stackSize + 1) / 2 : itemstack2.stackSize;
                            ItemStack itemstack5 = slot.decrStackSize(j1);
                            if (!(slot instanceof SlotViewOnly)){
                                inventoryplayer.setItemStack(itemstack5);
                            }
                            if(itemstack2.stackSize == 0)
                            {
                                slot.putStack(null);
                            }
                            if (!(slot instanceof SlotViewOnly)){
                                slot.onPickupFromSlot(inventoryplayer.getItemStack());
                            }
                    	}
                    	if(slot instanceof SlotViewOnly) {
                    		if(this instanceof ContainerDigitalChest || this instanceof ContainerDigitalTerminal) {
                    			withdrawItem(slot);
                    			//slot.getStack()
                    		}
                    		else if (this instanceof ContainerRequestTerminal) {
                    			requestItemCrafting(slot);
                    		}
                    	}
                    } else
                    if(slot.isItemValid(itemstack3))
                    {
                        if(itemstack2.itemID != itemstack3.itemID || itemstack2.getHasSubtypes() && itemstack2.getItemDamage() != itemstack3.getItemDamage() || !(itemstack2.getItemData().equals(itemstack3.getItemData())))
                        {
                            if(itemstack3.stackSize <= slot.getSlotStackLimit())
                            {
                                if (!(slot instanceof SlotViewOnly)){
                                    ItemStack itemstack4 = itemstack2;
                                    slot.putStack(itemstack3);
                                    inventoryplayer.setItemStack(itemstack4);
                                }
                            }
                        } else
                        {
                            int k1 = j != 0 ? 1 : itemstack3.stackSize;
                            if(k1 > slot.getSlotStackLimit() - itemstack2.stackSize)
                            {
                                k1 = slot.getSlotStackLimit() - itemstack2.stackSize;
                            }
                            if(k1 > itemstack3.getMaxStackSize() - itemstack2.stackSize)
                            {
                                k1 = itemstack3.getMaxStackSize() - itemstack2.stackSize;
                            }
                            itemstack3.splitStack(k1);
                            if(itemstack3.stackSize == 0)
                            {
                                inventoryplayer.setItemStack(null);
                            }
                            itemstack2.stackSize += k1;
                        }
                    } else
                    if(itemstack2.itemID == itemstack3.itemID && itemstack3.getMaxStackSize() > 1 && (!itemstack2.getHasSubtypes() || itemstack2.getItemDamage() == itemstack3.getItemDamage()) && itemstack2.getItemData().equals(itemstack3.getItemData()))
                    {
                        int l1 = itemstack2.stackSize;
                        if(l1 > 0 && l1 + itemstack3.stackSize <= itemstack3.getMaxStackSize())
                        {
                            if (!(slot instanceof SlotViewOnly)) {
                                itemstack3.stackSize += l1;
                                itemstack2.splitStack(l1);
                                if (itemstack2.stackSize == 0) {
                                    slot.putStack(null);
                                }
                                slot.onPickupFromSlot(inventoryplayer.getItemStack());
                            }
                        }
                    }
                }
            }
        }
        return itemstack;
    }

	public void onCraftGuiClosed(EntityPlayer entityPlayer1) {
		InventoryPlayer inventoryPlayer2 = entityPlayer1.inventory;
		if(inventoryPlayer2.getItemStack() != null) {
			entityPlayer1.dropPlayerItem(inventoryPlayer2.getItemStack());
			inventoryPlayer2.setItemStack((ItemStack)null);
		}

	}

	public void onCraftMatrixChanged(IInventory iInventory1) {
		this.updateCraftingResults();
	}

	public void putStackInSlot(int i1, ItemStack itemStack2) {
		this.getSlot(i1).putStack(itemStack2);
	}

	public void putStacksInSlots(ItemStack[] itemStack1) {
		for(int i2 = 0; i2 < itemStack1.length; ++i2) {
			this.getSlot(i2).putStack(itemStack1[i2]);
		}

	}

	public void func_20112_a(int i1, int i2) {
	}

	public short func_20111_a(InventoryPlayer inventoryPlayer1) {
		++this.field_20917_a;
		return this.field_20917_a;
	}

	public void func_20113_a(short s1) {
	}

	public void func_20110_b(short s1) {
	}

	public abstract boolean isUsableByPlayer(EntityPlayer entityPlayer1);

	protected void func_28125_a(ItemStack itemStack1, int i2, int i3, boolean z4) {
		int i5 = i2;
		if(z4) {
			i5 = i3 - 1;
		}

		Slot slot6;
		ItemStack itemStack7;
		if(itemStack1.isStackable()) {
			while(itemStack1.stackSize > 0 && (!z4 && i5 < i3 || z4 && i5 >= i2)) {
				slot6 = (Slot)this.slots.get(i5);
				itemStack7 = slot6.getStack();
				if(itemStack7 != null && itemStack7.itemID == itemStack1.itemID && (!itemStack1.getHasSubtypes() || itemStack1.getItemDamage() == itemStack7.getItemDamage())) {
					int i8 = itemStack7.stackSize + itemStack1.stackSize;
					if(i8 <= itemStack1.getMaxStackSize()) {
						itemStack1.stackSize = 0;
						itemStack7.stackSize = i8;
						slot6.onSlotChanged();
					} else if(itemStack7.stackSize < itemStack1.getMaxStackSize()) {
						itemStack1.stackSize -= itemStack1.getMaxStackSize() - itemStack7.stackSize;
						itemStack7.stackSize = itemStack1.getMaxStackSize();
						slot6.onSlotChanged();
					}
				}

				if(z4) {
					--i5;
				} else {
					++i5;
				}
			}
		}

		if(itemStack1.stackSize > 0) {
			if(z4) {
				i5 = i3 - 1;
			} else {
				i5 = i2;
			}

			while(!z4 && i5 < i3 || z4 && i5 >= i2) {
				slot6 = (Slot)this.slots.get(i5);
				itemStack7 = slot6.getStack();
				if(itemStack7 == null) {
					slot6.putStack(itemStack1.copy());
					slot6.onSlotChanged();
					itemStack1.stackSize = 0;
					break;
				}

				if(z4) {
					--i5;
				} else {
					++i5;
				}
			}
		}

	}
}
