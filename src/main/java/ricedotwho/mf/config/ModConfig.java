package ricedotwho.mf.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import ricedotwho.mf.hud.TitleHud;
import ricedotwho.mf.hud.TpsHud;
import ricedotwho.mf.utils.HyApi;

public class ModConfig extends Config {
    @Switch(
            name = "Mute skill exp gain",
            description = "Stops the random.orb sound from playing",
            category = "Sounds"
    )
    public static boolean killExpGain = false;
    @Switch(
            name = "Ability Ready Alert",
            description = "Creates a title",
            category = "Mining"
    )
    public static boolean miningAbilityAlert = false;
    @Switch(
            name = "Drill Fix",
            description = "Fixes the mining progress reset",
            category = "Mining",
            subcategory = "Fixes"
    )
    public static boolean drillFix = true;
    @Switch(
            name = "Pingless Mining",
            description = "Attempts to make a better mining experience",
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean pinglessMining = false;
    @Switch(
            name = "Allow Hardstone",
            description = "Very buggy btw",
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean allowHardstone = false;
    @Switch(
            name = "Fix breaking progress",
            description = "Makes breaking progress look like 0 ping",
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean fixBreakingProgress = true;
    @Switch(
            name = "Pingless breaking sound",
            description = "Plays the breaking sound when pingless things you've broken the block, blocks the vanilla sound",
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean pinglessSound = true;
    @Slider(
            name = "Extra Ticks",
            description = "Increase this if the mod is failing a lot",
            min = 0, max = 10,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static int extraTicks = 1;
    @Slider(
            name = "Api request interval (Minutes)",
            description = "Time to wait between api requests. Run \"/mf dev api\" to manually fetch",
            min = 5f, max = 60f,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static float apiInterval = 5f;
    @Switch(
            name = "Dev override",
            description = "developer purploses",
            category = "Dev"
    )
    public static boolean devOverride = false;
    @Switch(
            name = "Force not in skyblock",
            description = "developer purploses",
            category = "Dev"
    )
    public static boolean forceNotSkyblock = false;
    @Switch(
            name = "Dev info",
            description = "Sends additionally info to chat",
            category = "Dev"
    )
    public static boolean devInfo = false;
    @Switch(
            name = "Sounds info",
            description = "Sends additionally info to chat",
            category = "Dev"
    )
    public static boolean soundsInfo = false;
    @Text(
            name = "Custom API Key",
            description = "This does nothing rn",
            secure = true, multiline = true,
            category = "Dev"
    )
    public static String customApiKey = "";

    // yapology
    @HUD(
            name = "Title HUD",
            category = "HUD",
            subcategory = "Title HUD"
    )
    public TitleHud titleHud = new TitleHud();
    @HUD(
            name = "TPS HUD",
            category = "HUD",
            subcategory = "TPS HUD"
    )
    public TpsHud tpsHud = new TpsHud();

    public ModConfig() {
        super(new Mod("Mining Fix", ModType.SKYBLOCK), "mfconfig.json");
        initialize();

        // Listeners
        addListener("customApiKey", HyApi::changeApiKey);

        // Dependencies
        addDependency("fixBreakingProgress","pinglessMining");
        addDependency("miningSpeed","pinglessMining");
        addDependency("tablistMiningSpeed","pinglessMining");
        addDependency("pinglessSound","pinglessMining");
        addDependency("profLevel","pinglessMining");
        addDependency("extraTicks","pinglessMining");
        addDependency("miningSpeedBoost","pinglessMining");
        addDependency("apiInterval","pinglessMining");
        addDependency("allowHardstone","pinglessMining");
    }
}