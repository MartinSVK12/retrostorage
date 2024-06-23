package sunsetsatellite.retrostorage.tiles;


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


    public int page = 1;
    public int pages = 1;
}
