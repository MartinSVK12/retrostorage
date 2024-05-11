package sunsetsatellite.retrostorage.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.retrostorage.util.GuiRenderDigitalItem;
import sunsetsatellite.retrostorage.util.SlotDigital;

public abstract class GuiDigital extends GuiContainer {

    public GuiTooltip guiTooltip;
    public GuiRenderDigitalItem guiRenderItem;

    public GuiDigital(Container container) {
        super(container);
        Minecraft mc = Minecraft.getMinecraft(this);
        guiTooltip = new GuiTooltip(mc);
        guiRenderItem = new GuiRenderDigitalItem(mc);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTick)
    {
        drawDefaultBackground();
        final int centerX = (width - xSize) / 2;
        final int centerY = (height - ySize) / 2;
        drawGuiContainerBackgroundLayer(partialTick);
        GL11.glPushMatrix();
        GL11.glTranslatef(centerX, centerY, 0.0F);
        drawGuiContainerForegroundLayer();
        Slot slot = null;
        for (int i = 0; i < inventorySlots.inventorySlots.size(); i++)
        {
            final Slot slot1 = inventorySlots.inventorySlots.get(i);
            boolean mouseOver = getIsMouseOverSlot(slot1, mouseX, mouseY);
            if(!itemDragHandler.isSlotDragged(slot1)) {
                guiRenderItem.render(slot1.getStack(), slot1.xDisplayPosition, slot1.yDisplayPosition, mouseOver, slot1);
            }
            if (mouseOver)
            {
                slot = slot1;
            }
        }

        final InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
        ItemStack grabbedItem = inventoryplayer.getHeldItemStack();
        if(mc.gameSettings.enableItemDragging.value) {
            itemDragHandler.drawScreen(mouseX, mouseY, partialTick);

            ItemStack grabbedItemOverride = itemDragHandler.getHeldItemRenderOverride();
            if(grabbedItemOverride != null) {
                grabbedItem = grabbedItemOverride;
            }
        }

        if (grabbedItem != null)
        {
            GL11.glTranslatef(0.0F, 0.0F, 64F);
            guiRenderItem.render(grabbedItem, mouseX - centerX - 8, mouseY - centerY - 8);
        }
        GL11.glPopMatrix();

        for(int i = 0; i < this.controlList.size(); ++i) {
            GuiButton guibutton = this.controlList.get(i);
            guibutton.drawButton(this.mc, mouseX, mouseY);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (inventoryplayer.getHeldItemStack() == null && slot != null && slot.hasStack())
        {
            boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || mc.gameSettings.alwaysShowDescriptions.value;
            String str = guiTooltip.getTooltipText(slot.getStack(), showDescription, slot);
            if(!str.isEmpty())
            {
                if(slot instanceof SlotDigital && slot.getStack().stackSize >= 1000){
                    str += ("\n" + TextFormatting.GRAY + slot.getStack().stackSize + TextFormatting.WHITE);
                }
                guiTooltip.render(str, mouseX, mouseY, 8, -8);
            }
        }
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
    }

}
