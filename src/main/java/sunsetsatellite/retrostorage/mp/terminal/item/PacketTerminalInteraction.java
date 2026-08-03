package sunsetsatellite.retrostorage.mp.terminal.item;

import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.menus.MenuDigitalTerminal;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketTerminalInteraction implements NetworkMessage {

    private String searchQuery;
    private int slotId;
    private int vSlotId;
    private int page;
    private int mouseButton;
    private boolean shift;

    public PacketTerminalInteraction(String searchQuery, int slotId, int vSlotId, int page, int mouseButton, boolean shift) {
        this.searchQuery = searchQuery;
        this.slotId = slotId;
        this.vSlotId = vSlotId;
        this.page = page;
        this.mouseButton = mouseButton;
        this.shift = shift;
    }

    public PacketTerminalInteraction() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeString(searchQuery);
        packet.writeInt(slotId);
        packet.writeInt(vSlotId);
        packet.writeInt(page);
        packet.writeInt(mouseButton);
        packet.writeBoolean(shift);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        searchQuery = packet.readString();
        slotId = packet.readInt();
        vSlotId = packet.readInt();
        page = packet.readInt();
        mouseButton = packet.readInt();
        shift = packet.readBoolean();
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.containerMenu instanceof MenuDigitalTerminal){
            ((MenuDigitalTerminal) context.player.containerMenu).handleTerminalInteraction(searchQuery, slotId, vSlotId, page, mouseButton, shift);
        }
    }
}
