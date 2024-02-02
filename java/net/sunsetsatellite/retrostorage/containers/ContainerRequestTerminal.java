

package net.sunsetsatellite.retrostorage.containers;

import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.gui.GuiTaskRequest;
import net.sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import net.sunsetsatellite.retrostorage.util.SlotViewOnly;


public class ContainerRequestTerminal extends Container
{

    public ContainerRequestTerminal(IInventory iinventory, TileEntityRequestTerminal tile)
    {
    	addSlot(new SlotViewOnly(tile, 0, 60, 108));
    	
    	for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iinventory, k, 8 + k * 18, 198));
        }
    	
    	for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }

        }
    	
        this.tile = tile;
        for(int i = 0; i < 4; i++)
        {
            for(int l = 0; l < 9; l++)
            {
        		addSlot(new SlotViewOnly(tile,l + i * 9 + 1 , 8 + l * 18, 18 + i * 18));
            }

        }

        

        

    }

    @Override
    public ItemStack slotClick(int i, int j, boolean bl, EntityPlayer player) {
        try {
            Slot slot = this.getSlot(i);
            if (slot instanceof SlotViewOnly) {
                if (tile.network != null && slot.getStack() != null) {
                    ModLoader.openGUI(player, new GuiTaskRequest(tile, slot.getStack(), ((SlotViewOnly) slot).variableIndex));
                    //tile.network.requestCrafting(tile.recipeContents[((SlotViewOnly) slot).variableIndex]);
                }
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if(bl){
            return null;
        }
        return super.slotClick(i, j, bl, player);
    }
    
    private TileEntityRequestTerminal tile;

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tile.isUseableByPlayer(entityPlayer);
    }

    @Override
    protected void retrySlotClick(int i, int j, boolean bl, EntityPlayer par4EntityPlayer) {

    }
}
