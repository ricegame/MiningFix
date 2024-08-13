package ricedotwho.mf.mining;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.data.ApiData;
import ricedotwho.mf.events.BlockChangedEvent;
import ricedotwho.mf.events.OnTimeEvent;
import ricedotwho.mf.events.PacketEvent;
import ricedotwho.mf.hud.TitleClass;
import ricedotwho.mf.utils.TablistUtils;
import ricedotwho.mf.utils.Utils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ricedotwho.mf.mf.devInfoMessage;
import static ricedotwho.mf.mf.sendMessageWithPrefix;

public class PinglessMining {
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
    public static int currentProgress = 0;
    private Integer tablistMiningSpeed = 0;
    private Integer currentTicks = 0;
    private Integer ticksNeeded = 0;
    private Integer ticksThisSecond = 0;
    private boolean miningSpeedBoost = false;
    boolean playingSound = false;
    private boolean dontUpdateTablist = false;
    // todo: fix -> For some reason we are missing ticks, like blocks take 3-5 ticks longer than they should to break!
    // good enough for a patch now tho

    @SubscribeEvent
    public void onServerTick(PacketEvent.ReceiveEvent event) {
        if(event.packet instanceof S32PacketConfirmTransaction) {
            if(!ModConfig.pinglessMining || !(Utils.inMines || Utils.inHollows)) return;
            ticksThisSecond++;

            BlockPos oldBlock = currentBlock;
            currentBlock = Utils.lookingAt();
            if(!Objects.equals(currentBlock, oldBlock)) blockChanged();

            if(!should()) return;
            calcTicksNeeded();
            if(!Mouse.isButtonDown(0) || mc.currentScreen != null) {
                currentTicks = 0;
                return;
            }

            if(currentTicks >= ticksNeeded) {
                currentTicks = 0;

                if(Utils.inHollows) {
                    mc.theWorld.setBlockToAir(currentBlock);
                } else {
                    mc.theWorld.setBlockState(currentBlock, Blocks.bedrock.getStateFromMeta(0));
                }
            }
            if(ticksThisSecond >= 20) return;
            currentTicks++;
        }
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        Matcher activate = activatePattern.matcher(message);
        Matcher deactivate = expirePattern.matcher(message);
        Matcher ready = readyPattern.matcher(message);
        if(activate.find()) { if(Objects.equals(activate.group(1), "Mining Speed Boost")){ miningSpeedBoost = true; dontUpdateTablist = true; }}
        if(deactivate.find()) { if(Objects.equals(deactivate.group(1), "Mining Speed Boost")){
            miningSpeedBoost = false;
            Utils.executeWithDelay(() -> dontUpdateTablist = false, 3, TimeUnit.SECONDS);
        }}
        if(ready.find() && ModConfig.miningAbilityAlert) {
            TitleClass.createTitle(EnumChatFormatting.GOLD + ready.group(1), 3000);
            mc.thePlayer.playSound("note.pling",1f,1f);
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
        int hardness = MiningData.MINING_HARDNESS.get((blockId == 160 ? 95 : blockId) + ":" + blockMeta); // panes are a pain
        int raw_ticks = getRawTicks(hardness);

        ticksNeeded = Math.max((raw_ticks <= 4 && raw_ticks > 1 ? (4 + ModConfig.extraTicks) : raw_ticks), 2); // Prevent 0 and 1 ticks, 1 ticks make ghostblocks and is inconsistent
    }
    //todo: bluew cheese support
    private int getRawTicks(int hardness) {
        int ms = getMiningSpeed();
        int ticks = Math.round((float) (30 * hardness) / ms) + ModConfig.extraTicks;
        devInfoMessage("Mining Speed: " + ms + " Ticks: " + ticks);
        return ticks;
    }
    private int getMiningSpeed() {
        ApiData apiData = MiningStats.getApiData();
        int ms = tablistMiningSpeed;
        int miningSpeed = miningSpeedBoost ? (ms * (2 + apiData.msBoost + (apiData.potm > 0 ? 1 : 0))) : ms;
        miningSpeed += (isGemstone() ? apiData.professional : 0);

        /* ok maybe im just really stupid
        boolean bal = apiData.pet != null && apiData.pet.get("type").getAsString().equals("BAL");
        boolean mf = mc.thePlayer.posY <= 61;
        if(bal && miningSpeedBoost && mf) {
            ms = (int) Math.floor(ms / (1 +apiData.bal_buff));
        }
        ms += isGemstone() ? apiData.professional : 0;
        int halfBakedSpeed = miningSpeedBoost ? (ms * (1 + apiData.msBoost + (apiData.potm > 0 ? 1 : 0))) : ms;
        int miningSpeed = bal && miningSpeedBoost && mf ? (int) Math.floor(halfBakedSpeed * (1 + apiData.bal_buff)) : halfBakedSpeed;
        */

        int finalSpeed = miningSpeed;
        if(!mc.thePlayer.onGround) { finalSpeed = miningSpeed / 5; }
        if(mc.thePlayer.isInWater()) { finalSpeed = miningSpeed / 5; }

        return finalSpeed;
    }

    public static boolean should() {
        if(currentBlock == null) return false;
        ItemStack heldItem = mc.thePlayer.getHeldItem();
        if(heldItem == null) return false;
        if(!MiningData.MINING_ITEMS.contains(heldItem.getItem())) return false;
        IBlockState block = mc.theWorld.getBlockState(currentBlock);
        int blockId = Block.getIdFromBlock(block.getBlock());
        int blockMeta = block.getBlock().getMetaFromState(block);
        if(!ModConfig.allowHardstone && blockId == 1) return false;
        return MiningData.MINING_HARDNESS.containsKey((blockId == 160 ? 95 : blockId) + ":" + blockMeta);
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
            currentProgress = (int) Math.floor(((float) currentTicks / ticksNeeded) * 9);
        } else {currentProgress = 0; }
        devInfoMessage("currentTicks: " + currentTicks + " ticksNeeded: " + ticksNeeded + " currentProgress: " + currentProgress);
    }
    @SubscribeEvent
    public void onSecond(OnTimeEvent.Second event) {
        if(!ModConfig.pinglessMining || !(Utils.inMines || Utils.inHollows)) return;
        ticksThisSecond = 0;
        if(dontUpdateTablist) return;

        if(mc.theWorld == null) return;
        List<String> tabLines = TablistUtils.readTabList();
        for(String line : tabLines) {
            String cleaned = EnumChatFormatting.getTextWithoutFormattingCodes(line);
            Matcher matcher = msPattern.matcher(cleaned);
            if(matcher.find()) {
                tablistMiningSpeed = Integer.parseInt(matcher.group(1));
                //devInfoMessage("Tablist speed: " + tablistMiningSpeed);
            }
        }
        devInfoMessage("Tablist speed: " + tablistMiningSpeed);
        if(tablistMiningSpeed == 0) { sendMessageWithPrefix(EnumChatFormatting.RED + "Enable mining speed widget or MF wont work! (/widget -> Stats Widget -> Shown Stats -> Mining Speed)"); }
    }
    //todo: figure out how to make this work with the new sound playing code
    // Current method is scuffed, but if it works, it works
    @SubscribeEvent
    public void onSound(PlaySoundEvent event) {
        if(!ModConfig.pinglessSound || !ModConfig.pinglessMining || !ModConfig.fixBreakingProgress) return;
        if(!(Utils.inMines || Utils.inHollows)) return;

        if(playingSound) return;

        if(Objects.equals(event.name, "dig.glass")/* || Objects.equals(event.name, "random.orb") */) {
            event.result = null;
        }
    }
    @SubscribeEvent
    public void onBlockChange(BlockChangedEvent event) {
        if(!ModConfig.pinglessSound || !ModConfig.pinglessMining || !ModConfig.fixBreakingProgress) return;
        if(!(Utils.inMines || Utils.inHollows)) return;

        if(!Utils.isWithinRadius(event.getPos(), mc.thePlayer.getPosition(), 16)) return;
        if(Utils.equalsOneOf(event.getOldState().getBlock(), Blocks.stained_glass,Blocks.stained_glass_pane) && event.getNewState().getBlock().equals(Blocks.air)) {
            playingSound = true;
            mc.theWorld.playSoundAtPos(currentBlock, "dig.glass", 1f, 0f, false);
            mc.theWorld.playSoundAtPos(currentBlock, "dig.glass", 1f, 0.7936508059501648f, false);
            //mc.theWorld.playSoundAtPos(currentBlock, "random.orb", 0.5f, (1.403f + (rand.nextFloat() * 1.264f)), false);
            playingSound = false;
        }
    }
}
