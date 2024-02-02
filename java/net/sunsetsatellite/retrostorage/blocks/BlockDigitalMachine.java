package net.sunsetsatellite.retrostorage.blocks;

import forge.ITextureProvider;
import net.minecraft.src.*;
import net.sunsetsatellite.retrostorage.gui.*;
import net.sunsetsatellite.retrostorage.mod_RetroStorage;
import net.sunsetsatellite.retrostorage.tiles.*;

import java.util.Random;

public class BlockDigitalMachine extends BlockContainer implements ITextureProvider {
    public BlockDigitalMachine(int id) {
        super(id, mod_RetroStorage.machineSide, Material.iron);
    }

    public void onBlockAdded(World world, int i, int j, int k) {
        TileEntity te = this.getBlockEntity(world.getBlockMetadata(i, j, k));
        if(te != null){
            world.setBlockTileEntity(i, j, k, te);
        }
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        GuiScreen gui = this.getGui(world, i, j, k, entityplayer);
        TileEntity tile = world.getBlockTileEntity(i, j, k);
        int meta = world.getBlockMetadata(i, j, k);
        if(gui == null) {
            if(meta == mod_RetroStorage.machines.wirelessLink.ordinal()){
                if(entityplayer.isSneaking()){
                    return false;
                }
                TileEntityWirelessLink link = (TileEntityWirelessLink) tile;
                if(link.remoteLink == null){
                    entityplayer.addChatMessage("No link.");
                } else {
                    TileEntityWirelessLink remoteLink = link.remoteLink;
                    entityplayer.addChatMessage("Link established with Wireless Link at "+remoteLink.xCoord+", "+remoteLink.yCoord+", "+remoteLink.zCoord+".");
                }

                return true;
            }
            return false;
        } else if(world.isRemote) {
            return true;
        } else {
            if(entityplayer.getCurrentEquippedItem() != null){
                if(meta == mod_RetroStorage.machines.digitalController.ordinal()){
                    if (entityplayer.inventory.getCurrentItem() != null && entityplayer.inventory.getCurrentItem().getItem() == Item.redstone) {
                        entityplayer.inventory.getCurrentItem().stackSize--;
                        ((TileEntityDigitalController)tile).energy += 20*60;
                    }
                }
                if((meta == mod_RetroStorage.machines.wirelessLink.ordinal() && entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.linkingCard)){
                    return false;
                }
                /*if((meta == mod_RetroStorage.machines.digitalTerminal.ordinal() && entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.mobileTerminal)
                        || (meta == mod_RetroStorage.machines.requestTerminal.ordinal() && entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.mobileRequestTerminal)
                        || (meta == mod_RetroStorage.machines.wirelessLink.ordinal() && entityplayer.getCurrentEquippedItem().getItem() == mod_RetroStorage.linkingCard)){
                    return false;
                }*/
            }
            ModLoader.openGUI(entityplayer, gui);
            return true;
        }
    }

