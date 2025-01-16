package sunsetsatellite.retrostorage.screen;


import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import sunsetsatellite.retrostorage.util.crafting.calc.CalculationResultType;

import java.util.List;

public class RecipeItemSlotScreen extends SlotScreen {
    public TaskRequestScreen parent;

    public RecipeItemSlotScreen(Minecraft minecraft, int i, int j, int k, int l, int i1, TaskRequestScreen gui) {
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
                this.parent.drawString("Recursion detected!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString("Recipe requires item that requires itself.", j + 2, k + 12, 0xFFFF8080);
            } else if (parent.calculationResult.getType() == CalculationResultType.TOO_COMPLEX) {
                this.parent.drawString("Too complex!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString("Requirements unavailable.", j + 2, k + 12, 0xFFFF8080);
            } else if (parent.calculationResult.getType() == CalculationResultType.NO_RECIPE) {
                this.parent.drawString("Can't craft!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawString("Uncraftable item.", j + 2, k + 12, 0xFFFF8080);
            } else {
                List<Pair<ItemStack, String>> list = parent.calculationResult.getCraftingPreviewInfo().toList();
                Pair<ItemStack, String> pair = list.get(i);
                ItemStack stack = pair.getFirst();
                long availableAmount = parent.network.countItems(stack.itemId,stack.getDamage(),stack.getStationNbt());
                long availableAmountFluids = parent.network.countFluids(stack.itemId);
                //TODO:
                /*if(stack.itemId < 16384 && Block){
                    this.parent.drawString(stack.count + "mB " + stack.getDisplayName(), j + 2, k + 2, 0xFFFFFF);
                } else {*/
                    this.parent.drawString(stack.count + "x " + TranslationStorage.getInstance().getClientTranslation(stack.getTranslationKey()), j + 2, k + 2, 0xFFFFFF);
               // }
                switch (pair.getSecond()) {
                    case "missing":
                        this.parent.drawString("Missing: " + stack.count + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0xFF8080);
                        break;
                    case "missingFluids":
                        this.parent.drawString("Missing: " + stack.count + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0xFF8080);
                        break;
                    case "toCraft":
                        this.parent.drawString("Will craft: " + stack.count + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0x8080FF);
                        break;
                    case "toCraftFluids":
                        this.parent.drawString("Will craft: " + stack.count + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x8080FF);
                        break;
                    case "toProcess":
                        this.parent.drawString("Will process: " + stack.count + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0x40CCFF);
                        break;
                    case "toProcessFluids":
                        this.parent.drawString("Will process: " + stack.count + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x40CCFF);
                        break;
                    case "toTake":
                        this.parent.drawString("Will use: " + stack.count + "x | Available: " + availableAmount + "x", j + 2, k + 12, 0x80FF80);
                        break;
                    case "toTakeFluids":
                        this.parent.drawString("Will use: " + stack.count + "mB | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x80FF80);
                        break;
                }
            }
        }
    }
}
