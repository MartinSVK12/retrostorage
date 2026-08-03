package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.gui.ButtonElement;

import net.minecraft.client.gui.TextFieldElement;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.retrostorage.menus.MenuProcessProgrammer;
import sunsetsatellite.retrostorage.mp.PacketModifyFilterAmount;
import sunsetsatellite.retrostorage.tiles.TileEntityProcessProgrammer;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.Objects;

public class ScreenProcessProgrammer extends ScreenFluidFake {

    public String selected = "item";

    public ScreenProcessProgrammer(ContainerInventory inventoryplayer, TileEntityProcessProgrammer tileEntityprocessprogrammer) {
        super(inventoryplayer, new MenuProcessProgrammer(inventoryplayer, tileEntityprocessprogrammer));
        ySize = 220;
        tile = tileEntityprocessprogrammer;
        renderAmount = true;
    }

    protected void drawGuiContainerForegroundLayer() {
        drawStringNoShadow(fontRenderer,"Process Programmer", 35, 6, 0x404040);
        drawStringNoShadow(fontRenderer,"Process:", 10, 24, 0x404040);
        drawStringNoShadow(fontRenderer,"Step: " + tile.currentTask, 42, 50, 0x404040);
        drawStringNoShadow(fontRenderer,"Slot: " + tile.currentSlot, 42, 75, 0x404040);
        drawStringNoShadow(fontRenderer,"Inventory", 8, (ySize - 95) + 2, 0x404040);


    }

