package sunsetsatellite.retrostorage.event;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import sunsetsatellite.retrostorage.block.entity.*;

public class ReSBlockEntities {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public static void registerBlockEntities(BlockEntityRegisterEvent event) {
        event.register(NetworkDeviceBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "network_device")));
        event.register(DigitalControllerBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "digital_controller")));
        event.register(DigitalTerminalBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "digital_terminal")));
        event.register(DigitalFluidTerminalBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "digital_fluid_terminal")));
        event.register(RequestTerminalBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "request_terminal")));
        event.register(DiscDriveBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "disc_drive")));
        event.register(FluidDiscDriveBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "fluid_disc_drive")));
        event.register(AssemblerBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "assembler")));
        event.register(AdvInterfaceBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "adv_interface")));
        event.register(CoprocessorBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "coprocessor")));
        event.register(ImporterBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "importer")));
        event.register(ExporterBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "exporter")));
        event.register(FluidImporterBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "fluid_importer")));
        event.register(FluidExporterBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "fluid_exporter")));
        event.register(RedstoneEmitterBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "redstone_emitter")));
        event.register(StorageBusBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "storage_bus")));
        event.register(FluidStorageBusBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "fluid_storage_bus")));
        event.register(RecipeEncoderBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "recipe_encoder")));
        event.register(ProcessProgrammerBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "process_programmer")));
        event.register(NetworkCableBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "network_cable")));
    }
}
