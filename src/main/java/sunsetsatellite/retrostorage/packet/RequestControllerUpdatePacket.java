package sunsetsatellite.retrostorage.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;
import sunsetsatellite.retrostorage.packet.data.ControllerUpdateData;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RequestControllerUpdatePacket extends Packet implements ManagedPacket<RequestControllerUpdatePacket> {
    public static final PacketType<RequestControllerUpdatePacket> TYPE = PacketType.builder(false, true, RequestControllerUpdatePacket::new).build();

    @Override
    public @NotNull PacketType<RequestControllerUpdatePacket> getType() {
        return TYPE;
    }

    private int x;
    private int y;
    private int z;

    public RequestControllerUpdatePacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RequestControllerUpdatePacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            x = stream.readInt();
            y = stream.readInt();
            z = stream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeInt(z);
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
        BlockEntity e = player.world.getBlockEntity(x, y, z);
        if (e instanceof DigitalControllerBlockEntity controller) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(new ControllerUpdatePacket(x, y, z, new ControllerUpdateData().get(controller)));
            }
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
