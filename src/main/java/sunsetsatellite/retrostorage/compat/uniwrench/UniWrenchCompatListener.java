package sunsetsatellite.retrostorage.compat.uniwrench;

import net.danygames2014.uniwrench.api.WrenchMode;
import net.danygames2014.uniwrench.api.event.WrenchableBlockRegisterEvent;
import net.danygames2014.uniwrench.compat.VanillaCompat;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.retrostorage.api.AttachesToMachines;
import sunsetsatellite.retrostorage.block.base.RotatableBlockWithEntity;
import sunsetsatellite.retrostorage.event.ReSBlocks;

import java.lang.reflect.Field;

import static net.modificationstation.stationapi.api.state.property.Properties.FACING;

public class UniWrenchCompatListener {
    @EventListener
    public void registerCompat(WrenchableBlockRegisterEvent event){
        for (Field field : ReSBlocks.class.getDeclaredFields()) {
            try {
                Object o = field.get(null);
                if(o instanceof Block block){
                    event.registerRightClickAction(block, UniWrenchCompatListener::pickupMachine);
                }
                if(o instanceof RotatableBlockWithEntity block){
                    if(block.createBlockEntity() instanceof AttachesToMachines){
                        event.registerRightClickAction(block, UniWrenchCompatListener::rotateMachine);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean rotateMachine(ItemStack stack, PlayerEntity player, boolean isSneaking, World world, int x, int y, int z, int side, WrenchMode wrenchMode) {
        if (wrenchMode == WrenchMode.MODE_ROTATE && world.getBlockEntity(x,y,z) instanceof AttachesToMachines) {
            BlockState state = world.getBlockState(x, y, z);
            state = state.cycle(FACING);
            world.setBlockState(x, y, z, state);
            return true;
        } else {
            return false;
        }
    }

    public static boolean pickupMachine(ItemStack stack, PlayerEntity player, boolean isSneaking, World world, int x, int y, int z, int side, WrenchMode wrenchMode) {
        if (wrenchMode == WrenchMode.MODE_WRENCH) {
            world.getBlockState(x,y,z).getBlock().drop(world, x,y,z, world.getBlockState(x,y,z), world.getBlockMeta(x,y,z));
            world.setBlock(x,y,z,0);
            return true;
        } else {
            return false;
        }
    }
}
