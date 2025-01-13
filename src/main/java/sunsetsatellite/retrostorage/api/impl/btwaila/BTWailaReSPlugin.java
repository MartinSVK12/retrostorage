package sunsetsatellite.retrostorage.api.impl.btwaila;

import org.slf4j.Logger;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.api.impl.btwaila.tooltip.NetworkDeviceTooltip;
import toufoumaster.btwaila.entryplugins.waila.BTWailaCustomTooltipPlugin;
import toufoumaster.btwaila.tooltips.TooltipRegistry;

public class BTWailaReSPlugin implements BTWailaCustomTooltipPlugin {
    @Override
    public void initializePlugin(TooltipRegistry tooltipRegistry, Logger logger) {
        logger.info("Loading tooltips from "+ RetroStorage.MOD_ID+"..");
        tooltipRegistry.register(new NetworkDeviceTooltip());
    }
}
