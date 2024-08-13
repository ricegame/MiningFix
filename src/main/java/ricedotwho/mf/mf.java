package ricedotwho.mf;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.events.OnTimeEvent;
import ricedotwho.mf.handlers.SoundHandler;
import ricedotwho.mf.handlers.PacketHandler;
import ricedotwho.mf.mining.MiningStats;
import ricedotwho.mf.mining.PinglessMining;
import ricedotwho.mf.utils.RiceChatComponent;
import ricedotwho.mf.utils.Utils;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)

public class mf {
	public static RiceChatComponent PREFIX = new RiceChatComponent(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.DARK_GREEN + "MF" + EnumChatFormatting.DARK_GRAY + "] " + EnumChatFormatting.RESET);

	public static ModConfig config;
	public static final Minecraft mc;
	private boolean inSkyblock = false;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Oneconfig
		config = new ModConfig();
		ClientCommandHandler.instance.registerCommand(new mfCommands());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new PinglessMining());
		MinecraftForge.EVENT_BUS.register(new Ticker());
		MinecraftForge.EVENT_BUS.register(new SoundHandler());
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Ticker.startRunning();
		MiningStats.startRunning();
	}
	@SubscribeEvent
	public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		/**
		 * Taken from DungeonsRoomsMod under the GNU Affero General Public License v3.0
		 * https://github.com/Quantizr/DungeonRoomsMod/blob/1f4c4e009684712d1b57b992fdbef68805deb2e6/LICENSE
		 * @author Quantizr
		 */
		if (mc.getCurrentServerData() == null) return;
		if (mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.") || mc.getCurrentServerData().serverIP.toLowerCase().contains("localhost")) {

			// No packets are modified or sent.
			event.manager.channel().pipeline().addBefore("packet_handler", "mf_packet_handler", new PacketHandler());
			System.out.println("[MF] Successfully added packet handler.");
		}
	}
	@SubscribeEvent
	public void onSecond(OnTimeEvent.Second event) {
		Utils.checkLocation();

		/*
		if(Utils.inSkyblock != inSkyblock) {
			if(Utils.inSkyblock) {
				SkyblockEvent.Joined.postAndCatch();
			} else {
				SkyblockEvent.Left.postAndCatch();
			}
		}
		 */
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
}
