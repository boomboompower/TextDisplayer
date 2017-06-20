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

package me.boomboompower.textdisplayer.gui;

import me.boomboompower.textdisplayer.TextDisplayer;

import me.boomboompower.textdisplayer.loading.Message;
import me.boomboompower.textdisplayer.utils.ChatColor;
import me.boomboompower.textdisplayer.utils.GlobalUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

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

public class SettingsGui extends GuiScreen {

    private GuiTextField text;
    private String input = "";

    private int lastMouseX = 0;
    private int lastMouseY = 0;

    public SettingsGui() {
        this("");
    }

    public SettingsGui(String input) {
        this.input = input;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        text = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 75, this.height / 2 - 27, 150, 20);

        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 2, 200, 20, "Add"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 26, 200, 20, "Clear"));

        text.setText(input);
        text.setMaxStringLength(100);
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        drawTitle("TextDisplayer v" + TextDisplayer.VERSION);

        text.drawTextBox();
        super.drawScreen(x, y, ticks);

        TextDisplayer.events.renderDisplay(true);
    }

    @Override
    protected void keyTyped(char c, int key)  {
        if (key == 1) {
            mc.displayGuiScreen(null);
        } else {
            text.textboxKeyTyped(c, key);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        try {
            super.mouseClicked(mouseX, mouseY, button);
            text.mouseClicked(mouseX, mouseY, button);
        } catch (Exception ex) {}

        if (button == 0) {
            for (Message m : TextDisplayer.loader.getMessages()) {
                int startX = m.getX();
                int startY = m.getY();
                int endX = startX + mc.fontRendererObj.getStringWidth(m.getMessage()) + 4;
                int endY = startY + 14;
                if(mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
                    m.dragging = true;
                    this.lastMouseX = mouseX;
                    this.lastMouseY = mouseY;
                }
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int action) {
        super.mouseReleased(mouseX, mouseY, action);
        for (Message m : TextDisplayer.loader.getMessages()) {
            if (m.dragging) m.dragging = false;
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {
        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
        for (Message m : TextDisplayer.loader.getMessages()) {
            if (m.dragging) {
                m.setX(m.getX() + mouseX - this.lastMouseX);
                m.setY(m.getY() + mouseY - this.lastMouseY);
                this.lastMouseX = mouseX;
                this.lastMouseY = mouseY;
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        text.updateCursorCounter();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1:
                if (!text.getText().isEmpty()) {
                    TextDisplayer.loader.create(text.getText().substring(0, 2), text.getText());
                } else {
                    sendChatMessage("No text provided!");
                }
                break;
            case 2:
                boolean failed = false;
                TextDisplayer.loader.getMessages().clear();
                try {
                    FileUtils.deleteDirectory(TextDisplayer.loader.getMainDir());
                } catch (IOException ex) {failed = true;}

                sendChatMessage(failed ? ChatColor.RED + "Failed to clear messages!" : ChatColor.GREEN + "Succesfully removed all messages!");
                mc.displayGuiScreen(null);
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        TextDisplayer.loader.saveAll();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void sendChatMessage(String message) {
        GlobalUtils.sendMessage(message);
    }

    public void display() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    private void drawTitle(String text) {
        drawCenteredString(mc.fontRendererObj, text, this.width / 2, this.height / 2 - 80, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(text) / 2 + 5, this.height / 2 - 70, Color.WHITE.getRGB());
    }
}
