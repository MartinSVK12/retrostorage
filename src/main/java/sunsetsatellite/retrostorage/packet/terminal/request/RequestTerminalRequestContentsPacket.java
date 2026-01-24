package sunsetsatellite.retrostorage.packet.terminal.request;

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
import sunsetsatellite.retrostorage.screen.handler.RequestTerminalScreenHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RequestTerminalRequestContentsPacket extends Packet implements ManagedPacket<RequestTerminalRequestContentsPacket> {
    public static final PacketType<RequestTerminalRequestContentsPacket> TYPE = PacketType.builder(false, true, RequestTerminalRequestContentsPacket::new).build();

    @Override
    public @NotNull PacketType<RequestTerminalRequestContentsPacket> getType() {
        return TYPE;
    }

    private String searchQuery;

    public RequestTerminalRequestContentsPacket(String query) {
        this.searchQuery = query;
    }

    public RequestTerminalRequestContentsPacket() {

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
        if (player instanceof ServerPlayerEntity serverPlayer && player.currentScreenHandler instanceof RequestTerminalScreenHandler handler) {
            handler.getCraftables(searchQuery);
            serverPlayer.networkHandler.sendPacket(new RequestTerminalContentsPacket(handler.networkCraftables));
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
