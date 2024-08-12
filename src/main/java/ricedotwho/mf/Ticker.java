package ricedotwho.mf;

import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ricedotwho.mf.events.OnTimeEvent;
import ricedotwho.mf.events.PacketEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ticker {
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static double ticksThisSecond = 0;
    public static double tps = 20;
    public static boolean isServerTicking = false;
    @SubscribeEvent
    public void onServerTick(PacketEvent.ReceiveEvent event) {
        if(!(event.packet instanceof S32PacketConfirmTransaction)) return;
        ticksThisSecond++;
        if(!isServerTicking && ticksThisSecond > 0) {
            isServerTicking = true;
            mf.devInfoMessage("Server started ticking!");
        }
    }
    @SubscribeEvent
    public void onUnload(WorldEvent.Unload event) {isServerTicking = false;}
    static Runnable tpsCheck = new Runnable() {
        @Override
        public void run() {
            OnTimeEvent.Second.postAndCatch();
            tps = ticksThisSecond > 20 ? 20 : ticksThisSecond;
            ticksThisSecond = 0;
        }
    };
    public static void startRunning() {
        scheduler.scheduleAtFixedRate(tpsCheck, 1, 1, TimeUnit.SECONDS);
    }
}
