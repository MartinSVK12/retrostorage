package sunsetsatellite.retrostorage.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.util.Formatting;
import sunsetsatellite.retrostorage.screen.CraftingProcessScreen;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;

public class CraftingProcessListWidget extends ListWidget {

    public CraftingProcessScreen parent;

    public CraftingProcessListWidget(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight, CraftingProcessScreen gui) {
        super(minecraft, width, height, top, bottom, itemHeight);
        parent = gui;
    }

    @Override
    protected int getEntryCount() {
        return parent.list.size();
    }

    @Override
    protected void entryClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelectedEntry(int index) {
        return false;
    }

    @Override
    protected int getEntriesHeight() {
        return this.parent.list.size() * 36 + 36;
    }

    @Override
    protected void renderBackground() {

    }

    @Override
    protected void renderEntry(int index, int x, int y, int i, Tessellator tessellator) {
        CraftingProcess.Step step = this.parent.list.get(index);
        int color = 0xFFFFFF;
        drawString(String.format("Step %d:", step.id), x + 2, y + 1, color);
        drawString(String.format("%s%s %s %s%s %sslot %d",
                !(step.output) ? Formatting.GREEN : Formatting.RED,
                !(step.output) ? "INSERT" : "EXTRACT",
                step.type == StackType.ITEM ? Formatting.WHITE + "item" : Formatting.BLUE + "fluid",
                !(step.output) ? Formatting.GREEN : Formatting.RED,
                !(step.output) ? "INTO" : "FROM",
                Formatting.WHITE,
                step.slot
        ), x + 2, y + 12, 0x808080);
        switch (step.type) {
            case ITEM: {
                if (step.stack != null) {
                    drawString(String.format("%s%dx %s", Formatting.LIGHT_PURPLE, step.stack.count, TranslationStorage.getInstance().getClientTranslation(step.stack.getTranslationKey())), x + 2, y + 12 + 10, 0xFFFFFF);
                } else {
                    drawString(Formatting.LIGHT_PURPLE + "null", x + 2, y + 12 + 10, 0xFFFFFF);
                }
                break;
            }
            case FLUID: {
                if (step.fluidStack != null) {
                    drawString(String.format("%s%dmB %s", Formatting.LIGHT_PURPLE, step.fluidStack.amount, step.fluidStack.getTranslatedName()), x + 2, y + 12 + 10, 0xFFFFFF);
                } else {
                    drawString(Formatting.LIGHT_PURPLE + "null", x + 2, y + 12 + 10, 0xFFFFFF);
                }
                break;
            }
        }
    }

    protected void drawString(String s, int x, int y, int color) {
        this.parent.drawTextWithShadow(this.parent.getFont(), s, x, y, color);
    }
}
