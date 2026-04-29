package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.gui.container.ScreenContainer;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.menus.MenuAdvInterface;
import sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;

public class ScreenAdvInterface extends ScreenContainerAbstract {

    public TileEntityAdvInterface tile;

    public ScreenAdvInterface(ContainerInventory inventoryplayer, TileEntityAdvInterface TileEntityAdvInterface) {
        super(new MenuAdvInterface(inventoryplayer, TileEntityAdvInterface));
        this.tile = TileEntityAdvInterface;
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Interface", 64, 6, 0x404040);
        font.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        if(tile.workingTile != null && tile.workingTile instanceof TileEntity){
            Vec3i pos = new Vec3i(((TileEntity) tile.workingTile).x, ((TileEntity) tile.workingTile).y, ((TileEntity) tile.workingTile).z);
            font.drawString(tile.workingTile.getClass().getSimpleName().replace("TileEntity","")+" at "+pos,0,-10,0xFFFFFF);
        }

    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/disc_container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}