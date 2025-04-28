package sunsetsatellite.retrostorage.mp;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.util.INetworkController;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class PacketRequestCrafting implements NetworkMessage {

    public int x;
    public int y;
    public int z;
    public CompoundTag taskTag = new CompoundTag();

    public PacketRequestCrafting() {}

    public PacketRequestCrafting(int x, int y, int z, CraftingTask task) {
        this.x = x;
        this.y = y;
        this.z = z;
        task.writeToNbt(this.taskTag);
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(x);
        packet.writeInt(y);
        packet.writeInt(z);
        packet.writeCompoundTag(taskTag);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        x = packet.readInt();
        y = packet.readInt();
        z = packet.readInt();
        taskTag = packet.readCompoundTag();
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.world != null) {
            TileEntity te = context.player.world.getTileEntity(x, y, z);
            if (te instanceof INetworkController) {
                INetworkController c = (INetworkController) te;
                CraftingTask task = new CraftingTask(c, taskTag);
                c.requestCrafting(task);
            }
        }
    }
}
