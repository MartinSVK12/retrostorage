package sunsetsatellite.retrostorage.tiles;


import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.retrostorage.RetroStorage;

public class TileEntityRequestTerminal extends TileEntityNetworkDevice {

    public TileEntityRequestTerminal() {
    }

    public void tick() {
        if (network != null && network.drive != null) {
            this.pages = ((network.getAvailableRecipes().size() + network.getAvailableProcesses().size()) / 36) + 1;
        } else {
            page = 1;
            pages = 1;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() == RetroStorage.mobileRequestTerminal){
            return true;
        }
        return super.canInteractWith(entityplayer);
    }

    public int page = 1;
    public int pages = 1;
}
