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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class DirectionParser extends MessageParser {

    private final String[] directionsSmall = new String[] {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
    private final String[] directionsLarge = new String[] {"North", "North East", "East", "South East", "South", "South West", "West", "North West"};

    private String blockName = "Air";

    public DirectionParser() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getName() {
        return "DirectionParser";
    }

    @Override
    public ParsedMessage parse(final ParsedMessage input) {
        return input.replace("FACING_LARGE", getLargeDirection()).replace("FACING_SMALL", getSmallDirection()).replace("LOOKING_AT", getBlockName());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            MovingObjectPosition mop = player.rayTrace(200, 1.0F);
            if (mop != null) {
                Block blockLookingAt = player.worldObj.getBlockState(new BlockPos(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ())).getBlock();
                blockName = blockLookingAt.getLocalizedName();
            } else {
                blockName = "Air";
            }
        } else {
            blockName = "Air";
        }
    }

    public String getBlockName() {
        if (this.blockName.equals("tile.air.name")) {
            return "Air";
        } else {
            return this.blockName;
        }
    }

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
}
