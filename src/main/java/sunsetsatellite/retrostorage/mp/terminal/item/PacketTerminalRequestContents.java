package sunsetsatellite.retrostorage.mp.terminal.item;

import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.menus.MenuDigitalTerminal;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketTerminalRequestContents implements NetworkMessage {

    private String searchQuery;

    public PacketTerminalRequestContents() {}

    public PacketTerminalRequestContents(String searchQuery) {
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
        if(EnvironmentHelper.isMultiplayerServer()){
            if(context.player != null && context.player.containerMenu instanceof MenuDigitalTerminal){
                MenuDigitalTerminal menu = (MenuDigitalTerminal) context.player.containerMenu;
                menu.getFilteredStacks(searchQuery);
                NetworkHandler.sendToPlayer(context.player,new PacketTerminalContents(menu.networkStacks));
            }
        }
    }
}
