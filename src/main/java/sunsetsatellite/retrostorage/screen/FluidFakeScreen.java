
package sunsetsatellite.retrostorage.screen;

import net.teamterminus.machineessentials.fluid.core.FluidScreen;
import net.teamterminus.machineessentials.fluid.core.FluidScreenHandler;

public abstract class FluidFakeScreen extends FluidScreen {
    public FluidFakeScreen(FluidScreenHandler container) {
        super(container);
    }

    /*public GuiTooltip guiTooltip;
    public GuiRenderItem guiRenderItem;
    public boolean renderAmount = false;

    public FluidFakeScreen(ContainerFluidFake containerFluid, PlayerInventory invP) {
        super(containerFluid, invP);
        minecraft = Minecraft.getMinecraft(Minecraft.class);
        this.guiTooltip = new GuiTooltip(minecraft);
        this.guiRenderItem = new GuiRenderItem(minecraft);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {
        //to prevent running of the super method (which will crash the game) the code for deeper super methods has been copied here
        //removed the slot and tooltip parts that show amounts since they're irrelevant here

        //HandledScreen
        this.drawDefaultBackground();
        int centerX = (this.width - this.backgroundWidth) / 2;
        int centerY = (this.height - this.backgroundHeight) / 2;
        this.drawBackground(partialTick);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) centerX, (float) centerY, 0.0F);
        this.drawForeground();
        Slot slot = null;

        boolean showDescription;
        for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
            Slot slot1 = this.inventorySlots.inventorySlots.get(i);
            showDescription = this.getIsMouseOverSlot(slot1, mouseX, mouseY);
            if (!this.itemDragHandler.isSlotDragged(slot1)) {
                this.guiRenderItem.render(slot1.getStack(), slot1.xDisplayPosition, slot1.yDisplayPosition, showDescription, slot1);
            }

            if (showDescription) {
                slot = slot1;
            }
        }

        PlayerInventory inventoryplayer = this.minecraft.thePlayer.inventory;
        ItemStack grabbedItem = inventoryplayer.getHeldItemStack();
        if (this.minecraft.gameSettings.enableItemDragging.value) {
            this.itemDragHandler.render(mouseX, mouseY, partialTick);
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
        for (int i = 0; i < this.buttons.size(); ++i) {
            ButtonWidget guibutton = this.buttons.get(i);
            guibutton.drawButton(this.minecraft, mouseX, mouseY);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (inventoryplayer.getHeldItemStack() == null && slot != null && slot.hasStack()) {
            showDescription = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || this.minecraft.gameSettings.alwaysShowDescriptions.value;
            String str = this.guiTooltip.getTooltipText(slot.getStack(), showDescription, slot);
            if (!str.isEmpty()) {
                this.guiTooltip.render(str, mouseX, mouseY, 8, -8);
            }
        }

        GL11.glEnable(2929);

        //Own code
        int i4 = (this.width - this.backgroundWidth) / 2;
        int i5 = (this.height - this.backgroundHeight) / 2;
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
        ContainerFluidFake fluidContainer = ((ContainerFluidFake) inventorySlots);
        for (int i7 = 0; i7 < fluidContainer.fluidSlots.size(); i7++) {
            SlotFluid slot8 = fluidContainer.fluidSlots.get(i7);
            this.drawFluidSlotInventory(slot8);
            if (this.getIsMouseOverFluidSlot(slot8, mouseX, mouseY)) {
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
        if (slot6 != null && slot6.getHasStack() && slot6.getFluidStack().getLiquid() != null) {
            i9 = mouseX - i4;
            i10 = mouseY - i5;
            String name = I18n.getInstance().translateNameKey(slot6.getFluidStack().getLiquid().getLanguageKey(0)).replace("Flowing ", "").replace("Still ", "");
            String amount = slot6.getFluidStack().getAmount() + " mB";
            GuiTooltip tooltip = new GuiTooltip(minecraft);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            if (renderAmount && slot6.getFluidStack().getAmount() > 1)
                name += "\n" + TextFormatting.LIGHT_GRAY + amount + TextFormatting.WHITE;
            tooltip.render(name, i9, i10, 8, -8);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else if (slot6 != null) {
            i9 = mouseX - i4;
            i10 = mouseY - i5;
            String name = "Empty";
            GuiTooltip tooltip = new GuiTooltip(minecraft);
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

    @Override
    protected void drawFluidSlotInventory(SlotFluid slot1) {
        int i2 = slot1.xPos;
        int i3 = slot1.yPos;
        if (slot1.getHasStack() && slot1.getFluidStack().liquid != null) {
            ItemStack itemStack4 = new ItemStack(slot1.getFluidStack().getLiquid(), slot1.getFluidStack().amount, 0);
            int i5 = slot1.getBackgroundIconIndex();
            if (i5 >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.minecraft.textureManager.bindTexture(this.minecraft.textureManager.getTextureId("/gui/items.png"));
                this.drawTexture(i2, i3, i5 % 16 * 16, i5 / 16 * 16, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                return;
            }

            ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(slot1.getFluidStack().getLiquid().getDefaultStack().getItem());
            BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(slot1.getFluidStack().getLiquid());

            if (slot1.getFluidStack().getLiquid() == Block.fluidWaterFlowing && minecraft.gameSettings.biomeWater.value) {
                int waterColor = BlockColorDispatcher.getInstance().getDispatch(Block.fluidWaterFlowing).getWorldColor(this.minecraft.theWorld, (int) this.minecraft.thePlayer.x, (int) this.minecraft.thePlayer.y, (int) this.minecraft.thePlayer.z);
                Color c = new Color().setARGB(waterColor);
                c.setRGBA(c.getRed(), c.getGreen(), c.getBlue(), 0x40);

                ((IColorOverride) blockModel).overrideColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
                itemModel.renderItemIntoGui(Tessellator.instance, this.textRenderer, this.minecraft.renderEngine, itemStack4, i2, i3, 1.0F);
            } else {
                itemModel.renderItemIntoGui(Tessellator.instance, this.textRenderer, this.minecraft.renderEngine, itemStack4, i2, i3, 1.0F);
            }

            ((IColorOverride) blockModel).overrideColor(1, 1, 1, 1);
            itemModel.renderItemOverlayIntoGUI(Tessellator.instance, this.textRenderer, this.minecraft.renderEngine, itemStack4, i2, i3, (renderAmount && slot1.getFluidStack() != null && slot1.getFluidStack().amount > 1) ? NumberUtil.format(slot1.getFluidStack().amount) : "", 1.0F);
        }
    }*/
}
