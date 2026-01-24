package sunsetsatellite.retrostorage.screen.widget;

import com.mojang.datafixers.util.Pair;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import sunsetsatellite.retrostorage.screen.TaskRequestScreen;
import sunsetsatellite.retrostorage.util.crafting.CalculationResultType;

import java.util.List;

public class RecipeIngredientListWidget extends ListWidget {
    public TaskRequestScreen parent;

    public RecipeIngredientListWidget(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight, TaskRequestScreen gui) {
        super(minecraft, width, height, top, bottom, itemHeight);
        this.parent = gui;
    }

    @Override
    protected int getEntryCount() {
        if (parent.calculationResult.getCraftingPreviewInfo() != null) {
            return parent.calculationResult.getCraftingPreviewInfo().size();
        }
        return 1;
    }

    @Override
    protected int getEntriesHeight() {
        if (parent.calculationResult.getCraftingPreviewInfo() != null) {
            return parent.calculationResult.getCraftingPreviewInfo().size() * 36;
        }
        return 36;
    }

    @Override
    protected void entryClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelectedEntry(int index) {
        return false;
    }

    @Override
    protected void renderBackground() {

    }

    @Override
    protected void renderEntry(int index, int j, int k, int l, Tessellator tessellator) {
        if (parent.tile.network != null) {
            if (parent.calculationResult.getType() == CalculationResultType.RECURSIVE) {
                this.parent.drawTextWithShadow(this.parent.getFont(), "Recursion detected!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawTextWithShadow(this.parent.getFont(), "Recipe requires item that requires itself.", j + 2, k + 12, 0xFFFF8080);
            } else if (parent.calculationResult.getType() == CalculationResultType.TOO_COMPLEX) {
                this.parent.drawTextWithShadow(this.parent.getFont(), "Too complex!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawTextWithShadow(this.parent.getFont(), "Requirements unavailable.", j + 2, k + 12, 0xFFFF8080);
            } else if (parent.calculationResult.getType() == CalculationResultType.NO_RECIPE) {
                this.parent.drawTextWithShadow(this.parent.getFont(), "Can't craft!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawTextWithShadow(this.parent.getFont(), "Uncraftable item.", j + 2, k + 12, 0xFFFF8080);
            } else if (parent.calculationResult.getType() == CalculationResultType.ERROR) {
                this.parent.drawTextWithShadow(this.parent.getFont(), "Error occurred!", j + 2, k + 2, 0xFFFF0000);
                this.parent.drawTextWithShadow(this.parent.getFont(), "Request cannot be fulfilled.", j + 2, k + 2, 0xFFFF8080);
            } else {
                List<Pair<ItemStack, String>> list = parent.calculationResult.getCraftingPreviewInfo().toList();
                Pair<ItemStack, String> pair = list.get(index);
                ItemStack stack = pair.getFirst();
                long availableAmount = parent.network.countItems(stack.itemId, stack.getMaxDamage(), stack.getStationNbt());
                long availableAmountFluids = parent.network.countFluids(stack.itemId);
                if (FluidRegistry.get(stack.itemId) != null) {
                    this.parent.drawTextWithShadow(this.parent.getFont(), stack.count + "mB " + TranslationStorage.getInstance().getClientTranslation(stack.getTranslationKey()), j + 2, k + 2, 0xFFFFFF);
                } else {
                    this.parent.drawTextWithShadow(this.parent.getFont(), stack.count + "x " + TranslationStorage.getInstance().getClientTranslation(stack.getTranslationKey()), j + 2, k + 2, 0xFFFFFF);
                }
                switch (pair.getSecond()) {
                    case "missing":
                        this.parent.drawTextWithShadow(this.parent.getFont(), "Missing: " + stack.count + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0xFF8080);
                        break;
                    case "missingFluids":
                        this.parent.drawTextWithShadow(this.parent.getFont(), "Missing: " + stack.count + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0xFF8080);
                        break;
                    case "toCraft":
                        this.parent.drawTextWithShadow(this.parent.getFont(), "Will craft: " + stack.count + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0x8080FF);
                        break;
                    case "toCraftFluids":
                        this.parent.drawTextWithShadow(this.parent.getFont(), "Will craft: " + stack.count + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x8080FF);
                        break;
                    case "toProcess":
                        this.parent.drawTextWithShadow(this.parent.getFont(), "Will process: " + stack.count + "x" + " | Available: " + availableAmount + "x", j + 2, k + 12, 0x40CCFF);
                        break;
                    case "toProcessFluids":
                        this.parent.drawTextWithShadow(this.parent.getFont(), "Will process: " + stack.count + "mB" + " | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x40CCFF);
                        break;
                    case "toTake":
                        this.parent.drawTextWithShadow(this.parent.getFont(), "Will use: " + stack.count + "x | Available: " + availableAmount + "x", j + 2, k + 12, 0x80FF80);
                        break;
                    case "toTakeFluids":
                        this.parent.drawTextWithShadow(this.parent.getFont(), "Will use: " + stack.count + "mB | Available: " + availableAmountFluids + "mB", j + 2, k + 12, 0x80FF80);
                        break;
                }
            }
        }
    }
}
