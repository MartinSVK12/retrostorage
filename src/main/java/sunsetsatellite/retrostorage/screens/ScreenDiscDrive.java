package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;

import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.retrostorage.menus.MenuDiscDrive;
import sunsetsatellite.retrostorage.tiles.TileEntityDiscDrive;
import sunsetsatellite.retrostorage.util.DigitalItemElement;
import sunsetsatellite.retrostorage.api.INetworkController;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.NetworkHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenDiscDrive extends ScreenContainerAbstract {

    public final DigitalItemElement renderDigitalItem = new DigitalItemElement(Minecraft.getMinecraft());

    public ScreenDiscDrive(ContainerInventory inventoryplayer, TileEntityDiscDrive tileentitydiscdrive) {
        super(new MenuDiscDrive(inventoryplayer, tileentitydiscdrive));
        tile = tileentitydiscdrive;
    }

    protected void drawGuiContainerForegroundLayer() {
        drawStringNoShadow(fontRenderer,"Disc Drive", 60, 6, 0x404040);
        drawStringNoShadow(fontRenderer,"Inventory", 8, (ySize - 96) + 2, 0x404040);
        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (controller.getAmount() >= controller.getItemCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                drawStringCenteredShadow(fontRenderer,controller.getStackAmount() + "/" + controller.getStackCapacity(), 88, 40, color);
            }
        }
        if(!tile.discsUsed.isEmpty()){
            drawStringCenteredShadow(fontRenderer,tile.discsUsed.size()+"/"+tile.maxDiscs+" discs in use.", 88, 20, 0xFFFFFF);
        } else {
            drawStringCenteredShadow(fontRenderer,"No discs in use.", 88, 20, 0xFFFFFF);
        }

        for (int i = 0; i < Math.min(tile.discsUsed.size(),16); i++) {
            ItemStack disc = tile.discsUsed.get(i);
            renderDigitalItem.render(disc, 5 + (10 * i), 55);
        }
    }

    public void init() {
        super.init();
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 50), 20, 20, "-"));
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/discdrivegui.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            if (tile.getItem(1) == null) {
                if(EnvironmentHelper.isMultiplayerClient()){
                    NetworkHandler.sendToServer(new PacketScreenAction(guibutton.id,0,0,tile.getPosition(), tile.getClass()));
                }
                tile.removeLastDisc();
            }
        }
    }

    private final TileEntityDiscDrive tile;
}
