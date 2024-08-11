package ricedotwho.mf.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.EnumChatFormatting;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.mf;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class hyApi {
    static String API_KEY_CONST = ""; // It was expired anyway
    public static String API_KEY = "";
    static String MOJANG_UUID = "https://api.mojang.com/users/profiles/minecraft/";
    static String STATUS_URL = "https://api.hypixel.net/v2/status";
    static String DUNGEON_DATA = "https://api.icarusphantom.dev/v1/sbecommands/cata/%s/selected";
    static String ST_URL = "https://hypixel.skytils.gg/v2/skyblock";
    static String ST_AGENT = "Skytils/1.9.8";
    public static void changeApiKey() {
        if(Objects.equals(ModConfig.customApiKey, "")) {
            API_KEY = API_KEY_CONST;
        } else {
            API_KEY = ModConfig.customApiKey;
        }
        mf.devInfoMessage("Api key updated: " + API_KEY);
    }

    public boolean getStatus(String name) throws IOException {
        hyApi idObj = new hyApi();
        String uuid = idObj.getUUID(name);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("uuid", uuid);

        String params = ParameterStringBuilder.getParamsString(parameters);

        URL url = new URL(String.format(STATUS_URL) + params); // https://api.hypixel.net/v2/status/
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");

        // Header
        conn.addRequestProperty("API-Key", API_KEY);

        conn.setConnectTimeout(5000);
        conn.setReadTimeout(8000);

        conn.addRequestProperty("User-Agent", "Forge Mod");


        int codeResponse = conn.getResponseCode();

        if (codeResponse != HttpURLConnection.HTTP_OK) {
            mf.sendMessageWithPrefix(EnumChatFormatting.RED + "Get request for status failed! (" + readInput(conn.getErrorStream()) + ". URL: " + url);
            return false;
        }

        StringBuilder response = readInput(conn);
        conn.disconnect();

        JsonObject obj = toJson(response);

        if(!obj.get("success").getAsBoolean()) {
            mf.sendMessageWithPrefix(EnumChatFormatting.RED + "Connection to Hypixel API failed! (" + obj.get("cause") + ". URL: " + String.format(STATUS_URL, uuid));
            return false;
        }
        return obj.get("session").getAsJsonObject().get("online").getAsBoolean();
    }
    public static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException{
            StringBuilder result = new StringBuilder("?");

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }

    private JsonObject toJson(StringBuilder jsonStr) {
        Gson g = new Gson();
        return g.fromJson(jsonStr.toString(), JsonObject.class);
    }

    private StringBuilder readInput(HttpURLConnection connection) throws IOException {
        String read = null;
        InputStreamReader isrObj = new InputStreamReader(connection.getInputStream());
        BufferedReader bf = new BufferedReader(isrObj);
        // to store the response from the servers
        StringBuilder responseStr = new StringBuilder();
        while ((read = bf .readLine()) != null) {
            responseStr.append(read);
        }
        bf.close();
        return responseStr;
    }
    private StringBuilder readInput(InputStream connection) throws IOException {
        String read = null;
        InputStreamReader isrObj = new InputStreamReader(connection);
        BufferedReader bf = new BufferedReader(isrObj);
        // to store the response from the servers
        StringBuilder responseStr = new StringBuilder();
        while ((read = bf .readLine()) != null) {
            responseStr.append(read);
        }
        bf.close();
        return responseStr;
    }
    public String getUUID(String name) throws IOException {
        URL urlForGetReq = new URL(MOJANG_UUID + name);
        HttpURLConnection connection = (HttpURLConnection) urlForGetReq.openConnection();
        connection.setRequestMethod("GET");

        int codeResponse = connection.getResponseCode();

        if (codeResponse != HttpURLConnection.HTTP_OK) {
            mf.sendMessageWithPrefix(EnumChatFormatting.RED + "Get request for UUID failed!");
            return null;
        }


        StringBuilder responseStr = readInput(connection);

        connection.disconnect();

        Gson g = new Gson();
        JsonObject jsonObject = g.fromJson(String.valueOf(responseStr), JsonObject.class);
        return jsonObject.get("id").toString().replace("\"","");
    }
    public JsonObject getDungeonDataSbe(String name) throws IOException {
        URL urlForGetReq = new URL(String.format(DUNGEON_DATA,name));
        HttpURLConnection connection = (HttpURLConnection) urlForGetReq.openConnection();
        connection.setRequestMethod("GET");

        connection.addRequestProperty("User-Agent", "Mozilla/5.0");

        int codeResponse = connection.getResponseCode();

        if (codeResponse != HttpURLConnection.HTTP_OK) {
            mf.sendMessageWithPrefix(EnumChatFormatting.RED + "Get request for dungeon data failed!");
            return null;
        }
        StringBuilder responseStr = readInput(connection);
        connection.disconnect();

        if(ModConfig.devInfo) System.out.println(responseStr);

        return toJson(responseStr);
    }
    public JsonObject getDungeonDataSt(String name) throws IOException {
        String uuid = getUUID(name);
        if (uuid == null) {
            mf.sendMessageWithPrefix(EnumChatFormatting.RED + "Get request for dungeon data failed! Does the player exist?");
            return null;
        }
        JsonObject selectedProfile = getSelectedProfile(uuid);
        if (selectedProfile == null) {
            System.out.println("Selected profile not found.");
            return null;
        }

        String uuidStr = uuid.replace("-", "");
        JsonObject members = selectedProfile.getAsJsonObject("members").getAsJsonObject(uuidStr);
        return members.getAsJsonObject("dungeons");
    }

    public static JsonObject getSelectedProfile(String uuid) throws IOException {
        URL url = new URL(ST_URL + "/profiles?uuid=" + uuid);
        HttpURLConnection conn = null;
        Scanner scanner = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", ST_AGENT);

            int codeResponse = conn.getResponseCode();
            if (codeResponse != HttpURLConnection.HTTP_OK) {
                mf.sendMessageWithPrefix(EnumChatFormatting.RED + "Get request for dungeon data failed!");
                return null;
            }

            scanner = new Scanner(conn.getInputStream());
            String responseJson = scanner.useDelimiter("\\A").next();

            JsonObject data = new JsonParser().parse(responseJson).getAsJsonObject();
            for (JsonElement profileElement : data.getAsJsonArray("profiles")) {
                JsonObject profile = profileElement.getAsJsonObject();
                if (profile.get("selected").getAsBoolean()) {
                    //if(ModConfig.devInfo) {devInfoMessage("Found Selected Profile! " + profile);}
                    return profile;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
