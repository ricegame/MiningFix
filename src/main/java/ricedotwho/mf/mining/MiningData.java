package ricedotwho.mf.mining;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo: make this cleaner. rn its 92.5% chatgpt
public class MiningData {
    public static final Map<String, Integer> MINING_TALISMAN = new HashMap<>();
    public static final Map<String, Integer> PET_MS = new HashMap<>();
    public static final Map<String, Integer> MINING_HARDNESS = new HashMap<>();

    static {
        MINING_TALISMAN.put("TITANIUM_TALISMAN", 15);
        MINING_TALISMAN.put("TITANIUM_RING", 30);
        MINING_TALISMAN.put("TITANIUM_ARTIFACT", 45);
        MINING_TALISMAN.put("TITANIUM_RELIC", 60);

        MINING_TALISMAN.put("DWARVEN_METAL", 0);

        MINING_TALISMAN.put("POWER_TALISMAN", 0);
        MINING_TALISMAN.put("POWER_RING", 0);
        MINING_TALISMAN.put("POWER_ARTIFACT", 0);
        MINING_TALISMAN.put("POWER_RELIC", 0);

        // pet ms
        PET_MS.put("SCATHA", 100);
        PET_MS.put("GALCITE_GOLEM", 125);


        // Dwarvern Mines
        MINING_HARDNESS.put("35:7", 500); // Gray Wool
        MINING_HARDNESS.put("159:9", 500); // Cyan Stained Clay

        MINING_HARDNESS.put("168:0", 800); // Prismarine
        MINING_HARDNESS.put("168:1", 800); // Prismarine Bricks
        MINING_HARDNESS.put("168:2", 800); // Dark Prismarine

        MINING_HARDNESS.put("35:3", 1_500); // Light Blue Wool

        MINING_HARDNESS.put("1:4", 2_000); // Titanium

        // Crystal Hollows
        MINING_HARDNESS.put("1:0", 50); // Hardstone

        MINING_HARDNESS.put("95:1", 3_000); // Amber
        MINING_HARDNESS.put("95:2", 4_800); // Jasper
        MINING_HARDNESS.put("95:3", 3_000); // Sapphire
        MINING_HARDNESS.put("95:4", 3_800); // Topaz
        MINING_HARDNESS.put("95:5", 3_000); // Jade
        MINING_HARDNESS.put("95:10", 3_000); // Amethyst
        MINING_HARDNESS.put("95:14", 2_300); // Ruby

        // Glacite Tunnels
        MINING_HARDNESS.put("174:0", 6_000); // Glacite

        MINING_HARDNESS.put("179:0", 5_600); // Red Sandstone
        MINING_HARDNESS.put("172:0", 5_600); // Hardened Clay
        MINING_HARDNESS.put("159:12", 5_600); // Brown Stained Clay

        MINING_HARDNESS.put("82:0", 5_600); // Clay
        MINING_HARDNESS.put("4:0", 5_600); // Cobblestone
    }

    // GEMSTONE_STATS
    public static final Map<String, Map<String, Map<String, Integer>>> GEMSTONE_STATS = new HashMap<>();
    static {
        Map<String, Map<String, Integer>> amberMap = new HashMap<>();

        amberMap.put("COMMON", createGemstoneStats(4, 6, 10, 14, 20));
        amberMap.put("UNCOMMON", createGemstoneStats(8, 10, 14, 20, 28));
        amberMap.put("RARE", createGemstoneStats(12, 14, 20, 30, 40));
        amberMap.put("EPIC", createGemstoneStats(16, 18, 28, 44, 60));
        amberMap.put("LEGENDARY", createGemstoneStats(20, 24, 36, 58, 80));
        amberMap.put("MYTHIC", createGemstoneStats(24, 30, 45, 75, 100));
        amberMap.put("DIVINE", createGemstoneStats(28, 36, 54, 92, 120));

        GEMSTONE_STATS.put("AMBER", amberMap);
    }

    private static Map<String, Integer> createGemstoneStats(int rough, int flawed, int fine, int flawless, int perfect) {
        Map<String, Integer> map = new HashMap<>();
        map.put("ROUGH", rough);
        map.put("FLAWED", flawed);
        map.put("FINE", fine);
        map.put("FLAWLESS", flawless);
        map.put("PERFECT", perfect);
        return map;
    }

    // ARMOUR
    public static final Map<String, Map<String, Integer>> ARMOUR = new HashMap<>();
    static {
        ARMOUR.put("DIVAN", createStatMap(80, 30));
        ARMOUR.put("SORROW", createStatMap(50, 20));
        ARMOUR.put("FLAME", createStatMap(25, 10));
        ARMOUR.put("HEAT", createStatMap(10, 5));
        ARMOUR.put("GOBLIN", createStatMap(10, 0));
        ARMOUR.put("GLACITE", createStatMap(10, 0));
    }

    private static Map<String, Integer> createStatMap(int ms, int mf) {
        Map<String, Integer> map = new HashMap<>();
        map.put("ms", ms);
        map.put("mf", mf);
        return map;
    }

