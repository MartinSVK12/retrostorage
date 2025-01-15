package sunsetsatellite.retrostorage.api.impl.btwaila.tooltip;

import sunsetsatellite.retrostorage.tiles.*;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class NetworkDeviceTooltip extends TileTooltip<TileEntityNetworkDevice> {
    @Override
    public void initTooltip() {
        this.addClass(TileEntityDigitalController.class);
        this.addClass(TileEntityDigitalTerminal.class);
        this.addClass(TileEntityAssembler.class);
        this.addClass(TileEntityExporter.class);
        this.addClass(TileEntityImporter.class);
        this.addClass(TileEntityDigitalFluidTerminal.class);
        this.addClass(TileEntityFluidExporter.class);
        this.addClass(TileEntityFluidImporter.class);
        this.addClass(TileEntityStorageBus.class);
        this.addClass(TileEntityFluidStorageBus.class);
        this.addClass(TileEntityRequestTerminal.class);
        this.addClass(TileEntityDiscDrive.class);
        this.addClass(TileEntityFluidDiscDrive.class);
        this.addClass(TileEntityRedstoneEmitter.class);
        this.addClass(TileEntityAdvInterface.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityNetworkDevice tile, AdvancedInfoComponent c) {
        c.drawStringWithShadow(tile.getController() == null ? "Device offline." : "Device online!",0,0xFFFFFFFF);
    }

}
