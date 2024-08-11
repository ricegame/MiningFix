package ricedotwho.mf.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnTimeEvent extends Event {
    public static class Second extends OnTimeEvent {
        public Second() {}
        public static void postAndCatch() {
            Second event = new Second();
            try {
                MinecraftForge.EVENT_BUS.post(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
