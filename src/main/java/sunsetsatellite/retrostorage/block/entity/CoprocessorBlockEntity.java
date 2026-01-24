package sunsetsatellite.retrostorage.block.entity;

import sunsetsatellite.retrostorage.api.Coprocessor;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;

public class CoprocessorBlockEntity extends NetworkDeviceBlockEntity implements Coprocessor {
    @Override
    public String getName() {
        return "container.retrostorage.coprocessor";
    }
}
