package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.network.NetworkComponentBlock;
import net.teamterminus.machineessentials.network.NetworkType;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.entity.ImporterBlockEntity;
import sunsetsatellite.retrostorage.block.entity.RecipeEncoderBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.ImporterScreenHandler;
import sunsetsatellite.retrostorage.screen.handler.RecipeEncoderScreenHandler;
import sunsetsatellite.retrostorage.util.InventoryWrapper;

public class RecipeEncoderBlock extends RotatableBlockWithEntity {
    public RecipeEncoderBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new RecipeEncoderBlockEntity();
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        InventoryWrapper inv = new InventoryWrapper((Inventory) blockEntity);
        inv.ejectAll(world, x, y, z);
        super.onBreak(world, x, y, z);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        GuiHelper.openGUI(player,Identifier.of("retrostorage:open_recipe_encoder"), (Inventory) blockEntity,new RecipeEncoderScreenHandler(player.inventory, (RecipeEncoderBlockEntity) blockEntity));
        return true;
    }
}
