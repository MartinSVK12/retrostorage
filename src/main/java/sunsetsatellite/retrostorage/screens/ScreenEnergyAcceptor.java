package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.lang.I18n;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Color;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.CatalystEnergy;
import sunsetsatellite.retrostorage.menus.MenuEnergyAcceptor;
import sunsetsatellite.retrostorage.tiles.TileEntityEnergyAcceptor;

public class ScreenEnergyAcceptor extends ScreenContainerAbstract {

    public ScreenEnergyAcceptor(ContainerInventory inventoryPlayer, TileEntityEnergyAcceptor tile) {
        super(new MenuEnergyAcceptor(inventoryPlayer, tile));
        this.tile = tile;
    }

    public String name = "Energy Acceptor";
    public TileEntityEnergyAcceptor tile;

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/acceptor.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        int color;
        //1 (red, empty) -> 0.65 (green, full)
        double color_mapped = Catalyst.map((float) tile.getEnergy() / (float) tile.getCapacity(), 0, 1, 1, 0.65);
        double x_mapped = Catalyst.map((float) tile.getEnergy() / (float) tile.getCapacity(), 0, 1, 0, 15);
        Color c = new Color();
        byte[] colorBytes = Catalyst.HSBtoRGB((float) color_mapped, 1.0F, 1.0F);
        c.setRGB(colorBytes[0],colorBytes[1],colorBytes[2]);
        color = c.getAlpha() << 24 | c.getRed() << 16 | c.getBlue() << 8 | c.getGreen();
        drawRectWidthHeight(x + 80, y + 40, (int) x_mapped, 7, color);
        GL11.glEnable(3553);
    }

    @Override
    public void render(int x, int y, float renderPartialTicks) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        super.render(x, y, renderPartialTicks);
        I18n trans = I18n.getInstance();
        StringBuilder text = new StringBuilder();
        if (x > i + 80 && x < i + 94 && y > j + 40 && y < j + 46) {
            text.append(CatalystEnergy.ENERGY_NAME).append(": ").append(this.tile.getEnergy()).append(" ").append(CatalystEnergy.ENERGY_SUFFIX).append("/").append(this.tile.getCapacity()).append(" ").append(CatalystEnergy.ENERGY_SUFFIX);
            TooltipElement tooltip = new TooltipElement(this.mc);
            tooltip.render(text.toString(), x, y, 8, -8);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        font.drawString(name, 48, 6, 0xFF404040);
    }


    @Override
    public void init() {
        super.init();
    }
}
