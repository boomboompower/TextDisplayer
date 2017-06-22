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

import me.boomboompower.textdisplayer.TextDisplayer;
import me.boomboompower.textdisplayer.utils.ChatColor;
import me.boomboompower.textdisplayer.utils.GlobalUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/*
 * Created by boomboompower on 20/06/2017.
 */
public class Message {

    private String fileLocation;

    private String name;
    private String message;

    private boolean useShadow = false;

    private int x;
    private int y;

    public boolean dragging = false;

    public Message(JsonObject object) {
        this.name = object.has("name") ? object.get("name").getAsString() : "unknown";
        this.message = object.has("message") ? object.get("message").getAsString() : "unknown";
        this.useShadow = object.has("useshadow") && object.get("useshadow").getAsBoolean();
        this.x = object.has("x") ? object.get("x").getAsInt() : 0;
        this.y = object.has("y") ? object.get("y").getAsInt() : 0;

        fileLocation = TextDisplayer.loader.getMainDir().getPath() + "\\" + formatName(this.name).toLowerCase() + ".info";
    }

    /*
     * SAVING
     */

    public boolean configExists() {
        return new File(fileLocation).exists();
    }

    public void save() {
        if (this.name == null || this.message == null) {
            return;
        }

        try {
            if (!TextDisplayer.loader.getMainDir().exists()) {
                TextDisplayer.loader.getMainDir().mkdirs();
            }

            JsonObject config = new JsonObject();
            File configFile = new File(fileLocation);

            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            config.addProperty("name", this.name);
            config.addProperty("message", this.message);
            config.addProperty("useshadow", this.useShadow);
            config.addProperty("x", this.x);
            config.addProperty("y", this.y);

            bufferedWriter.write(config.toString());
            bufferedWriter.close();
            writer.close();
            GlobalUtils.DevUtils.log("Saved \"%s\"!", this.name);
        } catch (Exception ex) {
            GlobalUtils.DevUtils.err("Could not save config!");
            ex.printStackTrace();
        }
    }

    public void remove() {
        if (this.name == null || this.message == null) {
            return;
        }

        boolean failed = false;

        try {
            FileUtils.forceDelete(new File(fileLocation));
            TextDisplayer.loader.getMessages().remove(this);
        } catch (Exception ex) {
            failed = true;
        }

        GlobalUtils.sendMessage(failed ?
                String.format(ChatColor.RED + "Could not delete %s!", ChatColor.GOLD + this.name + ChatColor.RED) :
                String.format(ChatColor.GREEN + "Successfully deleted %s!", ChatColor.GOLD + this.name + ChatColor.GREEN), false
        );

        if (failed) {
            GlobalUtils.DevUtils.err("Could not delete \"%s\"!", formatName(this.name));
        } else {
            GlobalUtils.DevUtils.log("Deleted \"%s\"!", this.name);
        }
    }

    /*
     * GETTERS
     */

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes(parse(this.message));
    }

    public String getRawMessage() {
        return this.message;
    }

    public boolean useShadow() {
        return this.useShadow;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getStringWidth() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(ChatColor.translateAlternateColorCodes(parse(this.message)));
    }

    /*
     * SETTERS
     */

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUseShadow(boolean useShadow) {
        this.useShadow = useShadow;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /*
     * MISC
     */

    public String parse(String message) {
        Minecraft mc = Minecraft.getMinecraft();

        message = message.replaceAll("\\{USERNAME}", mc.getSession().getUsername());
        message = message.replaceAll("\\{HEALTH}", String.valueOf(MathHelper.floor_double(mc.thePlayer.getHealth())));

        message = message.replaceAll("\\{SERVERNAME}", (mc.getCurrentServerData() == null ? "Unknown" : mc.getCurrentServerData().serverName));
        message = message.replaceAll("\\{SERVERIP}", (mc.getCurrentServerData() == null ? "localhost" : mc.getCurrentServerData().serverIP));

        if (mc.thePlayer != null) {
            message = message.replaceAll("\\{ITEMINHAND_TYPE}", (mc.thePlayer.getHeldItem() == null ? "Air" : I18n.format(mc.thePlayer.getHeldItem().getUnlocalizedName())));
            message = message.replaceAll("\\{ITEMINHAND_DURA}", (mc.thePlayer.getHeldItem() == null ? "0" : String.valueOf(mc.thePlayer.getHeldItem().getMaxDamage() - mc.thePlayer.getHeldItem().getItemDamage())));
            message = message.replaceAll("\\{ITEMINHAND_MAX}", (mc.thePlayer.getHeldItem() == null ? "0" : String.valueOf(mc.thePlayer.getHeldItem().getMaxDamage())));
        }

        if (mc.theWorld != null) {
            message = message.replaceAll("\\{PLAYERCOUNT}", String.valueOf(mc.theWorld.playerEntities.size()));
        }

        if (mc.getRenderViewEntity() != null) {
            message = message.replaceAll("\\{X}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posX)));
            message = message.replaceAll("\\{Y}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posY)));
            message = message.replaceAll("\\{Z}", String.valueOf(MathHelper.floor_double(mc.getRenderViewEntity().posZ)));
        }

        for (Placeholder holder : TextDisplayer.loader.placeholders) {
            message = message.replaceAll("\\{" + holder.getPlaceholder() + "}", holder.getReplacement());
        }
        return message;
    }

    public String formatName(String name) {
        char[] charList = name.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : charList) {
            if (Character.isLetterOrDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString().trim().length() > 0 ? builder.toString().trim().length() > 50 ? builder.toString().trim().substring(0, 10) : builder.toString().trim() : "x";
    }
}
