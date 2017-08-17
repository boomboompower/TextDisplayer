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

import me.boomboompower.textdisplayer.gui.MainGui;
import me.boomboompower.textdisplayer.utils.ChatColor;
import me.boomboompower.textdisplayer.utils.WebsiteUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.IOException;

public class TextEvents {

    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean a = false;

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onGameTick(TickEvent.RenderTickEvent event) {
        if (TextDisplayer.getInstance().getWebsiteUtils().isDisabled()) return;

        if (mc.currentScreen != null) {
            if (this.mc.currentScreen instanceof MainGui) {
                try {
                    this.mc.currentScreen.handleInput();
                } catch (IOException ignored) {
                }
            }
        } else if (this.mc.inGameHasFocus && !this.mc.gameSettings.showDebugInfo && mc.thePlayer != null) {
            TextDisplayer.getInstance().getLoader().renderAll(false);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        WebsiteUtils utils = TextDisplayer.getInstance().getWebsiteUtils();

        if (utils.isDisabled()) return;

        if (utils.needsUpdate()) {
            utils.runAsync(() -> {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (Minecraft.getMinecraft().thePlayer == null) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                sendMessage("&9&m---------------------------------------------");
                sendMessage(" ");
                sendMessage(" &b\u26AB &eTextDisplayer is out of date!");
                sendMessage(" &b\u26AB &eDownload %s from the forum page!", "&6v" + utils.getUpdateVersion() + "&e");
                sendMessage(" ");
                sendMessage("&9&m---------------------------------------------");
            });
        }
        
        if (!a && utils.isRunningNewerVersion()) {
            a = true;
            utils.runAsync(() -> {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (Minecraft.getMinecraft().thePlayer == null) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sendMessage("&9&m-----------------------------------------------");
                sendMessage(" ");
                sendMessage(" &b\u26AB &aYou are running a newer version of TextDisplayer!");
                sendMessage(" ");
                sendMessage("&9&m-----------------------------------------------");
            });
        }
    }

    private void sendMessage(String message, Object... replacements) {
        if (Minecraft.getMinecraft().thePlayer == null) return; // Safety first! :)

        try {
            message = String.format(message, replacements);
        } catch (Exception ex) { }
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(ChatColor.translateAlternateColorCodes('&', message)));
    }
}
