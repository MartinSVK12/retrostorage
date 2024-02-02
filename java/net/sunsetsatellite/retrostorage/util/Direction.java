package net.sunsetsatellite.retrostorage.util;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3D;

public enum Direction {
    X_POS (new Vec3i(1,0,0),5,"EAST"),
    X_NEG (new Vec3i(-1,0,0),4,"WEST"),
    Y_POS (new Vec3i(0,1,0),1,"UP"),
    Y_NEG (new Vec3i(0,-1,0),0,"DOWN"),
    Z_POS (new Vec3i(0,0,1),3,"SOUTH"),
    Z_NEG (new Vec3i(0,0,-1),2,"NORTH");

    private final Vec3i vec;
    private Direction opposite;
    private final int side;
    private final String name;

    Direction(Vec3i vec3I, int side, String name) {
        this.vec = vec3I;
        this.side = side;
        this.name = name;
    }

    public TileEntity getTileEntity(IBlockAccess world, TileEntity tile){
        Vec3i pos = new Vec3i(tile.xCoord + vec.x, tile.yCoord + vec.y, tile.zCoord + vec.z);
        return world.getBlockTileEntity(pos.x,pos.y,pos.z);
    }

    public TileEntity getTileEntity(IBlockAccess world, Vec3i baseVec){
        Vec3i pos = new Vec3i(baseVec.x + vec.x, baseVec.y + vec.y, baseVec.z + vec.z);
        return world.getBlockTileEntity(pos.x,pos.y,pos.z);
    }

    public String getName() {
        return name;
    }

    public Direction getOpposite(){
        return opposite;
    }

    public Vec3i getVec() {
        return vec;
    }

    public static Direction getDirectionFromSide(int side){
        for (Direction dir : values()) {
            if(dir.side == side){
                return dir;
            }
        }
        return Direction.X_NEG;
    }

    public static Direction getFromName(String name){
        for (Direction dir : values()) {
            if(dir.name.equalsIgnoreCase(name)){
                return dir;
            }
        }
        return null;
    }

    /**
     * Gets minecraft's side number, NOTE: this and .ordinal() aren't the same!
     * @return Minecraft's side number.
     */
    public int getSide() {
        return side;
    }

    public Vec3f getVecF(){
        return new Vec3f(vec.x, vec.y, vec.z);
    }

    public Vec3D getMinecraftVec(){
        return Vec3D.createVectorHelper(vec.x, vec.y, vec.z);
    }

    static {
        X_POS.opposite = X_NEG;
        X_NEG.opposite = X_POS;
        Y_NEG.opposite = Y_POS;
        Y_POS.opposite = Y_NEG;
        Z_NEG.opposite = Z_POS;
        Z_POS.opposite = Z_NEG;
    }

}
