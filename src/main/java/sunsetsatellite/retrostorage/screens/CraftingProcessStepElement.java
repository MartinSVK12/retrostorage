package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;

public class CraftingProcessStepElement extends SlotElement {
    public ScreenCraftingProcess parent;

    public CraftingProcessStepElement(Minecraft minecraft, int i, int j, int k, int l, int slotHeight, ScreenCraftingProcess gui) {
        super(minecraft, i, j, k, l, slotHeight);
        parent = gui;
    }

    protected void drawString(String s, int x, int y, int color) {
        this.parent.drawStringNoShadow(this.parent.getFont(), s, x, y, color);
    }

	@Override
	protected int getMaxPosition() {
		return this.parent.list.size() * (36 * 1) + 36;
	}

	@Override
	protected int getItemCount() {
		return parent.list.size();
	}

	@Override
	protected void selectItem(int itemIndex, boolean doubleClicked) {

	}

	@Override
	protected boolean isSelectedItem(int itemIndex) {
		return false;
	}

	@Override
	protected void renderHoleBackground() {

	}

	@Override
	protected void renderItem(int index, int x, int y, int height, TessellatorGeneral tessellator) {
		CraftingProcess.Step step = this.parent.list.get(index);
		int color = 0xFFFFFF;
		drawString(String.format("Step %d:",step.id),x+2,y+1, color);
		drawString(String.format("%s%s %s %s%s %sslot %d",
			!(step.output) ? TextFormatting.LIME : TextFormatting.RED,
			!(step.output) ? "INSERT" : "EXTRACT",
			step.type == StackType.ITEM ? TextFormatting.WHITE + "item" : TextFormatting.LIGHT_BLUE + "fluid",
			!(step.output) ? TextFormatting.LIME : TextFormatting.RED,
			!(step.output) ? "INTO" : "FROM",
			TextFormatting.WHITE,
			step.slot
		),x+2,y+12, 0x808080);
		switch (step.type){
			case ITEM: {
				if (step.stack != null) {
					drawString(String.format("%s%dx %s",TextFormatting.MAGENTA,step.stack.stackSize,step.stack.getDisplayName()),x+2,y+12+10, 0xFFFFFF);
				} else {
					drawString(TextFormatting.MAGENTA+"null",x+2,y+12+10, 0xFFFFFF);
				}
				break;
			}
			case FLUID: {
				if (step.fluidStack != null) {
					drawString(String.format("%s%dmB %s",TextFormatting.MAGENTA,step.fluidStack.amount,step.fluidStack.toItemStack().getDisplayName()),x+2,y+12+10, 0xFFFFFF);
				} else {
					drawString(TextFormatting.MAGENTA+"null",x+2,y+12+10, 0xFFFFFF);
				}
				break;
			}
		}
	}
}
