package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;

import sunsetsatellite.retrostorage.menus.MenuAssembler;
import sunsetsatellite.retrostorage.tiles.TileEntityAssembler;

public class ScreenAssembler extends ScreenContainerAbstract {

    public TileEntityAssembler tile;

    public ScreenAssembler(ContainerInventory inventoryplayer, TileEntityAssembler tile) {
        super(new MenuAssembler(inventoryplayer, tile));
        this.tile = tile;
    }

    protected void drawGuiContainerForegroundLayer() {
        drawStringNoShadow(fontRenderer,tile.advanced ? "Adv. Assembler" : "Assembler", tile.advanced ? 54 : 64, 6, 0x404040);
        drawStringNoShadow(fontRenderer,"Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/disc_container.png");
        if(tile.advanced){
            i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/disc_container_extended.png");
        }
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}
