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

import org.lwjgl.input.Keyboard;

import java.awt.*;

//        - 94
//        - 70
//        - 46
//        - 22
//        + 2
//        + 26
//        + 50
//        + 74

public class TextSettingsGui extends GuiScreen {

    private static final String ENABLED = ChatColor.GREEN + "Enabled";
    private static final String DISABLED = ChatColor.RED + "Disabled";

    private GuiTextField text;

    private GuiButton update;
    private GuiButton remove;
    
    private GuiScreen previous;
    
    private Message message;

    public TextSettingsGui(GuiScreen screen, Message input) {
        this.previous = screen;
        this.message = input;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        text = new GuiTextField(0, mc.fontRendererObj, this.width / 2 - 75, this.height / 2 - 22, 150, 20);

        this.buttonList.add(this.update = new GuiButton(1, this.width / 2 - 75, this.height / 2 + 2, 150, 20, "Update"));
        this.buttonList.add(this.remove = new GuiButton(2, this.width / 2 - 75, this.height / 2 + 26, 150, 20, "Remove"));

        this.buttonList.add(new GuiButton(3, this.width / 2 - 75, this.height / 2 + 50, 150, 20, "Use Shadow: " + (message.useShadow() ? ENABLED : DISABLED)));

        text.setFocused(true);
        text.setCanLoseFocus(false);
        text.setText(message.getRawMessage());
        text.setMaxStringLength(100);
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        drawTitle(String.format("Modifying %s", ChatColor.GOLD + message.getName() + ChatColor.WHITE));
        drawMessage();

        update.enabled = ChatColor.formatUnformat('&', this.text.getText()).length() > 0;

        text.drawTextBox();
        super.drawScreen(x, y, ticks);
    }

    @Override
    protected void keyTyped(char c, int key)  {
        if (key == 1) {
            mc.displayGuiScreen(previous);
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
                message.setMessage(this.text.getText());
                sendChatMessage(String.format("Updated %s!", message.getName()));
                mc.displayGuiScreen(previous);
                break;
            case 2:
                message.remove();
                mc.displayGuiScreen(previous);
            case 3:
                this.message.setUseShadow(!this.message.useShadow());
                button.displayString = "Use Shadow: " + (this.message.useShadow() ? ENABLED : DISABLED);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        message.save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void sendChatMessage(String message) {
        GlobalUtils.sendMessage(message, false);
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
        drawCenteredString(mc.fontRendererObj, text, this.width / 2, this.height / 2 - 75, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(text) / 2 + 5, this.height / 2 - 65, Color.WHITE.getRGB());
    }

    private void drawMessage() {
        drawCenteredString(mc.fontRendererObj, "Name: " + message.getName(), this.width / 2, this.height / 2 - 50, Color.WHITE.getRGB());
        drawCenteredString(mc.fontRendererObj, "Message: " + message.getMessage(), this.width / 2, this.height / 2 - 40, Color.WHITE.getRGB());
    }
}
