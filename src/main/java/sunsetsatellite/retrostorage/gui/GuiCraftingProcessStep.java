package sunsetsatellite.retrostorage.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.retrostorage.util.StackType;
import sunsetsatellite.retrostorage.util.crafting.CraftingProcess;

public class GuiCraftingProcessStep extends GuiSlot {
    public GuiCraftingProcess parent;

    public GuiCraftingProcessStep(Minecraft minecraft, int i, int j, int k, int l, int slotHeight, GuiCraftingProcess gui) {
        super(minecraft, i, j, k, l, slotHeight);
        parent = gui;
    }

    @Override
    protected int getSize() {
        return parent.list.size();
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
        int i = 1;
        /*for (CraftingTask craftingTask : this.parent.list) {
            i += craftingTask.nodes.all().size();
        }*/
        return this.parent.list.size() * (36 * i) + 36;
    }

    @Override
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
        CraftingProcess.Step step = this.parent.list.get(i);
        int color = 0xFFFFFF;
        drawString(String.format("Step %d:",step.id),j+2,k+1, color);
        drawString(String.format("%s%s %s %s%s %sslot %d",
                !(step.output) ? TextFormatting.LIME : TextFormatting.RED,
                !(step.output) ? "INSERT" : "EXTRACT",
                step.type == StackType.ITEM ? TextFormatting.WHITE + "item" : TextFormatting.LIGHT_BLUE + "fluid",
                !(step.output) ? TextFormatting.LIME : TextFormatting.RED,
                !(step.output) ? "INTO" : "FROM",
                TextFormatting.WHITE,
                step.slot
        ),j+2,k+12, 0x808080);
        switch (step.type){
            case ITEM: {
                if (step.stack != null) {
                    drawString(String.format("%s%dx %s",TextFormatting.MAGENTA,step.stack.stackSize,step.stack.getDisplayName()),j+2,k+12+10, 0xFFFFFF);
                } else {
                    drawString(TextFormatting.MAGENTA+"null",j+2,k+12+10, 0xFFFFFF);
                }
                break;
            }
            case FLUID: {
                if (step.fluidStack != null) {
                    drawString(String.format("%s%dmB %s",TextFormatting.MAGENTA,step.fluidStack.amount,step.fluidStack.toItemStack().getDisplayName()),j+2,k+12+10, 0xFFFFFF);
                } else {
                    drawString(TextFormatting.MAGENTA+"null",j+2,k+12+10, 0xFFFFFF);
                }
                break;
            }
        }
        /*if (task.isStarted()) {
            color = 0x00FF00;
            int size = task.nodes.all().size();
            drawString(String.format("%dx %s", task.getQuantity(), task.getCraftable().getOutput().get(0).forceGetItem().getDisplayName()), j + 2, k + 1, color);
            drawString(String.format("%d s | %d%%", (System.currentTimeMillis() - task.getStartTime()) / 1000, task.getCompletionPercentage()), j + 2, k + 12, 0xFFFFFF);
            drawString(String.format("%d subtask%s remain%s.", size, size > 1 ? "s" : "", size <= 1 ? "s" : ""), j + 2, k + 12 + 10, 0x808080);
        } else {
            drawString(String.format("%dx %s", task.getQuantity(), task.getCraftable().getOutput().get(0).forceGetItem().getDisplayName()), j + 2, k + 1, color);
            drawString("Waiting..", j + 2, k + 12, 0x808080);
        }*/

        /*int y = k + 12 + 20;
        int x = j + 2;
        for (Node node : task.nodes.all()) {
            if (node instanceof ProcessNode) {
                ProcessNode pNode = (ProcessNode) node;
                ProcessingState state = pNode.getState();
                IProcessor processor = parent.network.findProcessorWithNode(pNode);
                color = (state == ProcessingState.BLOCKED || state == ProcessingState.NO_MACHINE) ? 0xFF0000 : (state == ProcessingState.ALREADY_IN_USE) ? 0xFF8C00 : (state == ProcessingState.ACTIVE) ? 0x00FF00 : 0xFFFFFF;
                if (state == ProcessingState.WAITING) {
                    drawString(" " + node.getClass().getSimpleName().replace("Node", "") + ": " + node.getPattern().getOutput().get(0).forceGetItem().getDisplayName(), x, y += 10, color);
                    drawString(" Waiting..", x, y += 10, 0x808080);
                    y += 10;
                } else {
                    drawString(" " + node.getClass().getSimpleName().replace("Node", "") + ": " + node.getPattern().getOutput().get(0).forceGetItem().stackSize * node.getTotalQuantity() + "x " + node.getPattern().getOutput().get(0).forceGetItem().getDisplayName(), x, y += 10, color);
                    drawString(String.format(" %d/%d (%d%%) | %s", pNode.getFinishedQuantity(), pNode.getTotalQuantity(), pNode.getCompletionPercentage(), pNode.getState()), x, y += 10, 0xFFFFFF);
                    drawString(" Processor: " + (processor == null ? "None" : processor.toString().replace("TileEntity", "")), x, y += 10, 0x808080);
                }
            } else {
                drawString(" " + node.getClass().getSimpleName().replace("Node", "") + ": " + node.getPattern().getOutput().get(0).forceGetItem().stackSize + "x " + node.getPattern().getOutput().get(0).forceGetItem().getDisplayName(), x, y += 10, 0xFFFFFF);
                drawString(" Waiting..", x, y += 10, 0x808080);
                y += 10;
            }
            y += 10;
        }*/
    }

    protected void drawString(String s, int x, int y, int color) {
        this.parent.drawString(this.parent.fontRenderer, s, x, y, color);
    }
}
