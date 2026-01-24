package sunsetsatellite.retrostorage.packet.terminal.fluid;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.screen.handler.FluidTerminalScreenHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FluidTerminalInteractionPacket extends Packet implements ManagedPacket<FluidTerminalInteractionPacket> {
    public static final PacketType<FluidTerminalInteractionPacket> TYPE = PacketType.builder(false, true, FluidTerminalInteractionPacket::new).build();

    @Override
    public @NotNull PacketType<FluidTerminalInteractionPacket> getType() {
        return TYPE;
    }

    private String searchQuery;
    private int slotId;
    private int vSlotId;
    private int page;
    private int mouseButton;
    private boolean shift;

    public FluidTerminalInteractionPacket(String query, int slotId, int vSlotId, int page, int mouseButton, boolean shift) {
        this.searchQuery = query;
        this.slotId = slotId;
        this.vSlotId = vSlotId;
        this.page = page;
        this.mouseButton = mouseButton;
        this.shift = shift;
    }

    public FluidTerminalInteractionPacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            searchQuery = stream.readUTF();
            slotId = stream.readInt();
            vSlotId = stream.readInt();
            page = stream.readInt();
            mouseButton = stream.readInt();
            shift = stream.readBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeUTF(searchQuery);
            stream.writeInt(slotId);
            stream.writeInt(vSlotId);
            stream.writeInt(page);
            stream.writeInt(mouseButton);
            stream.writeBoolean(shift);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if (player != null && player.currentScreenHandler instanceof FluidTerminalScreenHandler handler) {
            handler.handleTerminalInteraction(searchQuery, slotId, vSlotId, page, mouseButton, shift);
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
