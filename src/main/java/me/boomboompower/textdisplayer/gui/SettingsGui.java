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
import me.boomboompower.textdisplayer.loading.Message;
import me.boomboompower.textdisplayer.parsers.MessageParser;
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

public class SettingsGui extends GuiScreen {

    private static final String ENABLED = ChatColor.GREEN + "Enabled";
    private static final String DISABLED = ChatColor.RED + "Disabled";

    private GuiTextField text;

    private GuiButton add;
    private GuiButton clear;

    private String input = "";
    private Message lastClicked = null;

    private int lastMouseX = 0;
    private int lastMouseY = 0;

    private boolean useShadow;
    private boolean useChroma;

    public SettingsGui() {
        this("");
    }

    public SettingsGui(String input) {
        this.input = input;
        this.useShadow = false;
        this.lastClicked = null;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        text = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 75, this.height - 25, 150, 20);

        this.buttonList.add(this.add = new GuiButton(1, 20, this.height - 25, 50, 20, "Add"));
        this.buttonList.add(this.clear = new GuiButton(2, 75, this.height - 25, 50, 20, "Clear"));

        this.buttonList.add(new GuiButton(3, this.width - 120, this.height - 25, 100, 20, "Shadow: " + (this.useShadow ? ENABLED : DISABLED)));
        this.buttonList.add(new GuiButton(4, this.width - 230, this.height - 25, 100, 20, "Chroma: " + (this.useChroma ? ENABLED : DISABLED)));

        text.setMaxStringLength(TextDisplayer.MAX_CHARS);
        text.setText(input);

        this.lastClicked = null;
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        drawTitle("TextDisplayer v" + TextDisplayer.VERSION);
        drawSpecials();

        add.enabled = ChatColor.formatUnformat('&', this.text.getText()).length() > 0;
        clear.enabled = TextDisplayer.getInstance().getLoader().getMessages().size() > 0;

        text.drawTextBox();
        TextDisplayer.getInstance().getLoader().renderAll(true);
        super.drawScreen(x, y, ticks);
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
            for (Message m : TextDisplayer.getInstance().getLoader().getMessages()) {
                if (this.lastClicked != null && this.lastClicked.equals(m)) {
                    new TextSettingsGui(this, m).display();
                    return;
                }
                int startX = m.getX();
                int startY = m.getY();
                int endX = startX + mc.fontRendererObj.getStringWidth(m.getMessage()) + 4;
                int endY = startY + 14;
                if (mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
                    m.setDragging(true);
                    this.lastMouseX = mouseX;
                    this.lastMouseY = mouseY;
                    this.lastClicked = m;
                }
            }
        } else {
            this.lastClicked = null;
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int action) {
        super.mouseReleased(mouseX, mouseY, action);
        for (Message m : TextDisplayer.getInstance().getLoader().getMessages()) {
            if (m.isDragging()) m.setDragging(false);
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {
        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClick);
        for (Message m : TextDisplayer.getInstance().getLoader().getMessages()) {
            if (m.isDragging()) {
                m.setX(m.getX() + mouseX - this.lastMouseX);
                m.setY(m.getY() + mouseY - this.lastMouseY);
                this.lastMouseX = mouseX;
                this.lastMouseY = mouseY;
                this.lastClicked = null;
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
                String message = ChatColor.formatUnformat('&', this.text.getText());
                if (!message.isEmpty()) {
                    TextDisplayer.getInstance().getLoader().create(message.contains(" ") ? message.split(" ")[0] : message, text.getText(), this.useShadow, this.useChroma);
                    this.text.setText("");
                } else {
                    sendChatMessage("No text provided!");
                }
                break;
            case 2:
                new ClearConfirmationGui(this).display();
                break;
            case 3:
                button.displayString = "Shadow: " + ((this.useShadow = !this.useShadow) ? ENABLED : DISABLED);
                break;
            case 4:
                button.displayString = "Chroma: " + ((this.useChroma = !this.useChroma) ? ENABLED : DISABLED);
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        TextDisplayer.getInstance().getLoader().saveAll();
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
        drawCenteredString(mc.fontRendererObj, text, this.width / 2, 15, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(text) / 2 + 5, 25, Color.WHITE.getRGB());
    }

    private void drawSpecials() {
        if (ChatColor.formatUnformat('&', this.text.getText()).length() > 0) {
            drawCenteredString(mc.fontRendererObj, "Message will display as", this.width / 2, this.height - 50, Color.WHITE.getRGB());
            drawCenteredString(mc.fontRendererObj, ChatColor.translateAlternateColorCodes(MessageParser.parseAll(this.text.getText())), this.width / 2, this.height - 40, useChroma ? Message.getColor() : Color.WHITE.getRGB());
        }
    }
}
