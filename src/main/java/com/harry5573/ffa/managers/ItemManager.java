/*Copyright (C) Harry5573 2013-14

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package com.harry5573.ffa.managers;

import com.harry5573.ffa.FreeForAll;
import com.harry5573.ffa.managers.MessageManager.MessageType;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Harry5573
 */
public class ItemManager {

      FreeForAll plugin;

      public ItemManager(FreeForAll instance) {
            this.plugin = instance;
      }

      /**
       * Parses a kit from our config
       *
       * @param kitname
       * @return
       */
      public HashMap<ItemStack, Integer> parseConfigKit(String kitname) {
            HashMap<ItemStack, Integer> kitItemAndAmount = new HashMap();

            for (String itemstring : plugin.cfManager.getItemsConfig().getStringList("kit." + kitname)) {
                  String[] stringvalue = itemstring.split(":");

                  //If there is no short, just itemid and amount
                  if (stringvalue.length == 2) {
                        ItemStack item = new ItemStack(Material.getMaterial(Integer.valueOf(stringvalue[0])), 1);
                        Integer amount = Integer.valueOf(stringvalue[1]);
                        kitItemAndAmount.put(item, amount);
                  } else if (stringvalue.length == 3) {
                        ItemStack item = new ItemStack(Material.getMaterial(Integer.valueOf(stringvalue[0])), 1, Short.valueOf(stringvalue[1]));
                        Integer amount = Integer.valueOf(stringvalue[2]);
                        kitItemAndAmount.put(item, amount);
                  }
            }

            return kitItemAndAmount;
      }

      /**
       * Gives a player a kit
       *
       * @param player
       * @param kitname
       */
      public void giveKit(Player player, String kitname) {
            HashMap<ItemStack, Integer> kitItems = this.parseConfigKit(kitname);

            for (ItemStack stack : kitItems.keySet()) {
                  for (int i = 1; i <= kitItems.get(stack); i++) {

                        if (stack.getType() == Material.LEATHER_HELMET || stack.getType() == Material.CHAINMAIL_HELMET || stack.getType() == Material.GOLD_HELMET || stack.getType() == Material.IRON_HELMET || stack.getType() == Material.DIAMOND_HELMET) {
                              player.getInventory().setHelmet(stack);
                        } else if (stack.getType() == Material.LEATHER_CHESTPLATE || stack.getType() == Material.CHAINMAIL_CHESTPLATE || stack.getType() == Material.GOLD_CHESTPLATE || stack.getType() == Material.IRON_CHESTPLATE || stack.getType() == Material.DIAMOND_CHESTPLATE) {
                              player.getInventory().setChestplate(stack);
                        } else if (stack.getType() == Material.LEATHER_LEGGINGS || stack.getType() == Material.CHAINMAIL_LEGGINGS || stack.getType() == Material.GOLD_LEGGINGS || stack.getType() == Material.IRON_LEGGINGS || stack.getType() == Material.DIAMOND_LEGGINGS) {
                              player.getInventory().setLeggings(stack);
                        } else if (stack.getType() == Material.LEATHER_BOOTS || stack.getType() == Material.CHAINMAIL_BOOTS || stack.getType() == Material.GOLD_BOOTS || stack.getType() == Material.IRON_BOOTS || stack.getType() == Material.DIAMOND_BOOTS) {
                              player.getInventory().setBoots(stack);
                        } else {
                              player.getInventory().addItem(stack);
                        }
                  }
            }

            player.sendMessage(plugin.messages.get(MessageType.KITGIVE).replaceAll("KIT", kitname));
      }

      /**
       * Gives a player the starter kits
       *
       * @param p
       */
      public void givePlayerStarterKits(Player p) {
            for (String kit : plugin.cfManager.getItemsConfig().getStringList("joinkits")) {
                  this.giveKit(p, kit);
            }
      }
}
