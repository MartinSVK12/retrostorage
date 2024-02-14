package sunsetsatellite.retrostorage.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import sunsetsatellite.retrostorage.RetroStorage;
import sunsetsatellite.retrostorage.block.entity.DigitalChestBlockEntity;
import sunsetsatellite.retrostorage.container.DigitalChestContainer;

public class DigitalChestBlock extends RotatableBlockWithEntity {
    public DigitalChestBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new DigitalChestBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if(world.isRemote){
            return false;
        }
        DigitalChestBlockEntity tile = (DigitalChestBlockEntity) world.method_1777(x, y, z);
        GuiHelper.openGUI(player, Identifier.of(RetroStorage.NAMESPACE, "digital_chest"),tile,new DigitalChestContainer(player.inventory,tile));
        return true;
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        DigitalChestBlockEntity tile = (DigitalChestBlockEntity) world.method_1777(x, y, z);
        tile.ejectAll(world, x, y, z);
        super.onBreak(world, x, y, z);
    }
}
