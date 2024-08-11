package ricedotwho.mf.utils;

import cc.polyfrost.oneconfig.config.core.OneColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import ricedotwho.mf.mixin.AccessorRenderManager;

import static net.minecraft.client.renderer.GlStateManager.translate;

public class renderUtils {
    static Minecraft mc;
    static {
        mc = Minecraft.getMinecraft();
    }
    public static Double[] fixRenderPos(double x, double y, double z) {
        return new Double[]{x+getRenderX(), y+getRenderY(), z+getRenderZ()};
    }
    private static Double getRenderX() {
        return ((AccessorRenderManager) mc.getRenderManager()).getRenderX();
    }
    private static Double getRenderY() {
        return ((AccessorRenderManager) mc.getRenderManager()).getRenderY();
    }
    private static Double getRenderZ() {
        return ((AccessorRenderManager) mc.getRenderManager()).getRenderZ();
    }

    public static void mcText(String text, Number x, Number y, Number scale, OneColor color, boolean shadow, boolean center) {
        drawText(text + EnumChatFormatting.RESET, x.floatValue(), y.floatValue(), scale.doubleValue(), color, shadow, center);
    }

    public static void drawText(String text, float x, float y, double scale, OneColor color, boolean shadow, boolean center) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        translate(x, y, 0f);
        scale(scale, scale, scale);
        for (String line : text.split("\n")) {
            float yOffset = center ? mc.fontRendererObj.FONT_HEIGHT : 0f;
            float xOffset = center ? mc.fontRendererObj.getStringWidth(line) / -2f: 0f;
            mc.fontRendererObj.drawString(line, xOffset, yOffset, color.getRGB(), shadow);
        }

        GlStateManager.resetColor();
            GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    public static void scale(Number x, Number y, Number z) {
        GlStateManager.scale(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }
    public static int getMCTextWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }

}
