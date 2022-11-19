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
                return "tile.digitalController";
            case 1:
                return "tile.digitalTerminal";
            case 2:
                return "tile.discDrive";
            case 3:
                return "tile.assembler";
            case 4:
                return "tile.importer";
            case 5:
                return "tile.exporter";
            case 6:
                return "tile.requestTerminal";
            case 7:
                return "tile.interface";
            case 8:
                return "tile.recipeEncoder";
            case 9:
                return "tile.advInterface";
            case 10:
                return "tile.processProgrammer";
            case 11:
                return "tile.wirelessLink";
        }
        return null;
    }

}
