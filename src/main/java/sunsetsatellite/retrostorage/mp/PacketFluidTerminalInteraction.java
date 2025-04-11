package sunsetsatellite.retrostorage.mp;

import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.menus.MenuDigitalFluidTerminal;
import sunsetsatellite.retrostorage.menus.MenuDigitalTerminal;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketFluidTerminalInteraction implements NetworkMessage {

    private String searchQuery;
    private int slotId;
    private int vSlotId;
    private int mouseButton;
    private boolean shift;

    public PacketFluidTerminalInteraction(String searchQuery, int slotId, int vSlotId, int mouseButton, boolean shift) {
        this.searchQuery = searchQuery;
        this.slotId = slotId;
        this.vSlotId = vSlotId;
        this.mouseButton = mouseButton;
        this.shift = shift;
    }

    public PacketFluidTerminalInteraction() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeString(searchQuery);
        packet.writeInt(slotId);
        packet.writeInt(vSlotId);
        packet.writeInt(mouseButton);
        packet.writeBoolean(shift);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        searchQuery = packet.readString();
        slotId = packet.readInt();
        vSlotId = packet.readInt();
        mouseButton = packet.readInt();
        shift = packet.readBoolean();
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.craftingInventory instanceof MenuDigitalFluidTerminal){
            ((MenuDigitalFluidTerminal) context.player.craftingInventory).handleTerminalInteraction(searchQuery, slotId, vSlotId, mouseButton, shift);
        }
    }
}
