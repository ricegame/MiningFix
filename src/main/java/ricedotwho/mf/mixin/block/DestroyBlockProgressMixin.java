package ricedotwho.mf.mixin.block;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.mining.pinglessMining;
import ricedotwho.mf.mf;

import java.util.Map;
import java.util.Objects;

@Mixin(RenderGlobal.class)
public abstract class DestroyBlockProgressMixin {
    @Shadow
    private Map<Integer, DestroyBlockProgress> damagedBlocks;
    @Inject(method = "drawBlockDamageTexture", at = @At("HEAD"))
    public void drawBlockDamageTexture(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, CallbackInfo ci) {
        if(!ModConfig.fixBreakingProgress || !ModConfig.pinglessMining) return;
        for(DestroyBlockProgress block : damagedBlocks.values()) {
            if(!Objects.equals(entityIn, mf.mc.thePlayer) || !pinglessMining.should()) continue;
            block.setPartialBlockDamage(pinglessMining.currentProgress);
        }
    }
}
