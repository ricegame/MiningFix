package ricedotwho.mf.hud;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import net.minecraft.util.EnumChatFormatting;

public class titleHud extends SingleTextHud {
    @Switch(
            name = "MF Title"
    )
    public boolean yes;

    public titleHud() {
        super("", false);
        scale = 5;
        background = false;
        positionAlignment = 2;
        enabled = true;

    }
    @Override
    public String getText(boolean example) {
        if(example) return (EnumChatFormatting.RED + "MF Title");
        return titleClass.titleText;
    }
}
