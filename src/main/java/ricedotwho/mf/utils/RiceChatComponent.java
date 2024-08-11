package ricedotwho.mf.utils;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ricedotwho.mf.mf;

import java.io.File;

@SuppressWarnings("unused")
public class RiceChatComponent extends ChatComponentText {
    String msg;
    public RiceChatComponent(String msg) {
        super(msg);
        this.msg = msg;
    }
    public RiceChatComponent prefix() {
        this.msg = mf.PREFIX + this.msg;
        return this;
    }
    public RiceChatComponent black() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.BLACK));
        return this;
    }

    public RiceChatComponent darkBlue() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.DARK_BLUE));
        return this;
    }

    public RiceChatComponent darkGreen() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.DARK_GREEN));
        return this;
    }

    public RiceChatComponent darkAqua() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.DARK_AQUA));
        return this;
    }

    public RiceChatComponent darkRed() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.DARK_RED));
        return this;
    }

    public RiceChatComponent darkPurple() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE));
        return this;
    }

    public RiceChatComponent gold() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.GOLD));
        return this;
    }

    public RiceChatComponent gray() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.GRAY));
        return this;
    }

    public RiceChatComponent darkGray() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.DARK_GRAY));
        return this;
    }

    public RiceChatComponent blue() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.BLUE));
        return this;
    }

    public RiceChatComponent green() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.GREEN));
        return this;
    }

    public RiceChatComponent aqua() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.AQUA));
        return this;
    }

    public RiceChatComponent red() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.RED));
        return this;
    }

    public RiceChatComponent lightPurple() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE));
        return this;
    }

    public RiceChatComponent yellow() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.YELLOW));
        return this;
    }

    public RiceChatComponent white() {
        setChatStyle(getChatStyle().setColor(EnumChatFormatting.WHITE));
        return this;
    }

    public RiceChatComponent obfuscated() {
        setChatStyle(getChatStyle().setObfuscated(true));
        return this;
    }

    public RiceChatComponent bold() {
        setChatStyle(getChatStyle().setBold(true));
        return this;
    }

    public RiceChatComponent strikethrough() {
        setChatStyle(getChatStyle().setStrikethrough(true));
        return this;
    }

    public RiceChatComponent underline() {
        setChatStyle(getChatStyle().setUnderlined(true));
        return this;
    }

    public RiceChatComponent italic() {
        setChatStyle(getChatStyle().setItalic(true));
        return this;
    }

    public RiceChatComponent reset() {
        setChatStyle(getChatStyle().setParentStyle(null).setBold(false).setItalic(false).setObfuscated(false).setUnderlined(false).setStrikethrough(false));
        return this;
    }

    public RiceChatComponent setHover(IChatComponent hover) {
        setChatStyle(getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)));
        return this;
    }

    public RiceChatComponent setUrl(String url) {
        return setUrl(url, new KeyValueTooltipComponent("Click to visit", url));
    }

    public RiceChatComponent setUrl(String url, String hover) {
        return setUrl(url, new RiceChatComponent(hover).yellow());
    }

    public RiceChatComponent setUrl(String url, IChatComponent hover) {
        setChatStyle(getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
        setHover(hover);
        return this;
    }

    public RiceChatComponent setOpenFile(File filePath) {
        setChatStyle(getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, filePath.getAbsolutePath())));
        setHover(new RiceChatComponent(filePath.isFile() ? "Open " + filePath.getName() : "Open folder: " + filePath).yellow());
        return this;
    }

    public RiceChatComponent setSuggestCommand(String command) {
        setSuggestCommand(command, true);
        return this;
    }

    public RiceChatComponent setSuggestCommand(String command, boolean addTooltip) {
        setChatStyle(getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        if (addTooltip) {
            setHover(new KeyValueChatComponent("Run", command, " "));
        }
        return this;
    }

    @Override
    public RiceChatComponent appendSibling(IChatComponent component) {
        if (component != null) {
            super.appendSibling(component);
        }
        return this;
    }

    /**
     * Appends the given component in a new line, without inheriting formatting of previous siblings.
     *
     * @see ChatComponentText#appendSibling
     */
    public RiceChatComponent appendFreshSibling(IChatComponent sibling) {
        this.siblings.add(new ChatComponentText("\n").appendSibling(sibling));
        return this;
    }

    public static class KeyValueChatComponent extends RiceChatComponent {
        public KeyValueChatComponent(String key, String value) {
            this(key, value, ": ");
        }

        public KeyValueChatComponent(String key, String value, String separator) {
            super(key);
            appendText(separator);
            gold().appendSibling(new RiceChatComponent(value).yellow());
        }
    }

    public static class KeyValueTooltipComponent extends RiceChatComponent {
        public KeyValueTooltipComponent(String key, String value) {
            super(key);
            appendText(": ");
            gray().appendSibling(new RiceChatComponent(value).yellow());
        }
    }
}
