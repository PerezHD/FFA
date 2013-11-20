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
import java.util.Arrays;
import java.util.HashMap;
import org.bukkit.ChatColor;
 
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Harry5573
 */
public class FFACommandHandler implements CommandExecutor {

    FreeForAll plugin;
    
    public FFACommandHandler(FreeForAll instance) {
        this.plugin = instance;
    }
    
    private HashMap<String, FFACommand> subCommands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String base = args.length > 0 ? args[0] : "";
        FFACommand sc = subCommands.get(base);
        if (sc != null) {
            if (args.length > 1) {
                sc.run(sender, Arrays.copyOfRange(args, 1, args.length));
            } else {
                sc.run(sender);
            }
            return true;
        } else {
            showHelp(sender);
            return true;
        }
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        for (FFACommand command : subCommands.values()) {
            String permission = command.getPermission();
            if (permission.equalsIgnoreCase("none") || sender.hasPermission(permission)) {
                sender.sendMessage(command.getHelp());
            }
        }
        sender.sendMessage(ChatColor.RED + "FFA Version " + plugin.getVersion() + " by harry5573!");
        sender.sendMessage(ChatColor.YELLOW + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }

    public void registerCommand(String name, FFACommand command) {
        subCommands.put(name, command);
        command.init();
    }
}
