package sunsetsatellite.retrostorage.packet.terminal.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.screen.handler.DigitalTerminalScreenHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class TerminalRequestContentsPacket extends Packet implements ManagedPacket<TerminalRequestContentsPacket> {
    public static final PacketType<TerminalRequestContentsPacket> TYPE = PacketType.builder(false, true, TerminalRequestContentsPacket::new).build();

    @Override
    public @NotNull PacketType<TerminalRequestContentsPacket> getType() {
        return TYPE;
    }

    private String searchQuery;

    public TerminalRequestContentsPacket(String query) {
        this.searchQuery = query;
    }

    public TerminalRequestContentsPacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            searchQuery = stream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeUTF(searchQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> {
        }, () -> handleServer(networkHandler));
    }

    @Environment(EnvType.SERVER)
    public void handleServer(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if (player instanceof ServerPlayerEntity serverPlayer && player.currentScreenHandler instanceof DigitalTerminalScreenHandler handler) {
            handler.getFilteredStacks(searchQuery);
            serverPlayer.networkHandler.sendPacket(new TerminalContentsPacket(handler.networkStacks));
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
