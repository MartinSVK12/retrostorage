package sunsetsatellite.retrostorage.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.gui.GuiFluidImporter;
import sunsetsatellite.retrostorage.gui.GuiImporter;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.tiles.TileEntityFluidImporter;
import sunsetsatellite.retrostorage.tiles.TileEntityImporter;

public class BlockFluidImporter extends BlockNetworkDevice {
    public BlockFluidImporter(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityFluidImporter tile = (TileEntityFluidImporter) world.getBlockTileEntity(i, j, k);
            if (tile != null) {
                ((IOpenGUI) entityplayer).displayGUI(new GuiFluidImporter(entityplayer.inventory,tile));
            }
            return true;
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityFluidImporter();
    }
}
