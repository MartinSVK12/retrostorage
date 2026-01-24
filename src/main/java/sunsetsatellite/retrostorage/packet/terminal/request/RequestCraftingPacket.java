package sunsetsatellite.retrostorage.packet.terminal.request;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RequestCraftingPacket extends Packet implements ManagedPacket<RequestCraftingPacket> {
    public static final PacketType<RequestCraftingPacket> TYPE = PacketType.builder(false, true, RequestCraftingPacket::new).build();

    @Override
    public @NotNull PacketType<RequestCraftingPacket> getType() {
        return TYPE;
    }

    private int x;
    private int y;
    private int z;
    private NbtCompound taskTag = new NbtCompound();

    public RequestCraftingPacket(int x, int y, int z, CraftingTask task) {
        this.x = x;
        this.y = y;
        this.z = z;
        task.writeToNbt(taskTag);
    }

    public RequestCraftingPacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            x = stream.readInt();
            y = stream.readInt();
            z = stream.readInt();
            taskTag = Catalyst.readNbtFromStream(stream);
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
            Catalyst.writeNbtToStream(taskTag, stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        BlockEntity e = player.world.getBlockEntity(x, y, z);
        if (e instanceof NetworkController controller) {
            CraftingTask task = new CraftingTask(controller, taskTag);
            controller.requestCrafting(task);
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
