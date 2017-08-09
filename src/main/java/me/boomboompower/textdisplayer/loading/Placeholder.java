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

import org.apache.logging.log4j.LogManager;

@Deprecated
public class Placeholder {

    @Deprecated
    public Placeholder(String placeholder, Object replaceWith) {
        LogManager.getLogger().warn("Placeholder class is no longer used");
        LogManager.getLogger().warn("Use MessageParser instead!");
        throw new IllegalArgumentException();
    }

    @Deprecated
    public String getPlaceholder() {
        return "Empty";
    }

    @Deprecated
    public String getReplacement() {
        return "Empty";
    }


    @Deprecated
    public void setReplacement(Object replacement) {
    }
}
