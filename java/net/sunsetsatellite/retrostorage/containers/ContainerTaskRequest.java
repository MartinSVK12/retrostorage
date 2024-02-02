package net.sunsetsatellite.retrostorage.containers;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;

public class ContainerTaskRequest extends Container
{

    public ContainerTaskRequest(TileEntityRequestTerminal TileEntityRequestTerminal)
    {
        tile = TileEntityRequestTerminal;

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