    // MODIFIERS
    public static final Map<String, Map<String, Map<String, Integer>>> MODIFIERS = new HashMap<>();
    static {
        MODIFIERS.put("jaded", createModifierMap(
                createStatMap(5, 5), createStatMap(12, 10), createStatMap(20, 15),
                createStatMap(30, 20), createStatMap(45, 25), createStatMap(60, 30),
                createStatMap(60, 30), createStatMap(60, 30), createStatMap(60, 30)
        ));
        MODIFIERS.put("auspicious", createModifierMap(
                createStatMap(7, 0), createStatMap(14, 0), createStatMap(23, 0),
                createStatMap(34, 0), createStatMap(45, 0), createStatMap(60, 0),
                createStatMap(75, 0), createStatMap(75, 0), createStatMap(75, 0)
        ));
        MODIFIERS.put("lustrous", createModifierMap(
                createStatMap(5, 5), createStatMap(10, 7), createStatMap(15, 9),
                createStatMap(20, 12), createStatMap(25, 16), createStatMap(30, 20),
                createStatMap(35, 25), createStatMap(35, 25), createStatMap(35, 25)
        ));
        MODIFIERS.put("ambered", createModifierMap(
                createStatMap(25, 0), createStatMap(31, 0), createStatMap(38, 0),
                createStatMap(46, 0), createStatMap(55, 0), createStatMap(65, 0),
                createStatMap(75, 0), createStatMap(75, 0), createStatMap(75, 0)
        ));
        MODIFIERS.put("fleet", createModifierMap(
                createStatMap(25, 0), createStatMap(31, 0), createStatMap(38, 0),
                createStatMap(46, 0), createStatMap(55, 0), createStatMap(65, 0),
                createStatMap(75, 0), createStatMap(75, 0), createStatMap(75, 0)
        ));
        MODIFIERS.put("mithraic", createModifierMap(
                createStatMap(6, 0), createStatMap(12, 0), createStatMap(20, 0),
                createStatMap(30, 0), createStatMap(40, 0), createStatMap(55, 0),
                createStatMap(70, 0), createStatMap(70, 0), createStatMap(70, 0)
        ));

        MODIFIERS.put("stellar", createModifierMap(
                createStatMap(3, 0), createStatMap(6, 0), createStatMap(9, 0),
                createStatMap(12, 0), createStatMap(15, 0), createStatMap(20, 0),
                createStatMap(25, 0), createStatMap(25, 0), createStatMap(25, 0)
        ));
        MODIFIERS.put("excellent", createModifierMap(
                createStatMap(4, 0), createStatMap(8, 0), createStatMap(12, 0),
                createStatMap(16, 0), createStatMap(20, 0), createStatMap(25, 0),
                createStatMap(25, 0), createStatMap(25, 0), createStatMap(25, 0)
        ));
        MODIFIERS.put("fortunate", createModifierMap(
                createStatMap(1, 1), createStatMap(2, 1), createStatMap(3, 1),
                createStatMap(4, 2), createStatMap(6, 2), createStatMap(8, 3),
                createStatMap(8, 3), createStatMap(8, 3), createStatMap(8, 3)
        ));
        MODIFIERS.put("sturdy", createModifierMap(
                createStatMap(3, 0), createStatMap(6, 0), createStatMap(9, 0),
                createStatMap(12, 0), createStatMap(15, 0), createStatMap(20, 0),
                createStatMap(20, 0), createStatMap(20, 0), createStatMap(20, 0)
        ));
        MODIFIERS.put("glistening", createModifierMap(
                createStatMap(0, 2), createStatMap(0, 3), createStatMap(0, 4),
                createStatMap(0, 5), createStatMap(0, 6), createStatMap(0, 7),
                createStatMap(0, 8), createStatMap(0, 8), createStatMap(0, 8)
        ));
    }

