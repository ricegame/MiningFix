package ricedotwho.mf.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;
import ricedotwho.mf.mining.PinglessMining;
import ricedotwho.mf.utils.Utils;

public class DevHud extends SingleTextHud {
    public DevHud() {
        super("", false);
        scale = 1;
        background = false;
        positionAlignment = 2;
        enabled = false;
    }
    @Override
    public String getText(boolean example) {
        if(example) return ("MF Dev Hud");
        if(!(Utils.inMines || Utils.inHollows)) return "";
        return ("current: " + PinglessMining.currentTicks
                + ". needed: " + PinglessMining.ticksNeeded
                + ". progress: " + PinglessMining.currentProgress
                + ". timesave: " + PinglessMining.timeSaved + "ms"
                + ". pingdelay: " + PinglessMining.pingDelay + "ms"
        );
    }
}
