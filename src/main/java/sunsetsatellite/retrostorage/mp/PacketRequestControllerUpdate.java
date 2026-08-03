package sunsetsatellite.retrostorage.mp;

import net.minecraft.core.block.entity.TileEntity;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.mp.data.ControllerUpdateData;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketRequestControllerUpdate implements NetworkMessage {

    private int x;
    private int y;
    private int z;

    public PacketRequestControllerUpdate() {

    }

    public PacketRequestControllerUpdate(int x, int y, int z) {
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
        if(EnvironmentHelper.isMultiplayerServer()){
            if(context.player != null && context.player.world != null){
                TileEntity te = context.player.world.getTileEntity(x, y, z);
                if(te instanceof TileEntityDigitalController){
                    TileEntityDigitalController c = (TileEntityDigitalController) te;
                    NetworkHandler.sendToPlayer(context.player, new PacketControllerUpdate(x,y,z,new ControllerUpdateData().get(c)));
                }
            }
        }
    }
}
