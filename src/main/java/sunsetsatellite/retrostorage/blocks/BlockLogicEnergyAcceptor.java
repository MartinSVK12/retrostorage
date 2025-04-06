package sunsetsatellite.retrostorage.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.network.NetworkComponent;
import sunsetsatellite.catalyst.core.util.network.NetworkType;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;
import sunsetsatellite.retrostorage.tiles.TileEntityEnergyAcceptor;

import java.util.function.Supplier;

public class BlockLogicEnergyAcceptor extends BlockLogic implements NetworkComponent {

    public final String guiId;

    public BlockLogicEnergyAcceptor(Block<?> block, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, Material.stone);
        this.guiId = guiId;
        block.withEntity(tileEntitySupplier);
    }

    public boolean onBlockRightClicked(World world, int i, int j, int k, Player entityplayer, Side side, double xHit, double yHit) {
        if (world.isClientSide) {
            return true;
        } else {
            TileEntityEnergyAcceptor tile = (TileEntityEnergyAcceptor) world.getTileEntity(i, j, k);
            if (tile != null && guiId != null) {
                Catalyst.displayGui(entityplayer, tile, RetroStorage.key("gui/"+guiId));
            }
            return true;
        }
    }

    @Override
    public NetworkType getType() {
        return NetworkType.CATALYST_ENERGY;
    }
}
