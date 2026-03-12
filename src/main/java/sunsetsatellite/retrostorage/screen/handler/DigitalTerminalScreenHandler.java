package sunsetsatellite.retrostorage.screen.handler;

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
import sunsetsatellite.retrostorage.block.entity.DigitalTerminalBlockEntity;
import sunsetsatellite.retrostorage.item.MobileTerminalItem;
import sunsetsatellite.retrostorage.packet.UpdateSlotPacket;
import sunsetsatellite.retrostorage.packet.terminal.item.TerminalContentsPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class DigitalTerminalScreenHandler extends ScreenHandler {

    private final DigitalTerminalBlockEntity tile;
    public final ArrayList<Vec2i> virtualSlots = new ArrayList<>();
    public PlayerInventory playerInv;
    public List<ItemStack> networkStacks = new ArrayList<>();

    public DigitalTerminalScreenHandler(PlayerInventory playerInv, DigitalTerminalBlockEntity tile) {
        this.tile = tile;
        this.playerInv = playerInv;

        if (tile.getController() != null) {
            this.networkStacks = tile.getController().getAllItems();
        }

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
            ((ServerPlayerEntity) playerInv.player).networkHandler.sendPacket(new TerminalContentsPacket(networkStacks));
        }
    }

    public void handleTerminalInteraction(String searchQuery, int slotId, int vSlotId, int page, int mouseButton, boolean shift) {
        sendContentUpdates();
        NetworkController con = tile.getController();
        PlayerEntity player = playerInv.player;
        if (con != null) {
            Slot invSlot = null;
            if (slotId > -1 && slotId < slots.size()) {
                invSlot = (Slot) slots.get(slotId);
            }
            if (invSlot != null) {
                //left shift click to network
                if (mouseButton == 0 && shift) {
                    ItemStack stack = invSlot.getStack();
                    if (stack == null) return;
                    invSlot.setStack(con.addItemToNetwork(stack));
                    return;
                }
            }

            if (vSlotId == -1) {
                return;
            }

            int id = vSlotId + (page * 54);
            getFilteredStacks(searchQuery);

            //left click
            if (mouseButton == 0) {
                //left shift click from network
                if (shift) {
                    if (id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if (stack == null) return;
                    int amount = stack.getItem().getMaxCount();
                    playerInv.addStack(con.removeItemFromNetwork(stack.itemId, stack.getDamage(), stack.getStationNbt(), amount));
                    if (Catalyst.serverEnv()) {
                        ((ServerPlayerEntity) player).networkHandler.sendPacket(new UpdateSlotPacket(-1, playerInv.getCursorStack()));
                    }
                    return;
                }
                ItemStack heldItemStack = playerInv.getCursorStack();
                if (heldItemStack != null) {
                    playerInv.setCursorStack(con.addItemToNetwork(heldItemStack));
                    if (Catalyst.serverEnv()) {
                        ((ServerPlayerEntity) player).networkHandler.sendPacket(new UpdateSlotPacket(-1, playerInv.getCursorStack()));
                    }
                } else {
                    if (id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if (stack == null) return;
                    int amount = stack.getItem().getMaxCount();
                    playerInv.setCursorStack(con.removeItemFromNetwork(stack.itemId, stack.getDamage(), stack.getStationNbt(), amount));
                    if (Catalyst.serverEnv()) {
                        ((ServerPlayerEntity) player).networkHandler.sendPacket(new UpdateSlotPacket(-1, playerInv.getCursorStack()));
                    }
                }
            }
            //right click
            if (mouseButton == 1) {
                ItemStack heldItemStack = playerInv.getCursorStack();
                if (heldItemStack != null) {
                    Optional<ItemStack> leftovers = Optional.ofNullable(con.addItemToNetwork(heldItemStack.split(1)));
                    if (heldItemStack.count <= 0) {
                        playerInv.setCursorStack(leftovers.orElse(null));
                        if (Catalyst.serverEnv()) {
                            ((ServerPlayerEntity) player).networkHandler.sendPacket(new UpdateSlotPacket(-1, playerInv.getCursorStack()));
                        }
                    }
                    leftovers.ifPresent((S) -> heldItemStack.count += S.count);
                } else {
                    if (id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if (stack == null) return;
                    int amount = Math.min(stack.count / 2, stack.getItem().getMaxCount() / 2);
                    playerInv.setCursorStack(con.removeItemFromNetwork(stack.itemId, stack.getDamage(), stack.getStationNbt(), amount));
                    if (Catalyst.serverEnv()) {
                        ((ServerPlayerEntity) player).networkHandler.sendPacket(new UpdateSlotPacket(-1, playerInv.getCursorStack()));
                    }
                }
            }
        }
    }

    public void getFilteredStacks(String searchQuery) {
        if (tile.getController() == null) return;
        List<ItemStack> stacks = tile.getController().getAllItems();
        if (!Objects.equals(searchQuery, "")) {
            stacks = stacks.stream().filter(S -> TranslationStorage.getInstance().getClientTranslation(S.getTranslationKey()).toLowerCase().contains(searchQuery.toLowerCase())).collect(Collectors.toList());
        }
        networkStacks = stacks;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        if (player.inventory.getSelectedItem() != null && player.inventory.getSelectedItem().getItem() instanceof MobileTerminalItem) {
            return true;
        }
        return tile.canUse(player);
    }
}
