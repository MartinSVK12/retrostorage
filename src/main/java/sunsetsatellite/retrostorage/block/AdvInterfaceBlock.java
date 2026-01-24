package sunsetsatellite.retrostorage.block;

import net.danygames2014.nyalib.block.DropInventoryOnBreak;
import net.minecraft.block.entity.BlockEntity;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;

import java.util.function.Supplier;

public class AdvInterfaceBlock extends NetworkDeviceBlock implements DropInventoryOnBreak {
    public AdvInterfaceBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(identifier, blockEntityFactory, guiId);
    }
}
