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
import sunsetsatellite.retrostorage.packet.data.ControllerContentsUpdateData;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RequestControllerContentsUpdatePacket extends Packet implements ManagedPacket<RequestControllerContentsUpdatePacket> {
    public static final PacketType<RequestControllerContentsUpdatePacket> TYPE = PacketType.builder(false, true, RequestControllerContentsUpdatePacket::new).build();

    @Override
    public @NotNull PacketType<RequestControllerContentsUpdatePacket> getType() {
        return TYPE;
    }

    private int x;
    private int y;
    private int z;

    public RequestControllerContentsUpdatePacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RequestControllerContentsUpdatePacket() {

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
                serverPlayer.networkHandler.sendPacket(new ControllerContentsUpdatePacket(x, y, z, new ControllerContentsUpdateData().get(controller)));
            }
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
