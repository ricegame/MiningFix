package ricedotwho.mf.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.handlers.ScoreboardHandler;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Utils {
	public static boolean inSkyblock = false;
	public static boolean inMines = false;
	public static boolean inHollows = false;
	static Minecraft mc;
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static final long TICK_DURATION_MS = 50; // 1000 ms / 20 ticks
	static {
		mc = Minecraft.getMinecraft();
	}

	public static void checkLocation() {
		checkInSkyblock();

		if(ModConfig.forceNotSkyblock){
			inSkyblock = false;
			inMines = false;
			inHollows = false;
			return;
		}
		if(ModConfig.devOverride) {
			inMines = true;
			inHollows = true;
			return;
		}
		if(!inSkyblock) return;
		checkInHollows();
		checkInMines();
	}
	public static void checkInMines() {
		List<String> scoreboard = ScoreboardHandler.getSidebarLines();
		for(String s : scoreboard) {
			String sCleaned = ScoreboardHandler.cleanSB(s);
			if(Utils.strContainsOneOf(sCleaned, "Dwarven Mines", "Cliffside Veins", "Far Reserve", "Lava Springs", "Rampart's Quarry", "Royal Mines", "Upper Mines", "Dwarven Base Camp", "Glacite Tunnels", "Glacite Mineshafts")){
				inMines = true;
				return;
			}
		}
		inMines = false;
	}
	public static void checkInHollows() {
		List<String> scoreboard = ScoreboardHandler.getSidebarLines();
		for(String s : scoreboard) {
			String sCleaned = ScoreboardHandler.cleanSB(s);
			if(Utils.strContainsOneOf(sCleaned, "Crystal Nucleus", "Dragon's Lair", "Fairy Grotto", "Goblin Holdout", "Goblin Queen's Den", "Jungle Temple", "Jungle", "Khazad-dû\uD83D\uDC7Em", "Lost Precursor City", "Magma Fields", "Mines of Divan", "Mithril Deposits", "Precursor Remnants")){
				inHollows = true;
				return;
			}
		}
		inHollows = false;
	}
	
	public static void checkInSkyblock() {
		if(ModConfig.devOverride) {
			inSkyblock = true;
			return;
		}
		if(mc != null && mc.theWorld != null && !mc.isSingleplayer()) {
			ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
			if(scoreboardObj != null) {
				String scoreObjName = ScoreboardHandler.cleanSB(scoreboardObj.getDisplayName());
				if(scoreObjName.contains("SKYBLOCK")) {
					inSkyblock = true;
					return;
				}
			}
		}
		inSkyblock = false;
	}

	public static int calculateDistFast(BlockPos pos1, BlockPos pos2) {
		return calculateDistFast(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
	}
	public static int calculateDistFast(int x1, int y1, int z1, int x2, int y2, int z2) {
		int x = x2 - x1;
		int y = y2 - y1;
		int z = z2 - z1;

		int ret = x*x + y*y + z*z;

		if(ret < 0) ret *= -1;
		return ret;

	}

	public static BlockPos lookingAt() {
		EntityPlayerSP player = mc.thePlayer;
		MovingObjectPosition look = mc.objectMouseOver;
		BlockPos pos = null;
		if(look != null && look.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			pos = look.getBlockPos();
		}
		return pos;
	}
	public static boolean equalsOneOf(Object object, Object... others) {
		for (Object obj : others) {
			if (Objects.equals(object, obj)) {
				return true;
			}
		}
		return false;
	}
	public static boolean strContainsOneOf(String string, String... others) {
		for (String obj : others) {
			if(string.contains(obj)) {
				return true;
			}
		}
		return false;
	}
	public static void executeWithDelay(Runnable task, long delay, TimeUnit timeUnit) {
		scheduler.schedule(task, delay, timeUnit);
	}
	public static void executeWithTicks(Runnable task, int ticks) {
		long delayMillis = ticks * TICK_DURATION_MS;
		scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
	}
	public static boolean isWithinRadius(BlockPos pos1, BlockPos pos2, int radius) {
		double distanceSq = pos2.distanceSq(pos1);
		double radiusSq = radius * radius;

		return distanceSq <= radiusSq;
	}
}