    private GuiScreen getGui(World world, int i, int j, int k, EntityPlayer entityplayer) {
        TileEntity tile = world.getBlockTileEntity(i, j, k);
        switch(world.getBlockMetadata(i, j, k)) {
            case 0:
                return new GuiDigitalController((TileEntityDigitalController) tile);
            case 1:
                return new GuiDigitalTerminal(entityplayer.inventory, (TileEntityDigitalTerminal) tile);
            case 2:
                return new GuiDiscDrive(entityplayer.inventory, (TileEntityDiscDrive) tile);
            case 3:
                return new GuiAssembler(entityplayer.inventory, (TileEntityAssembler) tile);
            case 4:
                return new GuiImporter(entityplayer.inventory, (TileEntityImporter) tile);
            case 5:
                return new GuiExporter(entityplayer.inventory, (TileEntityExporter) tile);
            case 6:
                return new GuiRequestTerminal(entityplayer, (TileEntityRequestTerminal) tile);
            case 7:
                return new GuiRecipeEncoder(entityplayer.inventory, (TileEntityRecipeEncoder) tile);
            case 8:
                return new GuiAdvInterface(entityplayer.inventory, (TileEntityAdvInterface) tile);
            case 9:
                return new GuiProcessProgrammer(entityplayer.inventory, (TileEntityProcessProgrammer) tile);
            case 10:
                return null;
            case 11:
                return new GuiRedstoneEmitter(entityplayer.inventory, (TileEntityRedstoneEmitter) tile);
            default:
                return null;
        }
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if(l == 0) {
            setFacing(world, i, j, k, 2);
        }

        if(l == 1) {
            setFacing(world, i, j, k, 5);
        }

        if(l == 2) {
            setFacing(world, i, j, k, 3);
        }

        if(l == 3) {
            setFacing(world, i, j, k, 4);
        }

    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        int meta = iblockaccess.getBlockMetadata(i, j, k);
        if(meta >= mod_RetroStorage.sprites.length) {
            return mod_RetroStorage.machineSide;
        } else if(mod_RetroStorage.sprites[meta].length == 1){
            TileEntity tile = iblockaccess.getBlockTileEntity(i,j,k);
            if(tile instanceof TileEntityRedstoneEmitter){
                if(((TileEntityRedstoneEmitter) tile).isActive){
                    return mod_RetroStorage.emitterActive;
                }
            }
            return mod_RetroStorage.sprites[meta][0];
        } else {
            return side < 2 ? mod_RetroStorage.sprites[meta][side] : (side != getFacing(iblockaccess, i, j, k) ? mod_RetroStorage.sprites[meta][2] : mod_RetroStorage.sprites[meta][3]);
        }
    }

    @Override
    public int getBlockTextureFromSide(int i) {
        return super.getBlockTextureFromSide(i);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if(meta >= mod_RetroStorage.sprites.length) {
            return mod_RetroStorage.machineSide;
        } else if(mod_RetroStorage.sprites[meta].length == 1){
            return mod_RetroStorage.sprites[meta][0];
        } else {
            return side < 2 ? mod_RetroStorage.sprites[meta][side] : (side == 3 ? mod_RetroStorage.sprites[meta][3] : mod_RetroStorage.sprites[meta][2]);
        }
    }

    public static int getFacing(IBlockAccess iblockaccess, int i, int j, int k) {
        return ((TileEntityNetworkDevice)iblockaccess.getBlockTileEntity(i, j, k)).facing;
    }

    public static void setFacing(IBlockAccess iblockaccess, int i, int j, int k, int f) {
        ((TileEntityNetworkDevice)iblockaccess.getBlockTileEntity(i, j, k)).facing = f;
    }

    @Override
    public String getTextureFile() {
        return "/assets/retrostorage/blocks.png";
    }

    @Override
    public TileEntity getBlockEntity() {
        return null;
    }

    @Override
    public int damageDropped(int meta) {
        switch(meta) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                return meta;
            default:
                return 0;
        }
    }



    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public TileEntity getBlockEntity(int meta){
        switch (meta){
            case 0:
                return new TileEntityDigitalController();
            case 1:
                return new TileEntityDigitalTerminal();
            case 2:
                return new TileEntityDiscDrive();
            case 3:
                return new TileEntityAssembler();
            case 4:
                return new TileEntityImporter();
            case 5:
                return new TileEntityExporter();
            case 6:
                return new TileEntityRequestTerminal();
            case 7:
                return new TileEntityRecipeEncoder();
            case 8:
                return new TileEntityAdvInterface();
            case 9:
                return new TileEntityProcessProgrammer();
            case 10:
                return new TileEntityWirelessLink();
            case 11:
                return new TileEntityRedstoneEmitter();
            default:
                return new TileEntityNetworkDevice();
        }
    }

    @Override
    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        TileEntity tile = iblockaccess.getBlockTileEntity(i,j,k);
        if(tile instanceof TileEntityRedstoneEmitter){
            if(((TileEntityRedstoneEmitter) tile).isActive){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int side) {
        TileEntity tile = world.getBlockTileEntity(i,j,k);
        if(tile instanceof TileEntityRedstoneEmitter){
            if(((TileEntityRedstoneEmitter) tile).isActive){
                return true;
            }
        }
        return false;
    }

    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean isBlockNormalCube(World world, int i, int j, int k) {
        TileEntity tile = world.getBlockTileEntity(i,j,k);
        if(tile instanceof TileEntityRedstoneEmitter){
            return false;
        }
        return true;
    }
}
