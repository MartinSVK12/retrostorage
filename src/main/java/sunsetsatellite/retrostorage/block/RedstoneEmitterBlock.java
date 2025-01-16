package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.network.NetworkComponentBlock;
import net.teamterminus.machineessentials.network.NetworkType;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.entity.ExporterBlockEntity;
import sunsetsatellite.retrostorage.block.entity.RedstoneEmitterBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.ExporterScreenHandler;
import sunsetsatellite.retrostorage.screen.handler.RedstoneEmitterScreenHandler;
import sunsetsatellite.retrostorage.util.InventoryWrapper;

public class RedstoneEmitterBlock extends NetworkDeviceBlock {
    public RedstoneEmitterBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new RedstoneEmitterBlockEntity();
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
        GuiHelper.openGUI(player,Identifier.of("retrostorage:open_redstone_emitter"), (Inventory) blockEntity,new RedstoneEmitterScreenHandler(player.inventory, (RedstoneEmitterBlockEntity) blockEntity));
        return true;
    }

    @Override
    public boolean canEmitRedstonePower() {
        return true;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isEmittingRedstonePowerInDirection(BlockView blockView, int x, int y, int z, int direction) {
        RedstoneEmitterBlockEntity tile = (RedstoneEmitterBlockEntity) blockView.getBlockEntity(x, y, z);
        return tile != null && tile.isActive;
    }

    @Override
    public boolean canTransferPowerInDirection(World world, int x, int y, int z, int direction) {
        RedstoneEmitterBlockEntity tile = (RedstoneEmitterBlockEntity) world.getBlockEntity(x, y, z);
        return tile != null && tile.isActive;
    }
}
