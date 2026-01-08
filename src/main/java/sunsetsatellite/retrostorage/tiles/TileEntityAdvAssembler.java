package sunsetsatellite.retrostorage.tiles;

import net.minecraft.core.item.ItemStack;

public class TileEntityAdvAssembler extends TileEntityAssembler {

    public TileEntityAdvAssembler() {
        super();
        contents = new ItemStack[27];
        advanced = true;
    }
}
