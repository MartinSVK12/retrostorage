package sunsetsatellite.retrostorage.menus;


import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketContainerSetSlot;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.server.entity.player.PlayerServer;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.retrostorage.mp.terminal.fluid.PacketFluidTerminalContents;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalFluidTerminal;
import sunsetsatellite.retrostorage.api.INetworkController;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuDigitalFluidTerminal extends MenuAbstract {

    public final ArrayList<Vec2i> virtualSlots = new ArrayList<>();
    public ContainerInventory inventoryPlayer;
    public List<ItemStack> networkStacks = new ArrayList<>();

    public MenuDigitalFluidTerminal(ContainerInventory playerInv, TileEntityDigitalFluidTerminal tile) {
        this.tile = tile;
        this.inventoryPlayer = playerInv;
        if(tile.getController() != null) {
            this.networkStacks = tile.getController().getAllFluids().stream().map(FluidStack::toItemStack).collect(Collectors.toList());
        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 198));
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(playerInv, i1 + j * 9 + 9, 8 + i1 * 18, 140 + j * 18));
            }
        }
        if(EnvironmentHelper.isMultiplayerServer()){
            NetworkHandler.sendToPlayer(inventoryPlayer.player,new PacketFluidTerminalContents(networkStacks));
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
        INetworkController controller = tile.getController();
        if(controller != null){
            if(vSlotId == -1) {
                return;
            }
            int id = vSlotId + (page * 54);
            getFilteredStacks(searchQuery);
            //left click\
            if (mouseButton == 0) {
                ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                if(heldItemStack != null) {
                    if (id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if (stack == null) return;
                    Block<?> blockFluid = Blocks.blocksList[stack.itemID];
                    if(blockFluid == null) return;
                    if(inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer item) {
						if (item.canFill(heldItemStack)) {
                            int amount = item.getRemainingCapacity(heldItemStack);
                            FluidStack fluidStack = controller.removeFluidFromNetwork(blockFluid.id(), amount);
                            item.fill(fluidStack,heldItemStack);
                            if(fluidStack.amount <= 0) fluidStack = null;
                            if(fluidStack != null){
                                controller.addFluidToNetwork(fluidStack);
                            }
                            if(EnvironmentHelper.isMultiplayerServer()) {
                                ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, -1, inventoryPlayer.getHeldItemStack()));
                            }
                        }
                    }
                }
            } else if (mouseButton == 1) { //right click
                ItemStack heldItemStack = inventoryPlayer.getHeldItemStack();
                if(heldItemStack != null) {
                    if(inventoryPlayer.getHeldItemStack() != null && inventoryPlayer.getHeldItemStack().getItem() instanceof IItemFluidContainer item) {
						if (item.canDrain(heldItemStack)) {
                            int amount = item.getCurrentFluid(heldItemStack).amount;
                            if (amount > 0) {
                                FluidStack drained = item.drain(heldItemStack, amount);
                                if (drained != null) {
                                    Optional<FluidStack> fluidStack = Optional.ofNullable(controller.addFluidToNetwork(drained));
                                    fluidStack.ifPresent((S) -> item.fill(S, heldItemStack));
                                }
                                if (EnvironmentHelper.isMultiplayerServer()) {
                                    ((PlayerServer) inventoryPlayer.player).playerNetServerHandler.sendPacket(new PacketContainerSetSlot(-1, -1, -1, inventoryPlayer.getHeldItemStack()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void getFilteredStacks(String searchQuery) {
        if(tile.getController() == null) return;
        List<ItemStack> stacks = tile.getController().getAllFluids().stream().map(FluidStack::toItemStack).collect(Collectors.toList());
        if(!Objects.equals(searchQuery, "")){
            stacks = stacks.stream().filter(S -> S.getDisplayName().toLowerCase().contains(searchQuery.toLowerCase())).collect(Collectors.toList());
        }
        networkStacks = stacks;
    }

    public boolean stillValid(Player entityplayer) {
        return tile.stillValid(entityplayer);
    }

    private final TileEntityDigitalFluidTerminal tile;
}
