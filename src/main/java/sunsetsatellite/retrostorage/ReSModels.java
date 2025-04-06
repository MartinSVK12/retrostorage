package sunsetsatellite.retrostorage;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import org.useless.DragonFly;
import org.useless.dragonfly.models.block.BlockModelDFJava;
import sunsetsatellite.catalyst.CatalystMultipart;
import sunsetsatellite.catalyst.multipart.block.model.BlockModelMultipart;
import sunsetsatellite.catalyst.multipart.block.model.MultipartBlockModelBuilder;
import sunsetsatellite.retrostorage.blocks.models.BlockModelMachine;
import sunsetsatellite.retrostorage.blocks.models.BlockModelRedstoneEmitter;
import sunsetsatellite.retrostorage.blocks.states.NetworkCableStateInterpreter;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.io.IOException;
import java.net.URISyntaxException;

import static sunsetsatellite.retrostorage.ReSBlocks.*;
import static sunsetsatellite.retrostorage.ReSItems.*;
import static sunsetsatellite.retrostorage.RetroStorage.MOD_ID;

public class ReSModels implements ModelEntrypoint {
    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
        RetroStorage.LOGGER.info("Initializing block models...");

        try {
            TextureRegistry.initializeAllFiles(MOD_ID,TextureRegistry.blockAtlas,true);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        ModelHelper.setBlockModel(networkCable, ()-> {
            BlockModelMultipart modelMultipart = new MultipartBlockModelBuilder(CatalystMultipart.MOD_ID)
                    .build(networkCable);

            modelMultipart.parentModel =
                    new BlockModelDFJava<>(networkCable, DragonFly.loadBlockModel("retrostorage:network_cable/cable_all"))
                            .setStateInterpreter(new NetworkCableStateInterpreter())
                            .setStateData("retrostorage:network_cable");

            return modelMultipart;
        });

        ModelHelper.setBlockModel(redstoneEmitter, () -> new BlockModelRedstoneEmitter(redstoneEmitter));

        blockTextures.forEach((block, tex)-> {
            //LOGGER.info("Loading block model for '{}'", block.namespaceId());
            if (dispatcher.hasDispatch(block)) return;

            ModelHelper.setBlockModel(block, () -> new BlockModelMachine(block, tex));
        });
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        RetroStorage.LOGGER.info("Initializing items models...");

        itemTextures.forEach((item,texture)->{
            //LOGGER.info("Loading item model for '{}'", item.namespaceID.toString());
            ModelHelper.setItemModel(item,()->{
                ItemModelStandard model = new ItemModelStandard(item, MOD_ID);
                model.icon = TextureRegistry.getTexture(NamespaceID.getTemp(MOD_ID,"item/"+texture));
                return model;
            });
        });
    }

    @Override
    public void initEntityModels(EntityRenderDispatcher dispatcher) {

    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {

    }
}
