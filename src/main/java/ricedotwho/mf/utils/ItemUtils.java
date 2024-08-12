package ricedotwho.mf.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class ItemUtils {
    /**
     * Returns the Skyblock Item ID of a given Skyblock item
     *
     * @author BiscuitDevelopment
     * @param item the Skyblock item to check
     * @return the Skyblock Item ID of this item or `null` if this isn't a valid Skyblock item
     */
    public static String getSkyBlockItemID(ItemStack item) {
        if (item == null) {
            return null;
        }
        NBTTagCompound extraAttributes = getExtraAttributes(item);
        if (extraAttributes == null) {
            return null;
        }
        if (!extraAttributes.hasKey("id", 8)) { // 8 corresponds to NBT string type
            return null;
        }
        return extraAttributes.getString("id");
    }
    /**
     * Retrieves the extra attributes from the given item.
     *
     * @author BiscuitDevelopment
     * @param item the item from which to retrieve extra attributes
     * @return the NBTTagCompound containing the extra attributes or `null` if none exist
     */
    public static NBTTagCompound getExtraAttributes(ItemStack item) {
        if (item == null || !item.hasTagCompound()) {
            return null;
        }
        return item.getSubCompound("ExtraAttributes", false);
    }
    public static String getUUID(ItemStack item) {
        try {
            NBTTagCompound nbt = item.getSubCompound("ExtraAttributes",false);
            return nbt.getString("uuid");
        } catch (Exception e) {
            return "";
        }
    }

    public static List<NBTTagCompound> getInventory(String inv) throws IOException {
        byte[] decodedData = Base64.decodeBase64(inv);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(decodedData);
        GZIPInputStream gzipIn = new GZIPInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gzipIn.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] decompressedData = baos.toByteArray();

        // Parse NBT data
        ByteArrayInputStream nbtInput = new ByteArrayInputStream(decompressedData);
        NBTTagCompound nbtData = CompressedStreamTools.readCompressed(nbtInput);

        // Extract tag list
        NBTTagList tagList = nbtData.getTagList("i", 10); // 10 corresponds to NBTTagCompound

        List<NBTTagCompound> finalTags = new ArrayList<>();

        // Process tag compounds
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            if (!tag.hasKey("tag", 10)) continue;
            NBTTagCompound innerTag = tag.getCompoundTag("tag");
            if (!innerTag.hasKey("ExtraAttributes", 10)) continue;
            finalTags.add(tag);
        }

        return finalTags;
    }
}
