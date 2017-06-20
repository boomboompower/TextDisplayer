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

package me.boomboompower.textdisplayer;

import me.boomboompower.textdisplayer.gui.SettingsGui;
import me.boomboompower.textdisplayer.loading.Message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.io.IOException;

public class TextEvents {

    private Minecraft mc = Minecraft.getMinecraft();

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
        } else if (this.mc.inGameHasFocus && !this.mc.gameSettings.showDebugInfo) {
            this.renderDisplay(false);
        }
    }

    public void renderDisplay(boolean drawBox) {
        ScaledResolution res = new ScaledResolution(this.mc);

        for (Message message : TextDisplayer.loader.getMessages()) {
            int width = message.getStringWidth() + 4;
            int height = 14;
            if (message.getX() < 0) {
                message.setX(0);
            } else if (message.getX() > res.getScaledWidth() - width) {
                message.setX(res.getScaledWidth() - width);
            }

            if (message.getY() < 0) {
                message.setY(0);
            } else if (message.getY() > res.getScaledHeight() - height) {
                message.setY(res.getScaledHeight() - height);
            }

            if (drawBox) {
                Gui.drawRect(message.getX(), message.getY(), message.getX() + width, message.getY() + height, -1442840576);
            }

            this.mc.fontRendererObj.drawString(message.getMessage(), message.getX() + 2, message.getY() + 3, Color.WHITE.getRGB(), false);
        }
    }
}
