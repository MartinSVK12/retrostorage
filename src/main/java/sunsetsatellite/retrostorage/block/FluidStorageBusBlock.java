package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;

import java.util.function.Supplier;

public class FluidStorageBusBlock extends NetworkDeviceBlock {
    public FluidStorageBusBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(identifier, blockEntityFactory, guiId);
    }
}
