package sunsetsatellite.retrostorage.screen;


import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.AdvInterfaceBlockEntity;
import sunsetsatellite.retrostorage.block.entity.AssemblerBlockEntity;
import sunsetsatellite.retrostorage.block.entity.RedstoneEmitterBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.RedstoneEmitterScreenHandler;

public class RedstoneEmitterScreen extends ReSScreen {

    public RedstoneEmitterScreen(PlayerInventory inventoryplayer, RedstoneEmitterBlockEntity tileEntityRedstoneEmitter) {
        super(new RedstoneEmitterScreenHandler(inventoryplayer, tileEntityRedstoneEmitter));
        tile = tileEntityRedstoneEmitter;
    }

    @Override
    protected void drawBackground(float f) {
        int l = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/emittergui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(l);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }

    protected void drawForeground() {
        textRenderer.draw("Redstone Emitter", 45, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
        textRenderer.draw(String.valueOf(tile.amount), 120, 40, 0x404040);
        if (tile.connectedTile instanceof AssemblerBlockEntity) {
            textRenderer.draw("ASM", 9, 6, 0x404040);
            textRenderer.draw(String.valueOf(tile.asmSlot), 10, 40, 0x404040);
        } else if (tile.connectedTile instanceof AdvInterfaceBlockEntity) {
            textRenderer.draw("INT", 9, 6, 0x404040);
            textRenderer.draw(String.valueOf(tile.asmSlot), 10, 40, 0x404040);
        }
    }

    public void init() {
        super.init();
        ButtonWidget guibutton = new ButtonWidget(0, Math.round(width / 2 - 10), Math.round(height / 2 - 50), 20, 20, "=");
        buttons.add(guibutton);
        buttons.add(new ButtonWidget(1, Math.round(width / 2 + 30), Math.round(height / 2 - 65), 20, 20, "+"));
        buttons.add(new ButtonWidget(2, Math.round(width / 2 + 30), Math.round(height / 2 - 35), 20, 20, "-"));
        if (tile.connectedTile instanceof AssemblerBlockEntity || tile.connectedTile instanceof AdvInterfaceBlockEntity) {
            buttons.add(new ButtonWidget(5, Math.round(width / 2 - 80), Math.round(height / 2 - 65), 20, 20, "+"));
            buttons.add(new ButtonWidget(6, Math.round(width / 2 - 80), Math.round(height / 2 - 35), 20, 20, "-"));
        }
        buttons.add(new ButtonWidget(3, Math.round(width / 2 + 60), Math.round(height / 2) - 75, 20, 20, tile.useMeta ? "M" : "!M"));
        //buttons.add(new ButtonWidget(4, Math.round(width / 2 + 60) , Math.round(height / 2) - 55, 20, 20, "D"));
        switch (tile.mode) {
            case 0:
                guibutton.text = "=";
                break;
            case 1:
                guibutton.text = "!=";
                break;
            case 2:
                guibutton.text = ">";
                break;
            case 3:
                guibutton.text = "<";
                break;
            case 4:
                guibutton.text = ">=";
                break;
            case 5:
                guibutton.text = "<=";
                break;
            case 6:
                tile.mode = 0;
                guibutton.text = "=";
                break;
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
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
            guibutton.text = tile.useMeta ? "M" : "!M";
        }
        if (guibutton.id == 4) {
            tile.useData = !tile.useData;
            guibutton.text = tile.useData ? "D" : "!D";
        }
        if (guibutton.id == 5) {
            if (tile.asmSlot < 8) {
                tile.asmSlot++;
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
                    guibutton.text = "=";
                    break;
                case 1:
                    guibutton.text = "!=";
                    break;
                case 2:
                    guibutton.text = ">";
                    break;
                case 3:
                    guibutton.text = "<";
                    break;
                case 4:
                    guibutton.text = ">=";
                    break;
                case 5:
                    guibutton.text = "<=";
                    break;
                case 6:
                    tile.mode = 0;
                    guibutton.text = "=";
                    break;
            }
        }

    }

    private final RedstoneEmitterBlockEntity tile;
}
