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

import me.boomboompower.textdisplayer.TextDisplayerMod;
import me.boomboompower.textdisplayer.gui.utils.ModernButton;
import me.boomboompower.textdisplayer.gui.utils.ModernTextBox;
import me.boomboompower.textdisplayer.loading.Message;
import me.boomboompower.textdisplayer.parsers.MessageParser;
import me.boomboompower.textdisplayer.utils.ChatColor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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

public class MainGui extends GuiScreen {

    private static final String ENABLED = ChatColor.GREEN + "Enabled";
    private static final String DISABLED = ChatColor.GRAY + "Disabled";

    private ModernTextBox text;

    private ModernButton add;
    private ModernButton clear;

    private String input = "";

    private Message lastClicked = null;

    private int lastMouseX = 0;
    private int lastMouseY = 0;

    private boolean useShadow;
    private boolean useChroma;

    // Displaying message on screen
    private Timer timer;
    private String message = "";
    private int messageTimer;

    public MainGui() {
        this(true, "");
    }

    public MainGui(boolean command, String input) {
        this.input = input;
        initalize();
    }

    public MainGui(String message) {
        initalize();

        displayMessage(message);
    }

    private void initalize() {
        this.message = "";
        this.messageTimer = 0;
        this.useShadow = false;
        this.useChroma = false;
        this.lastClicked = null;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        this.text = new ModernTextBox(0, this.width / 2 - 75, this.height - 25, 150, 20);

        this.buttonList.add(this.add = new ModernButton(1, 20, this.height - 25, 50, 20, "Add"));
        this.buttonList.add(this.clear = new ModernButton(2, 75, this.height - 25, 50, 20, "Clear"));

        this.buttonList.add(new ModernButton(3, this.width - 120, this.height - 25, 100, 20, "Shadow: " + (this.useShadow ? ENABLED : DISABLED)));
        this.buttonList.add(new ModernButton(4, this.width - 120, this.height - 49, 100, 20, "Chroma: " + (this.useChroma ? ENABLED : DISABLED)));

        this.text.setMaxStringLength(TextDisplayerMod.MAX_CHARS);
        this.text.setText(input);

        this.lastClicked = null;
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        drawDefaultBackground();

        drawTitle("TextDisplayer v" + TextDisplayerMod.VERSION);
        drawSpecials();

        this.add.enabled = ChatColor.formatUnformat('&', this.text.getText()).length() > 0;
        this.clear.enabled = TextDisplayerMod.getInstance().getLoader().getMessages().size() > 0;

        this.text.drawTextBox();
        TextDisplayerMod.getInstance().getLoader().renderAll(true);
        super.drawScreen(x, y, ticks);

        runMessage();
    }

