package net.sunsetsatellite.retrostorage.items;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemDigitalMachineBlock extends ItemBlock {
    public ItemDigitalMachineBlock(int i1) {
        super(i1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
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
                return "tile.recipeEncoder";
            case 8:
                return "tile.advInterface";
            case 9:
                return "tile.processProgrammer";
            case 10:
                return "tile.wirelessLink";
            case 11:
                return "tile.emitter";
        }
        return null;
    }

}
