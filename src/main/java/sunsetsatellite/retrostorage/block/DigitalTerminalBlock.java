package sunsetsatellite.retrostorage.block;

import net.minecraft.block.entity.BlockEntity;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;

import java.util.function.Supplier;

public class DigitalTerminalBlock extends NetworkDeviceBlock {

    public DigitalTerminalBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(identifier, blockEntityFactory, guiId);
    }
}
