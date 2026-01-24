package sunsetsatellite.retrostorage.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Namespace;
import sunsetsatellite.retrostorage.packet.*;
import sunsetsatellite.retrostorage.packet.terminal.fluid.FluidTerminalContentsPacket;
import sunsetsatellite.retrostorage.packet.terminal.fluid.FluidTerminalInteractionPacket;
import sunsetsatellite.retrostorage.packet.terminal.fluid.FluidTerminalRequestContentsPacket;
import sunsetsatellite.retrostorage.packet.terminal.item.TerminalContentsPacket;
import sunsetsatellite.retrostorage.packet.terminal.item.TerminalInteractionPacket;
import sunsetsatellite.retrostorage.packet.terminal.item.TerminalRequestContentsPacket;
import sunsetsatellite.retrostorage.packet.terminal.request.RequestCraftingPacket;
import sunsetsatellite.retrostorage.packet.terminal.request.RequestTerminalContentsPacket;
import sunsetsatellite.retrostorage.packet.terminal.request.RequestTerminalRequestContentsPacket;

public class ReSPackets {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public static void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("fluid_terminal_contents"), FluidTerminalContentsPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("fluid_terminal_interaction"), FluidTerminalInteractionPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("fluid_terminal_request_contents"), FluidTerminalRequestContentsPacket.TYPE);

        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("terminal_contents"), TerminalContentsPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("terminal_interaction"), TerminalInteractionPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("terminal_request_contents"), TerminalRequestContentsPacket.TYPE);

        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("request_crafting"), RequestCraftingPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("request_terminal_contents"), RequestTerminalContentsPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("request_terminal_request_contents"), RequestTerminalRequestContentsPacket.TYPE);

        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("clear_request_queue"), ClearRequestQueuePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("controller_contents_update"), ControllerContentsUpdatePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("controller_crafting_queue"), ControllerCraftingQueuePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("controller_update"), ControllerUpdatePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("request_controller_contents_update"), RequestControllerContentsUpdatePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("request_controller_crafting_queue"), RequestControllerCraftingQueuePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("request_controller_update"), RequestControllerUpdatePacket.TYPE);
    }
}
