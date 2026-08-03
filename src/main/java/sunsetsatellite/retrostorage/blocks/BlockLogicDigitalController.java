package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityDigitalController;

import java.util.function.Supplier;

public class BlockLogicDigitalController extends BlockLogicNetworkDevice {

    public BlockLogicDigitalController(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, tileEntitySupplier, guiId);
    }

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (world.isClientSide) {
			return true;
		} else {
			TileEntityDigitalController tile = (TileEntityDigitalController) world.getTileEntity(tilePos);
			if (tile != null) {
				if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem().equals(Items.DUST_REDSTONE)) {
					player.inventory.getCurrentItem().stackSize--;
					tile.energy += 20 * 60;
				}
				if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == Blocks.BLOCK_REDSTONE.id()) {
					player.inventory.getCurrentItem().stackSize--;
					tile.energy += 20 * 60 * 9;
				}
				if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == Blocks.BEDROCK.id()) {
					player.inventory.getCurrentItem().stackSize--;
					tile.energy += 20 * 60 * 65535;
				}
				if (tile.network != null) {
					Catalyst.displayGui(player, tile, RetroStorage.key("gui/digital_controller"));
				}
			}
			return true;
		}
	}
}
