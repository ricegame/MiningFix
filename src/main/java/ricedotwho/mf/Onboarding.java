package ricedotwho.mf;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ricedotwho.mf.config.ModConfig;

import static ricedotwho.mf.mf.sendMessageWithPrefix;

public class Onboarding {
    @SubscribeEvent
    public void OnLoad(WorldEvent.Load event) {
        if(ModConfig.onboarding || !HypixelUtils.INSTANCE.isHypixel()) return;
        ModConfig.onboarding = true;
        sendMessageWithPrefix(
                        "It appears this is your first time using MiningFix!\n"
                        + "Open the gui with '/mf' or by using the OneConfig GUI\n"
                        + "Please note that this mod can be inconsistent with bad tps or inconsistent ping!\n"
                        + "If you have any bugs to report or want to ask questions add my discord @rice.who\n"
                        + " - rice"
        );
    }
}
