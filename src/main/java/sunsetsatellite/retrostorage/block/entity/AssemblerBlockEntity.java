package sunsetsatellite.retrostorage.block.entity;

import net.danygames2014.nyalib.block.BlockEntityInit;
import net.danygames2014.nyalib.item.block.ManagedItemHandlerWithInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;
import sunsetsatellite.catalyst.core.util.io.FluidStackList;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.catalyst.core.util.recipe.crafting.RecipeEntryCrafting;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.api.Processor;
import sunsetsatellite.retrostorage.block.AssemblerBlock;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;
import sunsetsatellite.retrostorage.event.ReSItems;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.NetworkCraftable;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssemblerBlockEntity extends NetworkDeviceBlockEntity implements ManagedItemHandlerWithInventory, BlockEntityInit, Processor {

    public boolean advanced = false;
    public boolean init = false;

    public AssemblerBlockEntity() {
        for (int i = 0; i < 9; i++) {
            addItemSlot();
        }
    }

    @Override
    public void init(BlockState blockState) {
        advanced = ((AssemblerBlock) blockState.getBlock()).advanced;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return canUse(player);
    }

    @Override
    public String getName() {
        if (advanced) {
            return "container.retrostorage.advAssembler";
        }
        return "container.retrostorage.assembler";
    }

    public ArrayList<RecipeEntryCrafting<?, ItemStack>> getRecipes() {
        ArrayList<RecipeEntryCrafting<?, ItemStack>> recipes = new ArrayList<>();
        for (ItemStack stack : getInventory(null)) {
            if (stack != null && stack.getItem() == ReSItems.recipeDisc) {
                RecipeEntryCrafting<?, ItemStack> recipe = RetroStorage.findRecipeFromNBT(stack.getStationNbt().getCompound("recipe"));
                if (recipe != null) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    @Override
    public List<NetworkCraftable> getCraftables() {
        return getRecipes().stream().map(NetworkCraftable::new).collect(Collectors.toList());
    }

    @Override
    public boolean isInUse() {
        return false;
    }

    @Override
    public void setFocus(ProcessNode node, CraftingTask task) {

    }

    @Override
    public Inventory getConnectedTile() {
        return null;
    }

    @Override
    public ProcessNode getWorkingNode() {
        return null;
    }

    @Override
    public CraftingTask getWorkingTask() {
        return null;
    }

    @Override
    public boolean insertItems(ItemStackList items) {
        return false;
    }

    @Override
    public boolean canInsertItems(ItemStackList items) {
        return false;
    }

    @Override
    public boolean insertFluids(FluidStackList items) {
        return false;
    }

    @Override
    public boolean canInsertFluids(FluidStackList items) {
        return false;
    }
}
