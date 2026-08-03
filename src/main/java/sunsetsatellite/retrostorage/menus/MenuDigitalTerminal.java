package sunsetsatellite.retrostorage.menus;


import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketContainerSetSlot;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.server.entity.player.PlayerServer;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.retrostorage.mp.terminal.item.PacketTerminalContents;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalTerminal;
import sunsetsatellite.retrostorage.api.INetworkController;
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

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                virtualSlots.add(new Vec2i(x,y));
            }
        }

        if(EnvironmentHelper.isMultiplayerServer()){
            NetworkHandler.sendToPlayer(inventoryPlayer.player,new PacketTerminalContents(networkStacks));
        }
    }

    @Override
    public IntList getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    @Override
    public IntList getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        return null;
    }

    public void handleTerminalInteraction(String searchQuery, int slotId, int vSlotId, int page, int mouseButton, boolean shift) {
        broadcastChanges();
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
            int id = vSlotId + (page * 54);
            getFilteredStacks(searchQuery);
            //left click
            if(mouseButton == 0){
                //left shift click from network
                if(shift){
                    if(id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if(stack == null) return;
                    int amount = stack.getItem().getItemStackLimit(stack);
                    ItemStack itemStack = controller.removeItemFromNetwork(stack.itemID, stack.getMetadata(), stack.getData(), amount);
                    inventoryPlayer.insertItem(itemStack,false);
                    if(itemStack.stackSize > 0){
                        inventoryPlayer.player.dropPlayerItem(itemStack);
                    }
                    if(EnvironmentHelper.isMultiplayerServer()){
                        ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, -1, inventoryPlayer.getHeldItemStack()));
                    }
                    return;
                }
                ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                if(heldItemStack != null){
                    inventoryPlayer.setHeldItemStack(controller.addItemToNetwork(heldItemStack));
                    if(EnvironmentHelper.isMultiplayerServer()) {
                        ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, -1, inventoryPlayer.getHeldItemStack()));
                    }
                } else {
                    if(id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if(stack == null) return;
                    int amount = stack.getItem().getItemStackLimit(stack);
                    inventoryPlayer.setHeldItemStack(controller.removeItemFromNetwork(stack.itemID,stack.getMetadata(),stack.getData(),amount));
                    if(EnvironmentHelper.isMultiplayerServer()) {
                        ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, -1, inventoryPlayer.getHeldItemStack()));
                    }
                }
            }
            //right click
            if(mouseButton == 1){
                ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                if(heldItemStack != null){
                    Optional<ItemStack> leftovers = Optional.ofNullable(controller.addItemToNetwork(heldItemStack.splitStack(1)));
                    if(heldItemStack.stackSize <= 0) {
                        inventoryPlayer.setHeldItemStack(leftovers.orElse(null));
                        if(EnvironmentHelper.isMultiplayerServer()) {
                            ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, -1, inventoryPlayer.getHeldItemStack()));
                        }
                    }
                    leftovers.ifPresent((S)->heldItemStack.stackSize += S.stackSize);
                } else {
                    if (id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if (stack == null) return;
                    int amount = Math.min(stack.stackSize / 2, stack.getItem().getItemStackLimit(stack) / 2);
                    inventoryPlayer.setHeldItemStack(controller.removeItemFromNetwork(stack.itemID, stack.getMetadata(), stack.getData(), amount));
                    if (EnvironmentHelper.isMultiplayerServer()) {
                        ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, -1, inventoryPlayer.getHeldItemStack()));
                    }
                }
            }
        }
    }

    public void getFilteredStacks(String searchQuery) {
        if(tile.getController() == null) return;
        List<ItemStack> stacks = tile.getController().getAllItems();
        if(!Objects.equals(searchQuery, "")){
            stacks = stacks.stream().filter(S -> S.getDisplayName().toLowerCase().contains(searchQuery.toLowerCase())).collect(Collectors.toList());
        }
        networkStacks = stacks;
    }

    public boolean stillValid(Player entityplayer) {
        return tile.stillValid(entityplayer);
    }

    private final TileEntityDigitalTerminal tile;
}
