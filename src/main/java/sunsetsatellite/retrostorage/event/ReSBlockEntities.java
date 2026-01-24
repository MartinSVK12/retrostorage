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
        event.register(DigitalTerminalBlockEntity.class, key("digital_terminal"));
        event.register(DigitalControllerBlockEntity.class, key("digital_controller"));
        event.register(DiscDriveBlockEntity.class, key("disc_drive"));
        event.register(RequestTerminalBlockEntity.class, key("request_terminal"));
        event.register(AssemblerBlockEntity.class, key("assembler"));
        event.register(AdvAssemblerBlockEntity.class, key("adv_assembler"));
        event.register(RecipeEncoderBlockEntity.class, key("recipe_encoder"));
        event.register(AdvInterfaceBlockEntity.class, key("adv_interface"));
        event.register(ProcessProgrammerBlockEntity.class, key("process_programmer"));
        event.register(StorageBusBlockEntity.class, key("storage_bus"));
        event.register(RedstoneEmitterBlockEntity.class, key("redstone_emitter"));
        event.register(CoprocessorBlockEntity.class, key("coprocessor"));
        event.register(ImporterBlockEntity.class, key("importer"));
        event.register(ExporterBlockEntity.class, key("exporter"));
        event.register(FluidDiscDriveBlockEntity.class, key("fluid_disc_drive"));
        event.register(FluidTerminalBlockEntity.class, key("fluid_terminal"));
        event.register(FluidStorageBusBlockEntity.class, key("fluid_storage_bus"));
        event.register(FluidImporterBlockEntity.class, key("fluid_importer"));
        event.register(FluidExporterBlockEntity.class, key("fluid_exporter"));
        event.register(FluidRedstoneEmitterBlockEntity.class, key("fluid_redstone_emitter"));
    }
}
