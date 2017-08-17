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

public class FightingParser extends MessageParser {

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
            this.lastDamaged = (EntityPlayer) entityEvent.target;
        }
    }

    public EntityPlayer getLastPlayerDamaged() {
        return this.lastDamaged;
    }

    public String getLastPlayerDamagedName() {
        return this.lastDamaged == null ? "None" : this.lastDamaged.getName();
    }
}
