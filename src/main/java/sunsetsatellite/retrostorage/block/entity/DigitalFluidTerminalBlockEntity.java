package sunsetsatellite.retrostorage.block.entity;


import net.minecraft.entity.player.PlayerEntity;

public class DigitalFluidTerminalBlockEntity extends NetworkDeviceBlockEntity {

    public DigitalFluidTerminalBlockEntity() {}

    public void tick() {
        super.tick();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity entityplayer) {
        return super.canPlayerUse(entityplayer);
    }

    public int page = 0;
    public int pages = 0;
}
