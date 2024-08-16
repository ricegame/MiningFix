package ricedotwho.mf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.events.OnTimeEvent;
import ricedotwho.mf.handlers.SoundHandler;
import ricedotwho.mf.mining.MiningStats;
import ricedotwho.mf.mining.PinglessMining;
import ricedotwho.mf.utils.RiceChatComponent;
import ricedotwho.mf.utils.Utils;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)

public class mf {
	public static RiceChatComponent PREFIX = new RiceChatComponent(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.DARK_GREEN + "MF" + EnumChatFormatting.DARK_GRAY + "] " + EnumChatFormatting.RESET);

	public static ModConfig config;
	public static final Minecraft mc;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Oneconfig
		config = new ModConfig();
		ClientCommandHandler.instance.registerCommand(new mfCommands());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new PinglessMining());
		MinecraftForge.EVENT_BUS.register(new Ticker());
		MinecraftForge.EVENT_BUS.register(new SoundHandler());
		MinecraftForge.EVENT_BUS.register(new Onboarding());
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Ticker.startRunning();
		MiningStats.startRunning();
	}
	@SubscribeEvent
	public void onSecond(OnTimeEvent.Second event) {
		Utils.checkLocation();
	}
    public static void sendMessage(final String message) {
		if(mc.thePlayer == null) return;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
	public static void sendMessage(final RiceChatComponent message) {
		if(mc.thePlayer == null) return;
		Minecraft.getMinecraft().thePlayer.addChatMessage(message);
	}
    public static void sendMessageWithPrefix(final String message) {
		if(mc.thePlayer == null) return;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX.getUnformattedText() + message));
    }
    public static void sendMessageUnknown(final String args) {
		if(mc.thePlayer == null) return;
    	mc.thePlayer.addChatMessage(new RiceChatComponent(PREFIX.getUnformattedTextForChat() + EnumChatFormatting.RED + "Unknown Setting (Args: " + args + ")"));
    }
	public static void devMessage(final String message) {
		if(!ModConfig.devOverride) return;
		sendMessage(message);
	}
	public static void devInfoMessage(final String message) {
		if(!ModConfig.devInfo) return;
		sendMessage(message);
	}
	public static void soundsInfoMessage(final String message) {
		if(!ModConfig.soundsInfo) return;
		sendMessage(message);
	}
    static {
    	mc = Minecraft.getMinecraft();
    }
	public static ISound empty = new PositionedSound(new ResourceLocation("mfutils", "empty")) {{
		volume = 1f;
		pitch = 1f;
		repeat = false;
		repeatDelay = 0;
		attenuationType = AttenuationType.NONE;
	}};
}
