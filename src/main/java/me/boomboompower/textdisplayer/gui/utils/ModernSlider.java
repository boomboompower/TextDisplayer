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

package me.boomboompower.textdisplayer.gui.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class ModernSlider extends GuiButton {

    public boolean dragging = false;

    public double sliderValue = 1F;
    public double minValue = 0D;
    public double maxValue = 5D;

    public String prefix = "";

    public int precision = 1;

    public ModernSlider(int id, int xPos, int yPos, int width, int height, String prefix, double minVal, double maxVal, double currentVal) {
        super(id, xPos, yPos, width, height, prefix);
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.sliderValue = (currentVal - minValue) / (maxValue - minValue);
        this.prefix = prefix;
        this.precision = 0;

        this.displayString = prefix + Integer.toString((int) Math.round(sliderValue * (maxValue - minValue) + minValue));
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            if (this.enabled) {
                drawRect(this.xPosition, this.yPosition, this.xPosition + width, this.yPosition + height, new Color(95, 255, 95, 75).getRGB());
            } else {
                drawRect(this.xPosition, this.yPosition, this.xPosition + width, this.yPosition + height,  new Color(67, 175, 67, 75).getRGB());
            }
            this.mouseDragged(mc, mouseX, mouseY);

            int color = 14737632;
            if (!this.enabled) {
                color = Color.WHITE.getRGB();
            } else if (this.hovered) {
                color = 16777120;
            }

            String buttonText = this.displayString;
            int strWidth = mc.fontRendererObj.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRendererObj.getStringWidth("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRendererObj.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";

            mc.fontRendererObj.drawString(this.displayString, (this.xPosition + this.width / 2 - mc.fontRendererObj.getStringWidth(this.displayString) / 2), this.yPosition + (this.height - 8) / 2, color, false);
        }
    }

    @Override
    public int getHoverState(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                updateSlider();
            }

            drawRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 4)), this.yPosition, this.xPosition + (int) (this.sliderValue * (float) (this.width - 4)) + 4, this.yPosition + this.height, Color.WHITE.getRGB());
        }
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
        if (super.mousePressed(minecraft, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            updateSlider();
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    public void updateSlider() {
        if (this.sliderValue < 0.0F) {
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F) {
            this.sliderValue = 1.0F;
        }

        displayString = prefix + new StringBuilder(Integer.toString((int) Math.round(sliderValue * (maxValue - minValue) + minValue)));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }

    public double getValue() {
        return sliderValue * (maxValue - minValue) + minValue;
    }

    public void setValue(double value) {
        this.sliderValue = (value - minValue) / (maxValue - minValue);
    }
}