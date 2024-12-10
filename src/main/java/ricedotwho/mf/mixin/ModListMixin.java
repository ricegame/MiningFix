package ricedotwho.mf.mixin;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ricedotwho.mf.Reference;

import java.util.List;
import java.util.Map;

@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false)
public abstract class ModListMixin {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void removeModID(List<ModContainer> modList, CallbackInfo ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            modTags.remove(Reference.MOD_ID);
        }
    }
}

