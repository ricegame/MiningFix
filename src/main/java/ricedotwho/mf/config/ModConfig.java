package ricedotwho.mf.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import ricedotwho.mf.Reference;
import ricedotwho.mf.hud.DevHud;
import ricedotwho.mf.hud.TitleHud;
import ricedotwho.mf.hud.TpsHud;
import ricedotwho.mf.utils.HyApi;

public class ModConfig extends Config {
    @Header(
            text = "MiningFix v" + Reference.VERSION + " by rice.who",
            size = OptionSize.DUAL,
            category = "Mining",
            subcategory = "Info"
    )
    public static boolean ignored;
    @Info(
            text = "This mod COULD be bannable! Use at your own risk!",
            type = InfoType.WARNING,
            size = OptionSize.DUAL,
            category = "Mining",
            subcategory = "Info"
    )
    public static boolean ignored1;
    @Switch(
            name = "Ability Ready Alert",
            description = "Creates a title",
            category = "Mining"
    )
    public static boolean miningAbilityAlert = false;
    @Switch(
            name = "Don't Break Cobblestone",
            description = "Stops breaking cobblestone in the ch",
            category = "Mining"
    )
    public static boolean dontBreakCobblestone = false;
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
    @Switch(
            name = "Ticking message",
            description = "Sends a message to chat when the server starts ticking",
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean tickingMessage = false;
    @Slider(
            name = "Api request interval (Minutes)",
            description = "Time to wait between api requests. Run \"/mf dev api\" to manually fetch",
            min = 5f, max = 60f,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static float apiInterval = 5f;
    @Slider(
            name = "Extra Ticks",
            description = "Increase this if the mod is failing a lot\nIf you're not sure what to put this at leave it at 0",
            min = 0, max = 10,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static int extraTicks = 0;
    @Switch(
            name = "Mute skill exp gain",
            description = "Stops the random.orb sound from playing",
            category = "Sounds"
    )
    public static boolean killExpGain = false;
    @Dropdown(
            name = "Custom Gemstone sounds",
            description = "Select from some custom gem sounds (NYI)",
            options = {"Vanilla","Amethyst"},
            category = "Sounds"
    )
    public static int gemstoneSound = 0;
    @Info(
            text = "These are DEV options, dont change these unless you know what you are doing!",
            type = InfoType.ERROR,
            size = OptionSize.DUAL,
            category = "Dev",
            subcategory = "Info"
    )
    public static boolean ignored2;
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
    @Slider(
            name = "Max Server tps",
            description = ("Max tps which the mod will accept. Default: 20\n§4Don't change this if you dont know what your doing"),
            step = 1,
            min = 1, max = 25,
            category = "Dev"
    )
    public static int maxServerTick = 20;
    @Slider(
            name = "Min ping delay",
            description = ("Min delay since last ping mod will accept. Default: 30\n§4Don't change this if you dont know what your doing"),
            step = 1,
            min = 1, max = 50,
            category = "Dev"
    )
    public static int pingDelay = 30;

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
    @HUD(
            name = "Dev HUD",
            category = "HUD",
            subcategory = "Dev HUD"
    )
    public DevHud devHud = new DevHud();

    // other
    public static boolean onboarding = false;

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
        addDependency("tickingMessage","pinglessMining");
    }
}