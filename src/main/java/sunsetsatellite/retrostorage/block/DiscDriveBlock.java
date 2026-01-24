package sunsetsatellite.retrostorage.block;

import net.danygames2014.nyalib.block.DropInventoryOnBreak;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import sunsetsatellite.catalyst.core.util.io.ItemStackList;
import sunsetsatellite.retrostorage.block.base.NetworkDeviceBlock;
import sunsetsatellite.retrostorage.block.entity.DiscDriveBlockEntity;

import java.util.ArrayList;
import java.util.function.Supplier;

public class DiscDriveBlock extends NetworkDeviceBlock implements DropInventoryOnBreak {
    public DiscDriveBlock(String identifier, Supplier<? extends BlockEntity> blockEntityFactory, String guiId) {
        super(identifier, blockEntityFactory, guiId);
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        DiscDriveBlockEntity discDrive = (DiscDriveBlockEntity) world.getBlockEntity(x, y, z);
        ArrayList<ItemStack> stacks = new ArrayList<>(discDrive.discsUsed);
        new ItemStackList(stacks).ejectAll(world, x, y, z);
        super.onBreak(world, x, y, z);
    }
}
