package sunsetsatellite.retrostorage.blocks.states;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.data.block.mojang.state.MetaStateInterpreter;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.IMultiConduit;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.blocks.BlockLogicNetworkCable;
import sunsetsatellite.retrostorage.tiles.TileEntityNetworkDevice;

import java.util.HashMap;

public class NetworkCableStateInterpreter extends MetaStateInterpreter {
    @Override
    public HashMap<String, String> getStateMap(WorldSource worldSource, int i, int j, int k, Block<?> block, int meta) {
        HashMap<String, String> states = new HashMap<>();
        for (Direction direction : Direction.values()) {
            boolean show = false;
            Vec3i offset = new Vec3i(i, j, k).add(direction.getVec());
            Block<?> neighbouringBlock = worldSource.getBlock(offset.x, offset.y, offset.z);
            if (neighbouringBlock != null) {
                if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
                    show = true;
                } else if (!(neighbouringBlock.getLogic() instanceof BlockLogicNetworkCable)) {
                    if (neighbouringBlock.isEntityTile) {
                        TileEntity neighbouringTile = worldSource.getTileEntity(offset.x, offset.y, offset.z);
                        if (neighbouringBlock.hasTag(RetroStorage.NETWORK_CABLES_CONNECT) || neighbouringTile instanceof IMultiConduit || neighbouringTile instanceof TileEntityNetworkDevice) {
                            show = true;
                        }
                    } else if (neighbouringBlock.hasTag(RetroStorage.NETWORK_CABLES_CONNECT)) {
                        show = true;
                    }
                }
            }
            states.put(direction.getName().toLowerCase(), String.valueOf(show));
        }
        return states;
    }
}
