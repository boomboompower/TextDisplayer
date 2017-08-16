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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.boomboompower.textdisplayer.TextDisplayer;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;

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
        if (!this.mainDir.exists()) {
            this.mainDir.mkdirs();
        }

        for (File file : this.mainDir.listFiles()) {
            if (file.getName().endsWith("info")) {
                try {
                    FileReader fileReader = new FileReader(file);
                    BufferedReader reader = new BufferedReader(fileReader);
                    StringBuilder builder = new StringBuilder();

                    String current;
                    while ((current = reader.readLine()) != null) {
                        builder.append(current);
                    }
                    JsonObject o = new JsonParser().parse(builder.toString()).getAsJsonObject();

                    if (o.size() > 0) {
                        if (o.has("name") && has(o.get("name").getAsString())) {
                            return;
                        }
                        this.messages.add(new Message(o));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public File getMainDir() {
        return this.mainDir;
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

    public void saveAll() {
        for (Message message : this.messages) {
            message.save();
        }
    }

    public void create(String name, String message, boolean useShadow, boolean isChroma) {
        if (has(name)) return; // Message already made...

        this.messages.add(new Message(create(name, message, Minecraft.getMinecraft().displayWidth / 2, Minecraft.getMinecraft().displayHeight / 2 + 20, useShadow, isChroma)));
    }

    protected boolean has(final String name) {
        for (Message message : this.messages) {
            if (message.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    protected JsonObject create(String name, String message, int x, int y, boolean shadow, boolean isChroma) {
        JsonObject newObject = new JsonObject();
        newObject.addProperty("name", name);
        newObject.addProperty("message", message);
        newObject.addProperty("usechroma", isChroma);
        newObject.addProperty("useshadow", shadow);
        newObject.addProperty("x", x);
        newObject.addProperty("y", y);
        return newObject.getAsJsonObject();
    }

    public void renderAll(boolean drawBox) {
        for (Message message : this.messages) {
            message.render(drawBox);
        }
    }
}
