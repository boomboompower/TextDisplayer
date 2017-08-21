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
import net.minecraft.util.MathHelper;

public class MainParser extends MessageParser {

    private static final Minecraft mc = Minecraft.getMinecraft();
    
    @Override
    public String getName() {
        return "MainParser";
    }

    @Override
    public ParsedMessage parse(ParsedMessage message) {
        return message.replace("USERNAME", mc.getSession().getUsername())
                .replace("HEALTH", getHealth(HealthType.TOTAL))
                .replace("HEARTS", getHealth(HealthType.HEARTS))
                .replace("HUNGER", String.valueOf(mc.thePlayer.getFoodStats().getFoodLevel()))

                .replace("X", getCoord(Coord.X))
                .replace("Y", getCoord(Coord.Y))
                .replace("Z", getCoord(Coord.Z));
    }

    public String getCoord(Coord type) {
        if (mc.getRenderViewEntity() == null) return "0";

        switch (type) {
            case X:
                return String.valueOf(mc.getRenderViewEntity().posX);
            case Y:
                return String.valueOf(mc.getRenderViewEntity().posY);
            case Z:
                return String.valueOf(mc.getRenderViewEntity().posZ);
            default:
                return "0";
        }
    }

    public String getHealth(HealthType type) {
        switch (type) {
            case TOTAL:
                return String.valueOf(MathHelper.floor_double(mc.thePlayer.getHealth()));
            case HEARTS:
                return String.valueOf(((double) MathHelper.floor_double(mc.thePlayer.getHealth()) / 2));
            default:
                return "20";
        }
    }

    private enum Coord {
        X,
        Y,
        Z
    }

    private enum HealthType {
        HEARTS,
        TOTAL
    }
}