    @Override
    protected void keyTyped(char c, int key)  {
        if (key == 1) {
            this.mc.displayGuiScreen(null);
        } else {
            text.textboxKeyTyped(c, key);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        try {
            super.mouseClicked(mouseX, mouseY, button);
            this.text.mouseClicked(mouseX, mouseY, button);
        } catch (Exception ex) {}

        if (button == 0) {
            for (Message message : TextDisplayerMod.getInstance().getLoader().getMessages()) {
                if (this.lastClicked != null && this.lastClicked.equals(message) && message.b()) {
                    new SettingsGui(this, message).display();
                    this.lastClicked = null;
                    return;
                }
                int startX = (int) (message.getX() * message.getScale());
                int startY = (int) (message.getY() * message.getScale());
                int endX = (int) ((startX + ((this.mc.fontRendererObj.getStringWidth(message.getMessage()) + 4) * message.getScale())));
                int endY = (int) ((startY + (14 * message.getScale())));
                if (mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
                    if (message.c()) {
                        message.setDragging(true);
                        this.lastMouseX = mouseX;
                        this.lastMouseY = mouseY;
                    }
                    this.lastClicked = message;
                }
            }
        } else {
            this.lastClicked = null;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int action) {
        super.mouseReleased(mouseX, mouseY, action);
        for (Message message : TextDisplayerMod.getInstance().getLoader().getMessages()) {
            if (message.isDragging()) message.setDragging(false);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {
        for (Message message : TextDisplayerMod.getInstance().getLoader().getMessages()) {
            if (message.isDragging()) {
                message.setX(message.getX() + (int) ((mouseX - this.lastMouseX) / message.getScale()));
                message.setY(message.getY() + (int) ((mouseY - this.lastMouseY) / message.getScale()));
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
                    TextDisplayerMod.getInstance().getLoader().create(Message.formatName(message), text.getText(), this.useShadow, this.useChroma);
                    this.text.setText("");
                } else {
                    sendChatMessage("No text provided!");
                }
                break;
            case 2:
                new ClearGui(this).display();
                break;
            case 3:
                ((ModernButton) button).displayString = "Shadow: " + ((this.useShadow = !this.useShadow) ? ENABLED : DISABLED);
                break;
            case 4:
                ((ModernButton) button).displayString = "Chroma: " + ((this.useChroma = !this.useChroma) ? ENABLED : DISABLED);
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        TextDisplayerMod.getInstance().getLoader().saveAll();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void sendChatMessage(String message) {
        TextDisplayerMod.getInstance().sendMessage(message);
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
        drawCenteredString(this.mc.fontRendererObj, text, this.width / 2, 15, Color.WHITE.getRGB());
        drawHorizontalLine(this.width / 2 - this.mc.fontRendererObj.getStringWidth(text) / 2 - 5, this.width / 2 + this.mc.fontRendererObj.getStringWidth(text) / 2 + 5, 25, Color.WHITE.getRGB());
        drawCenteredString(this.mc.fontRendererObj, "Created by boomboompower", this.width / 2, 30, Color.WHITE.getRGB());
    }

    private void drawSpecials() {
        if (ChatColor.formatUnformat('&', this.text.getText()).length() > 0) {
            drawCenteredString(this.mc.fontRendererObj, "Message will display as", this.width / 2, this.height - 50, Color.WHITE.getRGB());
            drawCenteredString(this.mc.fontRendererObj, ChatColor.translateAlternateColorCodes(MessageParser.parseAll(this.text.getText())), this.width / 2, this.height - 40, this.useChroma ? Message.getColor() : Color.WHITE.getRGB(), this.useShadow);
        }
    }

    private void displayMessage(String message, Object... replacements) {
        try {
            message = String.format(message, replacements);
        } catch (Exception ex) {
        }

        this.message = ChatColor.translateAlternateColorCodes('&', message);
        this.messageTimer = 80;
    }

    private void runMessage() {
        if (this.messageTimer > 0) {
            --this.messageTimer;
            ScaledResolution resolution = new ScaledResolution(this.mc);
            int scaledWidth = resolution.getScaledWidth();
            int scaledHight = resolution.getScaledHeight();
            float time = (float) this.messageTimer - getTimer().renderPartialTicks;
            int opacity = (int) (time * 255.0F / 20.0F);

            if (opacity > 255) {
                opacity = 255;
            }

            if (opacity > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(scaledWidth / 2), (float) (scaledHight - 80), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                int color = Color.WHITE.getRGB();

                this.fontRendererObj.drawString(ChatColor.RED + this.message, -this.fontRendererObj.getStringWidth(message) / 2, -4, color + (opacity << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    private Timer getTimer() {
        return this.timer == null ? this.timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, "timer", "field_71428_T") : this.timer;
    }

    @Override
    public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        drawCenteredString(fontRendererIn, text, x, y, color, true);
    }

    public void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y, int color, boolean shadow) {
        fontRenderer.drawString(text, (float)(x - fontRenderer.getStringWidth(text) / 2), (float)y, color, shadow);
    }
}
