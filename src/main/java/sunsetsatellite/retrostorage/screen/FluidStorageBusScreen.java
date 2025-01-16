package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.util.math.Vec2f;
import net.teamterminus.machineessentials.util.NumberFormat;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.FluidStorageBusBlockEntity;
import sunsetsatellite.retrostorage.interfaces.mixin.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.screen.handler.FluidStorageBusScreenHandler;
import sunsetsatellite.retrostorage.util.NetworkController;
import sunsetsatellite.retrostorage.util.RenderDigitalItem;

import java.util.ArrayList;

public class FluidStorageBusScreen extends ReSScreen implements IExtendedScreenDraw {

    public final FluidStorageBusBlockEntity tile;
    //public final GuiTooltip tooltip = new GuiTooltip(Minecraft.getMinecraft(this));
    public final ArrayList<Vec2f> slots = new ArrayList<>();
    public final PlayerInventory inventoryPlayer;

    public FluidStorageBusScreen(PlayerInventory inventoryplayer, FluidStorageBusBlockEntity tile) {
        super(new FluidStorageBusScreenHandler(inventoryplayer, tile));
        backgroundHeight = 220;
        this.tile = tile;
        this.inventoryPlayer = inventoryplayer;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                int x = 8 + j * 18;
                int y = 18 + i * 18;
                slots.add(new Vec2f(x,y));
            }
        }
    }

    public void init() {
        super.init();
        buttons.add(new ButtonWidget(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 5), 20, 20, "-"));
        buttons.add(new ButtonWidget(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 5), 20, 20, "+"));
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 0) {
            tile.setPriority(tile.getPriority() - 1);
        }
        if (guibutton.id == 1) {
            tile.setPriority(tile.getPriority() + 1);
        }
    }

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/digital_terminal.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }

    protected void drawForeground() {
        textRenderer.draw("Fluid Storage Bus", 45, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
        textRenderer.draw("Priority: " + tile.getPriority(), 63, 93, 0x404040);
        if(tile.network != null) {
            NetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (tile.getFluidAmount() >= tile.getMaxFluidAmount() * 0.9) {
                    color = 0xFF4040;
                }
                drawCenteredString(NumberFormat.format(tile.getFluidStackAmount()) + "/" + NumberFormat.format(tile.getMaxFluidStackSize()), 90, 112, color);

            }
        }
        drawCenteredString("Filtering not yet available :(", 88, 45, 0xFFFFFFFF);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {

    }
}
