package ricedotwho.mf;

import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ricedotwho.mf.events.OnTimeEvent;
import ricedotwho.mf.events.PacketEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ticker {
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static double ticksThisSecond = 0;
    public static double instantTps = 20;
    public static double tps = 20D;
    private static List<Integer> pastTps = new ArrayList<>();
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
    public static void getTps() {
        double total = 0D;
        for (Integer secondTps : pastTps) {
            total += secondTps;
        }
        tps = total / pastTps.size();
    }
    static Runnable tpsCheck = new Runnable() {
        @Override
        public void run() {
            OnTimeEvent.Second.postAndCatch();
            tps = ticksThisSecond > 20 ? 20 : ticksThisSecond;
            if(pastTps.size() < 5) {
                pastTps.add((int) tps);
            } else {
                pastTps.remove(0);
                pastTps.add((int) tps);
            }
            getTps();
            ticksThisSecond = 0;
        }
    };
    public static void startRunning() {
        scheduler.scheduleAtFixedRate(tpsCheck, 1, 1, TimeUnit.SECONDS);
    }
}
