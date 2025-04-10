package sunsetsatellite.retrostorage.menus;


import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketContainerSetSlot;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.server.entity.player.PlayerServer;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.retrostorage.mp.PacketTerminalContents;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.util.INetworkController;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuDigitalTerminal extends MenuAbstract {

    public final ArrayList<Vec2i> virtualSlots = new ArrayList<>();
    public ContainerInventory inventoryPlayer;
    public List<ItemStack> networkStacks = new ArrayList<>();

    public MenuDigitalTerminal(ContainerInventory playerInv, TileEntityDigitalTerminal tile) {
        this.tile = tile;
        this.inventoryPlayer = playerInv;
        if(tile.getController() != null) {
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

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                virtualSlots.add(new Vec2i(x,y));
            }
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
    public void broadcastChanges() {
        super.broadcastChanges();
        if(EnvironmentHelper.isServerEnvironment()){
            NetworkHandler.sendToPlayer(inventoryPlayer.player,new PacketTerminalContents(networkStacks));
        }
    }

    public void handleTerminalInteraction(String searchQuery, int slotId, int vSlotId, int mouseButton, boolean shift) {
        INetworkController controller = tile.getController();
        if(controller != null){
            Slot invSlot = null;
            if(slotId > -1 && slotId < slots.size()){
                invSlot = slots.get(slotId);
            }
            if(invSlot != null) {
                //left shift click to network
                if(mouseButton == 0 && shift){
                    ItemStack stack = invSlot.getItemStack();
                    invSlot.set(controller.addItemToNetwork(stack));
                    return;
                }
            }

            if(vSlotId == -1) {
                return;
            }
            int id = vSlotId + (tile.page * 36);
            networkStacks = getFilteredStacks(searchQuery, controller);
            //left click
            if(mouseButton == 0){
                //left shift click from network
                if(shift){
                    if(id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if(stack == null) return;
                    int amount = stack.getItem().getItemStackLimit(stack);
                    inventoryPlayer.insertItem(controller.removeItemFromNetwork(stack.itemID,stack.getMetadata(),stack.getData(),amount),false);
                    ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, inventoryPlayer.getHeldItemStack()));
                    return;
                }
                ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                if(heldItemStack != null){
                    inventoryPlayer.setHeldItemStack(controller.addItemToNetwork(heldItemStack));
                    ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, inventoryPlayer.getHeldItemStack()));
                } else {
                    if(id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if(stack == null) return;
                    int amount = stack.getItem().getItemStackLimit(stack);
                    inventoryPlayer.setHeldItemStack(controller.removeItemFromNetwork(stack.itemID,stack.getMetadata(),stack.getData(),amount));
                    ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, inventoryPlayer.getHeldItemStack()));
                }
            }
            //right click
            if(mouseButton == 1){
                ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                if(heldItemStack != null){
                    Optional<ItemStack> leftovers = Optional.ofNullable(controller.addItemToNetwork(heldItemStack.splitStack(1)));
                    if(heldItemStack.stackSize <= 0) {
                        inventoryPlayer.setHeldItemStack(leftovers.orElse(null));
                        ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, inventoryPlayer.getHeldItemStack()));
                    }
                    leftovers.ifPresent((S)->heldItemStack.stackSize += S.stackSize);
                } else {
                    if(id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if(stack == null) return;
                    int amount = Math.min(stack.stackSize / 2, stack.getItem().getItemStackLimit(stack) / 2);
                    inventoryPlayer.setHeldItemStack(controller.removeItemFromNetwork(stack.itemID,stack.getMetadata(),stack.getData(),amount));
                    ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, inventoryPlayer.getHeldItemStack()));
                }
            }
        }
        broadcastChanges();
    }

    private static List<ItemStack> getFilteredStacks(String searchQuery, INetworkController controller) {
        List<ItemStack> stacks = controller.getAllItems();
        if(!Objects.equals(searchQuery, "")){
            stacks = stacks.stream().filter(S -> S.getDisplayName().toLowerCase().contains(searchQuery.toLowerCase())).collect(Collectors.toList());
        }
        return stacks;
    }

    public boolean stillValid(Player entityplayer) {
        return tile.stillValid(entityplayer);
    }

    private final TileEntityDigitalTerminal tile;
}
