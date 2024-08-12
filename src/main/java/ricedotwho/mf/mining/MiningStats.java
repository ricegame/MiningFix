package ricedotwho.mf.mining;


import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.data.ApiData;
import ricedotwho.mf.utils.HyApi;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ricedotwho.mf.mf.devInfoMessage;

public class MiningStats {
    private static JsonObject miningCore = null;
    static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    static Minecraft mc;
    static {
        mc = Minecraft.getMinecraft();
    }

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
            if(ModConfig.devInfo) { System.out.println("miningCore: " + miningCore); }
        });
    }
    public static ApiData getApiData() { // returning ms, ms ability level
        int ms = 0;
        int ms_boost = 0;
        int potm = 0;
        if(miningCore == null) return new ApiData(ms, ms_boost, potm); // 0, 0
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
        ApiData msPoint = new ApiData(ms, ms_boost, potm);
        devInfoMessage(msPoint.toString());
        return new ApiData(ms, ms_boost, potm);
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
