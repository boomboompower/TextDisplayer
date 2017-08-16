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

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemParser extends MessageParser {

    private final Minecraft mc = Minecraft.getMinecraft();
    
    private String defaultName = "Air";
    private String defaultValue = "0";

    @Override
    public String getName() {
        return "ItemParser";
    }

    public String parse(String message) {
        return message.replaceAll("\\{HAND_NAME}", (mc.thePlayer.getHeldItem() == null ? defaultName : String.valueOf(getName())))
                .replaceAll("\\{HAND_TOTALAMOUNT}", (mc.thePlayer.getHeldItem() == null ? "1" : String.valueOf(getTotalAmount())))
                .replaceAll("\\{HAND_AMOUNT}", (mc.thePlayer.getHeldItem() == null ? "1" : String.valueOf(getAmount())))
                .replaceAll("\\{HAND_DURA}", (mc.thePlayer.getHeldItem() == null ? defaultValue : String.valueOf(getDura())))
                .replaceAll("\\{HAND_MAX}", (mc.thePlayer.getHeldItem() == null ? defaultValue : String.valueOf(getMaxDura())))

                .replaceAll("\\{ARROWCOUNT}", (mc.thePlayer.getInventory() == null ? defaultValue : String.valueOf(getArrowCount())))

                .replaceAll("\\{ARMOR_HEAD_NAME}", (mc.thePlayer.inventory.armorItemInSlot(3) == null ? defaultName : getItemName(mc.thePlayer.inventory.armorItemInSlot(3))))
                .replaceAll("\\{ARMOR_HEAD_DURA}", (mc.thePlayer.inventory.armorItemInSlot(3) == null ? defaultValue : String.valueOf(getDura(mc.thePlayer.inventory.armorItemInSlot(3)))))
                .replaceAll("\\{ARMOR_HEAD_MAX}", (mc.thePlayer.inventory.armorItemInSlot(3) == null ? defaultValue : String.valueOf(getMaxDura(mc.thePlayer.inventory.armorItemInSlot(3)))))

                .replaceAll("\\{ARMOR_CHEST_NAME}", (mc.thePlayer.inventory.armorItemInSlot(2) == null ? defaultName : getItemName(mc.thePlayer.inventory.armorItemInSlot(2))))
                .replaceAll("\\{ARMOR_CHEST_DURA}", (mc.thePlayer.inventory.armorItemInSlot(2) == null ? defaultValue : String.valueOf(getDura(mc.thePlayer.inventory.armorItemInSlot(2)))))
                .replaceAll("\\{ARMOR_CHEST_MAX}", (mc.thePlayer.inventory.armorItemInSlot(2) == null ? defaultValue : String.valueOf(getMaxDura(mc.thePlayer.inventory.armorItemInSlot(2)))))

                .replaceAll("\\{ARMOR_LEGS_NAME}", (mc.thePlayer.inventory.armorItemInSlot(1) == null ? defaultName : getItemName(mc.thePlayer.inventory.armorItemInSlot(1))))
                .replaceAll("\\{ARMOR_LEGS_DURA}", (mc.thePlayer.inventory.armorItemInSlot(1) == null ? defaultValue : String.valueOf(getDura(mc.thePlayer.inventory.armorItemInSlot(1)))))
                .replaceAll("\\{ARMOR_LEGS_MAX}", (mc.thePlayer.inventory.armorItemInSlot(1) == null ? defaultValue : String.valueOf(getMaxDura(mc.thePlayer.inventory.armorItemInSlot(1)))))

                .replaceAll("\\{ARMOR_BOOTS_NAME}", (mc.thePlayer.inventory.armorItemInSlot(0) == null ? defaultName : getItemName(mc.thePlayer.inventory.armorItemInSlot(0))))
                .replaceAll("\\{ARMOR_BOOTS_DURA}", (mc.thePlayer.inventory.armorItemInSlot(0) == null ? defaultValue : String.valueOf(getDura(mc.thePlayer.inventory.armorItemInSlot(0)))))
                .replaceAll("\\{ARMOR_BOOTS_MAX}", (mc.thePlayer.inventory.armorItemInSlot(0) == null ? defaultValue : String.valueOf(getMaxDura(mc.thePlayer.inventory.armorItemInSlot(0)))));
    }

    public String getItemName() {
        return getItemName(mc.thePlayer.getHeldItem());
    }

    public String getItemName(ItemStack stack) {
        return (stack.getDisplayName());
    }

    public int getDura() {
        return getDura(mc.thePlayer.getHeldItem());
    }

    public int getDura(ItemStack stack) {
        return (stack.getMaxDamage() - stack.getItemDamage() >= 0 ? stack.getMaxDamage() - stack.getItemDamage() : 0);
    }

    public int getMaxDura() {
        return getMaxDura(mc.thePlayer.getHeldItem());
    }

    public int getMaxDura(ItemStack stack) {
        return stack.getMaxDamage();
    }

    public int getAmount() {
        return getAmount(mc.thePlayer.getHeldItem());
    }

    public int getAmount(ItemStack stack) {
        return stack.stackSize;
    }

    public int getTotalAmount() {
        int amount = 0;
        for (int slot = 0; slot < mc.thePlayer.inventory.getSizeInventory(); slot++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);

            if (stack != null && stack.getItem().equals(mc.thePlayer.getHeldItem().getItem())) {
                amount += stack.stackSize;
            }
        }
        return amount;
    }

    public int getArrowCount() {
        int amount = 0;
        for (int slot = 0; slot < mc.thePlayer.inventory.getSizeInventory(); slot++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);

            if (stack != null && stack.getItem().equals(Items.arrow)) {
                amount += stack.stackSize;
            }
        }
        return amount;
    }
}
