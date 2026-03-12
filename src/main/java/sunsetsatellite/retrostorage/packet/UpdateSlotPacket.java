package sunsetsatellite.retrostorage.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UpdateSlotPacket extends Packet implements ManagedPacket<UpdateSlotPacket> {
    public static final PacketType<UpdateSlotPacket> TYPE = PacketType.builder(true, true, UpdateSlotPacket::new).build();

    private int slot;
    private ItemStack stack;

    public UpdateSlotPacket() {
    }

    public UpdateSlotPacket(int slot, ItemStack stack) {
        this.stack = stack;
        this.slot = slot;
    }

    public void read(DataInputStream stream) {
        this.stack = null;

        try {
            this.slot = stream.readShort();
            int count = Byte.toUnsignedInt(stream.readByte());
            if (count > 0) {
                int id = stream.readInt();
                int damage = stream.readInt();
                this.stack = new ItemStack(id, count, damage);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(DataOutputStream stream) {
        try {
            stream.writeShort((short)this.slot);
            if (this.stack == null) {
                stream.writeByte(0);
            } else {
                stream.writeByte((byte)this.stack.count);
                if (this.stack.count > 0) {
                    stream.writeInt(this.stack.itemId);
                    stream.writeInt(this.stack.getDamage());
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void apply(NetworkHandler handler) {
        if (handler instanceof ServerPlayNetworkHandler serverHandler) {
            PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(serverHandler);
            if (this.slot == -1) {
                player.inventory.setCursorStack(this.stack);
            } else {
                player.inventory.setStack(this.slot, this.stack);
            }
        }

    }

    public int size() {
        return 11;
    }

    public @NotNull PacketType<UpdateSlotPacket> getType() {
        return TYPE;
    }
}
