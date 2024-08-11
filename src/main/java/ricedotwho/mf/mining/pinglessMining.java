package ricedotwho.mf.mining;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.events.OnTimeEvent;
import ricedotwho.mf.events.packetEvent;
import ricedotwho.mf.hud.titleClass;
import ricedotwho.mf.utils.Utils;
import ricedotwho.mf.utils.tablistUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ricedotwho.mf.mf.devInfoMessage;

public class pinglessMining {
    static Minecraft mc;
    static String abilityActivate = "^You used your (.*) Pickaxe Ability!";
    static String abilityExpire = "^Your (.*) has expired!";
    static String abilityReady = "^(.*) is now available!";
    static String miningSpeed = "^ Mining Speed: \u2E15(\\d+)"; // this little space is very annoying
    static Pattern activatePattern;
    static Pattern expirePattern;
    static Pattern readyPattern;
    static Pattern msPattern;
    static {
        mc = Minecraft.getMinecraft();
        activatePattern = Pattern.compile(abilityActivate);
        expirePattern = Pattern.compile(abilityExpire);
        readyPattern = Pattern.compile(abilityReady);
        msPattern = Pattern.compile(miningSpeed);
    }
    public static BlockPos currentBlock = null;
    private static BlockPos lastBlock = null;
    private static BlockPos lastBlockPlayerPos = null;
    public static int currentProgress = 0;
    private Integer tablistMiningSpeed = 0;
    private Integer currentTicks = 0;
    private Integer ticksNeeded = 0;
    private boolean miningSpeedBoost = false;
    @SubscribeEvent
    public void onServerTick(packetEvent.ReceiveEvent event) {
        if(event.packet instanceof S32PacketConfirmTransaction) {
            if(!ModConfig.pinglessMining && !(Utils.inMines || Utils.inHollows)) return;
            BlockPos oldBlock = currentBlock;
            currentBlock = Utils.lookingAt();
            if(!Objects.equals(currentBlock, oldBlock)) blockChanged();

            if(!Mouse.isButtonDown(0)) {
                currentTicks = 0;
            }
            if(mc.currentScreen != null) {
                currentTicks = 0;
                return;
            }

            if(!should()) return;
            calcTicksNeeded();

            if(currentTicks >= ticksNeeded) {
                ticksNeeded = 0;
                lastBlock = currentBlock;
                lastBlockPlayerPos = new BlockPos(Math.floor(mc.thePlayer.posX), Math.floor(mc.thePlayer.posY), Math.floor(mc.thePlayer.posZ));

                if(Utils.inHollows) {
                    mc.theWorld.setBlockToAir(currentBlock);
                } else {
                    mc.theWorld.setBlockState(currentBlock, Blocks.bedrock.getDefaultState());
                }
                if(ModConfig.fixBreakingProgress) { mc.theWorld.playSoundAtPos(currentBlock.up(1), "dig.glass", 1, 0.7936508059501648f, false); }
            }

            currentTicks++;
        }
        else if (event.packet instanceof S02PacketChat) {
            String message = EnumChatFormatting.getTextWithoutFormattingCodes(((S02PacketChat) event.packet).getChatComponent().getUnformattedText());
            Matcher activate = activatePattern.matcher(message);
            Matcher deactivate = expirePattern.matcher(message);
            Matcher ready = readyPattern.matcher(message);
            if(activate.find()) { if(Objects.equals(activate.group(1), "Mining Speed Boost")){ miningSpeedBoost = true; } }
            if(deactivate.find()) { if(Objects.equals(deactivate.group(1), "Mining Speed Boost")){ miningSpeedBoost = false; } }
            if(ready.find() && ModConfig.miningAbilityAlert) {
                titleClass.createTitle(EnumChatFormatting.GOLD + ready.group(1), 3000);
                mc.thePlayer.playSound("note.pling",1f,1f);
            }
        }
    }
    private void blockChanged() {
        currentTicks = 0;
        if(!should()) return;
        calcTicksNeeded();
    }
    private void calcTicksNeeded() {
        IBlockState block = mc.theWorld.getBlockState(currentBlock);
        int blockId = Block.getIdFromBlock(block.getBlock());
        int blockMeta = block.getBlock().getMetaFromState(block);
        int hardness = miningData.MINING_HARDNESS.get((blockId == 160 ? 95 : blockId) + ":" + blockMeta); // panes are a pain
        int raw_ticks = getRawTicks(hardness);

        ticksNeeded = (raw_ticks <= 4 && raw_ticks > 1 ? 4 : raw_ticks) + ModConfig.extraTicks;
    }
    private int getRawTicks(int hardness) {
        int ms = (ModConfig.tablistMiningSpeed ? (tablistMiningSpeed > 0 ? tablistMiningSpeed : ModConfig.miningSpeed) : ModConfig.miningSpeed) + (ModConfig.profLevel > 0 ? 50 + (ModConfig.profLevel * 5) : 0);
        int halfBakedSpeed = miningSpeedBoost ? (ms * (ModConfig.miningSpeedBoost)) : ms;
        int miningSpeed = (mc.thePlayer.onGround || !mc.thePlayer.isInWater()) ? halfBakedSpeed : halfBakedSpeed / 5;
        return (int) Math.floor((float) (30 * hardness) / miningSpeed);
    }

