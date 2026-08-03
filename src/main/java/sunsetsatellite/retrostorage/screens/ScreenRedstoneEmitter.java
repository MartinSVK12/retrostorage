package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;

import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.retrostorage.menus.MenuRedstoneEmitter;
import sunsetsatellite.retrostorage.tiles.TileEntityAdvInterface;
import sunsetsatellite.retrostorage.tiles.TileEntityAssembler;
import sunsetsatellite.retrostorage.tiles.TileEntityRedstoneEmitter;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenRedstoneEmitter extends ScreenContainerAbstract {

    public ScreenRedstoneEmitter(ContainerInventory inventoryplayer, TileEntityRedstoneEmitter tileEntityRedstoneEmitter) {
        super(new MenuRedstoneEmitter(inventoryplayer, tileEntityRedstoneEmitter));
        tile = tileEntityRedstoneEmitter;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture l = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/emittergui.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(l);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    protected void drawGuiContainerForegroundLayer() {
        drawStringNoShadow(fontRenderer,"Redstone Emitter", 45, 6, 0x404040);
        drawStringNoShadow(fontRenderer,"Inventory", 8, (ySize - 96) + 2, 0x404040);
        drawStringNoShadow(fontRenderer,String.valueOf(tile.amount), 120, 40, 0x404040);
        if (tile.connectedTile instanceof TileEntityAssembler) {
            drawStringNoShadow(fontRenderer,"ASM", 9, 6, 0x404040);
            drawStringNoShadow(fontRenderer,String.valueOf(tile.asmSlot), 10, 40, 0x404040);
        } else if (tile.connectedTile instanceof TileEntityAdvInterface) {
            drawStringNoShadow(fontRenderer,"INT", 9, 6, 0x404040);
            drawStringNoShadow(fontRenderer,String.valueOf(tile.asmSlot), 10, 40, 0x404040);
        }
    }

    public void init() {
        super.init();
        ButtonElement guibutton = new ButtonElement(0, Math.round(width / 2f - 10), Math.round(height / 2f - 50), 20, 20, "=");
        buttons.add(guibutton);
        buttons.add(new ButtonElement(1, Math.round(width / 2f + 30), Math.round(height / 2f - 65), 20, 20, "+"));
        buttons.add(new ButtonElement(2, Math.round(width / 2f + 30), Math.round(height / 2f - 35), 20, 20, "-"));
        if (tile.connectedTile instanceof TileEntityAssembler || tile.connectedTile instanceof TileEntityAdvInterface) {
            buttons.add(new ButtonElement(5, Math.round(width / 2f - 80), Math.round(height / 2f - 65), 20, 20, "+"));
            buttons.add(new ButtonElement(6, Math.round(width / 2f - 80), Math.round(height / 2f - 35), 20, 20, "-"));
        }
        buttons.add(new ButtonElement(3, Math.round(width / 2f + 60), Math.round(height / 2f) - 75, 20, 20, tile.useMeta ? "M" : "!M"));
        //buttons.add(new ButtonElement(4, Math.round(width / 2 + 60) , Math.round(height / 2) - 55, 20, 20, "D"));
        switch (tile.mode) {
            case 0:
                guibutton.displayString = "=";
                break;
            case 1:
                guibutton.displayString = "!=";
                break;
            case 2:
                guibutton.displayString = ">";
                break;
            case 3:
                guibutton.displayString = "<";
                break;
            case 4:
                guibutton.displayString = ">=";
                break;
            case 5:
                guibutton.displayString = "<=";
                break;
            case 6:
                tile.mode = 0;
                guibutton.displayString = "=";
                break;
        }
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 2) {
            if (tile.amount > 0)
                tile.amount--;
        }
        if (guibutton.id == 1) {
            tile.amount++;
        }
        if (guibutton.id == 3) {
            tile.useMeta = !tile.useMeta;
            guibutton.displayString = tile.useMeta ? "M" : "!M";
        }
        if (guibutton.id == 4) {
            tile.useData = !tile.useData;
            guibutton.displayString = tile.useData ? "D" : "!D";
        }
        if (guibutton.id == 5) {
            if(tile.connectedTile instanceof TileEntityAssembler){
                TileEntityAssembler asm = (TileEntityAssembler) tile.connectedTile;
                if(asm.advanced){
                    if (tile.asmSlot < 26) {
                        tile.asmSlot++;
                    }
                } else {
                    if (tile.asmSlot < 8) {
                        tile.asmSlot++;
                    }
                }
            }
        }
        if (guibutton.id == 6) {
            if (tile.asmSlot > 0) {
                tile.asmSlot--;
            }
        }
        if (guibutton.id == 0) {
            tile.mode++;
            switch (tile.mode) {
                case 0:
                    guibutton.displayString = "=";
                    break;
                case 1:
                    guibutton.displayString = "!=";
                    break;
                case 2:
                    guibutton.displayString = ">";
                    break;
                case 3:
                    guibutton.displayString = "<";
                    break;
                case 4:
                    guibutton.displayString = ">=";
                    break;
                case 5:
                    guibutton.displayString = "<=";
                    break;
                case 6:
                    tile.mode = 0;
                    guibutton.displayString = "=";
                    break;
            }
        }

        if(EnvironmentHelper.isMultiplayerClient()){
            NetworkHandler.sendToServer(new PacketScreenAction(guibutton.id, 0, 0, tile.getPosition(), tile.getClass()));
        }
    }

    private final TileEntityRedstoneEmitter tile;
}
