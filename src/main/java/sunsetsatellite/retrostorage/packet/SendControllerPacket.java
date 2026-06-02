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
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SendControllerPacket extends Packet implements ManagedPacket<SendControllerPacket> {
    public static final PacketType<SendControllerPacket> TYPE = PacketType.builder(true, false, SendControllerPacket::new).build();

    @Override
    public @NotNull PacketType<SendControllerPacket> getType() {
        return TYPE;
    }

    private Vec3i pos;
    private Vec3i controllerPos;

    public SendControllerPacket(Vec3i pos, Vec3i controllerPos) {
        this.pos = pos;
        this.controllerPos = controllerPos;
    }

    public SendControllerPacket() {

    }
    @Override
    public void read(DataInputStream stream) {
        try {
            int x = stream.readInt();
            int y = stream.readInt();
            int z = stream.readInt();
            pos = new Vec3i(x, y, z);
            int controllerX = stream.readInt();
            int controllerY = stream.readInt();
            int controllerZ = stream.readInt();
            controllerPos = new Vec3i(controllerX, controllerY, controllerZ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(pos.x);
            stream.writeInt(pos.y);
            stream.writeInt(pos.z);
            stream.writeInt(controllerPos.x);
            stream.writeInt(controllerPos.y);
            stream.writeInt(controllerPos.z);
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
        BlockEntity self = pos.getBlockEntity(player.world);
        BlockEntity c = controllerPos.getBlockEntity(player.world);
        if (self instanceof NetworkDeviceBlockEntity device) {
            if(c instanceof DigitalControllerBlockEntity controller){
                device.controller = controller;
            } else {
                device.controller = null;
            }
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
