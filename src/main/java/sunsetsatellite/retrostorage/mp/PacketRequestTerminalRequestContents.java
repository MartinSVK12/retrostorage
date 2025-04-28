package sunsetsatellite.retrostorage.mp;

import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.menus.MenuRequestTerminal;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketRequestTerminalRequestContents implements NetworkMessage {

    private String searchQuery;

    public PacketRequestTerminalRequestContents() {}

    public PacketRequestTerminalRequestContents(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeString(searchQuery);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        searchQuery = packet.readString();
    }

    @Override
    public void handle(NetworkContext context) {
        if(EnvironmentHelper.isServerEnvironment()){
            if(context.player != null && context.player.craftingInventory instanceof MenuRequestTerminal){
                MenuRequestTerminal menu = (MenuRequestTerminal) context.player.craftingInventory;
                menu.getCraftables(searchQuery);
                NetworkHandler.sendToPlayer(context.player,new PacketRequestTerminalContents(menu.networkCraftables));
            }
        }
    }
}
