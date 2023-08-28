package sunsetsatellite.retrostorage.items;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.retrostorage.tiles.TileEntityWirelessLink;

public class ItemLinkingCard extends Item {

    public ItemLinkingCard(int i) {
        super(i);

    }


    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        TileEntityWirelessLink link = (TileEntityWirelessLink) world.getBlockTileEntity(blockX, blockY, blockZ);
        if (link != null) {
            if(link.remoteLink != null){
                link.remoteLink.remoteLink = null;
                link.remoteLink = null;
                entityplayer.addChatMessage("action.retrostorage.linkBroken");
                return true;
            }
            CompoundTag data = itemstack.getData().getCompound("position");
            if (!(data.containsKey("x") && data.containsKey("y") && data.containsKey("z"))){
                CompoundTag positionNBT = (new CompoundTag());
                positionNBT.putInt("x",blockX);
                positionNBT.putInt("y",blockY);
                positionNBT.putInt("z",blockZ);
                itemstack.getData().putCompound("position",positionNBT);
                entityplayer.addChatMessage("action.retrostorage.cardBound");
            } else {
                TileEntity tile = world.getBlockTileEntity(data.getInteger("x"),data.getInteger("y"),data.getInteger("z"));
                if(tile instanceof TileEntityWirelessLink){
                    link.remoteLink = (TileEntityWirelessLink) tile;
                    link.remoteLink.remoteLink = link;
                    entityplayer.addChatMessage("action.retrostorage.linkEstablished");
                } else {
                    entityplayer.addChatMessage("action.retrostorage.linkInvalidBlock");
                }

            }
        } else {
            if (entityplayer.isSneaking()) {
                itemstack.getData().putCompound("position",new CompoundTag());
                entityplayer.addChatMessage("action.retrostorage.cardUnbound");
            }
        }
        return true;
    }
}
