
package sunsetsatellite.retrostorage.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.enums.DescriptionPromptEnum;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorShader;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;

import net.minecraft.core.util.helper.LightIndexHelper;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.retrostorage.menus.MenuFluidFake;

public class ScreenFluidFake extends ScreenFluid {

    public TooltipElement guiTooltip;
    public ItemElement guiRenderItem;
    public boolean renderAmount = false;

    public ScreenFluidFake(ContainerInventory invP, MenuFluidFake containerFluid) {
        super(containerFluid);
        this.guiTooltip = new TooltipElement(mc);
        this.guiRenderItem = new ItemElement(mc);
    }

	@Override
	public void render(int mx, int my, float partialTick) {
		//to prevent running of the super method (which will crash the game) the code for deeper super methods has been copied here

		//ScreenContainerAbstract
		this.renderBackground();
		int centerX = (this.width - this.xSize) / 2;
		int centerY = (this.height - this.ySize) / 2;
		this.drawGuiContainerBackgroundLayer(partialTick);
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float)centerX, (float)centerY, 0.0F);
		this.drawGuiContainerForegroundLayer();
		Slot slot = null;

		for(int i = 0; i < this.inventorySlots.slots.size(); ++i) {
			Slot slot1 = this.inventorySlots.slots.get(i);
			boolean mouseOver = this.getIsMouseOverSlot(slot1, mx, my);
			if (!this.itemDragHandler.isSlotDragged(slot1)) {
				this.itemElement.render(slot1.getItemStack(), slot1.x, slot1.y, mouseOver, slot1);
			}

			if (mouseOver) {
				slot = slot1;
			}
		}

		ContainerInventory containerInventory = this.mc.thePlayer.inventory;
		ItemStack grabbedItem = containerInventory.getHeldItemStack();
		if (GameSettings.ENABLE_ITEM_DRAGGING.value) {
			this.itemDragHandler.drawScreen(mx, my, partialTick);
			ItemStack grabbedItemOverride = this.itemDragHandler.getHeldItemRenderOverride();
			if (grabbedItemOverride != null) {
				grabbedItem = grabbedItemOverride;
			}
		}

		GLRenderer.popFrame();
		//Screen
		for(ButtonElement button : this.buttons) {
			button.drawButton(this.mc, mx, my);
		}
		GLRenderer.pushFrame();
		if (grabbedItem != null) {
			GLRenderer.modelM4f().translate((float)centerX, (float)centerY, 100.0F);
			this.itemElement.render(grabbedItem, mx - centerX - 8, my - centerY - 8);
		}

		GLRenderer.popFrame();
		GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (containerInventory.getHeldItemStack() == null && slot != null && slot.hasItem()) {
			boolean showDescription = DescriptionPromptEnum.showDescription();
			String str = this.tooltipElement.getTooltipText(slot.getItemStack(), showDescription, slot);
			if (!str.isEmpty()) {
				this.tooltipElement.render(str, mx, my, 8, -8);
			}
		}

		GLRenderer.enableState(State.DEPTH_TEST);

		//Own code
		int i4 = (this.width - this.xSize) / 2;
		int i5 = (this.height - this.ySize) / 2;
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().rotate((float) Math.toRadians(120.0F), 1.0F, 0.0F, 0.0F);
		Lighting.disable();
		GLRenderer.popFrame();
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) i4, (float) i5, 0.0F);
		GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i9;
		int i10;
		SlotFluid slot6 = null;
		MenuFluidFake fluidContainer = ((MenuFluidFake) inventorySlots);
		for (int i7 = 0; i7 < fluidContainer.fluidSlots.size(); i7++) {
			SlotFluid slot8 = fluidContainer.fluidSlots.get(i7);
			this.drawFluidSlotInventory(slot8);
			if (this.getIsMouseOverFluidSlot(slot8, mx, my)) {
				slot6 = slot8;
				Lighting.disable();
				GLRenderer.disableState(State.DEPTH_TEST);
				i9 = slot8.x;
				i10 = slot8.y;
				this.drawGradientRect(i9, i10, i9 + 16, i10 + 16, 0x40FFFFFF, 0x40FFFFFF);
				Lighting.enableLight();
				GLRenderer.enableState(State.DEPTH_TEST);
			}
		}
		if (slot6 != null && slot6.hasStack() && slot6.getFluidStack().fluid != null) {
			i9 = mx - i4;
			i10 = my - i5;
			String name = slot6.getFluidStack().fluid.getName();//.replace("Flowing ", "").replace("Still ", "");
			String amount = slot6.getFluidStack().amount + " mB";
			TooltipElement tooltip = new TooltipElement(mc);
			Lighting.disable();
			GLRenderer.disableState(State.DEPTH_TEST);
			if (renderAmount && slot6.getFluidStack().amount > 1)
				name += "\n" + TextFormatting.LIGHT_GRAY + amount + TextFormatting.WHITE;
			tooltip.render(name, i9, i10, 8, -8);
			Lighting.enableLight();
			GLRenderer.enableState(State.DEPTH_TEST);
		} else if (slot6 != null) {
			i9 = mx - i4;
			i10 = my - i5;
			String name = "Empty";
			TooltipElement tooltip = new TooltipElement(mc);
			Lighting.disable();
			GLRenderer.disableState(State.DEPTH_TEST);
			tooltip.render(name, i9, i10, 8, -8);
			Lighting.enableLight();
			GLRenderer.enableState(State.DEPTH_TEST);
		}
		Lighting.enableInventoryLight();
		GLRenderer.popFrame();
		Lighting.enableLight();
		GLRenderer.enableState(State.DEPTH_TEST);
	}

	protected void drawFluidSlotInventory(SlotFluid slot) {
        int x = slot.x;
        int y = slot.y;
        if (slot.hasStack() && slot.getFluidStack().fluid != null) {
            ItemStack itemStack4 = new ItemStack(slot.getFluidStack().fluid.getFirstId(), slot.getFluidStack().amount, 0);
            int i5 = slot.getBackgroundIconIndex();
            if (i5 >= 0) {
                Lighting.disable();
                this.mc.textureManager.bindTexture(this.mc.textureManager.loadTexture("/gui/items.png"));
                this.drawTexturedModalRect(x, y, i5 % 16 * 16, i5 / 16 * 16, 16, 16);
                Lighting.enableLight();
                return;
            }

            ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(slot.getFluidStack().fluid.blocks.get(0).getDefaultStack().getItem());
            BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(slot.getFluidStack().fluid.blocks.get(0));
			TessellatorShader t = GLRenderer.getTessellator();

			itemModel.renderGui(t, null, itemStack4, x,y, LightIndexHelper.lightIndex2f(15,15),1F);
			itemModel.renderItemOverlayIntoGUI(t, fontRenderer, this.mc.textureManager, itemStack4, x,y,(renderAmount && slot.getFluidStack() != null && slot.getFluidStack().amount > 1) ? NumberUtil.format(slot.getFluidStack().amount) : "", 1.0F);
        }
    }
}
