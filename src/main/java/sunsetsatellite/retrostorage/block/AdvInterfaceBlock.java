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
import sunsetsatellite.retrostorage.block.entity.AdvInterfaceBlockEntity;
import sunsetsatellite.retrostorage.block.entity.AssemblerBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.AdvInterfaceScreenHandler;
import sunsetsatellite.retrostorage.screen.handler.AssemblerScreenHandler;
import sunsetsatellite.retrostorage.util.InventoryWrapper;

public class AdvInterfaceBlock extends NetworkDeviceBlock {
    public AdvInterfaceBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new AdvInterfaceBlockEntity();
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
        GuiHelper.openGUI(player,Identifier.of("retrostorage:open_adv_interface"), (Inventory) blockEntity,new AdvInterfaceScreenHandler(player.inventory, (AdvInterfaceBlockEntity) blockEntity));
        return true;
    }
}
