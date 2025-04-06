package sunsetsatellite.retrostorage.screens;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.BlockLogicFluid;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import sunsetsatellite.retrostorage.util.crafting.CalculationResultType;

import java.util.List;

public class RecipeItemSlotElement extends SlotElement {
    public ScreenTaskRequest parent;

    public RecipeItemSlotElement(Minecraft minecraft, int i, int j, int k, int l, int i1, ScreenTaskRequest gui) {
        super(minecraft, i, j, k, l, i1);
        parent = gui;
    }

    @Override
    protected int getSize() {
        if (parent.calculationResult.getCraftingPreviewInfo() != null) {
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
        if (parent.calculationResult.getCraftingPreviewInfo() != null) {
            return parent.calculationResult.getCraftingPreviewInfo().size() * 36;
        }
        return 36;
    }

    @Override
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        if (parent.tile.network != null) {
            if (parent.calculationResult.getType() == CalculationResultType.RECURSIVE) {
                this.parent.drawString(this.parent.getFont(), "Recursion detected!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString(this.parent.getFont(), "Recipe requires item that requires itself.", j + 2, k + 12, 0xFFFF8080);
            } else if (parent.calculationResult.getType() == CalculationResultType.TOO_COMPLEX) {
                this.parent.drawString(this.parent.getFont(), "Too complex!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString(this.parent.getFont(), "Requirements unavailable.", j + 2, k + 12, 0xFFFF8080);
            } else if (parent.calculationResult.getType() == CalculationResultType.NO_RECIPE) {
                this.parent.drawString(this.parent.getFont(), "Can't craft!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString(this.parent.getFont(), "Uncraftable item.", j + 2, k + 12, 0xFFFF8080);
            } else if(parent.calculationResult.getType() == CalculationResultType.ERROR) {
                this.parent.drawString(this.parent.getFont(), "Error occurred!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString(this.parent.getFont(), "Request cannot be fulfilled.", j + 2, k + 2, 0xFFFF8080);
            } else {
                List<Pair<ItemStack, String>> list = parent.calculationResult.getCraftingPreviewInfo().toList();
                Pair<ItemStack, String> pair = list.get(i);
                ItemStack stack = pair.getLeft();
                long availableAmount = parent.network.countItems(stack.itemID,stack.getMetadata(),stack.getData());
                long availableAmountFluids = parent.network.countFluids(stack.itemID);
                if(stack.itemID < 16384 && Blocks.blocksList[stack.itemID].getLogic() instanceof BlockLogicFluid){
                    this.parent.drawString(this.parent.getFont(), stack.stackSize + "mB " + stack.getDisplayName(), j + 2, k + 2, 0xFFFFFF);
                } else {
                    this.parent.drawString(this.parent.getFont(), stack.stackSize + "x " + stack.getDisplayName(), j + 2, k + 2, 0xFFFFFF);
                }
                switch (pair.getRight()) {
                    case "missing":
                        this.parent.drawString(this.parent.getFont(), "Missing: " + stack.stackSize + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0xFF8080);
                        break;
                    case "missingFluids":
                        this.parent.drawString(this.parent.getFont(), "Missing: " + stack.stackSize + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0xFF8080);
                        break;
                    case "toCraft":
                        this.parent.drawString(this.parent.getFont(), "Will craft: " + stack.stackSize + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0x8080FF);
                        break;
                    case "toCraftFluids":
                        this.parent.drawString(this.parent.getFont(), "Will craft: " + stack.stackSize + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x8080FF);
                        break;
                    case "toProcess":
                        this.parent.drawString(this.parent.getFont(), "Will process: " + stack.stackSize + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0x40CCFF);
                        break;
                    case "toProcessFluids":
                        this.parent.drawString(this.parent.getFont(), "Will process: " + stack.stackSize + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x40CCFF);
                        break;
                    case "toTake":
                        this.parent.drawString(this.parent.getFont(), "Will use: " + stack.stackSize + "x | Available: " + availableAmount + "x", j + 2, k + 12, 0x80FF80);
                        break;
                    case "toTakeFluids":
                        this.parent.drawString(this.parent.getFont(), "Will use: " + stack.stackSize + "mB | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x80FF80);
                        break;
                }
            }
        }
    }
}
