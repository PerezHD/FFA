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

package com.harry5573.ffa.task;

import com.harry5573.ffa.FreeForAll;
import com.harry5573.ffa.managers.MessageManager.MessageType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Harry5573
 */
public class WarmupTask extends BukkitRunnable {
    
    FreeForAll plugin;
    int time = 0;
    Player player;
    
    public WarmupTask(Player p, FreeForAll instance, int time2start) {
        this.plugin = instance;
        this.time = time2start;
        this.player = p;
    }
    
    @Override
    public void run() {
        
        if (time > 0) {
            player.sendMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.WARMUP).replaceAll("TIME", String.valueOf(time)));
            time--;
        }
        
        if (time == 0) {
            plugin.pmanager.joinFFAAfterTimer(player);
            this.cancel();
        }
        
    }
}
