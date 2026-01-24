package sunsetsatellite.retrostorage.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.mp.ScreenActionPacket;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.block.entity.ProcessProgrammerBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.ProcessProgrammerScreenHandler;
import sunsetsatellite.retrostorage.util.StackType;

import java.util.Objects;

import static sunsetsatellite.retrostorage.RetroStorage.gui;

public class ProcessProgrammerScreen extends FilterScreen {
    private final ProcessProgrammerBlockEntity tile;
    public TextFieldWidget processName;
    public StackType selected = StackType.ITEM;

    public ProcessProgrammerScreen(PlayerInventory playerInv, ProcessProgrammerBlockEntity tile) {
        super(new ProcessProgrammerScreenHandler(playerInv, tile));
        this.tile = tile;
        backgroundHeight = 220;
    }

    @Override
    public void init() {
        super.init();
        processName = new TextFieldWidget(this, textRenderer, Math.round((float) width / 2 - 31), Math.round((float) height / 2 - 92), 100, 20, Objects.equals(tile.processName, "") ? "New Process" : tile.processName);
        buttons.add(new ButtonWidget(0, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 12), 40, 20, "Save"));
        buttons.add(new ButtonWidget(1, Math.round((float) width / 2 + 31), Math.round((float) height / 2 - 12), 40, 20, "Clear"));
        buttons.add(new ButtonWidget(6, Math.round((float) width / 2 + 30), Math.round((float) height / 2 - 40), 40, 20, (tile.isOutput ? "Output" : "Input")));
        buttons.add(new ButtonWidget(7, Math.round((float) width / 2 + 30), Math.round((float) height / 2 - 65), 40, 20, "Set"));
        //buttons.add();
        buttons.add(new ButtonWidget(2, Math.round((float) width / 2 - 5), Math.round((float) height / 2 - 65), 20, 20, "+"));
        buttons.add(new ButtonWidget(3, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 65), 20, 20, "-"));// /2 - 34, - 150*/
        buttons.add(new ButtonWidget(4, Math.round((float) width / 2 - 5), Math.round((float) height / 2 - 40), 20, 20, "+"));
        buttons.add(new ButtonWidget(5, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 40), 20, 20, "-"));// /2 - 34, - 150*/

        buttons.add(new ButtonWidget(8, Math.round((float) width / 2 - 27), Math.round((float) height / 2 - 12 + 20), 18, 4, ""));
        buttons.add(new ButtonWidget(9, Math.round((float) width / 2 - 8), Math.round((float) height / 2 - 12 + 20), 18, 4, ""));

        ((ButtonWidget) buttons.get(8)).active = false;
    }

    @Override
    protected void drawForeground() {
        super.drawForeground();
        textRenderer.draw(TranslationStorage.getInstance().getClientTranslation(tile.getName()), 35, 6, 0x404040);
        textRenderer.draw("Process:", 10, 24, 0x404040);
        textRenderer.draw("Step: " + tile.task, 42, 50, 0x404040);
        textRenderer.draw("Slot: " + tile.slot, 42, 75, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);
    }

    @Override
    protected void drawBackground(float tickDelta) {
        int bg = this.minecraft.textureManager.getTextureId(gui("process_programmer"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.textureManager.bindTexture(bg);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        drawTexture(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        processName.render();
    }

    @Override
    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 6) {
            tile.isOutput = !tile.isOutput;
            guibutton.text = (tile.isOutput ? "Output" : "Input");
        }
        switch (guibutton.id) {
            case 0:
                tile.saveProcess();
                break;
            case 1:
                tile.clearDisc();
                tile.isOutput = false;
                ((ButtonWidget) buttons.get(2)).text = "Input";
                tile.slot = 0;
                tile.task = 0;
                tile.processName = "New Process";
                processName.setText("New Process");
                break;
            case 2:
                tile.task++;
                break;
            case 3:
                if (tile.task > 0) tile.task--;
                break;
            case 4:
                tile.slot++;
                break;
            case 5:
                if (tile.slot > 0) tile.slot--;
                break;
            case 7:
                tile.setTask(selected);
                break;
            case 8:
                selected = StackType.ITEM;
                guibutton.active = false;
                ((ButtonWidget) buttons.get(9)).active = true;
                break;
            case 9:
                selected = StackType.FLUID;
                guibutton.active = false;
                ((ButtonWidget) buttons.get(8)).active = true;
                break;
        }
        if (tile.world.isRemote) {
            PacketHelper.send(new ScreenActionPacket(guibutton.id, 0, 0, new Vec3i(tile.x, tile.y, tile.z)));
        }
    }

    @Override
    public void mouseClicked(int i1, int i2, int i3) {
        processName.mouseClicked(i1, i2, i3);
        super.mouseClicked(i1, i2, i3);
    }

    @Override
    protected void keyPressed(char character, int keyCode) {
        if (processName.focused) {
            Keyboard.enableRepeatEvents(true);
            if (character == Keyboard.KEY_ESCAPE) {
                Keyboard.enableRepeatEvents(false);
                processName.setFocused(false);
            } else processName.keyPressed(character, keyCode);
            tile.processName = processName.getText();
        } else {
            super.keyPressed(character, keyCode);
        }
    }
}
