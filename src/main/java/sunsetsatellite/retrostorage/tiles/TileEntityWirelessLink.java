package sunsetsatellite.retrostorage.tiles;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.retrostorage.RetroStorage;

public class TileEntityWirelessLink extends TileEntityNetworkDevice {

    public TileEntityWirelessLink()
    {
    }
    
    
    @Override
    public void tick() {
        super.tick();
        if(tempRemoteLinkNBT != null){
            initSavedLink(tempRemoteLinkNBT);
        }
    }

    public void readFromNBT(CompoundTag CompoundTag)
    {
        super.readFromNBT(CompoundTag);
        CompoundTag remoteLinkNBT = CompoundTag.getCompound("remoteLink");
        if(remoteLinkNBT.containsKey("x") && remoteLinkNBT.containsKey("y") && remoteLinkNBT.containsKey("z")){
            tempRemoteLinkNBT = remoteLinkNBT;
        }
    }

    public void initSavedLink(CompoundTag remoteLinkNBT)
    {
        TileEntity tile = RetroStorage.mc.theWorld.getBlockTileEntity(remoteLinkNBT.getInteger("x"),remoteLinkNBT.getInteger("y"),remoteLinkNBT.getInteger("z"));
        if(tile instanceof TileEntityWirelessLink){
            remoteLink = (TileEntityWirelessLink) tile;
        }
    }

    public void writeToNBT(CompoundTag CompoundTag)
    {
        CompoundTag remoteLinkNBT = new CompoundTag();
        if(remoteLink != null){
            remoteLinkNBT.putInt("x",remoteLink.x);
            remoteLinkNBT.putInt("y",remoteLink.y);
            remoteLinkNBT.putInt("z",remoteLink.z);
        }
        CompoundTag.putCompound("remoteLink",remoteLinkNBT);
        super.writeToNBT(CompoundTag);
    }


    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(x, y, z) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
    }

    private CompoundTag tempRemoteLinkNBT = null;
    public TileEntityWirelessLink remoteLink = null;
}
