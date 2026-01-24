package sunsetsatellite.retrostorage.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.block.entity.DigitalControllerBlockEntity;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayDeque;

public class ControllerCraftingQueuePacket extends Packet implements ManagedPacket<ControllerCraftingQueuePacket> {
    public static final PacketType<ControllerCraftingQueuePacket> TYPE = PacketType.builder(true, false, ControllerCraftingQueuePacket::new).build();

    @Override
    public @NotNull PacketType<ControllerCraftingQueuePacket> getType() {
        return TYPE;
    }

    private int x;
    private int y;
    private int z;
    private NbtCompound tasksTag;

    public ControllerCraftingQueuePacket(int x, int y, int z, ArrayDeque<CraftingTask> tasks) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tasksTag = new NbtCompound();
        int i = 0;
        for (CraftingTask task : tasks) {
            NbtCompound taskTag = new NbtCompound();
            task.writeToNbt(taskTag);
            this.tasksTag.put(String.valueOf(i), taskTag);
            i++;
        }
    }

    public ControllerCraftingQueuePacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            x = stream.readInt();
            y = stream.readInt();
            z = stream.readInt();
            tasksTag = Catalyst.readNbtFromStream(stream);
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
            Catalyst.writeNbtToStream(tasksTag, stream);
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
            ArrayDeque<CraftingTask> tasks = new ArrayDeque<>();
            for (Object value : tasksTag.values()) {
                NbtCompound tag = (NbtCompound) value;
                CraftingTask task = new CraftingTask(controller, tag);
                tasks.add(task);
            }
            controller.requestQueueCache = tasks;
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
