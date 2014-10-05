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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Harry5573
 */
public class WarmupTask extends BukkitRunnable {

      private final FreeForAll plugin = FreeForAll.plugin;

      public static final ConcurrentHashMap<Player, Integer> warmupPlayers = new ConcurrentHashMap();

      @Override
      public void run() {

            for (Entry<Player, Integer> next : warmupPlayers.entrySet()) {
                  int time = next.getValue();
                  Player player = next.getKey();
                  if (time > 0) {
                        player.sendMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.WARMUP).replaceAll("TIME", String.valueOf(time)));
                        warmupPlayers.remove(player);
                        warmupPlayers.put(player, (time - 1));
                        return;
                  }

                  if (time <= 0) {
                        plugin.pmanager.joinFFAAfterTimer(player);
                        warmupPlayers.remove(player);
                  }
            }
      }
}
