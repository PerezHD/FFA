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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Harry5573
 */
public class RewardsManager {

      FreeForAll plugin;

      public RewardsManager(FreeForAll instance) {
            this.plugin = instance;
      }

      /**
       * Simple method to determine if we need to reward or not
       *
       * @param killstreak
       * @return
       */
      public void tryRewardPlayer(Player killer, int killstreak) {
            ConfigurationSection section = plugin.cfManager.getRewardsConfig().getConfigurationSection("killstreak");

            for (String s : section.getKeys(false)) {
                  if (s.equals("everykill")) {
                        if (plugin.cfManager.getRewardsConfig().getString("killstreak." + s + ".givecash").equalsIgnoreCase("true")) {
                              this.rewardPlayerCash(killer, killstreak);
                        }

                        for (String itemtype : plugin.cfManager.getRewardsConfig().getStringList("killstreak." + s + ".removeitems")) {
                              for (ItemStack i : killer.getInventory().getContents()) {
                                    if (i != null) {
                                          if (i.getType() == Material.valueOf(itemtype)) {
                                                killer.getInventory().removeItem(i);
                                                break;
                                          }
                                    }
                              }
                        }

                        for (String kit : plugin.cfManager.getRewardsConfig().getStringList("killstreak." + s + ".givekits")) {
                              plugin.itemman.giveKit(killer, kit);
                        }
                  } else if (killstreak == Integer.valueOf(s)) {
                        if (plugin.cfManager.getRewardsConfig().getString("killstreak." + s + ".givecash").equalsIgnoreCase("true")) {
                              this.rewardPlayerCash(killer, killstreak);
                        }

                        for (String itemtype : plugin.cfManager.getRewardsConfig().getStringList("killstreak." + s + ".removeitems")) {
                              for (ItemStack i : killer.getInventory().getContents()) {
                                    if (i != null) {
                                          if (i.getType() == Material.valueOf(itemtype)) {
                                                killer.getInventory().removeItem(i);
                                                break;
                                          }
                                    }
                              }
                        }

                        for (String kit : plugin.cfManager.getRewardsConfig().getStringList("killstreak." + s + ".givekits")) {
                              plugin.itemman.giveKit(killer, kit);
                        }

                        this.tryBroadcastStreak(killer, Integer.valueOf(s), killstreak);
                  } else if (killstreak != Integer.valueOf(s)) {
                        if (plugin.cfManager.getRewardsConfig().getString("killstreak." + s + ".infinite").equalsIgnoreCase("true")) {
                //Otherwise if we dont give it to them as its not exact we still check if its infinite then round it if its works then BOOM
                              //if the killstreaks infinite then we round it, if its whole we reward them :)
                              double value = killstreak / Double.valueOf(s);

                              if (value == Math.round(value)) {
                                    if (plugin.cfManager.getRewardsConfig().getString("killstreak." + s + ".givecash").equalsIgnoreCase("true")) {
                                          this.rewardPlayerCash(killer, Integer.valueOf(s));
                                    }

                                    for (String itemtype : plugin.cfManager.getRewardsConfig().getStringList("killstreak." + s + ".removeitems")) {
                                          for (ItemStack i : killer.getInventory().getContents()) {
                                                if (i != null) {
                                                      if (i.getType() == Material.valueOf(itemtype)) {
                                                            killer.getInventory().removeItem(i);
                                                            break;
                                                      }
                                                }
                                          }
                                    }

                                    for (String kit : plugin.cfManager.getRewardsConfig().getStringList("killstreak." + s + ".givekits")) {
                                          plugin.itemman.giveKit(killer, kit);
                                    }

                                    this.tryBroadcastStreak(killer, Integer.valueOf(s), killstreak);
                              }
                        }
                  }
            }
      }

      /**
       * Broadcasts a killstreak
       *
       * @param killer
       * @param killstreakValue
       */
      public boolean tryBroadcastStreak(Player killer, int killstreakValue, int killstreak) {
            if (plugin.cfManager.getRewardsConfig().getBoolean("killstreak." + killstreakValue + ".broadcast")) {
                  Bukkit.broadcastMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageManager.MessageType.BROADCASTKILLSTREAK).replaceAll("PLAYER", killer.getName()).replaceAll("KS", String.valueOf(killstreak)));
                  return true;
            }
            return false;
      }

      /**
       * Rewards a player there cash reward needed (ADVANCED)
       *
       * @param killer
       * @param killstreak
       */
      private void rewardPlayerCash(Player killer, int killstreakValue) {
            String groupname = FreeForAll.permission.getPrimaryGroup(killer);
            long defaultamount = plugin.cfManager.getRewardsConfig().getLong("killstreak." + killstreakValue + ".defaultcash");

            if (groupname == null) {
                  plugin.pmanager.giveMoney(killer, defaultamount);
                  killer.sendMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.CASHREWARD).replaceAll("CASH", String.valueOf(defaultamount)).replaceAll("RANK", "defaultcash"));
                  return;
            }
            long amount2give = plugin.cfManager.getRewardsConfig().getLong("killstreak." + killstreakValue + ".ranks." + groupname);

            if (amount2give != 0) {
                  plugin.pmanager.giveMoney(killer, amount2give);
                  killer.sendMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.CASHREWARD).replaceAll("CASH", String.valueOf(amount2give)).replaceAll("RANK", groupname));
            } else {
                  plugin.pmanager.giveMoney(killer, defaultamount);
                  killer.sendMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.CASHREWARD).replaceAll("CASH", String.valueOf(defaultamount)).replaceAll("RANK", "defaultcash"));
            }
      }
}
