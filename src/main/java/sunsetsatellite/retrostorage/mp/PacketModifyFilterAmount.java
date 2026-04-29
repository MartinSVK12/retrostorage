package sunsetsatellite.retrostorage.mp;

import net.minecraft.core.block.entity.TileEntity;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.tiles.TileEntityProcessProgrammer;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketModifyFilterAmount implements NetworkMessage {

    private int x;
    private int y;
    private int z;
    private int slotId;
    private int amount;

    public PacketModifyFilterAmount() {
    }

    public PacketModifyFilterAmount(int x, int y, int z, int slotId, int amount) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.slotId = slotId;
        this.amount = amount;
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(x);
        packet.writeInt(y);
        packet.writeInt(z);
        packet.writeInt(slotId);
        packet.writeInt(amount);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        x = packet.readInt();
        y = packet.readInt();
        z = packet.readInt();
        slotId = packet.readInt();
        amount = packet.readInt();
    }

    @Override
    public void handle(NetworkContext context) {
        if(EnvironmentHelper.isServerEnvironment()){
            if(context.player != null && context.player.world != null) {
                TileEntity te = context.player.world.getTileEntity(x, y, z);
                if (te instanceof TileEntityProcessProgrammer) {
                    ((TileEntityProcessProgrammer) te).filter.getFluidInSlot(slotId).amount += amount;
                }
            }
        }
    }
}
