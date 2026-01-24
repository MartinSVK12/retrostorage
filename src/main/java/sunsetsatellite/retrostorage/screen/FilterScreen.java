package sunsetsatellite.retrostorage.screen;

import net.danygames2014.nyalib.fluid.FluidSlot;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import sunsetsatellite.retrostorage.util.GhostFluidSlot;
import sunsetsatellite.retrostorage.util.GhostSlot;

public abstract class FilterScreen extends HandledScreen {
    public FilterScreen(ScreenHandler handler) {
        super(handler);
    }

    public boolean isPointOverFluidSlot(FluidSlot fluidSlot, int x, int y) {
        int centerX = (this.width - this.backgroundWidth) / 2;
        int centerY = (this.height - this.backgroundHeight) / 2;
        x -= centerX;
        y -= centerY;
        return x >= fluidSlot.x - 1 && x < fluidSlot.x + fluidSlot.width + 1 && y >= fluidSlot.y - 1 && y < fluidSlot.y + fluidSlot.height + 1;
    }

    public FluidSlot getFluidSlotAt(int x, int y) {
        for (FluidSlot fluidSlot : this.handler.getFluidSlots()) {
            if (this.isPointOverFluidSlot(fluidSlot, x, y)) {
                return fluidSlot;
            }
        }
        return null;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        ghostFluidSlotManipulation(mouseX, mouseY, delta);
        ghostItemSlotManipulation(mouseX, mouseY, delta);
        super.render(mouseX, mouseY, delta);
    }

    private void ghostFluidSlotManipulation(int mouseX, int mouseY, float delta) {
        FluidSlot slot = getFluidSlotAt(mouseX, mouseY);
        if (!(slot instanceof GhostFluidSlot)) {
            return;
        }
        boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
        boolean control = (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
        boolean alt = (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
        int wheel = Mouse.getDWheel();
        if (slot.getStack() != null) {
            if (wheel < 0) {
                if (shift) {
                    if (slot.getStack().amount > 10) slot.getStack().amount -= 10;
                } else if (control) {
                    if (slot.getStack().amount > 100) slot.getStack().amount -= 100;
                } else if (alt) {
                    if (slot.getStack().amount > 1000) slot.getStack().amount -= 1000;
                } else {
                    if (slot.getStack().amount > 1) slot.getStack().amount--;
                }
            }
            if (wheel > 0) {
                if (shift) {
                    slot.getStack().amount += 10;
                } else if (control) {
                    slot.getStack().amount += 100;
                } else if (alt) {
                    slot.getStack().amount += 1000;
                } else {
                    slot.getStack().amount++;
                }
            }
        }
    }

    private void ghostItemSlotManipulation(int mouseX, int mouseY, float delta) {
        Slot slot = getSlotAt(mouseX, mouseY);
        if (!(slot instanceof GhostSlot)) {
            return;
        }
        boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
        boolean control = (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
        boolean alt = (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
        int wheel = Mouse.getDWheel();
        if (slot.getStack() != null) {
            if (wheel < 0) {
                if (shift) {
                    if (slot.getStack().count > 10) slot.getStack().count -= 10;
                } else if (control) {
                    if (slot.getStack().count > 100) slot.getStack().count -= 100;
                } else if (alt) {
                    if (slot.getStack().count > 1000) slot.getStack().count -= 1000;
                } else {
                    if (slot.getStack().count > 1) slot.getStack().count--;
                }
            }
            if (wheel > 0) {
                if (shift) {
                    slot.getStack().count += 10;
                } else if (control) {
                    slot.getStack().count += 100;
                } else if (alt) {
                    slot.getStack().count += 1000;
                } else {
                    slot.getStack().count++;
                }
            }
        }
    }
}
