package sunsetsatellite.retrostorage.mp;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.menus.MenuDigitalTerminal;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.ArrayList;
import java.util.List;

public class PacketTerminalContents implements NetworkMessage {

    public List<ItemStack> stacks = new ArrayList<>();

    public PacketTerminalContents(List<ItemStack> stacks){
        this.stacks = stacks;
    }

    public PacketTerminalContents() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(stacks.size());
        for (ItemStack stack : stacks) {
            CompoundTag tag = new CompoundTag();
            stack.writeToNBT(tag);
            packet.writeCompoundTag(tag);
        }
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        int amount = packet.readInt();
        for (int i = 0; i < amount; i++) {
            CompoundTag tag = packet.readCompoundTag();
            stacks.add(ItemStack.readItemStackFromNbt(tag));
        }
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.craftingInventory instanceof MenuDigitalTerminal){
            ((MenuDigitalTerminal) context.player.craftingInventory).networkStacks = stacks;
        }
    }
}
