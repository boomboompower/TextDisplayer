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

package me.boomboompower.textdisplayer.parsers;

public class ParsedMessage {

    private String input;

    public ParsedMessage(String input) {
        if (!isValidAndNotEmpty(input)) {
            throw new IllegalArgumentException("Input cannot be null or empty!");
        }

        this.input = input;
    }

    public ParsedMessage replace(String key, String value) {
        if (!isValidAndNotEmpty(getCharsOnly(key)) || !isValid(value)) {
            throw new IllegalArgumentException("Key or value cannot be null!");
        }

        this.input = input.replaceAll("\\{" + key + "}", value);

        return this;
    }

    public boolean containsKey(String key) {
        if (!isValidAndNotEmpty(key)) {
            throw new IllegalArgumentException("Key cannot be null or empty!");
        }

        return this.input.matches("\\{" + key +"}");
    }

    public String getMessage() {
        return this.input;
    }

    private boolean isValid(String input) {
        return input != null;
    }

    private boolean isValidAndNotEmpty(String input) {
        return isValid(input) && !input.isEmpty();
    }

    private String getCharsOnly(String input) {
        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '_') {
                builder.append(c);
            }
        }
        return builder.toString().trim().isEmpty() ? null : builder.toString().trim();
    }
}
