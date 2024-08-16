package ricedotwho.mf.events;

import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PacketEvents extends Event {
    @Cancelable
    public static class PacketReceivedEvent extends Event {
        public final Packet<?> packet;

        public PacketReceivedEvent(Packet<?> packet) {
            this.packet = packet;
        }
        public Packet<?> getPacket() {
            return packet;
        }

        public static boolean postAndCatch(Packet<?> _packet) {
            PacketReceivedEvent event = new PacketReceivedEvent(_packet);
            try {
                MinecraftForge.EVENT_BUS.post(event); // Post the event to MinecraftForge event bus
                return true;
            } catch (Exception e) {
                e.printStackTrace(); // Handle or log the exception as needed
            }
            return false;
        }

    }

    @Cancelable
    public static class PacketSentEvent extends Event {
        public Packet<?> packet;


        public PacketSentEvent(Packet<?> packet) {
            this.packet = packet;
        }
        public Packet<?> getPacket() {
            return packet;
        }
        public void setPacket(Packet<?> packet) { this.packet = packet; }

        public static boolean postAndCatch(Packet<?> _packet) {
            PacketSentEvent event = new PacketSentEvent(_packet);
            try {
                MinecraftForge.EVENT_BUS.post(event); // Post the event to MinecraftForge event bus
                return true;
            } catch (Exception e) {
                e.printStackTrace(); // Handle or log the exception as needed
            }
            return false;
        }
    }
}
