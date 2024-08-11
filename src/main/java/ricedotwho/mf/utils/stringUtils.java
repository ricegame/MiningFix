package ricedotwho.mf.utils;

import net.minecraft.util.EnumChatFormatting;

public class stringUtils {
    public static String stripHypixelCodes(String message) {
        while(message.startsWith(String.valueOf(EnumChatFormatting.RESET))) {
            message = message.substring(2);
        }
        while(message.endsWith(String.valueOf(EnumChatFormatting.RESET))) {
            message = message.substring(0, message.length() - 2);
        }
        return message;
    }
    public static String lastChars(String string, int num) {
        return string.substring(0,string.length()-num);
    }
}
