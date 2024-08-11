package ricedotwho.mf.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.renderer.entity.RenderManager;

@Mixin(RenderManager.class)
public interface AccessorRenderManager {

    @Accessor("renderPosX")
    double getRenderX();

    @Accessor("renderPosY")
    double getRenderY();

    @Accessor("renderPosZ")
    double getRenderZ();
}
