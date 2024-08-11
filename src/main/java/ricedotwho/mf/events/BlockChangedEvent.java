package ricedotwho.mf.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockChangedEvent extends Event {
    private final IBlockState oldState;
    private final IBlockState newState;
    private final BlockPos pos;

    public BlockChangedEvent(IBlockState oldState, IBlockState newState, BlockPos pos) {
        this.oldState = oldState;
        this.newState = newState;
        this.pos = pos;
    }
    public IBlockState getOldState() {return this.oldState;}
    public IBlockState getNewState() {return this.newState;}
    public BlockPos getPos() {return this.pos;}
    public static void postAndCatch(IBlockState oldState, IBlockState newState, BlockPos pos) {
        BlockChangedEvent event = new BlockChangedEvent(oldState, newState,  pos);
        try {
            MinecraftForge.EVENT_BUS.post(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
