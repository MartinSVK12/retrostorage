package sunsetsatellite.retrostorage.tiles;

import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.retrostorage.RetroStorage;

public class TileEntityDigitalFluidTerminal extends TileEntityNetworkDevice {
    public int page = 1;
    public int pages = 1;

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == RetroStorage.mobileFluidTerminal){
            return true;
        }
        return super.canInteractWith(entityplayer);
    }
}
