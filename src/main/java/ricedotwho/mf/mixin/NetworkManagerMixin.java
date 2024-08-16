package ricedotwho.mf.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ricedotwho.mf.events.PacketEvents;

@Mixin(value = NetworkManager.class, priority = 1003)
public abstract class NetworkManagerMixin extends SimpleChannelInboundHandler<Packet<?>> {
    @Shadow @Final private EnumPacketDirection direction;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (this.direction == EnumPacketDirection.CLIENTBOUND) {
            PacketEvents.PacketReceivedEvent event = new PacketEvents.PacketReceivedEvent(packet);
            if(MinecraftForge.EVENT_BUS.post(event) && event.isCanceled()) {
                ci.cancel();
            }
        }
    }
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V",
            at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        PacketEvents.PacketSentEvent event = new PacketEvents.PacketSentEvent(packet);
        if(MinecraftForge.EVENT_BUS.post(event) && event.isCanceled()) {
            ci.cancel();
        }
    }
}
