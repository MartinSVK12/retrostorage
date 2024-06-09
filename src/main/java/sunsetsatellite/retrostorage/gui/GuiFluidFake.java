package sunsetsatellite.retrostorage.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.util.helper.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.core.util.IColorOverride;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.catalyst.fluids.impl.GuiFluid;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.retrostorage.containers.ContainerDigitalFluid;
import sunsetsatellite.retrostorage.containers.ContainerFluidFake;

public class GuiFluidFake extends GuiFluid {

    public GuiTooltip guiTooltip;
    public GuiRenderItem guiRenderItem;

    public GuiFluidFake(ContainerFluidFake containerFluid, InventoryPlayer invP) {
        super(containerFluid, invP);
        mc = Minecraft.getMinecraft(Minecraft.class);
        this.guiTooltip = new GuiTooltip(mc);
        this.guiRenderItem = new GuiRenderItem(mc);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {
        //to prevent running of the super method (which will crash the game) the code for deeper super methods has been copied here
        //removed the slot and tooltip parts that show amounts since they're irrelevant here

        //GuiContainer
        this.drawDefaultBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(partialTick);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)centerX, (float)centerY, 0.0F);
        this.drawGuiContainerForegroundLayer();
        Slot slot = null;

        boolean showDescription;
        for(int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
            Slot slot1 = this.inventorySlots.inventorySlots.get(i);
            showDescription = this.getIsMouseOverSlot(slot1, mouseX, mouseY);
            if (!this.itemDragHandler.isSlotDragged(slot1)) {
                this.guiRenderItem.render(slot1.getStack(), slot1.xDisplayPosition, slot1.yDisplayPosition, showDescription, slot1);
            }

            if (showDescription) {
                slot = slot1;
            }
        }

        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
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

        //GuiScreen
        for(int i = 0; i < this.controlList.size(); ++i) {
            GuiButton guibutton = this.controlList.get(i);
            guibutton.drawButton(this.mc, mouseX, mouseY);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (inventoryplayer.getHeldItemStack() == null && slot != null && slot.hasStack()) {
            showDescription = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || this.mc.gameSettings.alwaysShowDescriptions.value;
            String str = this.guiTooltip.getTooltipText(slot.getStack(), showDescription, slot);
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
        GL11.glTranslatef((float)i4, (float)i5, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        int i9;
        int i10;
        SlotFluid slot6 = null;
        ContainerFluidFake fluidContainer = ((ContainerFluidFake) inventorySlots);
        for(int i7 = 0; i7 < fluidContainer.fluidSlots.size(); i7++) {
            SlotFluid slot8 = fluidContainer.fluidSlots.get(i7);
            this.drawFluidSlotInventory(slot8);
            if(this.getIsMouseOverFluidSlot(slot8, mouseX, mouseY)) {
                slot6 = slot8;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                i9 = slot8.xPos;
                i10 = slot8.yPos;
                this.drawGradientRect(i9, i10, i9 + 16, i10 + 16, 0x40FFFFFF, 0x40FFFFFF);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
        if(slot6 != null && slot6.getHasStack() && slot6.getFluidStack().getLiquid() != null) {
            i9 = mouseX - i4;
            i10 = mouseY - i5;
            String name = I18n.getInstance().translateNameKey(slot6.getFluidStack().getLiquid().getLanguageKey(0)).replace("Flowing ","").replace("Still ","");
            GuiTooltip tooltip = new GuiTooltip(mc);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            tooltip.render(name,i9,i10,8,-8);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else if(slot6 != null) {
            i9 = mouseX - i4;
            i10 = mouseY - i5;
            String name = "Empty";
            GuiTooltip tooltip = new GuiTooltip(mc);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            tooltip.render(name,i9,i10,8,-8);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        Lighting.enableInventoryLight();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    protected void drawFluidSlotInventory(SlotFluid slot1) {
        int i2 = slot1.xPos;
        int i3 = slot1.yPos;
        if(slot1.getHasStack() && slot1.getFluidStack().liquid != null){
            ItemStack itemStack4 = new ItemStack(slot1.getFluidStack().getLiquid(),slot1.getFluidStack().amount,0);
            int i5 = slot1.getBackgroundIconIndex();
            if(i5 >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
                this.drawTexturedModalRect(i2, i3, i5 % 16 * 16, i5 / 16 * 16, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                return;
            }

            ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(slot1.getFluidStack().getLiquid().getDefaultStack().getItem());
            BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(slot1.getFluidStack().getLiquid());

            if(slot1.getFluidStack().getLiquid() == Block.fluidWaterFlowing && mc.gameSettings.biomeWater.value){
                int waterColor = BlockColorDispatcher.getInstance().getDispatch(Block.fluidWaterFlowing).getWorldColor(this.mc.theWorld, (int) this.mc.thePlayer.x, (int) this.mc.thePlayer.y, (int) this.mc.thePlayer.z);
                Color c = new Color().setARGB(waterColor);
                c.setRGBA(c.getRed(),c.getGreen(),c.getBlue(),0x40);
                ((IColorOverride)itemModel).overrideColor(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
                ((IColorOverride)blockModel).overrideColor(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha());
                itemModel.renderItemIntoGui(Tessellator.instance,this.fontRenderer, this.mc.renderEngine, itemStack4, i2, i3,1.0F);
            } else {
                itemModel.renderItemIntoGui(Tessellator.instance,this.fontRenderer, this.mc.renderEngine, itemStack4, i2, i3,1.0F);
            }
            ((IColorOverride)itemModel).overrideColor(1,1,1,1);
            ((IColorOverride)blockModel).overrideColor(1,1,1,1);
            itemModel.renderItemOverlayIntoGUI(Tessellator.instance,this.fontRenderer, this.mc.renderEngine, itemStack4, i2, i3, "", 1.0F);
        }
    }
}
