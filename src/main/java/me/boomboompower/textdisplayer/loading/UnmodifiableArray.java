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

package me.boomboompower.textdisplayer.loading;

import java.util.ArrayList;

public class UnmodifiableArray<T> {

    private ArrayList<T> arrayList = new ArrayList<>();

    protected void put(T t) {
        arrayList.add(t);
    }

    protected boolean has(Object o) {
        return arrayList.contains(o);
    }

    protected ArrayList<T> get() {
        return arrayList;
    }

    public ArrayList<T> get(String access) {
        return arrayList;
    }
}
