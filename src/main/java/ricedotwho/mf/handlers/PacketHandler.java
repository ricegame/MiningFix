package ricedotwho.mf.handlers;


import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import ricedotwho.mf.events.PacketEvent;

public class PacketHandler extends ChannelDuplexHandler {

    /**
     * Taken from DungeonsRoomsMod under the GNU Affero General Public License v3.0
     * https://github.com/Quantizr/DungeonRoomsMod/blob/1f4c4e009684712d1b57b992fdbef68805deb2e6/LICENSE
     * @author Quantizr
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet) MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent((Packet) msg));
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Packet) MinecraftForge.EVENT_BUS.post(new PacketEvent.SendEvent((Packet) msg));
        super.write(ctx, msg, promise);
    }
}