package sunsetsatellite.retrostorage.tiles;

import net.minecraft.core.entity.player.Player;
import sunsetsatellite.retrostorage.ReSItems;

public class TileEntityRequestTerminal extends TileEntityNetworkDevice {

    public TileEntityRequestTerminal() {
    }

    public void tick() {
        super.tick();
        /*if (network != null && network.drive != null) {
            this.pages = ((network.getAvailableRecipes().size() + network.getAvailableProcesses().size()) / 36) + 1;
        } else {
            page = 1;
            pages = 1;
        }*/
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == ReSItems.mobileRequestTerminal){
            return true;
        }
        return super.stillValid(entityplayer);
    }

    public int page = 0;
    public int pages = 0;
}
