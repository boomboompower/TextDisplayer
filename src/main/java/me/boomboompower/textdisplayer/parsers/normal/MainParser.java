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

package me.boomboompower.textdisplayer.parsers.normal;

import me.boomboompower.textdisplayer.parsers.MessageParser;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class MainParser extends MessageParser {

    private static final Minecraft mc = Minecraft.getMinecraft();
    
    @Override
    public String getName() {
        return "MainParser";
    }

    @Override
    public String parse(String message) {
        message = message.replaceAll("\\{USERNAME}", mc.getSession().getUsername())
                .replaceAll("\\{HEALTH}", String.valueOf(MathHelper.floor_double(mc.thePlayer.getHealth())))
                .replaceAll("\\{HUNGER}", String.valueOf(mc.thePlayer.getFoodStats().getFoodLevel()))

                .replaceAll("\\{SERVERNAME}", (mc.getCurrentServerData() == null ? "Unknown" : mc.getCurrentServerData().serverName))
                .replaceAll("\\{SERVERIP}", (mc.getCurrentServerData() == null ? "localhost" : mc.getCurrentServerData().serverIP))
                .replaceAll("\\{PLAYERCOUNT}", String.valueOf(getPlayerCount()));

        if (mc.getRenderViewEntity() != null) {
            message = message.replaceAll("\\{X}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posX)))
                    .replaceAll("\\{Y}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posY)))
                    .replaceAll("\\{Z}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posZ)));
        }
        return message;
    }

    public int getPlayerCount() {
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
