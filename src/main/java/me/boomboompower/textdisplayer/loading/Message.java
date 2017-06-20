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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.boomboompower.textdisplayer.TextDisplayer;
import me.boomboompower.textdisplayer.utils.ChatColor;

import net.minecraft.client.Minecraft;

import java.io.*;

/*
 * Created by boomboompower on 20/06/2017.
 */
public class Message {

    private String fileLocation;

    private String name;
    private String message;

    private int x;
    private int y;

    public boolean dragging = false;

    /*
     * Easy way of creating messages without going through code-breaking checks
     *
     * This is used so texts can be created
     */
    protected Message(String name, String message, int x, int y) {
        this.name = name;
        this.message = message;
        this.x = x;
        this.y = y;

        fileLocation = TextDisplayer.loader.getMainDir().getPath() + "\\" + this.name + ".info";
    }

    public Message(JsonObject object) {
        this.name = object.has("name") ? object.get("name").getAsString() : "unknown";
        this.message = object.has("message") ? object.get("message").getAsString() : "unknown";
        this.x = object.has("x") ? object.get("x").getAsInt() : 10;
        this.y = object.has("y") ? object.get("y").getAsInt() : 10;

        fileLocation = TextDisplayer.loader.getMainDir().getAbsolutePath() + this.name + ".info";
    }

    /*
     * SAVING
     */

    public boolean configExists() {
        return new File(fileLocation).exists();
    }

    public void save() {
        JsonObject config = new JsonObject();
        File configFile = new File(fileLocation);
        try {
            if (!TextDisplayer.loader.getMainDir().exists()) {
                TextDisplayer.loader.getMainDir().mkdirs();
            }

            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            config.addProperty("name", this.name);
            config.addProperty("message", this.message);
            config.addProperty("x", this.x);
            config.addProperty("y", this.y);

            bufferedWriter.write(config.toString());
            bufferedWriter.close();
            writer.close();
            System.out.println(String.format("Saved \"%s\"!", this.name));
        } catch (Exception ex) {
            System.out.println("Could not save config!");
            ex.printStackTrace();
        }
    }

    /*
     * GETTERS
     */

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes(this.message);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getStringWidth() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(ChatColor.translateAlternateColorCodes(message));
    }

    /*
     * SETTERS
     */

    public void setMessage(String message) {
        this.message = ChatColor.translateAlternateColorCodes(message);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
