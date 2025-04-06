
package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.util.helper.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.catalyst.core.util.model.IColorOverride;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.retrostorage.menus.MenuFluidFake;

public class ScreenFluidFake extends ScreenFluid {

    public TooltipElement guiTooltip;
    public ItemElement guiRenderItem;
    public boolean renderAmount = false;

    public ScreenFluidFake(ContainerInventory invP, MenuFluidFake containerFluid) {
        super(containerFluid);
        mc = Minecraft.getMinecraft();
        this.guiTooltip = new TooltipElement(mc);
        this.guiRenderItem = new ItemElement(mc);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {
        //to prevent running of the super method (which will crash the game) the code for deeper super methods has been copied here
        //removed the slot and tooltip parts that show amounts since they're irrelevant here

        //GuiContainer
        this.renderBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(partialTick);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) centerX, (float) centerY, 0.0F);
        this.drawGuiContainerForegroundLayer();
        Slot slot = null;

        boolean showDescription;
        for (int i = 0; i < this.inventorySlots.slots.size(); ++i) {
            Slot slot1 = this.inventorySlots.slots.get(i);
            showDescription = this.getIsMouseOverSlot(slot1, mouseX, mouseY);
            if (!this.itemDragHandler.isSlotDragged(slot1)) {
                this.guiRenderItem.render(slot1.getItemStack(), slot1.x, slot1.y, showDescription, slot1);
            }

            if (showDescription) {
                slot = slot1;
            }
        }

        ContainerInventory inventoryplayer = this.mc.thePlayer.inventory;
        ItemStack grabbedItem = inventoryplayer.getHeldItemStack();
        if (this.mc.gameSettings.enableItemDragging.value) {
            this.itemDragHandler.drawScreen(mouseX, mouseY, partialTick);
            ItemStack grabbedItemOverride = this.itemDragHandler.getHeldItemRenderOverride();
            if (grabbedItemOverride != null) {
                grabbedItem = grabbedItemOverride;
            }
        }

        if (grabbedItem != null) {
            GL11.glTranslatef(0.0F, 0.0F, 64.0F);
            this.guiRenderItem.render(grabbedItem, mouseX - centerX - 8, mouseY - centerY - 8);
        }

        GL11.glPopMatrix();

        //Screen
        for (int i = 0; i < this.buttons.size(); ++i) {
            ButtonElement guibutton = this.buttons.get(i);
            guibutton.drawButton(this.mc, mouseX, mouseY);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (inventoryplayer.getHeldItemStack() == null && slot != null && slot.hasItem()) {
            showDescription = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || this.mc.gameSettings.alwaysShowDescriptions.value;
            String str = this.guiTooltip.getTooltipText(slot.getItemStack(), showDescription, slot);
            if (!str.isEmpty()) {
                this.guiTooltip.render(str, mouseX, mouseY, 8, -8);
            }
        }

        GL11.glEnable(2929);

        //Own code
        int i4 = (this.width - this.xSize) / 2;
        int i5 = (this.height - this.ySize) / 2;
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        Lighting.disable();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) i4, (float) i5, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        int i9;
        int i10;
        SlotFluid slot6 = null;
        MenuFluidFake fluidContainer = ((MenuFluidFake) inventorySlots);
        for (int i7 = 0; i7 < fluidContainer.fluidSlots.size(); i7++) {
            SlotFluid slot8 = fluidContainer.fluidSlots.get(i7);
            this.drawFluidSlotInventory(slot8);
            if (this.getIsMouseOverFluidSlot(slot8, mouseX, mouseY)) {
                slot6 = slot8;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                i9 = slot8.x;
                i10 = slot8.y;
                this.drawGradientRect(i9, i10, i9 + 16, i10 + 16, 0x40FFFFFF, 0x40FFFFFF);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
        if (slot6 != null && slot6.hasStack() && slot6.getFluidStack().fluid != null) {
            i9 = mouseX - i4;
            i10 = mouseY - i5;
            String name = slot6.getFluidStack().fluid.getName();//.replace("Flowing ", "").replace("Still ", "");
            String amount = slot6.getFluidStack().amount + " mB";
            TooltipElement tooltip = new TooltipElement(mc);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            if (renderAmount && slot6.getFluidStack().amount > 1)
                name += "\n" + TextFormatting.LIGHT_GRAY + amount + TextFormatting.WHITE;
            tooltip.render(name, i9, i10, 8, -8);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else if (slot6 != null) {
            i9 = mouseX - i4;
            i10 = mouseY - i5;
            String name = "Empty";
            TooltipElement tooltip = new TooltipElement(mc);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            tooltip.render(name, i9, i10, 8, -8);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        Lighting.enableInventoryLight();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }


    protected void drawFluidSlotInventory(SlotFluid slot) {
        int i2 = slot.x;
        int i3 = slot.y;
        if (slot.hasStack() && slot.getFluidStack().fluid != null) {
            ItemStack itemStack4 = new ItemStack(slot.getFluidStack().fluid.getFirstId(), slot.getFluidStack().amount, 0);
            int i5 = slot.getBackgroundIconIndex();
            if (i5 >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.mc.textureManager.bindTexture(this.mc.textureManager.loadTexture("/gui/items.png"));
                this.drawTexturedModalRect(i2, i3, i5 % 16 * 16, i5 / 16 * 16, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                return;
            }

            ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(slot.getFluidStack().fluid.blocks.get(0).getDefaultStack().getItem());
            BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(slot.getFluidStack().fluid.blocks.get(0));

            if (slot.getFluidStack().fluid.getFirstId() == Blocks.FLUID_WATER_FLOWING.id() && mc.gameSettings.biomeWater.value) {
                int waterColor = BlockColorDispatcher.getInstance().getDispatch(Blocks.FLUID_WATER_FLOWING).getWorldColor(this.mc.currentWorld, (int) this.mc.thePlayer.x, (int) this.mc.thePlayer.y, (int) this.mc.thePlayer.z);
                Color c = new Color().setARGB(waterColor);
                c.setRGBA(c.getRed(), c.getGreen(), c.getBlue(), 0x40);

                ((IColorOverride) blockModel).overrideColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
                itemModel.renderItemIntoGui(Tessellator.instance, this.font, this.mc.textureManager, itemStack4, i2, i3, 1.0F);
            } else {
                itemModel.renderItemIntoGui(Tessellator.instance, this.font, this.mc.textureManager, itemStack4, i2, i3, 1.0F);
            }

            ((IColorOverride) blockModel).overrideColor(1, 1, 1, 1);
            itemModel.renderItemOverlayIntoGUI(Tessellator.instance, this.font, this.mc.textureManager, itemStack4, i2, i3, (renderAmount && slot.getFluidStack() != null && slot.getFluidStack().amount > 1) ? NumberUtil.format(slot.getFluidStack().amount) : "", 1.0F);
        }
    }
}
