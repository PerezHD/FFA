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
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Harry5573
 */
public class CommandSetSpawn extends FFACommand {

    FreeForAll plugin;

    public CommandSetSpawn(FreeForAll instance) {
        this.plugin = instance;
    }

    @Override
    public void init() {
        usage = "/ffa setspawn";
        description = "Set one of the spawns for the FFA arena";
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
            
            Location loc = p.getLocation();
            Double x = loc.getX();
            Double y = loc.getY();
            Double z = loc.getZ();
            float yaw = loc.getYaw();
            
            plugin.spawnman.setSpawn(loc.getWorld(), x, y, z, yaw, id);
            p.sendMessage(plugin.messageman.getPrefix() + ChatColor.GREEN + " Spawn " + ChatColor.YELLOW + id + ChatColor.GREEN + " has been set at your exact location!");
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
        Player p = (Player)sender;
        
        p.sendMessage(this.getHelp());
    }
}
