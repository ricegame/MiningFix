package ricedotwho.mf.mining;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        // Glacite Gemstones

        MINING_HARDNESS.put("95:15", 5_200); // Onyx
        MINING_HARDNESS.put("95:11", 5_200); // Aquamarine
        MINING_HARDNESS.put("95:12", 5_200); // Citrine
        MINING_HARDNESS.put("95:13", 5_200); // Peridot

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
