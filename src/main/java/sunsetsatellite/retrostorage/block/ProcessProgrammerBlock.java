package sunsetsatellite.retrostorage.block;

import net.danygames2014.nyalib.block.DropInventoryOnBreak;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.model.FullyRotatableBlockWithEntity;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.base.RotatableBlockWithEntity;

import java.util.function.Supplier;

public class ProcessProgrammerBlock extends FullyRotatableBlockWithEntity implements DropInventoryOnBreak {

    private final Supplier<? extends BlockEntity> blockEntityFactory;
    private final String guiId;

    public ProcessProgrammerBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(RetroStorage.NAMESPACE.id(identifier), Material.STONE);
        this.blockEntityFactory = blockEntityFactory;
        this.guiId = guiId;
    }

    @Override
    public BlockEntity createBlockEntity() {
        return blockEntityFactory.get();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (world.isRemote) return super.onUse(world, x, y, z, player);
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        Catalyst.displayGui(player, blockEntity, RetroStorage.key("gui/" + guiId));
        return true;
    }

}
