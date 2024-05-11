package sunsetsatellite.retrostorage.containers;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalFluidTerminal;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.util.SlotDigital;
import sunsetsatellite.retrostorage.util.SlotDigitalFluid;

import java.util.List;

public class ContainerDigitalFluidTerminal extends ContainerDigitalFluid{

    public ContainerDigitalFluidTerminal(IInventory iinventory, TileEntityDigitalFluidTerminal tile)
    {
        super(iinventory,(tile.network != null && tile.network.fluidDrive != null) ? tile.network.fluidInventory : null);
        this.tile = tile;

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

        if(tile.network != null && tile.network.fluidInventory != null){
            //addSlot(new SlotViewOnly(tile.network.drive, 0, 60, 108));

            for(int i = 0; i < 4; i++)
            {
                for(int l = 0; l < 9; l++)
                {
                    addFluidSlot(new SlotDigitalFluid(tile.network.fluidInventory,l + i * 9, 8 + l * 18, 18 + i * 18));
                }
            }
        }
    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    private final TileEntityDigitalFluidTerminal tile;
}
