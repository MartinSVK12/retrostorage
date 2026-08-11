package sunsetsatellite.retrostorage;

import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import sunsetsatellite.retrostorage.blocks.models.BlockModelNetworkCable;
import sunsetsatellite.retrostorage.blocks.models.BlockModelFluidRedstoneEmitter;
import sunsetsatellite.retrostorage.blocks.models.BlockModelMachine;
import sunsetsatellite.retrostorage.blocks.models.BlockModelRedstoneEmitter;
import java.util.function.Supplier;

import static sunsetsatellite.retrostorage.ReSBlocks.*;
import static sunsetsatellite.retrostorage.ReSItems.*;
import static sunsetsatellite.retrostorage.RetroStorage.MOD_ID;

public class ReSModels {
    public void initBlockModels(BlockModelDispatcher dispatcher) {
        RetroStorage.LOGGER.info("Initializing block models...");

		dispatcher.addDispatch(networkCable, new BlockModelNetworkCable<>(networkCable));

        dispatcher.addDispatch(redstoneEmitter, new BlockModelRedstoneEmitter(redstoneEmitter));
		dispatcher.addDispatch(fluidRedstoneEmitter, new BlockModelFluidRedstoneEmitter(fluidRedstoneEmitter));

        blockTextures.forEach((block, tex)-> {
            //LOGGER.info("Loading block model for '{}'", block.namespaceId());
            if (dispatcher.hasDispatch(block)) return;

			dispatcher.addDispatch(block, new BlockModelMachine(block, tex));
        });
    }

    public void initItemModels(ItemModelDispatcher dispatcher) {
        RetroStorage.LOGGER.info("Initializing items models...");

        itemTextures.forEach((item,texture)->{
			Supplier<ItemModelStandard> supplier = () -> {
				ItemModelStandard model = new ItemModelStandard(item);
				model.icon = TextureRegistry.getTexture(NamespaceID.fromPool(MOD_ID, "item/" + texture));
				return model;
			};
			//LOGGER.info("Loading item model for '{}'", item.namespaceID.toString());
            dispatcher.addDispatch(item,supplier.get());
        });
    }


    public void initEntityModels(EntityRendererDispatcher dispatcher) {

    }


    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

    }


    public void initBlockColors(BlockColorDispatcher dispatcher) {

    }
}
