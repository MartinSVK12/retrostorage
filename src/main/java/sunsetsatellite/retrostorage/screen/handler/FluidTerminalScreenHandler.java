package sunsetsatellite.retrostorage.screen.handler;

import net.danygames2014.nyalib.capability.CapabilityHelper;
import net.danygames2014.nyalib.capability.item.fluidhandler.FluidHandlerItemCapability;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidBucket;
import net.danygames2014.nyalib.fluid.FluidStack;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.api.NetworkController;
import sunsetsatellite.retrostorage.block.entity.FluidTerminalBlockEntity;
import sunsetsatellite.retrostorage.item.MobileTerminalItem;
import sunsetsatellite.retrostorage.packet.UpdateSlotPacket;
import sunsetsatellite.retrostorage.packet.terminal.fluid.FluidTerminalContentsPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FluidTerminalScreenHandler extends ScreenHandler {

    private final FluidTerminalBlockEntity tile;
    public final ArrayList<Vec2i> virtualSlots = new ArrayList<>();
    public PlayerInventory playerInv;
    public List<ItemStack> networkStacks = new ArrayList<>();

    public FluidTerminalScreenHandler(PlayerInventory playerInv, FluidTerminalBlockEntity tile) {
        this.tile = tile;
        this.playerInv = playerInv;

        if (tile.getController() != null) {
            this.networkStacks = tile.getController().getAllFluids().stream().map(RetroStorage::f2i).toList();
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
            ((ServerPlayerEntity) playerInv.player).networkHandler.sendPacket(new FluidTerminalContentsPacket(networkStacks));
        }
    }

    public void handleTerminalInteraction(String searchQuery, int slotId, int vSlotId, int page, int mouseButton, boolean shift) {
        sendContentUpdates();
        NetworkController con = tile.getController();
        if (con != null) {
            if (vSlotId == -1) {
                return;
            }
            int id = vSlotId + (page * 54);
            getFilteredStacks(searchQuery);
            if (mouseButton == 0) {
                ItemStack heldItemStack = playerInv.getCursorStack();
                if (heldItemStack != null) {
                    if (id >= networkStacks.size()) return;
                    ItemStack stack = networkStacks.get(id);
                    if (stack == null) return;
                    if (!(stack.getItem() instanceof BlockItem)) return;
                    Block block = ((BlockItem) stack.getItem()).getBlock();
                    if (block == null) return;
                    if (heldItemStack.getItem() instanceof FluidBucket bucket) {
                        if (bucket.getFluid() == null && stack.count >= 1000) {
                            FluidStack fluid = con.removeFluidFromNetwork(block.id, 1000);
                            Item fullBucketItem = bucket.getFullBucketItem(fluid.fluid);
                            playerInv.setCursorStack(new ItemStack(fullBucketItem));
                            if (Catalyst.serverEnv()) {
                                ServerPlayerEntity player = (ServerPlayerEntity) playerInv.player;
                                player.networkHandler.sendPacket(new UpdateSlotPacket(-1, playerInv.getCursorStack()));
                            }
                            return;
                        }
                    }
                    FluidHandlerItemCapability cap = CapabilityHelper.getCapability(heldItemStack, FluidHandlerItemCapability.class);
                    if (cap != null) {
                        if (cap.canExtractFluid()) {
                            int amount = cap.getRemainingFluidCapacity(0);
                            FluidStack fluid = con.removeFluidFromNetwork(block.id, amount);
                            FluidStack remainder = cap.insertFluid(fluid, 0);
                            if (remainder != null && remainder.amount <= 0) remainder = null;
                            if (remainder != null) {
                                con.addFluidToNetwork(remainder);
                            }
                            if (Catalyst.serverEnv()) {
                                ServerPlayerEntity player = (ServerPlayerEntity) playerInv.player;
                                player.networkHandler.sendPacket(new UpdateSlotPacket(-1, playerInv.getCursorStack()));
                            }
                        }
                    }
                }
            } else if (mouseButton == 1) {
                ItemStack heldItemStack = playerInv.getCursorStack();
                if (heldItemStack == null) return;
                if (heldItemStack.getItem() instanceof FluidBucket bucket) {
                    Fluid fluid = bucket.getFluid();
                    if (fluid == null) return;
                    if (con.getFluidCapacity() - con.getFluidAmount() >= 1000) {
                        Optional<FluidStack> fluidOptional = Optional.ofNullable(con.addFluidToNetwork(new FluidStack(fluid, 1000)));
                        if (fluidOptional.isEmpty()) {
                            playerInv.setCursorStack(new ItemStack(bucket.getEmptyBucketItem()));
                        } else {
                            con.removeFluidFromNetwork(fluidOptional.get().fluid.getFlowingBlock().id, fluidOptional.get().amount);
                        }
                        return;
                    }
                }
                FluidHandlerItemCapability cap = CapabilityHelper.getCapability(heldItemStack, FluidHandlerItemCapability.class);
                if (cap != null) {
                    if (cap.canInsertFluid()) {
                        int amount = cap.getRemainingFluidCapacity(0);
                        if (amount > 0) {
                            FluidStack extracted = cap.extractFluid(0, amount);
                            if (extracted != null) {
                                Optional.ofNullable(con.addFluidToNetwork(extracted)).ifPresent((S) -> cap.insertFluid(S, 0));
                            }
                            if (Catalyst.serverEnv()) {
                                ServerPlayerEntity player = (ServerPlayerEntity) playerInv.player;
                                player.networkHandler.sendPacket(new UpdateSlotPacket(-1, playerInv.getCursorStack()));
                            }
                        }
                    }
                }
            }
        }
    }

    public void getFilteredStacks(String searchQuery) {
        if (tile.getController() == null) return;
        List<ItemStack> stacks = tile.getController().getAllFluids().stream().map(RetroStorage::f2i).toList();
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
