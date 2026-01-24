package sunsetsatellite.retrostorage.block.entity;

import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;

public class DigitalTerminalBlockEntity extends NetworkDeviceBlockEntity {
    public int page = 0;
    public int pages = 0;

    @Override
    public String getName() {
        return "container.retrostorage.digitalTerminal";
    }
}
