package sunsetsatellite.retrostorage.block;

import net.danygames2014.nyalib.block.DropInventoryOnBreak;
import net.minecraft.block.entity.BlockEntity;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;

import java.util.function.Supplier;

public class AssemblerBlock extends NetworkDeviceBlock implements DropInventoryOnBreak {

    public boolean advanced = false;

    public AssemblerBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId, boolean advanced) {
        super(identifier, blockEntityFactory, guiId);
        this.advanced = advanced;
    }
}
