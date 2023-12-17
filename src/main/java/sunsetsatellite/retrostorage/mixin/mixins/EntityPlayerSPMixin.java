package sunsetsatellite.retrostorage.mixin.mixins;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sunsetsatellite.retrostorage.interfaces.mixins.IOpenGUI;

@Mixin(
        value={EntityPlayerSP.class},
        remap = false
)

public class EntityPlayerSPMixin implements IOpenGUI {
    @Shadow
    protected Minecraft mc;

    @Override
    public void displayGUI(GuiScreen gui) {
        this.mc.displayGuiScreen(gui);
    }
}