    private static Map<String, Map<String, Integer>> createModifierMap(
            Map<String, Integer> common, Map<String, Integer> uncommon, Map<String, Integer> rare,
            Map<String, Integer> epic, Map<String, Integer> legendary, Map<String, Integer> mythic,
            Map<String, Integer> divine, Map<String, Integer> special, Map<String, Integer> verySpecial) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        map.put("COMMON", common);
        map.put("UNCOMMON", uncommon);
        map.put("RARE", rare);
        map.put("EPIC", epic);
        map.put("LEGENDARY", legendary);
        map.put("MYTHIC", mythic);
        map.put("DIVINE", divine);
        map.put("SPECIAL", special);
        map.put("VERY", verySpecial);
        return map;
    }

    // EQUIPMENT
    public static final Map<String, Map<String, Integer>> EQUIPMENT = new HashMap<>();
    static {
        EQUIPMENT.put("MITHRIL_BELT", createStatMap(20, 5));
        EQUIPMENT.put("MITHRIL_CLOAK", createStatMap(20, 5));
        EQUIPMENT.put("MITHRIL_NECKLACE", createStatMap(20, 5));
        EQUIPMENT.put("MITHRIL_GAUNTLET", createStatMap(20, 5));

        EQUIPMENT.put("TITANIUM_BELT", createStatMap(30, 10));
        EQUIPMENT.put("TITANIUM_CLOAK", createStatMap(30, 10));
        EQUIPMENT.put("TITANIUM_NECKLACE", createStatMap(30, 10));
        EQUIPMENT.put("TITANIUM_GAUNTLET", createStatMap(30, 10));

        EQUIPMENT.put("JADE_BELT", createStatMap(30, 10));
        EQUIPMENT.put("SAPPHIRE_CLOAK", createStatMap(30, 10));
        EQUIPMENT.put("AMBER_NECKLACE", createStatMap(30, 10));
        EQUIPMENT.put("AMETHYST_GAUNTLET", createStatMap(30, 10));

        EQUIPMENT.put("DWARVEN_HANDWARMERS", createStatMap(45, 30));

        EQUIPMENT.put("DIVAN_PENDANT", createStatMap(100, 25));
    }
    public static final Map<String, Map<String, Integer>> ITEM_MS = new HashMap<>();

    static {
        // Drills
        // Mithril
        addItem("MITHRIL_DRILL_1", 450, 0, 5);
        addItem("MITHRIL_DRILL_2", 600, 0, 6);

        // Titanium
        addItem("TITANIUM_DRILL_1", 700, 25, 7);
        addItem("TITANIUM_DRILL_2", 900, 40, 8);
        addItem("TITANIUM_DRILL_3", 1200, 70, 9);
        addItem("TITANIUM_DRILL_4", 1600, 120, 9);
        addItem("DIVAN_DRILL", 1800, 150, 10);

        // Gemstone
        addItem("GEMSTONE_DRILL_1", 150, 0, 7);
        addItem("GEMSTONE_DRILL_2", 300, 0, 8);
        addItem("GEMSTONE_DRILL_3", 450, 0, 9);
        addItem("GEMSTONE_DRILL_4", 600, 0, 9);

        // Other
        addItem("GEMSTONE_GAUNTLET", 1600, 0, 9);

        // Pickaxes
        addItem("WOOD_PICKAXE", 70, 0, 1);
        addItem("GOLD_PICKAXE", 250, 0, 1);
        addItem("STONE_PICKAXE", 110, 0, 2);
        addItem("IRON_PICKAXE", 160, 0, 3);
        addItem("DIAMOND_PICKAXE", 220, 0, 4);
        addItem("LAPIS_PICKAXE", 200, 0, 4);
        addItem("ZOMBIE_PICKAXE", 190, 0, 3);
        addItem("ROOKIE_PICKAXE", 150, 0, 2);
        addItem("PROMISING_PICKAXE", 190, 0, 3);
        addItem("STONK_PICKAXE", 380, 0, 1);
        addItem("JUNGLE_PICKAXE", 330, 0, 5);
        addItem("FRACTURED_MITHRIL_PICKAXE", 225, 0, 5);
        addItem("BANDAGED_MITHRIL_PICKAXE", 250, 0, 5);
        addItem("MITHRIL_PICKAXE", 280, 0, 5);
        addItem("REFINED_MITHRIL_PICKAXE", 300, 0, 5);
        addItem("TITANIUM_PICKAXE", 310, 0, 6);
        addItem("REFINED_TITANIUM_PICKAXE", 400, 0, 6);
        addItem("PICKONIMBUS", 1500, 0, 7);
        addItem("BINGONIMBUS_2000", 1500, 100, 7);
        addItem("ALPHA_PICK", 1, 0, 0);
    }

    private static void addItem(String itemName, int ms, int mf, int bp) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("ms", ms);
        stats.put("mf", mf);
        stats.put("bp", bp);
        ITEM_MS.put(itemName, stats);
    }

    public static final Map<String, Map<String, Integer>> DRILL_ENGINES = new HashMap<>();

    static {
        DRILL_ENGINES.put("MITHRIL_DRILL_ENGINE", createStatMap(50, 0));
        DRILL_ENGINES.put("TITANIUM_DRILL_ENGINE", createStatMap(100, 0));
        DRILL_ENGINES.put("RUBY_POLISHED_DRILL_ENGINE", createStatMap(150, 30));
        DRILL_ENGINES.put("SAPPHIRE_POLISHED_DRILL_ENGINE", createStatMap(250, 50));
        DRILL_ENGINES.put("AMBER_POLISHED_DRILL_ENGINE", createStatMap(400, 100));
    }
    static List<Item> MINING_ITEMS = new ArrayList<>();
    static {
        MINING_ITEMS.add(Item.getItemById(409)); // Prismarine Shard
        MINING_ITEMS.add(Item.getItemById(397)); // Player Head (Gauntlet)

        MINING_ITEMS.add(Item.getItemById(270)); // Wooden Pickaxe
        MINING_ITEMS.add(Item.getItemById(274)); // Stone Pickaxe
        MINING_ITEMS.add(Item.getItemById(257)); // Iron Pickaxe
        MINING_ITEMS.add(Item.getItemById(285)); // Golden Pickaxe
        MINING_ITEMS.add(Item.getItemById(278)); // Diamond Pickaxe
    }
}
