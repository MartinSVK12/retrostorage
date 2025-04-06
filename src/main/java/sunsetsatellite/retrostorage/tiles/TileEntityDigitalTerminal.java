package sunsetsatellite.retrostorage.tiles;

import net.minecraft.core.entity.player.Player;
import sunsetsatellite.retrostorage.ReSItems;
import sunsetsatellite.retrostorage.RetroStorage;

public class TileEntityDigitalTerminal extends TileEntityNetworkDevice {

    public TileEntityDigitalTerminal() {}

    public void tick() {
        if (worldObj != null && worldObj.isClientSide) return;
        super.tick();
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == ReSItems.mobileTerminal){
            return true;
        }
        return super.stillValid(entityplayer);
    }

    public int page = 0;
    public int pages = 0;
}
