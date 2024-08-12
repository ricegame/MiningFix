package ricedotwho.mf.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;
import net.minecraft.util.EnumChatFormatting;
import ricedotwho.mf.Ticker;

public class TpsHud extends SingleTextHud {
    public TpsHud() {
        super("", false);
        scale = 2;
        background = false;
        positionAlignment = 2;
        enabled = false;
    }
    @Override
    public String getText(boolean example) {
        if(example) return ("tps: " + EnumChatFormatting.GREEN + "20.0");
        EnumChatFormatting color = EnumChatFormatting.GREEN;
        if(Ticker.tps < 18) { color = EnumChatFormatting.YELLOW; }
        if(Ticker.tps < 15) { color = EnumChatFormatting.RED; }
        return ("tps: " + color + Ticker.tps);
    }
}
