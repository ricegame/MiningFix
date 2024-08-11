package ricedotwho.mf.mining;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.utils.hyApi;
import ricedotwho.mf.utils.hyUtils;
import ricedotwho.mf.utils.itemUtils;
import ricedotwho.mf.data.Point;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

public class miningStats {
    public static Point msAndBoost = new Point(0,0);
    static int miningSpeed = 0;
    static int drillBreakingPower = 0;
    private static Map<String, Object> playerData = null;
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    static Minecraft mc;
    static {
        mc = Minecraft.getMinecraft();
    }

    private static JsonObject getStats() {
        try {
            return hyApi.getSelectedProfile(getUUID());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String getUUID() {return mc.thePlayer.getUniqueID().toString().replace("-","");}

    private static void getNeededStats() {
        executorService.execute(() -> {
            JsonObject raw_data = getStats();
            Map<String, Object> result = new HashMap<>();
            JsonObject player_data = raw_data
                    .getAsJsonObject("members")
                    .getAsJsonObject(getUUID());

            JsonArray petsArray = player_data.getAsJsonObject("pets_data").getAsJsonArray("pets");

            Optional<JsonObject> activePet = StreamSupport.stream(petsArray.spliterator(), false)
                    .filter(JsonElement::isJsonObject)
                    .map(JsonElement::getAsJsonObject)
                    .filter(pet -> pet.get("active").getAsBoolean())
                    .findFirst();

            JsonObject pet = activePet.orElse(null);

            Set<JsonObject> mining_talisman = new HashSet<>();

            // Check if "bag_contents" exists in playerData
            if (player_data.getAsJsonObject("inventory").has("bag_contents")) {
                JsonArray dataArray = player_data
                        .getAsJsonObject("inventory")
                        .getAsJsonObject("bag_contents")
                        .getAsJsonObject("talisman_bag")
                        .getAsJsonArray("data");

                // Convert JsonArray to stream
                for (JsonElement element : dataArray) {
                    if (!element.isJsonObject()) continue;
                    JsonObject tali = element.getAsJsonObject();
                    if (!tali.has("tag")) continue;
                    JsonObject tag = tali.getAsJsonObject("tag");
                    if (!tag.has("ExtraAttributes")) continue;
                    JsonObject extraAttributes = tag.getAsJsonObject("ExtraAttributes");
                    String id = extraAttributes.has("id") ? extraAttributes.get("id").getAsString() : null;
                    if (id != null && miningData.MINING_TALISMAN.containsKey(id) && !mining_talisman.contains(tali)) {
                        mining_talisman.add(tali);
                    }
                }
            }

            JsonArray equipment = new JsonArray();
            if (player_data.getAsJsonObject("inventory").has("equipment_contents")) {
                equipment = player_data
                        .getAsJsonObject("inventory")
                        .getAsJsonObject("equipment_contents")
                        .getAsJsonArray("data");
            }

            JsonObject mining_core = new JsonObject();
            if (player_data.has("mining_core")) {
                mining_core = player_data
                        .getAsJsonObject("mining_core");
            }

            int mining_level = 0;
            if (player_data.getAsJsonObject("player_data").getAsJsonObject("experience").has("SKILL_MINING")) {
                mining_level = player_data
                        .getAsJsonObject("player_data")
                        .getAsJsonObject("experience")
                        .getAsJsonPrimitive("SKILL_MINING").getAsInt();
            }

            // Result map
            result.put("success", true);
            result.put("equipment", equipment);
            result.put("mining_level", mining_level);
            result.put("mining_talisman", mining_talisman);
            result.put("mining_core", mining_core);
            result.put("pet", pet);
            playerData = result;
            System.out.println("Data from api: " + playerData);
        });
    }
    public static Integer getMiningSpeed() { return miningSpeed; }
    public static void findMiningSpeed() {
        double ms = 0;
        int bp = 0;
        String dp = "";
        Map<String, Object> data = playerData;
        if(data == null) return;

        if (mc.thePlayer.isPotionActive(Potion.digSpeed)) {
            ms += mc.thePlayer.getActivePotionEffect(Potion.digSpeed).getAmplifier()*50;
        }

        // Pet
        JsonObject pet = (JsonObject) data.getOrDefault("pet", null);
        if (pet != null) {
            int pet_lvl = hyUtils.findClosestLevel(pet.getAsJsonObject("exp").getAsInt(), false);
            String pet_type = pet.getAsJsonObject("type").getAsString();
            int pet_ms = miningData.PET_MS.getOrDefault(pet_type, 0);

            switch (pet.getAsJsonObject("heldItem").getAsString()) {
                case "PET_ITEM_QUICK_CLAW":
                    ms += Math.floor((double) pet_lvl / 2);
                    break;
                case "BEJEWELED_COLLAR":
                    ms += 25;
                    break;
                case "MINOS_RELIC":
                    ms += pet_ms * 0.333;
                    break;
            }
        }

        ItemStack[] armour = mc.thePlayer.inventory.armorInventory;
        for (ItemStack piece : armour) {
            ms += parseGemstonesAndReforge(piece);
        }

        ItemStack heldItem = mc.thePlayer.getHeldItem();
        boolean heated = false;
        // Check if its a pick / drill
        if (miningData.MINING_ITEMS.contains(heldItem.getItem())) {
            String _id = itemUtils.getSkyBlockItemID(heldItem);

            Map<String, Integer> currentItem = miningData.ITEM_MS.getOrDefault(_id, null);
            if(currentItem != null) {
                ms += currentItem.get("ms");
                bp += currentItem.get("bp");
            }

            ms += parseGemstonesAndReforge(heldItem);

            NBTTagCompound ea = itemUtils.getExtraAttributes(heldItem);
            if(ea.hasKey("modifier")) { heated = ea.getString("modifier").equals("heated"); }

            if(ea.hasKey("enchantments")) {
                NBTTagCompound ench = ea.getCompoundTag("enchantments");
                ms += ench.hasKey("efficiency") ? ench.getInteger("efficiency")*20 : 0;
            }

            if(ea.hasKey("polarvoid")) {
                ms += ea.getInteger("polarvoid")*10;
                bp++;
            }

            if(ea.hasKey("drill_part_engine")) {
                String eng = ea.getString("drill_part_engine");
                if(miningData.DRILL_ENGINES.containsKey(eng)) {
                    ms += miningData.DRILL_ENGINES.get("drill_part_engine").get("ms");
                }
            }

            // drill part
            dp = ea.hasKey("drill_part_upgrade_module") ? ea.getString("drill_part_upgrade_module") : "";
            if(Objects.equals(dp, "GOBLIN_OMELETTE_SUNNY_SIDE")) {
                ms += 50;
            }

            if(ea.hasKey("divan_powder_coating")) {
                ms += ea.getInteger("divan_powder_coating")*500;
            }
        }

        // equipment
        JsonArray equipment = (JsonArray) data.get("equipment");
        for(JsonElement eq : equipment) {

        }
    }

    public static int parseGemstonesAndReforge(ItemStack item) {
        int ms = 0;

        NBTTagCompound tag = item.getSubCompound("ExtraAttributes",false);

        if (!tag.hasKey("ExtraAttributes")) {
            return ms;
        }

        NBTTagCompound extraAttributes = tag.getCompoundTag("ExtraAttributes");

        NBTTagCompound displayTag = tag.getCompoundTag("display");
        NBTTagList loreTagList = displayTag.getTagList("Lore", 8); // 8 means it's a list of strings
        List<String> lore = getStringListFromNBT(loreTagList);

        if (lore.isEmpty()) {
            return ms;
        }

        String lastLoreLine = lore.get(lore.size() - 1);
        String[] rarityLines = EnumChatFormatting.getTextWithoutFormattingCodes(lastLoreLine).split(" ");
        String rarity = rarityLines[0].equals("a") ? rarityLines[1] : rarityLines[0];

        String modifier = extraAttributes.getString("modifier");

        if (modifier != null && miningData.MODIFIERS.containsKey(modifier)) {
            Map<String, Integer> rarityStats = miningData.MODIFIERS.get(modifier).get(rarity);
            if (rarityStats != null) {
                ms += rarityStats.getOrDefault("ms", 0);
            }
        }

        String id = extraAttributes.getString("id");
        String type = id.split("_")[0];

        ms += getMsValue(miningData.ARMOUR, type);
        ms += getMsValue(miningData.EQUIPMENT, id);

        NBTTagCompound gems = extraAttributes.getCompoundTag("gems");
        if (gems == null) {
            return ms;
        }

        for (String gemKey : gems.getKeySet()) {
            String gemName = gemKey.split("_")[0].toUpperCase();
            if ("AMBER".equals(gemName)) {
                NBTTagCompound gemDetails = gems.getCompoundTag(gemKey);
                String quality = gemDetails.hasKey("quality") ? gemDetails.getString("quality") : "ROUGH";

                Map<String, Integer> rarityStats = miningData.GEMSTONE_STATS.get(gemName).get(rarity);
                if (rarityStats != null) {
                    ms += rarityStats.getOrDefault(quality, 0);
                }
            }
        }

        System.out.println(id + ": " + ms);
        return ms;
    }
    public static Point getGemstoneSpeed() { // returning ms, ms ability level
        Map<String, Object> data = playerData;
        int ms = 0;
        int ms_boost = 0;
        JsonObject mining_core = (JsonObject) data.getOrDefault("mining_core", null);
        if(mining_core == null) return new Point(ms, ms_boost); // 0, 0
        JsonObject nodes = mining_core.getAsJsonObject("nodes");
        if(nodes.has("professional")) {
            int pro_lvl = nodes.get("professional").getAsInt();
            ms += pro_lvl > 0 ? 50 + (pro_lvl*5) : 0;
        }
        if(nodes.has("mining_speed_boost")) {
            ms_boost = nodes.get("mining_speed_boost").getAsInt();
        }
        return new Point(ms, ms_boost);
    }

    private static int getMsValue(Map<String, Map<String, Integer>> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key).getOrDefault("ms", 0);
        }
        return 0;
    }
    private static List<String> getStringListFromNBT(NBTTagList nbtTagList) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            NBTBase nbtBase = nbtTagList.get(i);
            if (nbtBase instanceof net.minecraft.nbt.NBTTagString) {
                list.add(((net.minecraft.nbt.NBTTagString) nbtBase).getString());
            }
        }
        return list;
    }

    static Runnable updateStats = new Runnable() {
        @Override
        public void run() {
            if(!ModConfig.apiRequests || mc.thePlayer == null) return;
            getNeededStats();
        }
    };
    public static void startRunning() {
        scheduler.scheduleAtFixedRate(updateStats, 1, (long) ModConfig.apiInterval, TimeUnit.MINUTES);
    }
}
