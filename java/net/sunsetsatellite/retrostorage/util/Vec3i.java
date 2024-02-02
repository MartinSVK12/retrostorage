package net.sunsetsatellite.retrostorage.util;

import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;

public class Vec3i {
    public int x;
    public int y;
    public int z;

    public Vec3i(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    };

    public Vec3i(){
        this.x = this.y = this.z = 0;
    }

    public Vec3i(int size){
        this.x = this.y = this.z = size;
    }


    @Override
    public String toString() {
        return "Vec3i{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public double distanceTo(Vec3f vec3f) {
        double d = vec3f.x - this.x;
        double d1 = vec3f.y - this.y;
        double d2 = vec3f.z - this.z;
        return MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
    }

    public Vec3i add(double value){
        this.x += value;
        this.y += value;
        this.z += value;
        return this;
    }

    public Vec3i subtract(double value){
        this.x -= value;
        this.y -= value;
        this.z -= value;
        return this;
    }

    public Vec3i divide(double value){
        this.x /= value;
        this.y /= value;
        this.z /= value;
        return this;
    }

    public Vec3i multiply(double value){
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }

    public Vec3i add(Vec3i value){
        this.x += value.x;
        this.y += value.y;
        this.z += value.z;
        return this;
    }

    public Vec3i subtract(Vec3i value){
        this.x -= value.x;
        this.y -= value.y;
        this.z -= value.z;
        return this;
    }

    public Vec3i divide(Vec3i value){
        this.x /= value.x;
        this.y /= value.y;
        this.z /= value.z;
        return this;
    }

    public Vec3i multiply(Vec3i value){
        this.x *= value.x;
        this.y *= value.y;
        this.z *= value.z;
        return this;
    }

    public Vec3i rotate(Vec3i origin, Direction direction){
        Vec3i pos;
        switch (direction){
            case X_POS:
                pos = new Vec3i(this.z + origin.x, this.y + origin.y, this.x + origin.z);
                break;
            case X_NEG:
                pos = new Vec3i(-this.z + origin.x, this.y + origin.y, -this.x + origin.z);
                break;
            case Z_NEG:
                pos = new Vec3i(-this.x + origin.x, this.y + origin.y, -this.z + origin.z);
                break;
            case Y_NEG:
                pos = new Vec3i(this.x + origin.x, -this.y + origin.y, this.z + origin.z);
                break;
            default:
                pos = new Vec3i(this.x + origin.x, this.y + origin.y, this.z + origin.z);
                break;
        }
        return pos;
    }

    public Vec3i rotate(Direction direction){
        Vec3i pos;
        switch (direction){
            case X_POS:
                pos = new Vec3i(this.z, this.y, this.x);
                break;
            case X_NEG:
                pos = new Vec3i(-this.z, this.y, -this.x);
                break;
            case Z_NEG:
                pos = new Vec3i(-this.x, this.y, -this.z);
                break;
            case Y_NEG:
                pos = new Vec3i(this.x, -this.y, this.z);
                break;
            default:
                pos = new Vec3i(this.x, this.y, this.z);
                break;
        }
        return pos;
    }

    public void writeToNBT(NBTTagCompound tag){
        tag.setInteger("x",this.x);
        tag.setInteger("y",this.y);
        tag.setInteger("z",this.z);
    }

    public void readFromNBT(NBTTagCompound tag){
        this.x = tag.getInteger("x");
        this.y = tag.getInteger("y");
        this.z = tag.getInteger("z");
    }

    public Vec3i copy(){
        return new Vec3i(this.x,this.y,this.z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3i vec3I = (Vec3i) o;

        if (x != vec3I.x) return false;
        if (y != vec3I.y) return false;
        return z == vec3I.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
