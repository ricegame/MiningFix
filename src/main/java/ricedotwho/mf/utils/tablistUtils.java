package ricedotwho.mf.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class tablistUtils {

    public static List<String> readTabList() {
       EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
       List<NetworkPlayerInfo> players = playerOrdering.sortedCopy(player.sendQueue.getPlayerInfoMap());
       List<String> result = new ArrayList<>();
       for(NetworkPlayerInfo info : players) {
           String name = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(info);
            result.add(stringUtils.stripHypixelCodes(name));
       }
        if (!result.isEmpty()) {
            return result.subList(0, result.size() - 1);
        }
        return result;
    }
    private static final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from(new PlayerComparator());
    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo> {

        @Override
        public int compare(NetworkPlayerInfo o1, NetworkPlayerInfo o2) {
            String team1 = (o1.getPlayerTeam() != null) ? o1.getPlayerTeam().getRegisteredName() : "";
            String team2 = (o2.getPlayerTeam() != null) ? o2.getPlayerTeam().getRegisteredName() : "";

            return ComparisonChain.start()
                    .compareTrueFirst(
                            o1.getGameType() != WorldSettings.GameType.SPECTATOR,
                            o2.getGameType() != WorldSettings.GameType.SPECTATOR
                    )
                    .compare(team1, team2)
                    .compare(o1.getGameProfile().getName(), o2.getGameProfile().getName())
                    .result();
        }
    }
}
