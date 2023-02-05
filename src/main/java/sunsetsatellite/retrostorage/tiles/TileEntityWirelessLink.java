package sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
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

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagCompound remoteLinkNBT = nbttagcompound.getCompoundTag("remoteLink");
        if(remoteLinkNBT.hasKey("x") && remoteLinkNBT.hasKey("y") && remoteLinkNBT.hasKey("z")){
            tempRemoteLinkNBT = remoteLinkNBT;
        }
    }

    public void initSavedLink(NBTTagCompound remoteLinkNBT)
    {
        TileEntity tile = RetroStorage.mc.theWorld.getBlockTileEntity(remoteLinkNBT.getInteger("x"),remoteLinkNBT.getInteger("y"),remoteLinkNBT.getInteger("z"));
        if(tile instanceof TileEntityWirelessLink){
            remoteLink = (TileEntityWirelessLink) tile;
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        NBTTagCompound remoteLinkNBT = new NBTTagCompound();
        if(remoteLink != null){
            remoteLinkNBT.setInteger("x",remoteLink.xCoord);
            remoteLinkNBT.setInteger("y",remoteLink.yCoord);
            remoteLinkNBT.setInteger("z",remoteLink.zCoord);
        }
        nbttagcompound.setCompoundTag("remoteLink",remoteLinkNBT);
        super.writeToNBT(nbttagcompound);
    }


    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private NBTTagCompound tempRemoteLinkNBT = null;
    public TileEntityWirelessLink remoteLink = null;
}
