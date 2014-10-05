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
package com.harry5573.ffa.command;

import com.harry5573.ffa.FreeForAll;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Harry5573
 */
public class CommandEnable extends FFACommand {

      FreeForAll plugin;

      public CommandEnable(FreeForAll instance) {
            this.plugin = instance;
      }

      @Override
      public void init() {
            usage = "/ffa enable";
            description = "Enables/Disables players joining FFA";
            permission = "ffa.admin";
      }

      @Override
      public void run(CommandSender sender, String[] args) {
            sender.sendMessage(this.getHelp());
      }

      @Override
      public void run(CommandSender sender) {
            if (!this.hasPermission(sender)) {
                  sender.sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " Permission Denied!");
                  return;
            }

            if (plugin.enabled) {
                  plugin.getConfig().set("enabled", false);
                  plugin.saveConfig();
                  plugin.enabled = false;

                  for (Player p : Bukkit.getOnlinePlayers()) {
                        plugin.pmanager.removeFromFFA(p, true);
                  }

                  sender.sendMessage(plugin.messageman.getPrefix() + " " + ChatColor.RED + "Plugin disabled!");
            } else {
                  plugin.getConfig().set("enabled", true);
                  plugin.saveConfig();
                  plugin.enabled = true;
                  sender.sendMessage(plugin.messageman.getPrefix() + " " + ChatColor.GREEN + "Plugin enabled!");
            }
      }
}
