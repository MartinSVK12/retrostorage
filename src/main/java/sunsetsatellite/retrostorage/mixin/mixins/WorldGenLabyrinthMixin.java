package sunsetsatellite.retrostorage.mixin.mixins;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.WorldGenLabyrinth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.Config;

import java.util.Random;

@Mixin(
        value = WorldGenLabyrinth.class,
        remap = false
)
public class WorldGenLabyrinthMixin {
    @Inject(
            method = "pickCheckLootItem",
            at = @At("HEAD"),
            cancellable = true
    )
    public void pickCheckLootItem(Random random, CallbackInfoReturnable<ItemStack> cir){
        if(Config.getFromConfig("enableGoldenDiscLoot",0) == 1){
            int i = random.nextInt(200);
            if(i == 0){
                cir.setReturnValue(new ItemStack(RetroStorage.goldenDisc,1));
            }
        }
    }
}
