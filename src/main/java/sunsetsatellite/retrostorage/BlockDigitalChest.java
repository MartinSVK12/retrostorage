package sunsetsatellite.retrostorage;

import net.minecraft.src.*;

public class BlockDigitalChest extends BlockContainerRotatable {
    public BlockDigitalChest(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDigitalChest();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityDigitalChest tile = (TileEntityDigitalChest)world.getBlockTileEntity(i, j, k);
            //System.out.println(TileEntityDigitalChest);
            if (tile != null) {
                System.out.println("Tile entity alive!");
                ((IOpenGUI) entityplayer).displayGUI(new GuiDigitalChest(entityplayer.inventory,tile));
            }
            return true;
        }
    }
}
