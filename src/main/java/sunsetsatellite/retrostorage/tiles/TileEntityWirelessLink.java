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
    public void updateEntity() {
        super.updateEntity();
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
            remoteLinkNBT.putInt("x",remoteLink.xCoord);
            remoteLinkNBT.putInt("y",remoteLink.yCoord);
            remoteLinkNBT.putInt("z",remoteLink.zCoord);
        }
        CompoundTag.putCompound("remoteLink",remoteLinkNBT);
        super.writeToNBT(CompoundTag);
    }


    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.distanceToSqr((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private CompoundTag tempRemoteLinkNBT = null;
    public TileEntityWirelessLink remoteLink = null;
}
