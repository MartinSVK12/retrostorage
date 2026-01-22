package sunsetsatellite.retrostorage.mp;

import net.minecraft.core.block.entity.TileEntity;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.api.INetworkController;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketClearRequestQueue implements NetworkMessage {

    private int x;
    private int y;
    private int z;

    public PacketClearRequestQueue() {}

    public PacketClearRequestQueue(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(x);
        packet.writeInt(y);
        packet.writeInt(z);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        x = packet.readInt();
        y = packet.readInt();
        z = packet.readInt();
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.world != null) {
            TileEntity te = context.player.world.getTileEntity(x, y, z);
            if (te instanceof INetworkController) {
                INetworkController c = (INetworkController) te;
                c.clearRequestQueue();
            }
        }
    }
}
