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

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Pattern;

public class GlobalUtils {

    private static final String PREFIX = EnumChatFormatting.GOLD + "TextDisplayer" + EnumChatFormatting.AQUA + " > " + EnumChatFormatting.GRAY;

    public static boolean containsIgnoreCase(String message, String contains) {
        return Pattern.compile(Pattern.quote(contains), Pattern.CASE_INSENSITIVE).matcher(message).find();
    }

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean useColor) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + (useColor ? ChatColor.translateAlternateColorCodes(message) : message)));
    }

    public static class DevUtils {

        public static void log(String message, Object... replace) {
            if (isDev()) System.out.println(String.format(message, replace));
        }

        public static void err(String error, Object... replace) {
            if (isDev()) System.err.println(String.format(error, replace));
        }

        private static boolean isDev() {
            return (Minecraft.getMinecraft().getSession().getPlayerID().equals("54d50dc1-f5ba-4e83-ace6-65b5b6c2ba8d") || Minecraft.getMinecraft().getSession().getPlayerID().equals("3c51db8b-fea0-4c5a-ba65-f490b4b96e24"));
        }
    }
}
