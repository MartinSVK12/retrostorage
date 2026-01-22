package sunsetsatellite.retrostorage.menus;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.retrostorage.mp.terminal.request.PacketRequestTerminalContents;
import sunsetsatellite.retrostorage.tiles.TileEntityRequestTerminal;
import sunsetsatellite.retrostorage.api.INetworkController;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuRequestTerminal extends MenuAbstract {

    public final ArrayList<Vec2i> virtualSlots = new ArrayList<>();
    public ContainerInventory inventoryPlayer;
    public List<Pair<ItemStack,NetworkCraftable>> networkCraftables = new ArrayList<>();

    public MenuRequestTerminal(ContainerInventory playerInv, TileEntityRequestTerminal tile) {
        this.tile = tile;
        this.inventoryPlayer = playerInv;
        getCraftables("");

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 198));
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(playerInv, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                virtualSlots.add(new Vec2i(x,y));
            }
        }

        if(EnvironmentHelper.isServerEnvironment()){
            NetworkHandler.sendToPlayer(inventoryPlayer.player,new PacketRequestTerminalContents(networkCraftables));
        }
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    @Override
    public boolean stillValid(Player entityplayer) {
        return tile.stillValid(entityplayer);
    }

    private final TileEntityRequestTerminal tile;

    public void getCraftables(String query) {
        INetworkController controller = tile.getController();
        if(controller != null) {
            List<NetworkCraftable> craftables = controller.getCraftables();
            List<Pair<ItemStack,NetworkCraftable>> stacks = new ArrayList<>();
            craftables.stream().map(NC -> {
                if(NC.getOutput().isEmpty()) return null;
                return Pair.of(NC.getOutput().get(0).forceGetItem(),NC);
            }).filter(Objects::nonNull).forEach(stacks::add);
            if(!Objects.equals(query, "")){
                stacks = stacks.stream().filter(P -> P.getLeft().getDisplayName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            networkCraftables = stacks;
        }
    }
}
