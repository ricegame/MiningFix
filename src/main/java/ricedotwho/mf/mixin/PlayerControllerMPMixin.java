package ricedotwho.mf.mixin;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ricedotwho.mf.mining.DrillFix;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {

    @Shadow
    private ItemStack currentItemHittingBlock;

    @Shadow
    private BlockPos currentBlock;

    /**
     * @author
     * Mojang
     * @reason
     * Fixes block-breaking progress resetting on inventory updates.
     */
    @Overwrite
    private boolean isHittingPosition(BlockPos pos) {
        return DrillFix.isHittingPosition(pos, currentItemHittingBlock, currentBlock);
    }
}