    public void init() {
        super.init();
        processName = new TextFieldElement(this, fontRenderer, Math.round((float) width / 2 - 31), Math.round((float) height / 2 - 92), 100, 20, Objects.equals(tile.currentProcessName, "") ? "New Process" : tile.currentProcessName, "Process name..");
        buttons.add(new ButtonElement(0, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 12), 40, 20, "Save"));
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 + 31), Math.round((float) height / 2 - 12), 40, 20, "Clear"));
        buttons.add(new ButtonElement(6, Math.round((float) width / 2 + 30), Math.round((float) height / 2 - 40), 40, 20, (tile.isCurrentOutput ? "Output" : "Input")));
        buttons.add(new ButtonElement(7, Math.round((float) width / 2 + 30), Math.round((float) height / 2 - 65), 40, 20, "Set"));
        //buttons.add();
        buttons.add(new ButtonElement(2, Math.round((float) width / 2 - 5), Math.round((float) height / 2 - 65), 20, 20, "+"));
        buttons.add(new ButtonElement(3, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 65), 20, 20, "-"));// /2 - 34, - 150*/
        buttons.add(new ButtonElement(4, Math.round((float) width / 2 - 5), Math.round((float) height / 2 - 40), 20, 20, "+"));
        buttons.add(new ButtonElement(5, Math.round((float) width / 2 - 70), Math.round((float) height / 2 - 40), 20, 20, "-"));// /2 - 34, - 150*/

        buttons.add(new ButtonElement(8, Math.round((float) width / 2 - 27), Math.round((float) height / 2 - 12 + 20), 18, 4, ""));
        buttons.add(new ButtonElement(9, Math.round((float) width / 2 - 8), Math.round((float) height / 2 - 12 + 20), 18, 4, ""));

        buttons.get(8).enabled = false;
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        @NotNull Texture i = mc.textureManager.loadTexture("/assets/retrostorage/textures/gui/process_programmer.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        processName.drawTextBox();
    }

    @Override
    protected void buttonClicked(ButtonElement guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton.id == 6) {
            tile.isCurrentOutput = !tile.isCurrentOutput;
            guibutton.displayString = (tile.isCurrentOutput ? "Output" : "Input");
        }
        switch (guibutton.id) {
            case 0:
                tile.saveProcess();
                break;
            case 1:
                tile.clearDisc();
                tile.isCurrentOutput = false;
                buttons.get(2).displayString = "Input";
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
                guibutton.enabled = false;
                buttons.get(9).enabled = true;
                break;
            case 9:
                selected = "fluid";
                guibutton.enabled = false;
                buttons.get(8).enabled = true;
                break;
        }
        if(EnvironmentHelper.isMultiplayerClient()){
            NetworkHandler.sendToServer(new PacketScreenAction(guibutton.id,0,0,new Vec3i(tile.tilePos), tile.getClass()));
        }

    }

    @Override
    public void mouseClicked(int i1, int i2, int i3) {
        processName.mouseClicked(i1, i2, i3);
        super.mouseClicked(i1, i2, i3);
    }

    @Override
    public void keyPressed(char c, int i, int mouseX, int mouseY) {
        if (processName.isFocused) {
            Keyboard.enableRepeatEvents(true);
            if (c == Keyboard.KEY_ESCAPE) {
                Keyboard.enableRepeatEvents(false);
                processName.setFocused(false);
            } else processName.textboxKeyTyped(c, i);
            tile.currentProcessName = processName.getText();
        } else {
            super.keyPressed(c, i, mouseX, mouseY);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {
        SlotFluid slot = getFluidSlotAtPosition(mouseX, mouseY);
        boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
        boolean control = (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
        boolean alt = (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
        int wheel = Mouse.getDWheel();
        if (slot != null && slot.getFluidStack() != null) {
            if (wheel < 0) {
                if (shift) {
                    if (slot.getFluidStack().amount > 10) {
                        slot.getFluidStack().amount -= 10;
                        if(EnvironmentHelper.isMultiplayerClient()){
                            NetworkHandler.sendToServer(new PacketModifyFilterAmount(tile.tilePos.x(), tile.tilePos.y(), tile.tilePos.z(), slot.slotIndex, -10));
                        }
                    }
                } else if (control) {
                    if (slot.getFluidStack().amount > 100) {
                        slot.getFluidStack().amount -= 100;
                        NetworkHandler.sendToServer(new PacketModifyFilterAmount(tile.tilePos.x(), tile.tilePos.y(), tile.tilePos.z(), slot.slotIndex, -100));
                    }
                } else if (alt) {
                    if (slot.getFluidStack().amount > 1000) {
                        slot.getFluidStack().amount -= 1000;
                        NetworkHandler.sendToServer(new PacketModifyFilterAmount(tile.tilePos.x(), tile.tilePos.y(), tile.tilePos.z(), slot.slotIndex, -1000));
                    }
                } else {
                    if (slot.getFluidStack().amount > 1) {
                        slot.getFluidStack().amount--;
                        NetworkHandler.sendToServer(new PacketModifyFilterAmount(tile.tilePos.x(), tile.tilePos.y(), tile.tilePos.z(), slot.slotIndex, -1));
                    }
                }
            }
            if (wheel > 0) {
                if (shift) {
                    slot.getFluidStack().amount += 10;
                    NetworkHandler.sendToServer(new PacketModifyFilterAmount(tile.tilePos.x(), tile.tilePos.y(), tile.tilePos.z(), slot.slotIndex, 10));
                } else if (control) {
                    slot.getFluidStack().amount += 100;
                    NetworkHandler.sendToServer(new PacketModifyFilterAmount(tile.tilePos.x(), tile.tilePos.y(), tile.tilePos.z(), slot.slotIndex, 100));
                } else if (alt) {
                    slot.getFluidStack().amount += 1000;
                    NetworkHandler.sendToServer(new PacketModifyFilterAmount(tile.tilePos.x(), tile.tilePos.y(), tile.tilePos.z(), slot.slotIndex, 1000));
                } else {
                    slot.getFluidStack().amount++;
                    NetworkHandler.sendToServer(new PacketModifyFilterAmount(tile.tilePos.x(), tile.tilePos.y(), tile.tilePos.z(), slot.slotIndex, 1));
                }
            }
        }
        super.render(mouseX, mouseY, partialTick);
    }

    public void onClosed() {
    }

    public TextFieldElement processName;
    private final TileEntityProcessProgrammer tile;
}
