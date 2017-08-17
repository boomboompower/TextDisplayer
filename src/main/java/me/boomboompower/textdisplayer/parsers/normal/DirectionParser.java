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

public class DirectionParser extends MessageParser {

    @Override
    public String getName() {
        return "DirectionParser";
    }

    private final String[] directionsSmall = new String[] {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
    private final String[] directionsLarge = new String[] {"North", "North East", "East", "South East", "South", "South West", "West", "North West"};

    public String getLargeDirection() {
        int yaw;
        for(yaw = (int) (Minecraft.getMinecraft().thePlayer.getRotationYawHead() + 180.0F); yaw < 0; yaw += 360) {
            // Let it loop.
        }

        int partSize = 360 / this.directionsLarge.length;
        int index = (yaw + partSize / 2) / partSize;
        return this.directionsLarge[index % this.directionsLarge.length];
    }

    public String getSmallDirection() {
        int yaw;
        for(yaw = (int) (Minecraft.getMinecraft().thePlayer.getRotationYawHead() + 180.0F); yaw < 0; yaw += 360) {
            // Let it loop.
        }

        int partSize = 360 / this.directionsSmall.length;
        int index = (yaw + partSize / 2) / partSize;
        return this.directionsSmall[index % this.directionsSmall.length];
    }

    @Override
    public ParsedMessage parse(ParsedMessage input) {
        return input.replace("FACING_LARGE", getLargeDirection()).replace("FACING_SMALL", getSmallDirection());
    }
}
