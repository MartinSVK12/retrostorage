package sunsetsatellite.retrostorage.compat.whatsthis;

import net.danygames2014.whatsthis.api.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.api.AttachesToMachines;
import sunsetsatellite.retrostorage.block.base.entity.NetworkDeviceBlockEntity;

public class NetworkDeviceProbeInfoProvider implements IProbeInfoProvider {
    @Override
    public String getID() {
        return RetroStorage.NAMESPACE.id("network_device").toString();
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData data) {
        BlockEntity blockEntity = new Vec3i(data.getPos()).getBlockEntity(world);
        if (blockEntity instanceof NetworkDeviceBlockEntity networkDevice) {
            if (networkDevice.getController() != null) {
                info.text(TextStyleClass.OK + "Device online!");
                if(networkDevice instanceof AttachesToMachines machine){
                    if(machine.getAttachedMachine() == null) return;
                    info.text(TextStyleClass.OK + "Connected to: "+ TranslationStorage.getInstance().get(machine.getAttachedMachine().getBlock().getTranslationKey()+".name"));
                }
            } else {
                info.text(TextStyleClass.WARNING + "Device offline.");
            }

        }
    }
}
