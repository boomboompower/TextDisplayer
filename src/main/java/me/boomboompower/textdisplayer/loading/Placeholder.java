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

package me.boomboompower.textdisplayer.loading;

import me.boomboompower.textdisplayer.TextDisplayer;
import me.boomboompower.textdisplayer.utils.ChatColor;

/*
 * Created by boomboompower on 22/06/2017.
 */
public class Placeholder {

    private String placeholder = "";
    private Object replaceWith;

    public Placeholder(String placeholder, Object replaceWith) {
        if (placeholder != null && replaceWith != null && !ChatColor.formatUnformat('&', placeholder).isEmpty() && !setupMessage(placeholder).isEmpty()) {
            this.placeholder = setupMessage(placeholder);
            this.replaceWith = replaceWith;

            if (!TextDisplayer.loader.placeholders.has(this)) {
                TextDisplayer.loader.placeholders.put(this);
                System.out.print(String.format("[%s] Placeholder registered!", this.placeholder));
            } else {
                System.out.print(String.format("[%s] Placeholder already exists. Not registered!", this.placeholder));
            }
        }
    }

    /*
     * GETTERS
     */

    public String getPlaceholder() {
        return this.placeholder;
    }

    public String getReplacement() {
        return String.valueOf(this.replaceWith);
    }

    /*
     * SETTERS
     */

    public void setReplacement(Object replacement) {
        this.replaceWith = replacement;
    }

    /*
     * MISC
     */

    private String setupMessage(String input) {
        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c) || c == '_') builder.append(c);
        }
        return builder.toString().trim().toUpperCase();
    }
}
