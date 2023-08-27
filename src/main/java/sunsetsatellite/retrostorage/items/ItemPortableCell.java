package sunsetsatellite.retrostorage.items;





import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.containers.ContainerPlayerExtra;
import sunsetsatellite.retrostorage.gui.GuiPlayerExtra;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;
import sunsetsatellite.retrostorage.util.DiscManipulator;
import sunsetsatellite.retrostorage.util.InventoryPortable;
import sunsetsatellite.sunsetutils.util.ICustomDescription;

public class ItemPortableCell extends Item implements ICustomDescription {
    public ItemPortableCell(int i) {
        super(i);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        InventoryPortable inventoryPortable = new InventoryPortable("Portable Cell",itemstack);
        ((IOpenGUI)entityplayer).displayGUI(new GuiPlayerExtra(entityplayer,new ContainerPlayerExtra(entityplayer.inventory,inventoryPortable)));
        DiscManipulator.loadDisc(itemstack,inventoryPortable);
        return super.onItemUse(itemstack, entityplayer, world, blockX, blockY, blockZ, side, xPlaced, yPlaced);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        InventoryPortable inventoryPortable = new InventoryPortable("Portable Cell",itemstack);
        ((IOpenGUI)entityplayer).displayGUI(new GuiPlayerExtra(entityplayer,new ContainerPlayerExtra(entityplayer.inventory,inventoryPortable)));
        DiscManipulator.loadDisc(itemstack,inventoryPortable);
        return super.onItemRightClick(itemstack, world, entityplayer);
    }

    @Override
    public String getDescription(ItemStack itemStack) {
        return TextFormatting.MAGENTA+""+itemStack.tag.getCompound("disc").getValues().size()+" entries out of 35";
    }
}
