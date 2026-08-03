package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;

import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec2i;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.retrostorage.menus.MenuStorageBus;
import sunsetsatellite.retrostorage.tiles.TileEntityStorageBus;
import sunsetsatellite.retrostorage.util.DigitalItemElement;
import sunsetsatellite.retrostorage.api.INetworkController;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;

public class ScreenStorageBus extends ScreenContainerAbstract implements IExtendedScreenDraw {

    public final TileEntityStorageBus tile;
    public final DigitalItemElement renderDigitalItem = new DigitalItemElement(Minecraft.getMinecraft());
    public final TooltipElement tooltip = new TooltipElement(Minecraft.getMinecraft());
    public final ArrayList<Vec2i> slots = new ArrayList<>();
    public final ContainerInventory inventoryPlayer;

    public ScreenStorageBus(ContainerInventory inventoryplayer, TileEntityStorageBus tile) {
        super(new MenuStorageBus(inventoryplayer, tile));
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
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 + 50), Math.round((float) height / 2 - 5), 20, 20, "-"));
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 5), 20, 20, "+"));
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 0) {
            tile.setPriority(tile.getPriority() - 1);
        }
        if (guibutton.id == 1) {
            tile.setPriority(tile.getPriority() + 1);
        }

        if(EnvironmentHelper.isMultiplayerClient()){
            NetworkHandler.sendToServer(new PacketScreenAction(guibutton.id,0,0,tile.getPosition(), tile.getClass()));
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/storage_bus.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void drawGuiContainerForegroundLayer() {
        drawStringNoShadow(fontRenderer,"Storage Bus", 56, 6, 0x404040);
        drawStringNoShadow(fontRenderer,"Inventory", 8, (ySize - 95) + 2, 0x404040);
        drawStringNoShadow(fontRenderer,"Priority: " + tile.getPriority(), 63, 93, 0x404040);
        if(tile.network != null) {
            INetworkController controller = tile.getController();
            if (controller != null) {
                int color = 0xFFFFFF;
                if (tile.getAmount() >= tile.getItemCapacity() * 0.9) {
                    color = 0xFF4040;
                }
                drawStringCenteredShadow(fontRenderer,tile.getStackAmount() + "/" + tile.getStackCapacity(), 90, 112, color);
            }
        }
        drawStringCenteredShadow(fontRenderer,"Filtering not yet available :(", 88, 45, 0xFFFFFFFF);
        if(tile.wrapper.connected != null){
            drawStringCenteredShadow(fontRenderer,TextFormatting.LIME+"Connected to "+tile.wrapper.connected.getClass().getSimpleName().replace("TileEntity","")+"!", 88, 65, 0xFFFFFFFF);
        }
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {

    }
}
