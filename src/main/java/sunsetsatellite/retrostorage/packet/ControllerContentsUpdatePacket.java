package sunsetsatellite.retrostorage.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
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

public class ControllerContentsUpdatePacket extends Packet implements ManagedPacket<ControllerContentsUpdatePacket> {
    public static final PacketType<ControllerContentsUpdatePacket> TYPE = PacketType.builder(true, false, ControllerContentsUpdatePacket::new).build();

    @Override
    public @NotNull PacketType<ControllerContentsUpdatePacket> getType() {
        return TYPE;
    }

    private int x;
    private int y;
    private int z;
    private ControllerContentsUpdateData data;

    public ControllerContentsUpdatePacket(int x, int y, int z, ControllerContentsUpdateData data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }

    public ControllerContentsUpdatePacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            x = stream.readInt();
            y = stream.readInt();
            z = stream.readInt();
            data = new ControllerContentsUpdateData().read(stream);
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
            data.write(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        SideUtil.run(() -> handleClient(networkHandler), () -> {
        });
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        BlockEntity e = player.world.getBlockEntity(x, y, z);
        if (e instanceof DigitalControllerBlockEntity controller) {
            data.apply(controller);
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
