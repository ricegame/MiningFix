package ricedotwho.mf.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import ricedotwho.mf.hud.titleHud;
import ricedotwho.mf.utils.hyApi;

public class ModConfig extends Config {
    @Switch(
            name = "Ability Ready Alert",
            description = "Creates a title",
            size = OptionSize.SINGLE,
            category = "Mining"
    )
    public static boolean miningAbilityAlert = false;
    @Switch(
            name = "Drill Fix",
            description = "Fixes the mining progress reset",
            size = OptionSize.SINGLE,
            category = "Mining",
            subcategory = "Fixes"
    )
    public static boolean drillFix = true;
    @Switch(
            name = "Pingless Mining",
            description = "Attempts to make a better mining experience",
            size = OptionSize.SINGLE,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean pinglessMining = false;
    @Switch(
            name = "Allow Hardstone",
            description = "Be warned this may flag, have not tested",
            size = OptionSize.SINGLE,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean allowHardstone = false;
    @Switch(
            name = "Fix breaking progress",
            description = "Makes breaking progress look like 0 ping",
            size = OptionSize.SINGLE,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean fixBreakingProgress = false;
    @Switch(
            name = "Pingless breaking sound",
            description = "Plays the breaking sound when pingless things you've broken the block, blocks the vanilla sound",
            size = OptionSize.SINGLE,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean pinglessSound = false;
    @Switch(
            name = "Use Mining Speed from Tablist",
            description = "Uses the Mining Speed from the tablist",
            size = OptionSize.SINGLE,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean tablistMiningSpeed = false;

    @Number(
            name = "Mining Speed",
            description = "USE GEMSTONE SPEED FOR GEMSTONE (use coleweight or do 'miningSpeed = 50 + (professionalLevel*5)')",
            min = 1, max = 50000,
            size = OptionSize.DUAL,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static int miningSpeed = 1;
    @Slider(
            name = "Professional Level",
            description = "Used for gemstone speed calc, 0 if you don't have it :)",
            min = 0, max = 140,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static int profLevel = 0;
    @Slider(
            name = "Extra Ticks",
            description = "Increase this if the mod is failing a lot",
            min = 0, max = 10,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static int extraTicks = 1;
    @Slider(
            name = "Mining Speed Boost Level",
            description = "Set this",
            min = 0, max = 3,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static int miningSpeedBoost = 1;
    @Switch(
            name = "Api requests",
            description = "Sends api requests to get ur ms and mf",
            size = OptionSize.SINGLE,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static boolean apiRequests = false;
    @Slider(
            name = "Api request interval",
            description = "Time to wait between api requests (Minutes)",
            min = 1f, max = 60f,
            category = "Mining",
            subcategory = "Pingless"
    )
    public static float apiInterval = 5f;
    @HUD(
            name = "Title HUD",
            category = "HUD",
            subcategory = "Title HUD"
    )
    public titleHud titleHud = new titleHud();
    @Switch(
            name = "Dev override",
            description = "developer purploses",
            size = OptionSize.SINGLE,
            category = "Dev"
    )
    public static boolean devOverride = false;
    @Switch(
            name = "Force not in skyblock",
            description = "developer purploses",
            size = OptionSize.SINGLE,
            category = "Dev"
    )
    public static boolean forceNotSkyblock = false;
    @Switch(
            name = "Dev info",
            description = "Sends additionally info to chat",
            size = OptionSize.SINGLE,
            category = "Dev"
    )
    public static boolean devInfo = false;
    @Text(
            name = "Custom API Key",
            description = "Overrides the mod api, only use this if you know what ur doing. If this doesnt work try /rsm setkey {API_KEY}",
            size = OptionSize.SINGLE,
            secure = true, multiline = true,
            category = "Dev"
    )
    public static String customApiKey = "";

    public ModConfig() {
        super(new Mod("Mining Fix", ModType.SKYBLOCK), "mfconfig.json");
        initialize();

        // Listeners
        addListener("customApiKey", hyApi::changeApiKey);

        // Dependencies
        addDependency("fixBreakingProgress","pinglessMining");
        addDependency("miningSpeed","pinglessMining");
        addDependency("tablistMiningSpeed","pinglessMining");
    }
}