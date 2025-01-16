package sunsetsatellite.retrostorage.screen;


import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.teamterminus.machineessentials.fluid.core.FluidSlot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.block.entity.ProcessProgrammerBlockEntity;
import sunsetsatellite.retrostorage.screen.handler.ProcessProgrammerScreenHandler;

import java.util.Objects;

public class ProcessProgrammerScreen extends FluidFakeScreen {

    public String selected = "item";

    public ProcessProgrammerScreen(PlayerInventory inventoryplayer, ProcessProgrammerBlockEntity tileEntityprocessprogrammer) {
        super(new ProcessProgrammerScreenHandler(inventoryplayer, tileEntityprocessprogrammer));
        backgroundHeight = 220;
        tile = tileEntityprocessprogrammer;
        //renderAmount = true;
    }

    protected void drawForeground() {
        textRenderer.draw("Process Programmer", 35, 6, 0x404040);
        textRenderer.draw("Process:", 10, 24, 0x404040);
        textRenderer.draw("Step: " + tile.currentTask, 42, 50, 0x404040);
        textRenderer.draw("Slot: " + tile.currentSlot, 42, 75, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 95) + 2, 0x404040);


    }

    public void init() {
        super.init();
        processName = new TextFieldWidget(this, textRenderer, Math.round((float) width / 2 - 31), Math.round((float) height / 2 - 92), 100, 20, Objects.equals(tile.currentProcessName, "") ? "New Process" : tile.currentProcessName);
        buttons.add(new ButtonWidget(0, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 12), 40, 20, "Save"));
        buttons.add(new ButtonWidget(1, Math.round((float) width / 2 + 31), Math.round((float) height / 2 - 12), 40, 20, "Clear"));
        buttons.add(new ButtonWidget(6, Math.round((float) width / 2 + 30), Math.round((float) height / 2 - 40), 40, 20, (tile.isCurrentOutput ? "Output" : "Input")));
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

    protected void drawBackground(float f) {
        int i = minecraft.textureManager.getTextureId("/assets/retrostorage/stationapi/textures/gui/process_programmer.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
        processName.render();
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 6) {
            tile.isCurrentOutput = !tile.isCurrentOutput;
            guibutton.text = (tile.isCurrentOutput ? "Output" : "Input");
        }
        switch (guibutton.id) {
            case 0:
                tile.saveProcess();
                break;
            case 1:
                tile.clearDisc();
                tile.isCurrentOutput = false;
                ((ButtonWidget) buttons.get(2)).text = "Input";
                tile.currentSlot = 0;
                tile.currentTask = 0;
                tile.currentProcessName = "New Process";
                processName.setText("New Process");
                break;
            case 2:
                tile.currentTask++;
                break;
            case 3:
                if (tile.currentTask > 0) tile.currentTask--;
                break;
            case 4:
                tile.currentSlot++;
                break;
            case 5:
                if (tile.currentSlot > 0) tile.currentSlot--;
                break;
            case 7:
                tile.setTask(selected);
                break;
            case 8:
                selected = "item";
                guibutton.active = false;
                ((ButtonWidget) buttons.get(9)).active = true;
                break;
            case 9:
                selected = "fluid";
                guibutton.active = false;
                ((ButtonWidget) buttons.get(8)).active = true;
                break;
        }

    }

    @Override
    public void mouseClicked(int i1, int i2, int i3) {
        processName.mouseClicked(i1, i2, i3);
        super.mouseClicked(i1, i2, i3);
    }

    @Override
    public void keyPressed(char c, int i) {
        if (processName.focused) {
            Keyboard.enableRepeatEvents(true);
            if (c == Keyboard.KEY_ESCAPE) {
                Keyboard.enableRepeatEvents(false);
                processName.setFocused(false);
            } else processName.keyPressed(c, i);
            tile.currentProcessName = processName.getText();
        } else {
            super.keyPressed(c, i);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {
        FluidSlot slot = getFluidSlotAtPosition(mouseX, mouseY);
        boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
        boolean control = (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
        boolean alt = (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
        int wheel = Mouse.getDWheel();
        if (slot != null && slot.getFluidStack() != null) {
            if (wheel < 0) {
                if (shift) {
                    if (slot.getFluidStack().amount > 10) slot.getFluidStack().amount -= 10;
                } else if (control) {
                    if (slot.getFluidStack().amount > 100) slot.getFluidStack().amount -= 100;
                } else if (alt) {
                    if (slot.getFluidStack().amount > 1000) slot.getFluidStack().amount -= 1000;
                } else {
                    if (slot.getFluidStack().amount > 1) slot.getFluidStack().amount--;
                }
            }
            if (wheel > 0) {
                if (shift) {
                    slot.getFluidStack().amount += 10;
                } else if (control) {
                    slot.getFluidStack().amount += 100;
                } else if (alt) {
                    slot.getFluidStack().amount += 1000;
                } else {
                    slot.getFluidStack().amount++;
                }
            }
        }
        super.render(mouseX, mouseY, partialTick);
    }

    public void onClosed() {
    }

    public TextFieldWidget processName;
    private final ProcessProgrammerBlockEntity tile;
}
