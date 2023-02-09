package sunsetsatellite.retrostorage.mixin.mixins;

import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.interfaces.mixins.IGuiContainer;
import sunsetsatellite.retrostorage.items.ItemAdvRecipeDisc;
import sunsetsatellite.retrostorage.items.ItemPortableCell;
import sunsetsatellite.retrostorage.items.ItemRecipeDisc;
import sunsetsatellite.retrostorage.items.ItemStorageDisc;

import java.util.ArrayList;

@Debug( export = true )
@Mixin(
        value = GuiContainer.class,
        remap = false
)
public class GuiContainerMixin extends GuiScreen
    implements IGuiContainer
{

    @Shadow private static RenderItem itemRenderer;

    @Inject(
            method = "drawScreen",
            remap = false,
            at = @At(value = "INVOKE",target = "Lnet/minecraft/src/GuiContainer;formatDescription(Ljava/lang/String;I)Ljava/lang/String;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void setDescription(int x, int y, float renderPartialTicks, CallbackInfo ci, int centerX, int centerY, Slot slot, InventoryPlayer inventoryplayer, StringTranslate trans, StringBuilder text, boolean multiLine, boolean control, boolean shift, boolean showDescription, boolean isCrafting, String itemName, String itemNick, boolean debug){
        ItemStack stack = slot.getStack();
        if(stack != null && stack.getItem() == RetroStorage.slotIdFinder){
            text.append(ChatColor.magenta).append("ID of this slot is: ").append(slot.id).append(" (").append(slot.getClass().getSimpleName()).append(")").append("\n");
        }
        if(stack != null && (stack.getItem() == RetroStorage.mobileTerminal || stack.getItem() == RetroStorage.mobileRequestTerminal || stack.getItem() == RetroStorage.linkingCard)){
            if(stack.tag.hasKey("position")){
                if(stack.tag.getCompoundTag("position").hasKey("x") && stack.tag.getCompoundTag("position").hasKey("y") && stack.tag.getCompoundTag("position").hasKey("z")){
                    text.append(ChatColor.magenta).append("Bound to block at X:").append(stack.tag.getCompoundTag("position").getInteger("x")).append(" Y:").append(stack.tag.getCompoundTag("position").getInteger("y")).append(" Z:").append(stack.tag.getCompoundTag("position").getInteger("z")).append(".\n");
                } else {
                    text.append(ChatColor.magenta).append("Unbound.\n");
                }
            } else {
                text.append(ChatColor.magenta).append("Unbound.\n");
            }
        }
        if(stack != null && (stack.getItem() instanceof ItemStorageDisc || stack.getItem() instanceof ItemPortableCell)){
            text.append(ChatColor.magenta).append(stack.tag.getCompoundTag("disc")).append("\n");
        }
        if(stack != null && stack.getItem() instanceof ItemAdvRecipeDisc){
            if(stack.tag.getCompoundTag("disc").func_28110_c().size() > 0){
                text.append(ChatColor.magenta).append(stack.tag.getCompoundTag("disc").getCompoundTag("tasks").func_28110_c().size()).append(" steps.").append("\n");
            } else if(stack.tag.getCompoundTag("disc").func_28110_c().size() == 0){
                text.append(ChatColor.magenta).append("0 steps.").append("\n");
            }
            NBTTagCompound tasksNBT = stack.tag.getCompoundTag("disc").getCompoundTag("tasks");
            ArrayList<NBTTagCompound> tasks = new ArrayList<>();
            tasks.addAll(tasksNBT.func_28110_c());
            ItemStack output = RetroStorage.getMainOutputOfProcess(tasks);
            if(output != null){
                String name = StringTranslate.getInstance().translateKey(output.getItemName() + ".name");
                text.append(ChatColor.purple).append("Output: ").append(output.stackSize).append("x ").append(name).append("\n");
            }
        }
    };

    public void drawItemStack(ItemStack stack, int x, int y) {
        if(stack != null) {
            GL11.glPushMatrix();
            GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, stack, x, y, 1.0F);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }
    }

}
