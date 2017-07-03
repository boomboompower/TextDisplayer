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

package me.boomboompower.textdisplayer.gui;

import me.boomboompower.textdisplayer.TextDisplayer;
import me.boomboompower.textdisplayer.utils.ChatColor;
import me.boomboompower.textdisplayer.utils.GlobalUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.IOException;

//        - 94
//        - 70
//        - 46
//        - 22
//        + 2
//        + 26
//        + 50
//        + 74

public class ClearConfirmationGui extends GuiScreen {

    private GuiScreen previousScreen;

    public ClearConfirmationGui(GuiScreen previous) {
        this.previousScreen = previous;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 160, this.height / 2 + 2, 150, 20, "Cancel"));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 13, this.height / 2 + 2, 150, 20, "Confirm"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        drawTitle("Message clear confirmation");

        drawCenteredString(mc.fontRendererObj, String.format("Are you sure you wish to clear %s %s from the screen?", ChatColor.GOLD.toString() + TextDisplayer.loader.getMessages().size() + ChatColor.WHITE,
                TextDisplayer.loader.getMessages().size() == 1 ? "entry" : "entries"),
                this.width / 2, this.height / 2 - 40, Color.WHITE.getRGB()
        );

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                clear();
            default:
                mc.displayGuiScreen(this.previousScreen);
                break;
        }
    }

    @Override
    public void sendChatMessage(String msg) {
        GlobalUtils.sendMessage(msg);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void display() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    private void drawTitle(String text) {
        drawCenteredString(mc.fontRendererObj, text, this.width / 2, this.height / 2 - 75, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(text) / 2 + 5, this.height / 2 - 65, Color.WHITE.getRGB());
    }

    private void clear() {
        boolean failed = false;

        TextDisplayer.loader.getMessages().clear();
        try {
            FileUtils.deleteDirectory(TextDisplayer.loader.getMainDir());
        } catch (IOException ex) {
            failed = true;
        }

        GlobalUtils.log(failed ? "Failed to clear all display messages" : "Removed all display messages!");
        sendChatMessage(failed ? ChatColor.RED + "Failed to clear messages!" : ChatColor.GREEN + "Succesfully removed all messages!");
    }
}
