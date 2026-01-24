package sunsetsatellite.retrostorage.util;

import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class InventoryLighting {
    private static final FloatBuffer buffer = GlAllocationUtils.allocateFloatBuffer(16);

    private static void disable() {
        GL11.glDisable(2896);
        GL11.glDisable(16384);
        GL11.glDisable(16385);
        GL11.glDisable(2903);
    }

    public static void enableInventoryLight() {
        GL11.glPushMatrix();
        GL11.glRotatef(-30.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(155.0F, 1.0F, 0.0F, 0.0F);
        enableLight(0.4F, 0.5F);
        GL11.glPopMatrix();
    }

    private static void enableLight() {
        enableLight(0.4F, 0.6F);
    }

    private static void enableLight(float ambientBrightness, float lightBrightness) {
        GL11.glEnable(2896);
        GL11.glEnable(16384);
        GL11.glEnable(16385);
        GL11.glEnable(2903);
        GL11.glColorMaterial(1032, 5634);
        Vec3d vec = Vec3d.create(0.2, 1.0F, -0.7).normalize();
        GL11.glLight(16384, 4611, getBuffer(vec.x, vec.y, vec.z, 0.0F));
        GL11.glLight(16384, 4609, getBuffer(lightBrightness, lightBrightness, lightBrightness, 1.0F));
        GL11.glLight(16384, 4608, getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(16384, 4610, getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        vec = Vec3d.create(-0.2, 1.0F, 0.7).normalize();
        GL11.glLight(16385, 4611, getBuffer(vec.x, vec.y, vec.z, 0.0F));
        GL11.glLight(16385, 4609, getBuffer(lightBrightness, lightBrightness, lightBrightness, 1.0F));
        GL11.glLight(16385, 4608, getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(16385, 4610, getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glShadeModel(7424);
        GL11.glLightModel(2899, getBuffer(ambientBrightness, ambientBrightness, ambientBrightness, 1.0F));
    }

    private static FloatBuffer getBuffer(double d, double d1, double d2, double d3) {
        return getBuffer((float) d, (float) d1, (float) d2, (float) d3);
    }

    private static FloatBuffer getBuffer(float f, float f1, float f2, float f3) {
        buffer.clear();
        buffer.put(f).put(f1).put(f2).put(f3);
        buffer.flip();
        return buffer;
    }
}
