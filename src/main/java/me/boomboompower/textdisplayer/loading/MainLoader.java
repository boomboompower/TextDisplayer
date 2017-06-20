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

import com.google.gson.JsonParser;

import me.boomboompower.textdisplayer.TextDisplayer;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Created by boomboompower on 20/06/2017.
 */
public class MainLoader {

    private File mainDir;
    private ArrayList<Message> messages = new ArrayList<>();

    public MainLoader() {
        this(new File(".", "mods\\" + TextDisplayer.MODID + "\\"));
    }

    public MainLoader(File mainDir) {
        this.mainDir = mainDir;
    }

    public void begin() {
        if (!mainDir.exists()) {
            mainDir.mkdirs();
        }

        System.out.println(Arrays.toString(mainDir.listFiles()));

        BufferedReader f;
        List options;

        for (File file : mainDir.listFiles()) {
            try {
                f = new BufferedReader(new FileReader(file));
                options = f.lines().collect(Collectors.toList());

                if (options.get(0) != null) {
                    messages.add(new Message(new JsonParser().parse((String) options.get(0)).getAsJsonObject()));
                }

                System.out.println(f);
                System.out.println(options);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        System.out.println(messages);
    }

    public File getMainDir() {
        return this.mainDir;
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public void saveAll() {
        for (Message m : messages) {
            m.save();
        }
    }

    public void create(String name, String message) {
        if (has(name, message)) return; // Message already made...
        messages.add(new Message(name, message, Minecraft.getMinecraft().displayWidth / 2, Minecraft.getMinecraft().displayHeight / 2 + 20));
    }

    private boolean has(String name, String message) {
        boolean has = false;
        for (Message m : messages) {
            if (!has) {
                has = m.getName().equalsIgnoreCase(name) && m.getMessage().equalsIgnoreCase(message);
            } else {
                break;
            }
        }
        return has;
    }
}
