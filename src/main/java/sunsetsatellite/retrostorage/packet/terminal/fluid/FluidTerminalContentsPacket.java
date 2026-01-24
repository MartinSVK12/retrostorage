package sunsetsatellite.retrostorage.packet.terminal.fluid;

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
import sunsetsatellite.retrostorage.screen.handler.FluidTerminalScreenHandler;
import sunsetsatellite.retrostorage.util.DiscManipulator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FluidTerminalContentsPacket extends Packet implements ManagedPacket<FluidTerminalContentsPacket> {
    public static final PacketType<FluidTerminalContentsPacket> TYPE = PacketType.builder(true, false, FluidTerminalContentsPacket::new).build();

    @Override
    public @NotNull PacketType<FluidTerminalContentsPacket> getType() {
        return TYPE;
    }

    public List<ItemStack> stacks = new ArrayList<>();

    public FluidTerminalContentsPacket(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public FluidTerminalContentsPacket() {

    }

    @Override
    public void read(DataInputStream stream) {
        try {
            int amount = stream.readInt();
            for (int i = 0; i < amount; i++) {
                NbtCompound tag = Catalyst.readNbtFromStream(stream);
                stacks.add(DiscManipulator.readUnlimitedStackFromNbt(tag));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(stacks.size());
            for (ItemStack stack : stacks) {
                NbtCompound tag = new NbtCompound();
                stack.writeNbt(tag);
                tag.putInt("Count", stack.count);
                Catalyst.writeNbtToStream(tag, stream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        if (player != null && player.currentScreenHandler instanceof FluidTerminalScreenHandler handler) {
            handler.networkStacks = stacks;
        }
    }

    @Override
    public int size() {
        return 3 * 4;
    }
}
