package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;

import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.menus.MenuImporter;
import sunsetsatellite.retrostorage.menus.MenuSubinterface;
import sunsetsatellite.retrostorage.tiles.TileEntityImporter;
import sunsetsatellite.retrostorage.tiles.TileEntitySubinterface;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenSubinterface extends ScreenContainerAbstract {

    public ScreenSubinterface(ContainerInventory inventoryplayer, TileEntitySubinterface tile) {
        super(new MenuSubinterface(inventoryplayer, tile));
        this.tile = tile;
    }

    public void init() {
        super.init();
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 60), 20, 20, "-"));
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 60), 20, 20, "+"));
    }

    protected void drawGuiContainerForegroundLayer() {
        drawStringNoShadow(fontRenderer,"Subinterface", 56, 6, 0x404040);
        drawStringNoShadow(fontRenderer,"ID: " + tile.id, 16, 50, 0x404040);
        drawStringNoShadow(fontRenderer,"Inventory", 8, (ySize - 95) + 2, 0x404040);
		if(tile.workingTile != null && tile.workingTile instanceof TileEntity){
			Vec3i pos = new Vec3i(((TileEntity) tile.workingTile).tilePos);
			drawStringNoShadow(fontRenderer,tile.workingTile.getClass().getSimpleName().replace("TileEntity","")+" at "+pos,0,-10,0xFFFFFF);
		} else {
			drawStringNoShadow(fontRenderer,"No machine connected!",0,-10,0xFFFFFF);
		}
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/subinterface.png");
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
			if(tile.id == 1){
				tile.id = -1;
			} else {
				tile.id--;
			}
        }
        if (button.id == 1) {
			if(tile.id == -1){
				tile.id = 1;
			} else {
            	tile.id++;
			}
        }

        if(EnvironmentHelper.isMultiplayerClient()){
            NetworkHandler.sendToServer(new PacketScreenAction(button.id, 0, 0, tile.getPosition(), tile.getClass()));
        }
    }

    public TileEntitySubinterface tile;
}
