package net.sunsetsatellite.retrostorage;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;

public class ItemDigitalMachineBlock extends ItemBlock {
    public ItemDigitalMachineBlock(int i1) {
        super(i1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    public int getPlacedBlockMetadata(int i) {
        return i;
    }
    public String getItemNameIS(ItemStack itemstack) {
        int meta = itemstack.getItemDamage();
        switch(meta) {
            case 0:
                return StringTranslate.getInstance().translateNamedKey("tile.digitalController");
            case 1:
                return StringTranslate.getInstance().translateNamedKey("tile.digitalTerminal");
            case 2:
                return StringTranslate.getInstance().translateNamedKey("tile.discDrive");
            case 3:
                return StringTranslate.getInstance().translateNamedKey("tile.assembler");
            case 4:
                return StringTranslate.getInstance().translateNamedKey("tile.importer");
            case 5:
                return StringTranslate.getInstance().translateNamedKey("tile.exporter");
            case 6:
                return StringTranslate.getInstance().translateNamedKey("tile.requestTerminal");
            case 7:
                return StringTranslate.getInstance().translateNamedKey("tile.interface");
            case 8:
                return StringTranslate.getInstance().translateNamedKey("tile.recipeEncoder");
        }
        return null;
    }

}
