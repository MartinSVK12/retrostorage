package sunsetsatellite.retrostorage.mp.terminal.request;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.retrostorage.menus.MenuRequestTerminal;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.ArrayList;
import java.util.List;

public class PacketRequestTerminalContents implements NetworkMessage {

    public List<Pair<ItemStack,NetworkCraftable>> craftables = new ArrayList<>();

    public PacketRequestTerminalContents(List<Pair<ItemStack,NetworkCraftable>> craftables){
        this.craftables = craftables;
    }

    public PacketRequestTerminalContents() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(craftables.size());
        for (Pair<ItemStack, NetworkCraftable> craftable : craftables) {
            CompoundTag tag = new CompoundTag();
            craftable.getRight().writeToNBT(tag);
            packet.writeCompoundTag(tag);
        }
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        int amount = packet.readInt();
        for (int i = 0; i < amount; i++) {
            CompoundTag tag = packet.readCompoundTag();
            NetworkCraftable craftable = new NetworkCraftable(tag);
            craftables.add(Pair.of(craftable.getOutput().get(0).forceGetItem(),craftable));
        }
    }

    @Override
    public void handle(NetworkContext context) {
        if(context.player != null && context.player.craftingInventory instanceof MenuRequestTerminal){
            ((MenuRequestTerminal) context.player.craftingInventory).networkCraftables = craftables;
        }
    }
}
