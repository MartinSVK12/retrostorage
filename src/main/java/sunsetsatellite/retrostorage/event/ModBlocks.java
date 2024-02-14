package sunsetsatellite.retrostorage.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import sunsetsatellite.retrostorage.block.DigitalChestBlock;

public class ModBlocks {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    public static Block digitalChest;

    @EventListener
    public static void registerBlocks(BlockRegistryEvent event){
        digitalChest = new DigitalChestBlock(Identifier.of(NAMESPACE, "digital_chest"), Material.STONE).setTranslationKey(NAMESPACE, "digital_chest").setHardness(1).setResistance(5);
    }
}
