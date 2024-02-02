package net.sunsetsatellite.retrostorage.tiles;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

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
        TileEntity tile = ModLoader.getMinecraftInstance().theWorld.getBlockTileEntity(remoteLinkNBT.getInteger("x"),remoteLinkNBT.getInteger("y"),remoteLinkNBT.getInteger("z"));
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

    private NBTTagCompound tempRemoteLinkNBT = null;
    public TileEntityWirelessLink remoteLink = null;
}
