package sunsetsatellite.retrostorage.screen.widget;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.api.Processor;
import sunsetsatellite.retrostorage.screen.RequestQueueScreen;
import sunsetsatellite.retrostorage.util.ProcessingState;
import sunsetsatellite.retrostorage.util.crafting.CraftingTask;
import sunsetsatellite.retrostorage.util.crafting.Node;
import sunsetsatellite.retrostorage.util.crafting.ProcessNode;

public class TaskListWidget extends ListWidget {
    public RequestQueueScreen parent;

    public TaskListWidget(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight, RequestQueueScreen parent) {
        super(minecraft, width, height, top, bottom, itemHeight);
        this.parent = parent;
    }

    @Override
    protected int getEntryCount() {
        return parent.tasks.size();
    }

    @Override
    protected int getEntriesHeight() {
        int i = 1;
        for (CraftingTask task : this.parent.tasks) {
            i += task.nodes.all().size();
        }
        return parent.tasks.size() * (36 * i) + 36;
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
    protected void renderEntry(int index, int i, int k, int l, Tessellator tessellator) {
        CraftingTask task = this.parent.tasks.get(index);
        int color = 0xFFFFFF;
        if (task.isStarted()) {
            color = 0x00FF00;
            int size = task.nodes.all().size();
            drawString(String.format("%dx %s", task.getQuantity(), TranslationStorage.getInstance().getClientTranslation(task.getCraftable().getOutput().get(0).forceGetItem().getTranslationKey())), i + 2, k + 1, color);
            drawString(String.format("%d s | %d%%", (System.currentTimeMillis() - task.getStartTime()) / 1000, task.getCompletionPercentage()), i + 2, k + 12, 0xFFFFFF);
            drawString(String.format("%d subtask%s remain%s.", size, size > 1 ? "s" : "", size <= 1 ? "s" : ""), i + 2, k + 12 + 10, 0x808080);
        } else {
            drawString(String.format("%dx %s", task.getQuantity(), TranslationStorage.getInstance().getClientTranslation(task.getCraftable().getOutput().get(0).forceGetItem().getTranslationKey())), i + 2, k + 1, color);
            drawString("Waiting..", i + 2, k + 12, 0x808080);
        }

        int y = k + 12 + 20;
        int x = i + 2;
        for (Node node : task.nodes.all()) {
            if (node instanceof ProcessNode pNode) {
                ProcessingState state = pNode.getState();
                Processor processor = parent.controller.findProcessorWithNode(pNode);
                BlockEntity machine = null;
                if(processor != null){
                    if(processor.getConnectedTile() != null){
                        machine = processor.getConnectedTile();
                    }
                }
                String machineInfo = machine == null ? "None" : machine.getClass().getSimpleName().replace("TileEntity", "");
                if(machine != null){
                    machineInfo += " at " + new Vec3i(machine.x, machine.y, machine.z);
                }
                color = (state == ProcessingState.BLOCKED || state == ProcessingState.NO_MACHINE) ? 0xFF0000 : (state == ProcessingState.ALREADY_IN_USE) ? 0xFF8C00 : (state == ProcessingState.ACTIVE) ? 0x00FF00 : 0xFFFFFF;
                if (state == ProcessingState.WAITING) {
                    drawString(" " + node.getClass().getSimpleName().replace("Node", "") + ": " + TranslationStorage.getInstance().getClientTranslation(node.getPattern().getOutput().get(0).forceGetItem().getTranslationKey()), x, y += 10, color);
                    drawString(" Waiting..", x, y += 10, 0x808080);
                    y += 10;
                } else {
                    drawString(" " + node.getClass().getSimpleName().replace("Node", "") + ": " + node.getPattern().getOutput().get(0).forceGetItem().count * node.getTotalQuantity() + "x " + TranslationStorage.getInstance().getClientTranslation(node.getPattern().getOutput().get(0).forceGetItem().getTranslationKey()), x, y += 10, color);
                    drawString(String.format(" %d/%d (%d%%) | %s", pNode.getFinishedQuantity(), pNode.getTotalQuantity(), pNode.getCompletionPercentage(), pNode.getState()), x, y += 10, 0xFFFFFF);
                    drawString(" Processor: " + machineInfo, x, y += 10, 0x808080);
                }
            } else {
                drawString(" " + node.getClass().getSimpleName().replace("Node", "") + ": " + node.getPattern().getOutput().get(0).forceGetItem().count + "x " + TranslationStorage.getInstance().getClientTranslation(node.getPattern().getOutput().get(0).forceGetItem().getTranslationKey()), x, y += 10, 0xFFFFFF);
                drawString(" Waiting..", x, y += 10, 0x808080);
                y += 10;
            }
            y += 10;
        }
    }

    protected void drawString(String s, int x, int y, int color) {
        this.parent.drawTextWithShadow(this.parent.getFont(), s, x, y, color);
    }
}
