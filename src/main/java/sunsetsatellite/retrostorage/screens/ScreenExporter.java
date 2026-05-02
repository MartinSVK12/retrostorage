package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.ButtonElement;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.retrostorage.menus.MenuExporter;
import sunsetsatellite.retrostorage.tiles.TileEntityExporter;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenExporter extends ScreenContainerAbstract {

    public ScreenExporter(ContainerInventory inventoryplayer, TileEntityExporter tileentityexporter) {
        super(new MenuExporter(inventoryplayer, tileentityexporter));
        tile = tileentityexporter;
    }

    public void init() {
        super.init();
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 60), 20, 20, "-"));
        buttons.add(new ButtonElement(2, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 30), 20, 20, tile.isStocking ? "S" : "F"));
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 60), 20, 20, "+"));
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Item Exporter", 56, 6, 0x404040);
        font.drawString("Slot: " + tile.slot, 16, 50, 0x404040);
        font.drawString("Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/gui/trap.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        super.buttonClicked(button);
        if (!button.enabled) {
            return;
        }
        if (button.id == 0) {
            if (tile.slot >= 0) {
                tile.slot--;
            }
        }
        if (button.id == 1) {
            tile.slot++;
        }
        if (button.id == 2) {
            tile.isStocking = !tile.isStocking;
            button.displayString = tile.isStocking ? "S" : "F";
        }

        if(EnvironmentHelper.isClientWorld()){
            NetworkHandler.sendToServer(new PacketScreenAction(button.id, 0, 0, tile.getPosition(), tile.getClass()));
        }
    }

    public TileEntityExporter tile;
}
