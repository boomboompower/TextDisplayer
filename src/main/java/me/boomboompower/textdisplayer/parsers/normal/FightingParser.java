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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FightingParser extends MessageParser {

    // Example UUIDs for reference
    /*
     * 254645c1-03b3-4a26-8069-c14bf72d8cfe
     * 430fcd60-bb2f-4569-9a7e-3f4feb41d56e
     * e517456e-84b2-4433-bcd6-15c850d3e9cb
     *               ^
     * This is always the same. A good way
     * of detecting if the entity is a player
     */


    private int timer;
    private EntityPlayer lastDamaged;

    public FightingParser() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getName() {
        return "FightingParser";
    }

    @Override
    public ParsedMessage parse(ParsedMessage input) {
        return input.replace("LASTDAMAGED", getLastPlayerDamagedName());
    }

    @SubscribeEvent
    public void onDamage(AttackEntityEvent entityEvent) {
        if (entityEvent.target instanceof EntityPlayer) {

            // Making sure the player isn't fake (Delivery man etc)
            if (entityEvent.target.getUniqueID().toString().charAt(14) != '4') {
                return;
            }

            this.lastDamaged = (EntityPlayer) entityEvent.target;
            timer = 120;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (this.timer > 0) {
            this.timer--;
        } else {
            this.lastDamaged = null;
        }
    }

    public EntityPlayer getLastPlayerDamaged() {
        return this.lastDamaged;
    }

    public String getLastPlayerDamagedName() {
        return this.lastDamaged == null ? "None" : this.lastDamaged.getName();
    }
}
