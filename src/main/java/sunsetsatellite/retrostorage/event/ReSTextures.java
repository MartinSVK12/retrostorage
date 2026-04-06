package sunsetsatellite.retrostorage.event;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import sunsetsatellite.catalyst.core.util.Side;
import sunsetsatellite.catalyst.core.util.model.LayeredCubeModel;
import sunsetsatellite.retrostorage.util.MachineTextures;

import java.util.Map;

public class ReSTextures {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerTextures(TextureRegisterEvent event) {
        try {
            ReSItems.itemTextures.forEach((item, texture) -> {
                item.setTexture(NAMESPACE.id(texture));
            });

            for (Map.Entry<Block, MachineTextures> entry : ReSBlocks.blockTextures.entrySet()) {
                Block block = entry.getKey();
                MachineTextures value = entry.getValue();

                if (block instanceof LayeredCubeModel model) {
                    for (Map.Entry<Side, String> e : value.defaultTextures.entrySet()) {
                        Side side = e.getKey();
                        String textureName = e.getValue();
                        if (textureName == null) continue;
                        model.getTextureLayers()[0].set(Identifier.of(textureName), side);
                    }
                    if(model.getTextureLayers().length > 1){
                        for (Map.Entry<Side, String> e : value.activeTextures.entrySet()) {
                            Side side = e.getKey();
                            String textureName = e.getValue();
                            if (textureName == null) continue;
                            model.getTextureLayers()[1].set(Identifier.of(textureName), side);
                        }
                    }
                    if(model.getTextureLayers().length > 2){
                        for (Map.Entry<Side, String> e : value.overbrightTextures.entrySet()) {
                            Side side = e.getKey();
                            String textureName = e.getValue();
                            if (textureName == null) continue;
                            model.getTextureLayers()[2].set(Identifier.of(textureName), side);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
