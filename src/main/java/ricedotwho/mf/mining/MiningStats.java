package ricedotwho.mf.mining;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.data.ApiData;
import ricedotwho.mf.utils.HyApi;
import ricedotwho.mf.utils.HyUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ricedotwho.mf.mf.devInfoMessage;

public class MiningStats {
    private static JsonObject miningCore = null;
    private static JsonObject pets_data = null;
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    static Minecraft mc = Minecraft.getMinecraft();

    private static JsonObject getStats() {
        try {
            return HyApi.getSelectedProfile(myUUID());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static String myUUID() {
        devInfoMessage("UUID called");
        return mc.thePlayer.getUniqueID().toString().replace("-","");
    }

    public static void getMiningCore() {
        executorService.execute(() -> {
            JsonObject myMember = getStats().getAsJsonObject("members").getAsJsonObject(myUUID());
            miningCore = myMember.has("mining_core") ? myMember.getAsJsonObject("mining_core") : null;
            pets_data = myMember.has("pets_data") ? myMember.getAsJsonObject("pets_data") : null;
            if(ModConfig.devInfo) { System.out.println("miningCore: " + miningCore); }
        });
    }
    public static ApiData getApiData() { // returning ms, ms ability level
        int ms = 0;
        int ms_boost = 0;
        int potm = 0;
        int pet_lvl = 0;
        double bal_buff = 0D;
        JsonObject pet = null;
        if(miningCore == null) return new ApiData(ms, ms_boost, potm, pet, pet_lvl,bal_buff); // 0, 0
        JsonObject nodes = miningCore.getAsJsonObject("nodes");

        if(nodes.has("professional")) {
            int pro_lvl = nodes.get("professional").getAsInt();
            ms += pro_lvl > 0 ? 50 + (pro_lvl*5) : 0;
        }
        if(nodes.has("mining_speed_boost")) {
            ms_boost = nodes.get("mining_speed_boost").getAsInt();
        }
        if(nodes.has("special_0")) {
            potm = nodes.get("special_0").getAsInt();
        }
        if(pets_data.has("pets")) {
            JsonArray pets = pets_data.getAsJsonArray("pets");
            for(JsonElement pElement : pets) {
                JsonObject pObj = pElement.getAsJsonObject();
                if(pObj.get("active").getAsBoolean()) {
                    pet = pObj;
                }
            }
            if(pet != null && pet.get("type").getAsString().equals("BAL") && pet.get("tier").getAsString().equals("LEGENDARY")) {
                pet_lvl = HyUtils.findClosestLevel((int) pet.get("exp").getAsFloat(), false);
                bal_buff = BigDecimal.valueOf(((double) pet_lvl / 100) * 0.15).setScale(3, RoundingMode.HALF_UP).doubleValue();
            }
        }

        ApiData msPoint = new ApiData(ms, ms_boost, potm, pet, pet_lvl, bal_buff);
        devInfoMessage(msPoint.toString());
        return msPoint;
    }

    static Runnable updateStats = new Runnable() {
        @Override
        public void run() {
            if(mc.thePlayer == null) return;
            getMiningCore();
        }
    };
    public static void startRunning() {
        scheduler.scheduleAtFixedRate(updateStats, 1, (long) ModConfig.apiInterval, TimeUnit.MINUTES);
    }
}
