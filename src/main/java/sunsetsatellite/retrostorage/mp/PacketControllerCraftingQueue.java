package sunsetsatellite.retrostorage.mp;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.entity.TileEntity;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class PacketControllerCraftingQueue implements NetworkMessage {

    private int x;
    private int y;
    private int z;
    private CompoundTag tasksTag;

    public PacketControllerCraftingQueue(int x, int y, int z, ArrayDeque<CraftingTask> tasks) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tasksTag = new CompoundTag();
        int i = 0;
        for (CraftingTask task : tasks) {
            CompoundTag taskTag = new CompoundTag();
            task.writeToNbt(taskTag);
            this.tasksTag.putCompound(String.valueOf(i),taskTag);
            i++;
        }
    }

    public PacketControllerCraftingQueue() {

    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(x);
        packet.writeInt(y);
        packet.writeInt(z);
        packet.writeCompoundTag(tasksTag);

    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        x = packet.readInt();
        y = packet.readInt();
        z = packet.readInt();
        tasksTag = packet.readCompoundTag();
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.world != null) {
            TileEntity te = context.player.world.getTileEntity(x, y, z);
            if (te instanceof TileEntityDigitalController) {
                TileEntityDigitalController c = (TileEntityDigitalController) te;
                ArrayDeque<CraftingTask> tasks = new ArrayDeque<>();
                for (Tag<?> value : tasksTag.getValues()) {
                    CompoundTag tag = (CompoundTag) value;
                    CraftingTask task = new CraftingTask(c, tag);
                    tasks.add(task);
                }
                c.requestQueueCache = tasks;
            }
        }
    }
}
