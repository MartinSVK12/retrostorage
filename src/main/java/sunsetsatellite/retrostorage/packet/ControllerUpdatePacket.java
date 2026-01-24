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
import sunsetsatellite.retrostorage.packet.data.ControllerUpdateData;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ControllerUpdatePacket extends Packet implements ManagedPacket<ControllerUpdatePacket> {
    public static final PacketType<ControllerUpdatePacket> TYPE = PacketType.builder(true, false, ControllerUpdatePacket::new).build();

    @Override
    public @NotNull PacketType<ControllerUpdatePacket> getType() {
        return TYPE;
    }

    private int x;
    private int y;
    private int z;
    private ControllerUpdateData data;

    public ControllerUpdatePacket(int x, int y, int z, ControllerUpdateData data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }

    public ControllerUpdatePacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            x = stream.readInt();
            y = stream.readInt();
            z = stream.readInt();
            data = new ControllerUpdateData().read(stream);
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
