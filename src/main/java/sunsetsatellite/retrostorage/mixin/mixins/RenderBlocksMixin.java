package sunsetsatellite.retrostorage.mixin.mixins;




import net.minecraft.client.render.RenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.util.RenderCableBlock;

@Mixin(
        value={RenderBlocks.class},
        remap = false
)

public class RenderBlocksMixin {
    @Shadow private WorldSource blockAccess;

    @Inject(
            method = "renderBlockByRenderType",
            at = @At("HEAD"),
            cancellable = true)
    void renderBlockByRenderType(Block block, int renderType, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if(block.id == RetroStorage.networkCable.id){
            cir.setReturnValue(RenderCableBlock.render((RenderBlocks) ((Object)this),this.blockAccess,x,y,z,block,0));
        }
    }
}
