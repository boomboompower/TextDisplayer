/*
 *     Copyright (C) 2017 boomboompower
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class GlobalUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static final String PREFIX = EnumChatFormatting.GOLD + "TextDisplayer" + EnumChatFormatting.AQUA + " > " + EnumChatFormatting.GRAY;

    public static boolean containsIgnoreCase(String message, String contains) {
        return Pattern.compile(Pattern.quote(contains), Pattern.CASE_INSENSITIVE).matcher(message).find();
    }

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean useColor) {
        mc.thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + (useColor ? ChatColor.translateAlternateColorCodes(message) : message)));
    }

    public static void log(String message, Object... replacements) {
        Logger.getLogger("TextDisplayer").log(Level.ALL, String.format(message, replacements));
    }

    public static int getPlayerCount() {
        if (mc.theWorld == null) return 0;
        int players = 0;
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (!player.isInvisibleToPlayer(mc.thePlayer) && !player.isPotionActive(14)) {
                players += 1;
            }
        }
        return players;
    }
}
