package sunsetsatellite.retrostorage.containers;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;

public class ContainerTaskRequest extends Container
{

    public ContainerTaskRequest(TileEntityRequestTerminal TileEntityRequestTerminal)
    {
        tile = TileEntityRequestTerminal;

    }

    @Override
    public void quickMoveItems(int i, EntityPlayer entityPlayer, boolean bl, boolean bl2) {

    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return tile.canInteractWith(entityplayer);
    }

    private TileEntityRequestTerminal tile;
}

