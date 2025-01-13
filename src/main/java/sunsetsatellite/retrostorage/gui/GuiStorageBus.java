package sunsetsatellite.retrostorage.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Vec2i;
import sunsetsatellite.retrostorage.containers.ContainerStorageBus;
import sunsetsatellite.retrostorage.interfaces.mixins.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.tiles.TileEntityStorageBus;
import sunsetsatellite.retrostorage.util.GuiRenderDigitalItem;
import sunsetsatellite.retrostorage.util.INetworkController;

import java.util.ArrayList;

public class GuiStorageBus extends GuiContainer implements IExtendedScreenDraw {

    public final TileEntityStorageBus tile;
    public final GuiRenderDigitalItem renderDigitalItem = new GuiRenderDigitalItem(Minecraft.getMinecraft(this));
    public final GuiTooltip tooltip = new GuiTooltip(Minecraft.getMinecraft(this));
    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public final InventoryPlayer inventoryPlayer;

    public GuiStorageBus(InventoryPlayer inventoryplayer, TileEntityStorageBus tile) {
        super(new ContainerStorageBus(inventoryplayer, tile));
        ySize = 220;
        this.tile = tile;
        this.inventoryPlayer = inventoryplayer;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                slots.add(new Vec2i(x,y));
            }
        }
    }

    public void init() {
        super.init();
        controlList.add(new GuiButton(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 5), 20, 20, "-"));
        controlList.add(new GuiButton(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 5), 20, 20, "+"));
    }

    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            tile.setPriority(tile.getPriority() - 1);
        }
        if (guibutton.id == 1) {
            tile.setPriority(tile.getPriority() + 1);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = mc.renderEngine.getTexture("/assets/retrostorage/textures/gui/digital_terminal.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString("Storage Bus", 56, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
        fontRenderer.drawString("Priority: " + tile.getPriority(), 63, 93, 0x404040);
        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (tile.getAmount() >= tile.getItemCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                fontRenderer.drawCenteredString(tile.getStackAmount() + "/" + tile.getStackCapacity(), 90, 112, color);
            }
        }
        fontRenderer.drawCenteredString("Filtering not yet available :(", 88, 45, 0xFFFFFFFF);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {

    }
}
