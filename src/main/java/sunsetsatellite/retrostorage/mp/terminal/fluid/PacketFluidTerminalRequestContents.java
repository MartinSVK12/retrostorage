package sunsetsatellite.retrostorage.mp.terminal.fluid;

import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.menus.MenuDigitalFluidTerminal;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketFluidTerminalRequestContents implements NetworkMessage {

    private String searchQuery;

    public PacketFluidTerminalRequestContents() {}

    public PacketFluidTerminalRequestContents(String searchQuery) {
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
            if(context.player != null && context.player.containerMenu instanceof MenuDigitalFluidTerminal){
                MenuDigitalFluidTerminal menu = (MenuDigitalFluidTerminal) context.player.containerMenu;
                menu.getFilteredStacks(searchQuery);
                NetworkHandler.sendToPlayer(context.player,new PacketFluidTerminalContents(menu.networkStacks));
            }
        }
    }
}
