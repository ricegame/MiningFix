package ricedotwho.mf.utils;

public class hyUtils {
    // Data from Cowlection (tysm)
    private static final int PET_MAX_XP_LEG = 25353230;
    private static final int[] PET_XP_LEG = {
            0, 660, 1390, 2190, 3070, 4030, 5080, 6230, 7490, 8870,  // 1-10
            10380, 12030, 13830, 15790, 17920, 20230, 22730, 25430, 28350, 31510,  // 11-20
            34930, 38630, 42630, 46980, 51730, 56930, 62630, 68930, 75930, 83730,  // 21-30
            92430, 102130, 112930, 124930, 138230, 152930, 169130, 186930, 206430, 227730,  // 31-40
            250930, 276130, 303530, 333330, 365730, 400930, 439130, 480530, 525330, 573730,  // 41-50
            625930, 682130, 742530, 807330, 876730, 950930, 1030130, 1114830, 1205530, 1302730,  // 51-60
            1406930, 1518630, 1638330, 1766530, 1903730, 2050430, 2207130, 2374830, 2554530, 2747230,  // 61-70
            2953930, 3175630, 3413330, 3668030, 3940730, 4232430, 4544130, 4877830, 5235530, 5619230,  // 71-80
            6030930, 6472630, 6949330, 7466030, 8027730, 8639430, 9306130, 10032830, 10824530, 11686230,  // 81-90
            12622930, 13639630, 14741330, 15933030, 17219730, 18606430, 20103130, 21719830, 23466530, 25353230  // 91-100
    };
    public static int findClosestLevel(int target, boolean gdrag) {
        if (target >= PET_MAX_XP_LEG && gdrag) {
            int overflowExp = target - PET_MAX_XP_LEG;
            int overflowLvl = 1;
            if (overflowExp >= 1) {
                overflowLvl = 2;
                overflowExp = overflowExp - 1;
            }
            return 100 + overflowLvl + Math.min(98, (int) (overflowExp / 1886700.0));
        }

        // Find the index of the closest level
        int closestIndex = findClosestIndex(target);

        // Ensure it returns the current level if target is closer to the next level
        if (target < PET_XP_LEG[closestIndex] && closestIndex > 0) {
            return closestIndex;
        }
        return closestIndex + 1;
    }
    private static int findClosestIndex(int target) {
        // Find the index of the closest value to the target
        int closestIndex = 0;
        int minDiff = Integer.MAX_VALUE;
        for (int i = 0; i < hyUtils.PET_XP_LEG.length; i++) {
            int diff = Math.abs(hyUtils.PET_XP_LEG[i] - target);
            if (diff < minDiff) {
                minDiff = diff;
                closestIndex = i;
            }
        }
        return closestIndex;
    }
}
