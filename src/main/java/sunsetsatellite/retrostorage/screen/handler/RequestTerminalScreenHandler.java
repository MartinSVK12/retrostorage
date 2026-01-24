package sunsetsatellite.retrostorage.screen.handler;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.block.entity.RequestTerminalBlockEntity;
import sunsetsatellite.retrostorage.item.MobileTerminalItem;
import sunsetsatellite.retrostorage.packet.terminal.request.RequestTerminalContentsPacket;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestTerminalScreenHandler extends ScreenHandler {

    private final RequestTerminalBlockEntity tile;
    public final ArrayList<Vec2i> virtualSlots = new ArrayList<>();
    public PlayerInventory playerInv;
    public List<Pair<ItemStack, NetworkCraftable>> networkCraftables = new ArrayList<>();

    public RequestTerminalScreenHandler(PlayerInventory playerInv, RequestTerminalBlockEntity tile) {
        this.tile = tile;
        this.playerInv = playerInv;
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
                virtualSlots.add(new Vec2i(x, y));
            }
        }

        if (Catalyst.serverEnv()) {
            ((ServerPlayerEntity) playerInv.player).networkHandler.sendPacket(new RequestTerminalContentsPacket(networkCraftables));
        }
    }

    public void getCraftables(String query) {
        NetworkController controller = tile.getController();
        if (controller != null) {
            List<NetworkCraftable> craftables = controller.getCraftables();
            List<Pair<ItemStack, NetworkCraftable>> stacks = new ArrayList<>();
            craftables.stream().map(NC -> {
                if (NC.getOutput().isEmpty()) return null;
                return Pair.of(NC.getOutput().get(0).forceGetItem(), NC);
            }).filter(Objects::nonNull).forEach(stacks::add);
            if (!Objects.equals(query, "")) {
                stacks = stacks.stream().filter(P -> TranslationStorage.getInstance().getClientTranslation(P.getFirst().getTranslationKey()).toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
            }
            networkCraftables = stacks;
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        if (player.inventory.getSelectedItem() != null && player.inventory.getSelectedItem().getItem() instanceof MobileTerminalItem) {
            return true;
        }
        return tile.canUse(player);
    }
}
