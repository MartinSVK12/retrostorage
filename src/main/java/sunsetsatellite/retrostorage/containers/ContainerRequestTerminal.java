

package sunsetsatellite.retrostorage.containers;


import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.gui.GuiTaskRequest;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.util.SlotViewOnly;

import java.util.ArrayList;
import java.util.List;

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
    public ItemStack clickInventorySlot(InventoryAction action, int[] args, EntityPlayer player) {
        if(args != null){
            Slot slot = this.getSlot(args[0]);
            if(slot instanceof SlotViewOnly){
                if(tile.network != null && slot.getStack() != null){
                    RetroStorage.mc.displayGuiScreen(new GuiTaskRequest(tile,slot.getStack(),((SlotViewOnly) slot).variableIndex));
                    //tile.network.requestCrafting(tile.recipeContents[((SlotViewOnly) slot).variableIndex]);
                }
                return null;
            }
        }
        return super.clickInventorySlot(action,args,player);
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }
    
    private TileEntityRequestTerminal tile;
}
