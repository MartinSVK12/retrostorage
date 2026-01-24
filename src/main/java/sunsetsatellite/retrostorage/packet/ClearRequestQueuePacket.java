package sunsetsatellite.retrostorage.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.api.NetworkController;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ClearRequestQueuePacket extends Packet implements ManagedPacket<ClearRequestQueuePacket> {
    public static final PacketType<ClearRequestQueuePacket> TYPE = PacketType.builder(false, true, ClearRequestQueuePacket::new).build();

    @Override
    public @NotNull PacketType<ClearRequestQueuePacket> getType() {
        return TYPE;
    }

    private int x;
    private int y;
    private int z;

    public ClearRequestQueuePacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ClearRequestQueuePacket() {

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
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        BlockEntity e = player.world.getBlockEntity(x, y, z);
        if (e instanceof NetworkController controller) {
            controller.clearRequestQueue();
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
