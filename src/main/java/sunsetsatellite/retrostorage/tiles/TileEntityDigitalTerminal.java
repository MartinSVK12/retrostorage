package sunsetsatellite.retrostorage.tiles;

import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.retrostorage.RetroStorage;

public class TileEntityDigitalTerminal extends TileEntityNetworkDevice {

    public TileEntityDigitalTerminal() {}

    public void tick() {
        super.tick();
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == RetroStorage.mobileTerminal){
            return true;
        }
        return super.canInteractWith(entityplayer);
    }

    public int page = 0;
    public int pages = 0;
}
