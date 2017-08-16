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

package me.boomboompower.textdisplayer.parsers.normal;

import me.boomboompower.textdisplayer.parsers.MessageParser;

import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class CPSParser extends MessageParser {

    private List<Long> leftClicks = new ArrayList<>();
    private List<Long> rightClicks = new ArrayList<>();

    private boolean lastLeft;
    private boolean lastRight;

    @Override
    public String getName() {
        return "CPSParser";
    }

    @Override
    public String parse(String input) {
        return input.replaceAll("\\{CPS}", String.valueOf(getLeftCPS()))
                .replaceAll("\\{LEFT_CPS}", String.valueOf(getLeftCPS()))
                .replaceAll("\\{CPS_LEFT}", String.valueOf(getLeftCPS()))
                .replaceAll("\\{RIGHT_CPS}", String.valueOf(getRightCPS()))
                .replaceAll("\\{CPS_RIGHT}", String.valueOf(getRightCPS()));
    }

    public int getLeftCPS() {
        this.leftClicks.removeIf(o -> o + 1000L < System.currentTimeMillis());

        return this.leftClicks.size();
    }

    public int getRightCPS() {
        this.rightClicks.removeIf(o -> o + 1000L < System.currentTimeMillis());

        return this.rightClicks.size();
    }

    public void incrementCPS() {
        boolean isClickedLeft = Mouse.isButtonDown(0);
        if (isClickedLeft != this.lastLeft) {
            this.lastLeft = isClickedLeft;
            if (isClickedLeft) {
                this.leftClicks.add(System.currentTimeMillis());
            }
        }

        boolean isClickedRight = Mouse.isButtonDown(1);
        if (isClickedRight != this.lastRight) {
            this.lastRight = isClickedRight;
            if (isClickedRight) {
                this.rightClicks.add(System.currentTimeMillis());
            }
        }
    }
}
