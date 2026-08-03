package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;

import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.retrostorage.menus.MenuImporter;
import sunsetsatellite.retrostorage.tiles.TileEntityImporter;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenImporter extends ScreenContainerAbstract {

    public ScreenImporter(ContainerInventory inventoryplayer, TileEntityImporter tileEntityImporter) {
        super(new MenuImporter(inventoryplayer, tileEntityImporter));
        tile = tileEntityImporter;
    }

    public void init() {
        super.init();
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 60), 20, 20, "-"));
        buttons.add(new ButtonElement(2, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 30), 20, 20, tile.isWhitelist ? "W" : "B"));
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 60), 20, 20, "+"));
    }

    protected void drawGuiContainerForegroundLayer() {
        drawStringNoShadow(fontRenderer,"Item Importer", 56, 6, 0x404040);
        drawStringNoShadow(fontRenderer,"Slot: " + tile.slot, 16, 50, 0x404040);
        drawStringNoShadow(fontRenderer,"Inventory", 8, (ySize - 95) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/gui/trap.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
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
            tile.isWhitelist = !tile.isWhitelist;
            button.displayString = tile.isWhitelist ? "W" : "B";
        }

        if(EnvironmentHelper.isMultiplayerClient()){
            NetworkHandler.sendToServer(new PacketScreenAction(button.id, 0, 0, tile.getPosition(), tile.getClass()));
        }
    }

    public TileEntityImporter tile;
}
