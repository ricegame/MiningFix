package ricedotwho.mf.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;
import net.minecraft.util.EnumChatFormatting;
import ricedotwho.mf.ticker;

public class tpsHud extends SingleTextHud {
    public tpsHud() {
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
        if(ticker.tps < 18) { color = EnumChatFormatting.YELLOW; }
        if(ticker.tps < 15) { color = EnumChatFormatting.RED; }
        return ("tps: " + color + ticker.tps);
    }
}
