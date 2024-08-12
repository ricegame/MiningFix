package ricedotwho.mf.hud;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import net.minecraft.util.EnumChatFormatting;

public class TitleHud extends SingleTextHud {
    @Switch(
            name = "MF Title"
    )
    public boolean yes;

    public TitleHud() {
        super("", false);
        scale = 5;
        background = false;
        positionAlignment = 2;
        enabled = true;

    }
    @Override
    public String getText(boolean example) {
        if(example) return (EnumChatFormatting.RED + "MF Title");
        return TitleClass.titleText;
    }
}
