package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.network.NetworkComponent;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;

import java.util.function.Supplier;

public abstract class BlockLogicNetworkDevice extends BlockLogicRotatable implements NetworkComponent {

    public final String guiId;

    public BlockLogicNetworkDevice(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, Materials.STONE);
        this.guiId = guiId;
        block.withEntity(tileEntitySupplier);
    }

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (world.isClientSide) {
			return true;
		} else {
			TileEntity tile = world.getTileEntity(tilePos);
			if (tile != null && guiId != null) {
				Catalyst.displayGui(player, tile, RetroStorage.key("gui/"+guiId));
				return true;
			}
			return false;
		}
	}

    @Override
    public NetworkType getType() {
        return NetworkType.RES_NETWORK;
    }

}
