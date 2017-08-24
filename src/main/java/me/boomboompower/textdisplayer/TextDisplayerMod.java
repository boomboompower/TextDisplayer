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

import me.boomboompower.textdisplayer.commands.TextCommand;
import me.boomboompower.textdisplayer.loading.MainLoader;
import me.boomboompower.textdisplayer.parsers.MessageParser;
import me.boomboompower.textdisplayer.utils.ChatColor;
import me.boomboompower.textdisplayer.utils.WebsiteUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TextDisplayerMod.MODID, version = TextDisplayerMod.VERSION, acceptedMinecraftVersions="*")
public class TextDisplayerMod {

    public static final String MODID = "textdisplayer";
    public static final String VERSION = "2.0.0";
    public static final String PREFIX = ChatColor.GOLD + "TextDisplayer" + ChatColor.AQUA + " > " + ChatColor.GRAY;

    public static final Integer MAX_CHARS = 100;

    private WebsiteUtils websiteUtils;
    private MainLoader loader;
    private TextEvents events;

    @Mod.Instance
    private static TextDisplayerMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        websiteUtils = new WebsiteUtils();
        loader = new MainLoader();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MessageParser.remake();
        ClientCommandHandler.instance.registerCommand(new TextCommand());
        MinecraftForge.EVENT_BUS.register(events = new TextEvents());

        Minecraft.getMinecraft().addScheduledTask(() -> {
            websiteUtils.begin();
            loader.begin();
        });
    }

    public MainLoader getLoader() {
        return this.loader;
    }

    public TextEvents getEvents() {
        return this.events;
    }

    public WebsiteUtils getWebsiteUtils() {
        return this.websiteUtils;
    }

    public void sendMessage(String message, Object... replacements) {
        if (Minecraft.getMinecraft().thePlayer == null) return; // Safety first! :)

        try {
            message = String.format(message, replacements);
        } catch (Exception ex) { }
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(TextDisplayerMod.PREFIX + ChatColor.translateAlternateColorCodes('&', message)));
    }

    public static TextDisplayerMod getInstance() {
        return instance;
    }
}
