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
import me.boomboompower.textdisplayer.parsers.ParsedMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ServerParser extends MessageParser {

    private final Minecraft mc = Minecraft.getMinecraft();

    // Example UUIDs for reference
    /*
     * 254645c1-03b3-4a26-8069-c14bf72d8cfe
     * 430fcd60-bb2f-4569-9a7e-3f4feb41d56e
     * e517456e-84b2-4433-bcd6-15c850d3e9cb
     *               ^
     * This is always the same. A good way
     * of detecting if the entity is a player
     */

    @Override
    public String getName() {
        return "ServerParser";
    }

    @Override
    public ParsedMessage parse(final ParsedMessage message) {
        return message.replace("SERVERNAME", getServerData(ServerDataType.NAME))
                .replace("SERVERIP", getServerData(ServerDataType.ADDRESS))
                .replace("PLAYERCOUNT", getPlayerCount());
    }

    public String getPlayerCount() {
        if (mc.theWorld == null) return "0";

        int players = 0;
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (!player.isInvisibleToPlayer(mc.thePlayer) && !player.isPotionActive(14) && player.getUniqueID().toString().charAt(14) == '4') {
                players += 1;
            }
        }
        return String.valueOf(players);
    }

    public String getServerData(ServerDataType type) {
        boolean isServerDataNull = mc.getCurrentServerData() == null;
        String returnValue;
        switch (type) {
            case NAME:
                returnValue = isServerDataNull ? "Unknown" : mc.getCurrentServerData().serverName;
            case ADDRESS:
                returnValue = isServerDataNull ? "localhost" : mc.getCurrentServerData().serverIP;
            default:
                returnValue = "Unknown";
        }
        return returnValue.isEmpty() ? "Unknown" : returnValue;
    }

    private enum ServerDataType {
        NAME,
        ADDRESS
    }
}
