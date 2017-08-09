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

package me.boomboompower.textdisplayer;

import me.boomboompower.textdisplayer.gui.SettingsGui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

public class TextEvents {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onGameTick(TickEvent.RenderTickEvent event) {
        if (mc.currentScreen != null) {
            if (this.mc.currentScreen instanceof SettingsGui) {
                try {
                    this.mc.currentScreen.handleInput();
                } catch (IOException ex) {
                    // Mehhhhhhhh
                }
            }
        } else if (this.mc.inGameHasFocus && !this.mc.gameSettings.showDebugInfo && mc.thePlayer != null) {
            TextDisplayer.getInstance().getLoader().renderAll(false);
        }
    }
}
