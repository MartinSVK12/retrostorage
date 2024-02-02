package net.sunsetsatellite.retrostorage.items;

import net.minecraft.src.ItemBlock;

public class ItemCableBlock extends ItemBlock {
    public ItemCableBlock(int id) {
        super(id);
        setIconIndex(24);
    }

    @Override
    public String getTextureFile() {
        return "/assets/retrostorage/items.png";
    }
}
