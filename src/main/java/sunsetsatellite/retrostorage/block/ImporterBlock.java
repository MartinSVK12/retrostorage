package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;
import sunsetsatellite.retrostorage.block.entity.ImporterBlockEntity;

import java.util.function.Supplier;

public class ImporterBlock extends NetworkDeviceBlock {
    public ImporterBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(identifier, blockEntityFactory, guiId);
    }

    @Override
    public void neighborUpdate(World world, int x, int y, int z, int id) {
        super.neighborUpdate(world, x, y, z, id);
        ImporterBlockEntity blockEntity = (ImporterBlockEntity) world.getBlockEntity(x, y, z);
        if (blockEntity != null) {
            blockEntity.enabled = !world.isPowered(x, y, z);
        }
    }
}
