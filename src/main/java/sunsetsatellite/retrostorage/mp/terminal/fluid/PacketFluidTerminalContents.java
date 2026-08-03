package sunsetsatellite.retrostorage.mp.terminal.fluid;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.menus.MenuDigitalFluidTerminal;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.ArrayList;
import java.util.List;

public class PacketFluidTerminalContents implements NetworkMessage {

    public List<ItemStack> stacks = new ArrayList<>();

    public PacketFluidTerminalContents(List<ItemStack> stacks){
        this.stacks = stacks;
    }

    public PacketFluidTerminalContents() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(stacks.size());
        for (ItemStack stack : stacks) {
            CompoundTag tag = new CompoundTag();
            stack.writeToNBT(tag);
            tag.putInt("Count",stack.stackSize);
            packet.writeCompoundTag(tag);
        }
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        int amount = packet.readInt();
        for (int i = 0; i < amount; i++) {
            CompoundTag tag = packet.readCompoundTag();
            stacks.add(DiscManipulator.readUnlimitedStackFromNbt(tag));
        }
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.containerMenu instanceof MenuDigitalFluidTerminal){
            ((MenuDigitalFluidTerminal) context.player.containerMenu).networkStacks = stacks;
        }
    }
}
