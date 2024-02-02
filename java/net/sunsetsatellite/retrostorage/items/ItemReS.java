package net.sunsetsatellite.retrostorage.items;

import forge.ITextureProvider;
import net.minecraft.src.Item;

public class ItemReS extends Item implements ITextureProvider {
    public ItemReS(int id) {
        super(id);
    }

    @Override
    public String getTextureFile() {
        return "/assets/retrostorage/items.png";
    }
}
