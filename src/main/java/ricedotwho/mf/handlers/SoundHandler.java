package ricedotwho.mf.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.events.PacketEvents;
import ricedotwho.mf.utils.Utils;

import java.util.Objects;
import java.util.Random;

import static ricedotwho.mf.mf.soundsInfoMessage;

public class SoundHandler {
    static Random rand = new Random();
    static Minecraft mc = Minecraft.getMinecraft();
    public static ISound empty = new PositionedSound(new ResourceLocation("mfutils", "empty")) {{
        volume = 1f;
        pitch = 1f;
        repeat = false;
        repeatDelay = 0;
        attenuationType = AttenuationType.NONE;
    }};
    //@SubscribeEvent
    public void onSound(PlaySoundEvent event) {
        if(event.result == null) return;
        soundsInfoMessage("Sound: " + event.name + " Pitch: " + event.sound.getPitch() + " Volume: " + event.sound.getVolume());
        if(!ModConfig.killExpGain) return;
        if(!Utils.inSkyblock) return;
        if(Objects.equals(event.name, "random.orb")) {
            event.result = empty;
        }
    }
    public static void playGemstoneSound(BlockPos pos, float volume, float pitch, boolean distanceDelay) {
        String sound = "dig.glass";
        switch (ModConfig.gemstoneSound) {
            case 0:
                mc.theWorld.playSoundAtPos(pos, sound, volume, pitch, distanceDelay);
                return;
        }
    }
    @SubscribeEvent
    public void onPacket(PacketEvents.PacketReceivedEvent event) {
        if(event.packet instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect sound = (S29PacketSoundEffect) event.packet;
            soundsInfoMessage("Sound: " + sound.getSoundName() + " Pitch: " + sound.getPitch() + " Volume: " + sound.getVolume());
            if(!ModConfig.killExpGain) return;
            if(!Utils.inSkyblock) return;
            if(Objects.equals(sound.getSoundName(), "random.orb")) {
                event.setCanceled(true);
            }
        }
    }
}