    public static boolean should() {
        if(currentBlock == null) return false;
        ItemStack heldItem = mc.thePlayer.getHeldItem();
        if(heldItem == null) return false;
        if(!miningData.MINING_ITEMS.contains(heldItem.getItem())) return false;
        IBlockState block = mc.theWorld.getBlockState(currentBlock);
        if(!Utils.equalsOneOf(block.getBlock(), Blocks.stained_glass, Blocks.stained_glass_pane)) {return false;}
        int blockId = Block.getIdFromBlock(block.getBlock());
        int blockMeta = block.getBlock().getMetaFromState(block);
        return miningData.MINING_HARDNESS.containsKey((blockId == 160 ? 95 : blockId) + ":" + blockMeta) || (ModConfig.allowHardstone && blockId == 1 && Utils.inHollows);
    }
    private static boolean isGemstone() {
        if (currentBlock == null) return false;
        IBlockState block = mc.theWorld.getBlockState(currentBlock);
        return Utils.equalsOneOf(block.getBlock(), Blocks.stained_glass, Blocks.stained_glass_pane);
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(!ModConfig.fixBreakingProgress || !ModConfig.pinglessMining || !Utils.inHollows) return;
        if(currentTicks != 0 && ticksNeeded != 0) {
            currentProgress = Math.round(((float) currentTicks / ticksNeeded) * 9);
        } else { currentProgress = 9; }
    }
    @SubscribeEvent
    public void onSecond(OnTimeEvent.Second event) {
        if(!ModConfig.pinglessMining || !ModConfig.tablistMiningSpeed || miningSpeedBoost) return;
        if(mc.theWorld == null) return;
        List<String> tabLines = tablistUtils.readTabList();
        for(String line : tabLines) {
            String cleaned = EnumChatFormatting.getTextWithoutFormattingCodes(line);
            Matcher matcher = msPattern.matcher(cleaned);
            if(matcher.find()) {
                tablistMiningSpeed = Integer.parseInt(matcher.group(1));
                devInfoMessage("Tablist speed: " + tablistMiningSpeed);
            }
        }
    }
    @SubscribeEvent
    public void onSound(PlaySoundEvent event) {
        if(!ModConfig.pinglessSound && !ModConfig.fixBreakingProgress || !ModConfig.pinglessMining || !Utils.inHollows) return;
        if(Objects.equals(event.name, "dig.glass") && lastBlock != null && lastBlockPlayerPos != null) {
            float sX = event.sound.getXPosF(); float sY = event.sound.getYPosF(); float sZ = event.sound.getZPosF();
            BlockPos soundPos = new BlockPos(event.sound.getXPosF(), event.sound.getYPosF(), event.sound.getZPosF());
            devInfoMessage("Player: " + lastBlockPlayerPos.toString() + " Sound: " + soundPos);
            if(!Objects.equals(soundPos, lastBlock.up(1))) {
                event.result = null;
            }
        }
    }
}
