/*
 *     Copyright (C) 2016 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.boomboompower.textdisplayer.utils;

import me.boomboompower.textdisplayer.TextDisplayer;
import me.boomboompower.textdisplayer.loading.Placeholder;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import java.util.regex.Pattern;

public class GlobalUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final String PREFIX = EnumChatFormatting.GOLD + "TextDisplayer" + EnumChatFormatting.AQUA + " > " + EnumChatFormatting.GRAY;

    public static boolean containsIgnoreCase(String message, String contains) {
        return Pattern.compile(Pattern.quote(contains), Pattern.CASE_INSENSITIVE).matcher(message).find();
    }

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean useColor) {
        mc.thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + (useColor ? ChatColor.translateAlternateColorCodes(message) : message)));
    }

    public static String parse(String message) {
        Minecraft mc = Minecraft.getMinecraft();

        message = message.replaceAll("\\{USERNAME}", mc.getSession().getUsername());
        message = message.replaceAll("\\{HEALTH}", String.valueOf(MathHelper.floor_double(mc.thePlayer.getHealth())));
        message = message.replaceAll("\\{HUNGER}", String.valueOf(mc.thePlayer.getFoodStats().getFoodLevel()));

        message = message.replaceAll("\\{SERVERNAME}", (mc.getCurrentServerData() == null ? "Unknown" : mc.getCurrentServerData().serverName));
        message = message.replaceAll("\\{SERVERIP}", (mc.getCurrentServerData() == null ? "localhost" : mc.getCurrentServerData().serverIP));

        if (mc.thePlayer != null) {
            message = ItemUtils.parse(message);
        }

        if (mc.theWorld != null) {
            message = message.replaceAll("\\{PLAYERCOUNT}", String.valueOf(mc.theWorld.playerEntities.size()));
        }

        if (mc.getRenderViewEntity() != null) {
            message = message.replaceAll("\\{X}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posX)));
            message = message.replaceAll("\\{Y}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posY)));
            message = message.replaceAll("\\{Z}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posZ)));
        }

        for (Placeholder holder : TextDisplayer.loader.placeholders) {
            message = message.replaceAll("\\{" + holder.getPlaceholder() + "}", holder.getReplacement());
        }
        return message;
    }

    public static class DevUtils {

        public static void log(String message, Object... replace) {
            if (isDev()) System.out.println(String.format(message, replace));
        }

        public static void err(String error, Object... replace) {
            if (isDev()) System.err.println(String.format(error, replace));
        }

        private static boolean isDev() {
            return (mc.getSession().getPlayerID().equals("54d50dc1-f5ba-4e83-ace6-65b5b6c2ba8d") || mc.getSession().getPlayerID().equals("3c51db8b-fea0-4c5a-ba65-f490b4b96e24"));
        }
    }

    public static class ItemUtils {

        private static String defaultName = "Air";
        private static String defaultValue = "0";

        public static String getName() {
            return getName(mc.thePlayer.getHeldItem());
        }

        public static String getName(ItemStack stack) {
            return (stack.getDisplayName());
        }

        public static int getDura() {
            return getDura(mc.thePlayer.getHeldItem());
        }

        public static int getDura(ItemStack stack) {
            return (stack.getMaxDamage() - stack.getItemDamage() >= 0 ? stack.getMaxDamage() - stack.getItemDamage() : 0);
        }

        public static int getMaxDura() {
            return getMaxDura(mc.thePlayer.getHeldItem());
        }

        public static int getMaxDura(ItemStack stack) {
            return stack.getMaxDamage();
        }

        public static int getAmount() {
            return getAmount(mc.thePlayer.getHeldItem());
        }

        public static int getAmount(ItemStack stack) {
            return stack.stackSize;
        }

        public static String parse(String message) {
            message = message.replaceAll("\\{ITEMINHAND_NAME}", (mc.thePlayer.getHeldItem() == null ? defaultName : String.valueOf(GlobalUtils.ItemUtils.getName())));
            message = message.replaceAll("\\{ITEMINHAND_AMOUNT}", (mc.thePlayer.getHeldItem() == null ? defaultValue + 1 : String.valueOf(GlobalUtils.ItemUtils.getAmount())));
            message = message.replaceAll("\\{ITEMINHAND_DURA}", (mc.thePlayer.getHeldItem() == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getDura())));
            message = message.replaceAll("\\{ITEMINHAND_MAX}", (mc.thePlayer.getHeldItem() == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getMaxDura())));

            message = message.replaceAll("\\{ARMOR_HEAD_NAME}", (mc.thePlayer.inventory.armorItemInSlot(3) == null ? defaultName : GlobalUtils.ItemUtils.getName(mc.thePlayer.inventory.armorItemInSlot(3))));
            message = message.replaceAll("\\{ARMOR_HEAD_DURA}", (mc.thePlayer.inventory.armorItemInSlot(3) == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getDura(mc.thePlayer.inventory.armorItemInSlot(3)))));
            message = message.replaceAll("\\{ARMOR_HEAD_MAX}", (mc.thePlayer.inventory.armorItemInSlot(3) == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getMaxDura(mc.thePlayer.inventory.armorItemInSlot(3)))));

            message = message.replaceAll("\\{ARMOR_CHEST_NAME}", (mc.thePlayer.inventory.armorItemInSlot(2) == null ? defaultName : GlobalUtils.ItemUtils.getName(mc.thePlayer.inventory.armorItemInSlot(2))));
            message = message.replaceAll("\\{ARMOR_CHEST_DURA}", (mc.thePlayer.inventory.armorItemInSlot(2) == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getDura(mc.thePlayer.inventory.armorItemInSlot(2)))));
            message = message.replaceAll("\\{ARMOR_CHEST_MAX}", (mc.thePlayer.inventory.armorItemInSlot(2) == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getMaxDura(mc.thePlayer.inventory.armorItemInSlot(2)))));

            message = message.replaceAll("\\{ARMOR_LEGS_NAME}", (mc.thePlayer.inventory.armorItemInSlot(1) == null ? defaultName : GlobalUtils.ItemUtils.getName(mc.thePlayer.inventory.armorItemInSlot(1))));
            message = message.replaceAll("\\{ARMOR_LEGS_DURA}", (mc.thePlayer.inventory.armorItemInSlot(1) == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getDura(mc.thePlayer.inventory.armorItemInSlot(1)))));
            message = message.replaceAll("\\{ARMOR_LEGS_MAX}", (mc.thePlayer.inventory.armorItemInSlot(1) == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getMaxDura(mc.thePlayer.inventory.armorItemInSlot(1)))));

            message = message.replaceAll("\\{ARMOR_BOOTS_NAME}", (mc.thePlayer.inventory.armorItemInSlot(0) == null ? defaultName : GlobalUtils.ItemUtils.getName(mc.thePlayer.inventory.armorItemInSlot(0))));
            message = message.replaceAll("\\{ARMOR_BOOTS_DURA}", (mc.thePlayer.inventory.armorItemInSlot(0) == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getDura(mc.thePlayer.inventory.armorItemInSlot(0)))));
            message = message.replaceAll("\\{ARMOR_BOOTS_MAX}", (mc.thePlayer.inventory.armorItemInSlot(0) == null ? defaultValue : String.valueOf(GlobalUtils.ItemUtils.getMaxDura(mc.thePlayer.inventory.armorItemInSlot(0)))));

            return message;
        }
    }
}
