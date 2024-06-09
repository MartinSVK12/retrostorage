package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.retrostorage.util.crafting.CalculationResultType;

import java.util.List;

public class GuiRecipeItemSlot extends GuiSlot {
    public GuiTaskRequest parent;
    public GuiRecipeItemSlot(Minecraft minecraft, int i, int j, int k, int l, int i1, GuiTaskRequest gui) {
        super(minecraft, i, j, k, l, i1);
        parent = gui;
    }

    @Override
    protected int getSize() {
        if(parent.calculationResult.getCraftingPreviewInfo() != null){
            return parent.calculationResult.getCraftingPreviewInfo().size();
        }
        return 1;
    }

    @Override
    protected void elementClicked(int i, boolean bl) {

    }

    @Override
    protected boolean isSelected(int i) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    protected int getContentHeight() {
        if(parent.calculationResult.getCraftingPreviewInfo() != null){
            return parent.calculationResult.getCraftingPreviewInfo().size() * 36;
        }
        return 36;
    }

    @Override
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        if(parent.tile.network != null){
            if(parent.calculationResult.getType() == CalculationResultType.RECURSIVE){
                this.parent.drawString(this.parent.fontRenderer, "Recursion detected!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString(this.parent.fontRenderer, "Recipe requires item that requires itself.", j + 2, k + 12, 0xFFFF8080);
            } else if(parent.calculationResult.getType() == CalculationResultType.TOO_COMPLEX){
                this.parent.drawString(this.parent.fontRenderer, "Too complex!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString(this.parent.fontRenderer, "Requirements unavailable.", j + 2, k + 12, 0xFFFF8080);
            } else if(parent.calculationResult.getType() == CalculationResultType.NO_RECIPE){
                this.parent.drawString(this.parent.fontRenderer, "Can't craft!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString(this.parent.fontRenderer, "Uncraftable item.", j + 2, k + 12, 0xFFFF8080);
            } else {
                List<Pair<ItemStack, String>> list = parent.calculationResult.getCraftingPreviewInfo().toList();
                Pair<ItemStack, String> pair = list.get(i);
                ItemStack stack = pair.getLeft();
                int availableAmount = parent.tile.network.inventory.count(stack.itemID,stack.getMetadata());
                this.parent.drawString(this.parent.fontRenderer, stack.stackSize+"x "+stack.getDisplayName(), j + 2, k + 2, 0xFFFFFF);
                switch (pair.getRight()){
                    case "missing":
                        this.parent.drawString(this.parent.fontRenderer, "Missing: "+stack.stackSize+"x"+" | Available: "+availableAmount+"x", j + 2, k + 12, 0xFF8080);
                        break;
                    case "toCraft":
                        this.parent.drawString(this.parent.fontRenderer, "Will craft: "+stack.stackSize+"x"+" | Available: "+availableAmount+"x", j + 2, k + 12, 0x8080FF);
                        break;
                    case "toProcess":
                        this.parent.drawString(this.parent.fontRenderer, "Will process: "+stack.stackSize+"x"+" | Available: "+availableAmount+"x", j + 2, k + 12, 0x40CCFF);
                        break;
                    case "toTake":
                        this.parent.drawString(this.parent.fontRenderer, "Will use: "+stack.stackSize+"x | Available: "+availableAmount+"x", j + 2, k + 12, 0x80FF80);
                        break;
                }
            }
        }
    }
}
