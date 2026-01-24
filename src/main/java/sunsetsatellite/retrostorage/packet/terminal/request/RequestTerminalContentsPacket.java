package sunsetsatellite.retrostorage.packet.terminal.request;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.screen.handler.RequestTerminalScreenHandler;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RequestTerminalContentsPacket extends Packet implements ManagedPacket<RequestTerminalContentsPacket> {
    public static final PacketType<RequestTerminalContentsPacket> TYPE = PacketType.builder(true, false, RequestTerminalContentsPacket::new).build();

    @Override
    public @NotNull PacketType<RequestTerminalContentsPacket> getType() {
        return TYPE;
    }

    public List<Pair<ItemStack, NetworkCraftable>> craftables = new ArrayList<>();

    public RequestTerminalContentsPacket(List<Pair<ItemStack, NetworkCraftable>> craftables) {
        this.craftables = craftables;
    }

    public RequestTerminalContentsPacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            int amount = stream.readInt();
            for (int i = 0; i < amount; i++) {
                NbtCompound tag = Catalyst.readNbtFromStream(stream);
                NetworkCraftable craftable = new NetworkCraftable(tag);
                craftables.add(Pair.of(craftable.getOutput().get(0).forceGetItem(), craftable));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(craftables.size());
            for (Pair<ItemStack, NetworkCraftable> craftable : craftables) {
                NbtCompound tag = new NbtCompound();
                craftable.getSecond().writeToNBT(tag);
                Catalyst.writeNbtToStream(tag, stream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if (player != null && player.currentScreenHandler instanceof RequestTerminalScreenHandler handler) {
            handler.networkCraftables = craftables;
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
