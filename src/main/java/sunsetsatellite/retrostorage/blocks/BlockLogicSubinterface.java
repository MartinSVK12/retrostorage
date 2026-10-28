package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BlockLogicSubinterface extends BlockLogicNetworkDevice {

    public BlockLogicSubinterface(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }
}
