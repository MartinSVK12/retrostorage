package sunsetsatellite.retrostorage.util;

public class Vec3 {
    public int x;
    public int y;
    public int z;

    public Vec3(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    };


    @Override
    public String toString() {
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3 vec3 = (Vec3) o;

        if (x != vec3.x) return false;
        if (y != vec3.y) return false;
        return z == vec3.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
