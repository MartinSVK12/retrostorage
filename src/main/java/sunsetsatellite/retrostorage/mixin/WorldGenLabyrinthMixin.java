package sunsetsatellite.retrostorage.mixin;


import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.retrostorage.ReSConfig;
import sunsetsatellite.retrostorage.ReSItems;
import sunsetsatellite.retrostorage.RetroStorage;

import java.util.Random;

@Mixin(
        value = WorldFeatureLabyrinth.class,
        remap = false
)
public class WorldGenLabyrinthMixin {
    @Inject(
            method = "pickCheckLootItem",
            at = @At("HEAD"),
            cancellable = true
    )
    public void pickCheckLootItem(Random random, CallbackInfoReturnable<ItemStack> cir) {
        if (ReSConfig.config.getBoolean("Other.goldenDiscLoot")) {
            int i = random.nextInt(200);
            if (i == 0) {
                cir.setReturnValue(new ItemStack(ReSItems.goldenDisc, 1));
            }
        }
    }
}
