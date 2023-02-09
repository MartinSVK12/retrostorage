package sunsetsatellite.retrostorage.items;

import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import sunsetsatellite.retrostorage.RetroStorage;
import turniplabs.halplibe.helper.TextureHelper;

public class ItemBlockCable extends ItemBlock {
    public ItemBlockCable(int i) {
        super(i);
    }

    public Item setIconCoord(int[] coords) {
        return super.setIconCoord(coords[0],coords[1]);
    }
}
