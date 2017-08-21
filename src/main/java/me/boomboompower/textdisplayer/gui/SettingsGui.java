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
import me.boomboompower.textdisplayer.gui.utils.ModernButton;
import me.boomboompower.textdisplayer.gui.utils.ModernSlider;
import me.boomboompower.textdisplayer.gui.utils.ModernTextBox;
import me.boomboompower.textdisplayer.loading.Message;
import me.boomboompower.textdisplayer.parsers.MessageParser;
import me.boomboompower.textdisplayer.utils.ChatColor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

//        - 118
//        - 94
//        - 70
//        - 46
//        - 22
//        + 2
//        + 26
//        + 50
//        + 74
//        + 98

public class SettingsGui extends GuiScreen {

    private static final String ENABLED = ChatColor.GREEN + "Enabled";
    private static final String DISABLED = ChatColor.GRAY + "Disabled";

    private boolean removed;

    private ModernTextBox text;

    private GuiButton update;
    private GuiButton remove;
    
    private GuiScreen previous;
    
    private Message message;

    public SettingsGui(GuiScreen screen, Message input) {
        this.previous = screen;
        this.message = input;

        this.removed = false;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        text = new ModernTextBox(0,this.width / 2 - 75, this.height / 2 - 46, 150, 20);

        this.buttonList.add(this.update = new ModernButton(1, this.width / 2 - 100, this.height / 2 - 22, 200, 20, "Update"));
        this.buttonList.add(this.remove = new ModernButton(2, this.width / 2 - 100, this.height / 2 + 2, 200, 20, "Remove"));
        this.buttonList.add(new ModernButton(3, this.width / 2 - 100, this.height / 2 + 26, 200, 20, "Use Shadow: " + (message.useShadow() ? ENABLED : DISABLED)));
        this.buttonList.add(new ModernButton(4, this.width / 2 - 100, this.height / 2 + 50, 200, 20, "Use Chroma: " + (message.isChroma() ? ENABLED : DISABLED)));
        this.buttonList.add(new ModernSlider(5, this.width / 2 - 100, this.height / 2 + 74, 200, 20, "Scale: ", 100.0D, 200.0D, message.getScale() * 100) {
            @Override
            public void updateSlider() {
                super.updateSlider();
                message.setScale((float) (getValue() / 100.0D));
            }
        });

        text.setFocused(true);
        text.setCanLoseFocus(false);
        text.setMaxStringLength(TextDisplayer.MAX_CHARS);
        text.setText(message.getRawMessage());
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        drawTitle(String.format("Modifying %s", ChatColor.GOLD + message.getName() + ChatColor.WHITE));
        drawMessage();

        text.drawTextBox();
        super.drawScreen(x, y, ticks);

        update.enabled = ChatColor.formatUnformat('&', this.text.getText()).length() > 0;
    }

    @Override
    protected void keyTyped(char c, int key)  {
        if (key == 1) {
            displayPrevious(null);
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
                displayPrevious("&aModified " + message.getName());
                break;
            case 2:
                boolean hasRemoved = message.remove();
                if (hasRemoved) {
                    this.removed = true;
                    displayPrevious("&aSuccessfully removed " + message.getName());
                } else {
                    displayPrevious("An issue occured while removing " + message.getName());
                }
                break;
            case 3:
                this.message.setUseShadow(!this.message.useShadow());
                ((ModernButton) button).displayString = "Use Shadow: " + (this.message.useShadow() ? ENABLED : DISABLED);
                break;
            case 4:
                this.message.setUseChroma(!this.message.isChroma());
                ((ModernButton) button).displayString = "Use Chroma: " + (this.message.isChroma() ? ENABLED : DISABLED);
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (!this.removed) message.save();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void sendChatMessage(String message) {
        TextDisplayer.getInstance().sendMessage(message);
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
        drawCenteredString(mc.fontRendererObj, text, this.width / 2, this.height / 2 - 99, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + mc.fontRendererObj.getStringWidth(text) / 2 + 5, this.height / 2 - 89, Color.WHITE.getRGB());
    }

    private void drawMessage() {
        drawCenteredString(mc.fontRendererObj, "Message will display as", this.width / 2, this.height / 2 - 78, Color.WHITE.getRGB());

        GlStateManager.pushMatrix();
        GL11.glScaled(message.getScale(), message.getScale(), 1.0);
        drawCenteredString(mc.fontRendererObj, ChatColor.translateAlternateColorCodes(MessageParser.parseAll(this.text.getText())), (int) ((this.width / 2) / message.getScale()), (int) ((this.height / 2 - 68) / message.getScale()), message.isChroma() ? Message.getColor() :  Color.WHITE.getRGB(), message.useShadow());
        GlStateManager.popMatrix();
    }

    private void displayPrevious(String message) {
        if (previous instanceof MainGui && message != null) {
            mc.displayGuiScreen(new MainGui(message));
        } else {
            mc.displayGuiScreen(previous);
        }
    }

    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color, boolean shadow) {
        fontRendererIn.drawString(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color, shadow);
    }
}
