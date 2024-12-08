package org.ginafro.notenoughfakepixel.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.jetbrains.annotations.NotNull;

public class ItemUtils {

    public static String getInternalName(ItemStack item){
        if(!item.hasTagCompound()) return null;
        if(!item.getTagCompound().hasKey("ExtraAttributes")) return null;

        NBTTagCompound extraAttributes = item.getTagCompound().getCompoundTag("ExtraAttributes");
        if(!extraAttributes.hasKey("id")) return null;

        return extraAttributes.getString("id");
    }

    public static @NotNull NBTTagCompound getExtraAttributes(ItemStack itemStack) {
        NBTTagCompound tag = getOrCreateTag(itemStack);
        NBTTagCompound extraAttributes = tag.getCompoundTag("ExtraAttributes");
        tag.setTag("ExtraAttributes", extraAttributes);
        return extraAttributes;
    }

    public static int getExtraAttributesIntTag(ItemStack item, String tag){
        NBTTagCompound extraAttributes = getExtraAttributes(item);
        if(!extraAttributes.hasKey(tag)) return -1;
        return extraAttributes.getInteger(tag);
    }

    public static String getExtraAttributesStringTag(ItemStack item, String tag){
        NBTTagCompound extraAttributes = getExtraAttributes(item);
        if(!extraAttributes.hasKey(tag)) return null;
        return extraAttributes.getString(tag);
    }

    public static NBTTagCompound getOrCreateTag(ItemStack is) {
        if (is.hasTagCompound()) return is.getTagCompound();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        is.setTagCompound(nbtTagCompound);
        return nbtTagCompound;
    }

    public static String getLoreLine(ItemStack item, String matcher){
        if(!item.hasTagCompound()) return null;
        if(!item.getTagCompound().hasKey("display")) return null;
        if(!item.getTagCompound().getCompoundTag("display").hasKey("Lore")) return null;

        NBTTagCompound displayTag = item.getTagCompound().getCompoundTag("display");
        NBTTagList lore = displayTag.getTagList("Lore", 8);

        for(int i = 0;i < lore.tagCount(); i++){
            String line = lore.getStringTagAt(i);
            if(line.contains(matcher)) return line;
        }

        return null;
    }
}
