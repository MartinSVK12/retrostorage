package sunsetsatellite.retrostorage.containers;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.util.InventoryPortable;
import sunsetsatellite.retrostorage.util.SlotDigital;

import java.util.ArrayList;
import java.util.List;

public class ContainerPlayerExtra extends ContainerPlayer {
    public int page;
    public int maxPage;
    protected int creativeSlotsStart;
    public String searchText;
    protected List<ItemStack> searchedItems;
    public InventoryPortable inventory;
    public static int creativeItemsCount;

    public ContainerPlayerExtra(InventoryPlayer inventoryplayer, InventoryPortable inventory) {
        this(inventoryplayer, inventory, true);

    }

    public ContainerPlayerExtra(InventoryPlayer inventoryplayer, InventoryPortable inventory, boolean isSinglePlayer) {
        super(inventoryplayer, isSinglePlayer);
        this.inventory = inventory;
        this.page = 0;
        this.searchText = "";
        this.searchedItems = new ArrayList();
        this.creativeSlotsStart = this.inventorySlots.size();

        for(int i = 0; i < 36; ++i) {
            int x = i % 6;
            int y = i / 6;
            this.addSlot(new SlotDigital(inventory,this.creativeSlotsStart + i, 174 + x * 18, 30 + y * 18));
            //inventorySlots.get(this.creativeSlotsStart+i).putStack(new ItemStack(1,64,0));
        }

        this.searchPage("");

        updatePage();
    }

    public void setInventoryStatus(int page, String searchText) {
        if (this.page != page) {
            this.page = page;
            this.updatePage();
        }

        if (!this.searchText.equals(searchText)) {
            this.searchText = searchText;
            this.searchPage(searchText);
        }

    }

    public void lastPage() {
        if (this.page != 0) {
            --this.page;
            this.updatePage();
        }
    }

    public void nextPage() {
        if (this.page != this.maxPage) {
            ++this.page;
            this.updatePage();
        }
    }

    public void searchPage(String search) {
        this.searchText = search;
        this.searchedItems.clear();
        this.page = 0;
        I18n t = I18n.getInstance();
        for (ItemStack inventoryContent : inventory.inventoryContents) {
            if(inventoryContent != null){
                if (t.translateNameKey(inventoryContent.getItemName()).toLowerCase().contains(search.toLowerCase())) {
                    this.searchedItems.add(inventoryContent);
                }
            }
        }

       /* for(int i = 0; i < creativeItemsCount; ++i) {
            if (t.translateNamedKey(creativeItems[i].getItemName()).toLowerCase().contains(search.toLowerCase())) {
                this.searchedItems.add(creativeItems[i]);
            }
        }*/

        this.updatePage();
    }

    protected void updatePage() {
        this.maxPage = inventory.getStackAmount() / 36;
        /*if (this.searchedItems.size() % 36 == 0) {
            --this.maxPage;
        }*/

        if (this.maxPage == -1) {
            this.maxPage = 0;
        }
        for (Slot inventorySlot : inventorySlots) {
            if(inventorySlot instanceof SlotDigital){
                ((SlotDigital) inventorySlot).variableIndex = ((SlotDigital) inventorySlot).slotIndex + (this.page * 36);
            }
        }

        /*for(int i = 0; i < 36; ++i) {
            if (i + this.page * 36 >= this.searchedItems.size()) {
                ((SlotCreative)this.inventorySlots.get(this.creativeSlotsStart + i)).item = null;
            } else {
                ((SlotCreative)this.inventorySlots.get(this.creativeSlotsStart + i)).item = (ItemStack)this.searchedItems.get(i + this.page * 36);
            }
        }*/

        //this.playerInv.player.updateCreativeInventory(this.page, this.searchText);
    }

    /*public void quickMoveItems(int i, EntityPlayer entityPlayer, boolean shift, boolean ctrl) {
        ItemStack item = this.getSlot(i).getStack().copy();
        ItemStack original = this.getSlot(i).getStack();
        if(inventory.owner.isItemEqual(original)){
            super.quickMoveItems(i,entityPlayer,shift,ctrl);
            return;
        }
        if(i > 9 && i < 44){
            this.onStackMergeShiftClick(this.getSlot(i).getStack(),45,80,false);
        } else {
            this.onStackMergeShiftClick(this.getSlot(i).getStack(),9,44,false);
            this.getSlot(i).onPickupFromSlot(item);
        }
        if (original.stackSize == 0) {
            this.getSlot(i).putStack(null);
        } else {
            this.getSlot(i).onSlotChanged();
        }
        super.quickMoveItems(i,entityPlayer,shift,ctrl);
    }*/

    static {
        int count = 0;

        /*int i;
        int j;
        for(i = 0; i < 1000; ++i) {
            if (Block.blocksList[i] != null && !Block.blocksList[i].notInCreativeMenu) {
                creativeItems[count] = new ItemStack(Block.blocksList[i]);
                ++count;
                if (i != Block.wool.blockID && i != Block.planksOakPainted.blockID && i != Block.lampIdle.blockID && i != Block.fencePlanksOakPainted.blockID) {
                    if (i == Block.chestPlanksOakPainted.blockID || i == Block.slabPlanksOakPainted.blockID || i == Block.fencegatePlanksOakPainted.blockID || i == Block.stairsPlanksOakPainted.blockID) {
                        for(j = 16; j < 256; j += 16) {
                            creativeItems[count] = new ItemStack(Block.blocksList[i], 1, j);
                            ++count;
                        }
                    }
                } else {
                    for(j = 1; j < 16; ++j) {
                        creativeItems[count] = new ItemStack(Block.blocksList[i], 1, j);
                        ++count;
                    }
                }
            }
        }

        for(i = Block.blocksList.length; i < Block.blocksList.length + 2256; ++i) {
            if (Item.itemsList[i] != null && !Item.itemsList[i].notInCreativeMenu) {
                creativeItems[count] = new ItemStack(Item.itemsList[i]);
                ++count;
                if (i == Item.coal.itemID) {
                    for(j = 1; j < 2; ++j) {
                        creativeItems[count] = new ItemStack(Item.itemsList[i], 1, j);
                        ++count;
                    }
                }

                if (i == Item.dye.itemID) {
                    for(j = 1; j < 16; ++j) {
                        creativeItems[count] = new ItemStack(Item.itemsList[i], 1, j);
                        ++count;
                    }
                }
            }
        }*/

        creativeItemsCount = count;
    }
}

