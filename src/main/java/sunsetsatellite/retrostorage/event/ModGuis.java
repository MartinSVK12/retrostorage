package sunsetsatellite.retrostorage.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import sunsetsatellite.retrostorage.block.entity.DigitalChestBlockEntity;
import sunsetsatellite.retrostorage.gui.DigitalChestGui;
import uk.co.benjiweber.expressions.tuple.BiTuple;

public class ModGuis {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerGuiHandler(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
        registry.registerValueNoMessage(NAMESPACE.id("digital_chest"), BiTuple.of(this::openDigitalChest, DigitalChestBlockEntity::new));
    }

    private Screen openDigitalChest(PlayerEntity playerEntity, Inventory inventory) {
        return new DigitalChestGui(playerEntity, inventory);
    }
}
