package ricedotwho.mf.mining;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import ricedotwho.mf.config.ModConfig;
import ricedotwho.mf.utils.Utils;
import ricedotwho.mf.utils.itemUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Taken from Hot-Shirtless-Men under the MIT License
 * https://github.com/Rekteiru/Hot-Shirtless-Men/blob/main/LICENSE
 * @author Rekteiru
 */
public class drillFix {
    static Minecraft mc;
    static List<Item> items = new ArrayList<>();
    static {
        mc = Minecraft.getMinecraft();
        items.add(Item.getItemById(409)); // Prismarine Shard
        items.add(Item.getItemById(397)); // Player Head (Gauntlet)

        items.add(Item.getItemById(270)); // Wooden Pickaxe
        items.add(Item.getItemById(274)); // Stone Pickaxe
        items.add(Item.getItemById(257)); // Iron Pickaxe
        items.add(Item.getItemById(285)); // Golden Pickaxe
        items.add(Item.getItemById(278)); // Diamond Pickaxe
    }
    public static boolean isHittingPosition(BlockPos pos, ItemStack currentItemHittingBlock, BlockPos currentBlock)
    {
        if(!Utils.inSkyblock) { return false; }
        ItemStack itemstack = mc.thePlayer.getHeldItem();
        boolean flag = currentItemHittingBlock == null && itemstack == null;

        if (currentItemHittingBlock != null && itemstack != null)
        {
            if (ModConfig.drillFix &&
                    itemstack.getItem() == currentItemHittingBlock.getItem() &&
                    itemUtils.getUUID(itemstack).equals(itemUtils.getUUID(currentItemHittingBlock)) &&
                    items.contains(itemstack.getItem())) {
                return pos.equals(currentBlock);
            }

            flag = itemstack.getItem() == currentItemHittingBlock.getItem() &&
                    ItemStack.areItemStackTagsEqual(itemstack, currentItemHittingBlock) &&
                    (
                            itemstack.isItemStackDamageable() ||
                                    itemstack.getMetadata() == currentItemHittingBlock.getMetadata()
                    );
        }

        return pos.equals(currentBlock) && flag;
    }
}
