package sunsetsatellite.retrostorage.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import sunsetsatellite.retrostorage.block.entity.*;

import static sunsetsatellite.retrostorage.RetroStorage.key;

public class ReSBlockEntities {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public static void registerBlockEntities(BlockEntityRegisterEvent event) {
        event.register(key("digital_terminal"), DigitalTerminalBlockEntity.class);
        event.register(key("digital_controller"), DigitalControllerBlockEntity.class);
        event.register(key("disc_drive"), DiscDriveBlockEntity.class);
        event.register(key("request_terminal"), RequestTerminalBlockEntity.class);
        event.register(key("assembler"), AssemblerBlockEntity.class);
        event.register(key("adv_assembler"), AdvAssemblerBlockEntity.class);
        event.register(key("recipe_encoder"), RecipeEncoderBlockEntity.class);
        event.register(key("adv_interface"), AdvInterfaceBlockEntity.class);
        event.register(key("process_programmer"), ProcessProgrammerBlockEntity.class);
        event.register(key("storage_bus"), StorageBusBlockEntity.class);
        event.register(key("redstone_emitter"), RedstoneEmitterBlockEntity.class);
        event.register(key("coprocessor"), CoprocessorBlockEntity.class);
        event.register(key("importer"), ImporterBlockEntity.class);
        event.register(key("exporter"), ExporterBlockEntity.class);
        event.register(key("fluid_disc_drive"), FluidDiscDriveBlockEntity.class);
        event.register(key("fluid_terminal"), FluidTerminalBlockEntity.class);
        event.register(key("fluid_storage_bus"), FluidStorageBusBlockEntity.class);
        event.register(key("fluid_importer"), FluidImporterBlockEntity.class);
        event.register(key("fluid_exporter"), FluidExporterBlockEntity.class);
        event.register(key("fluid_redstone_emitter"), FluidRedstoneEmitterBlockEntity.class);
    }
}
