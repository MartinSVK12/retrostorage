package sunsetsatellite.retrostorage.api.impl.tmb;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.api.impl.tmb.fillers.EncoderRecipeFiller;
import sunsetsatellite.retrostorage.screens.ScreenRecipeEncoder;
import turing.tmb.TMB;
import turing.tmb.api.ITMBPlugin;
import turing.tmb.api.TMBEntrypoint;
import turing.tmb.api.runtime.ITMBRuntime;

public class TMBReSPlugin implements ITMBPlugin, TMBEntrypoint {

    @Override
    public void registerRecipeFilling(ITMBRuntime runtime) {
        runtime.getRecipeFillers().put(ScreenRecipeEncoder.class, new EncoderRecipeFiller());
    }

    @Override
    public void onGatherPlugins(boolean isReload) {
        TMB.LOGGER.info("Loading plugin: {} from {}", this.getClass().getSimpleName(), RetroStorage.MOD_ID);
        TMB.registerPlugin(this);
    }
}
