package ricedotwho.mf.handlers;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.utils.Utils;

import java.util.Objects;

import static ricedotwho.mf.mf.soundsInfoMessage;

public class SoundHandler {
    @SubscribeEvent
    public void onSound(PlaySoundEvent event) {
        soundsInfoMessage("Sound: " + event.name + " Pitch: " + event.result.getPitch() + " Volume: " + event.result.getVolume());
        if(!ModConfig.killExpGain) return;
        if(!Utils.inSkyblock) return;
        if(Objects.equals(event.name, "random.orb")) {
            event.result = null;
        }
    }
}
