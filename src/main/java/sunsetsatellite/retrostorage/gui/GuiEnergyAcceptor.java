package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import sunsetsatellite.catalyst.CatalystEnergy;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyContainer;
import sunsetsatellite.retrostorage.containers.ContainerEnergyAcceptor;
import sunsetsatellite.retrostorage.tiles.TileEntityEnergyAcceptor;

public class GuiEnergyAcceptor extends GuiContainer {

    public GuiEnergyAcceptor(InventoryPlayer inventoryPlayer, TileEntityEnergyAcceptor tile) {
        super(new ContainerEnergyAcceptor(inventoryPlayer,tile));
        this.tile = tile;
    }

    public String name = "Energy Acceptor";
    public TileEntityEnergyContainer tile;

    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/retrostorage/gui/acceptor.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        int color;
        //1 (red, empty) -> 0.65 (green, full)
        double color_mapped = CatalystEnergy.map((float)tile.energy/(float)tile.capacity,0,1,1,0.65);
        double x_mapped = CatalystEnergy.map((float)tile.energy/(float)tile.capacity, 0,1,0,15);
        Color c = new Color();
        c.fromHSB((float) color_mapped,1.0F,1.0F);
        color = c.getAlpha() << 24 | c.getRed() << 16 | c.getBlue() << 8 | c.getGreen();
        drawRectWidthHeight(x+80,y+40, (int) x_mapped,7,color);
        GL11.glEnable(3553);
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        super.drawScreen(x, y, renderPartialTicks);
        I18n trans = I18n.getInstance();
        StringBuilder text = new StringBuilder();
        if (x > i + 80 && x < i + 94 && y > j + 40 && y < j + 46) {
            text.append(CatalystEnergy.ENERGY_NAME).append(": ").append(this.tile.energy).append(" ").append(CatalystEnergy.ENERGY_SUFFIX).append("/").append(this.tile.capacity).append(" ").append(CatalystEnergy.ENERGY_SUFFIX);
            GuiTooltip tooltip = new GuiTooltip(this.mc);
            tooltip.render(text.toString(), x, y, 8, -8);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawString(name, 48, 6, 0xFF404040);
    }


    @Override
    public void init()
    {
        super.init();
    }
}
