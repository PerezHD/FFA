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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Harry5573
 */
public class CommandRemoveSpawn extends FFACommand {

      FreeForAll plugin;

      public CommandRemoveSpawn(FreeForAll instance) {
            this.plugin = instance;
      }

      @Override
      public void init() {
            usage = "/ffa removespawn";
            description = "Remove one of the spawns for the FFA arena";
            permission = "ffa.admin";
      }

      @Override
      public void run(CommandSender sender, String[] args) {
            if (!this.senderIsPlayer(sender)) {
                  plugin.log("Not a console command!");
            }

            if (!this.hasPermission(sender)) {
                  sender.sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " Permission Denied!");
                  return;
            }
            Player p = (Player) sender;

            if (args.length == 1) {
                  int id = 0;

                  try {
                        id = Integer.valueOf(args[0]);
                  } catch (NumberFormatException e) {
                        p.sendMessage(plugin.messageman.getPrefix() + ChatColor.GRAY + " Spawn id must be a number!");
                        return;
                  }

                  plugin.spawnman.removeSpawn(id);
                  p.sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " Spawn " + ChatColor.YELLOW + id + ChatColor.RED + " has been removed!");
            } else {
                  sender.sendMessage(this.getHelp());
            }
      }

      @Override
      public void run(CommandSender sender) {

            if (!this.hasPermission(sender)) {
                  sender.sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " Permission Denied!");
                  return;
            }

            if (!this.senderIsPlayer(sender)) {
                  plugin.log("Not a console command!");
            }
            Player p = (Player) sender;

            p.sendMessage(this.getHelp());
      }
}
