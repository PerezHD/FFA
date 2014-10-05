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
import com.harry5573.ffa.region.Selection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

/**
 *
 * @author Harry5573
 */
public class CommandDefine extends FFACommand {

      FreeForAll plugin;

      public CommandDefine(FreeForAll instance) {
            this.plugin = instance;
      }

      @Override
      public void init() {
            usage = "/ffa define";
            description = "Define the FFA arena region";
            permission = "ffa.admin";
      }

      @Override
      public void run(CommandSender sender, String[] args) {
            if (!this.senderIsPlayer(sender)) {
                  plugin.log("Not a console command!");
            }
            Player player = (Player) sender;

            player.sendMessage(this.getHelp());
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
            Player player = (Player) sender;

            BlockVector p1 = Selection.getP1();
            BlockVector p2 = Selection.getP2();
            if (p1 != null && p2 != null) {
                  plugin.regionman.setRegion(player.getWorld(), p1, p2);
                  plugin.ffaregion = plugin.rus.getFFARegion();
                  sender.sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " Region has been defined.");
            } else {
                  sender.sendMessage(plugin.messageman.getPrefix() + ChatColor.RED + " You need to select both points");
            }
      }
}
