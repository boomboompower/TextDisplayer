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

package me.boomboompower.textdisplayer.utils;

import java.util.Random;

public abstract class MessageHelper {

    private static char[] numbers = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static Random rand = new Random();

    public static String getRandomString(int charCount) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < charCount; i++) {
            builder.append(getRandomCharacter());
        }
        return builder.toString().trim().isEmpty() ? "ansnla" + getRandomNumber(100) : builder.toString().trim();
    }

    public static int getRandomNumber() {
        return rand.nextInt();
    }

    public static int getRandomNumber(int boundary) {
        return rand.nextInt(boundary);
    }

    public static char getRandomCharacter() {
        return alphabet[rand.nextInt(alphabet.length)];
    }

    public static char getRandomSingleNumber() {
        return numbers[rand.nextInt(numbers.length)];
    }

    public static float cap(float valueIn, float minValue, float maxValue) {
        return valueIn < minValue ? minValue : valueIn > maxValue ? maxValue : valueIn;
    }
}
