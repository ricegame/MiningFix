package ricedotwho.mf.mining;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.events.PacketEvents;
import ricedotwho.mf.utils.Utils;

import static ricedotwho.mf.mf.devInfoMessage;


public class DontBreakCobble {
    static Minecraft mc = Minecraft.getMinecraft();
    @SubscribeEvent
    public void onPacket(PacketEvents.PacketSentEvent event) {
        if(!(event.getPacket() instanceof C07PacketPlayerDigging)) return;
        C07PacketPlayerDigging packet = (C07PacketPlayerDigging) event.packet;
        BlockPos pos = packet.getPosition();
        if(!Utils.inHollows) return;
        if(!ModConfig.dontBreakCobblestone) return;;
        if(pos == null) return;
        if(mc.theWorld.getBlockState(pos).getBlock() == Blocks.cobblestone) {
            event.setCanceled(true);
            devInfoMessage("Tried to cancel");
        }
    }
}
