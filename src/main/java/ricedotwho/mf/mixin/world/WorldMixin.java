package ricedotwho.mf.mixin.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ricedotwho.mf.events.BlockChangedEvent;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow
    public abstract IBlockState getBlockState(BlockPos pos);

    @Shadow
    public abstract boolean isValid(BlockPos pos);

    @Inject(method = "setBlockState", at = @At("HEAD"), cancellable = true)
    private void onSetBlockState(BlockPos pos, IBlockState state, int flags, CallbackInfoReturnable<Boolean> cir) {
        if(!isValid(pos)) return;
        IBlockState oldState = getBlockState(pos);
        if(pos == null || state == null || oldState == null) return;
        BlockChangedEvent.postAndCatch(oldState, state, pos);
    }
}
