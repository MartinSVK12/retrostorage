package sunsetsatellite.retrostorage.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import sunsetsatellite.retrostorage.containers.ContainerPlayerExtra;
import sunsetsatellite.retrostorage.gui.GuiPlayerExtra;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.InventoryPortable;

public class ItemPortableCell extends Item {
    public ItemPortableCell(int i) {
        super(i);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, double heightPlaced) {
        InventoryPortable inventoryPortable = new InventoryPortable("Portable Cell",itemstack);
        ((IOpenGUI)entityplayer).displayGUI(new GuiPlayerExtra(entityplayer,new ContainerPlayerExtra(entityplayer.inventory,inventoryPortable)));
        DiscManipulator.loadDisc(itemstack,inventoryPortable);
        return super.onItemUse(itemstack, entityplayer, world, i, j, k, l, heightPlaced);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        InventoryPortable inventoryPortable = new InventoryPortable("Portable Cell",itemstack);
        ((IOpenGUI)entityplayer).displayGUI(new GuiPlayerExtra(entityplayer,new ContainerPlayerExtra(entityplayer.inventory,inventoryPortable)));
        DiscManipulator.loadDisc(itemstack,inventoryPortable);
        return super.onItemRightClick(itemstack, world, entityplayer);
    }
}
