package ricedotwho.mf.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SkyblockEvent extends Event {
    public static class Joined extends SkyblockEvent {
        public Joined() {}
        public static void postAndCatch() {
            Joined event = new Joined();
            try {
                MinecraftForge.EVENT_BUS.post(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static class Left extends SkyblockEvent {
        public Left() {}
        public static void postAndCatch() {
            Left event = new Left();
            try {
                MinecraftForge.EVENT_BUS.post(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

