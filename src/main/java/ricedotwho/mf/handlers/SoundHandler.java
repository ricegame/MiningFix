package ricedotwho.mf.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.events.PacketEvents;
import ricedotwho.mf.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static ricedotwho.mf.mf.soundsInfoMessage;

public class SoundHandler {
    static Random rand = new Random();
    static Minecraft mc = Minecraft.getMinecraft();
    static ISound amethyst1 = new PositionedSound(new ResourceLocation("rsmutils", "amethyst1")) {{ volume = 1f; pitch = 1f; repeat = false; repeatDelay = 0; attenuationType = ISound.AttenuationType.NONE;}};
    static ISound amethyst2 = new PositionedSound(new ResourceLocation("rsmutils", "amethyst2")) {{ volume = 1f; pitch = 1f; repeat = false; repeatDelay = 0; attenuationType = ISound.AttenuationType.NONE;}};
    static ISound amethyst3 = new PositionedSound(new ResourceLocation("rsmutils", "amethyst3")) {{ volume = 1f; pitch = 1f; repeat = false; repeatDelay = 0; attenuationType = ISound.AttenuationType.NONE;}};
    static List<ISound> amethyst = new ArrayList<>();
    static {
        amethyst.add(amethyst1);
        amethyst.add(amethyst2);
        amethyst.add(amethyst3);
    }
    public static void playGemstoneSound(BlockPos pos, float volume, float pitch, boolean distanceDelay) {
        String sound = "dig.glass";
        switch (ModConfig.gemstoneSound) {
            case 0:
                mc.theWorld.playSoundAtPos(pos, sound, volume, pitch, distanceDelay);
                return;
            case 1:
                mc.getSoundHandler().playSound(amethyst.get(rand.nextInt(3)));
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
