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
import com.harry5573.ffa.utilitys.SpawnData;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Harry5573
 */
public class SpawnManager {

      FreeForAll plugin;

      public SpawnManager(FreeForAll instance) {
            this.plugin = instance;
      }

      public void loadSpawns() {
            ConfigurationSection spawnSection = plugin.getConfig().getConfigurationSection("spawns");

            for (String spawnid : spawnSection.getKeys(false)) {
                  Integer i = Integer.valueOf(spawnid);
                  this.loadSpawn(i);
            }
      }

      private void loadSpawn(int id) {
            SpawnData sd = new SpawnData();
            sd.setX(plugin.getConfig().getDouble("spawns." + id + ".x"));
            sd.setY(plugin.getConfig().getDouble("spawns." + id + ".y"));
            sd.setZ(plugin.getConfig().getDouble("spawns." + id + ".z"));
            sd.setYaw(plugin.getConfig().getInt("spawns." + id + ".yaw"));

            plugin.spawns.put(id, sd);
      }

      public void removeSpawn(int id) {
            if (plugin.getConfig().get("spawns." + id) == null) {
                  return;
            }

            plugin.getConfig().set("spawns." + id, null);
            plugin.saveConfig();

            plugin.spawns.remove(id);
      }

      public void setSpawn(World w, Double x, Double y, Double z, float yaw, int id) {
            plugin.getConfig().set("spawns." + id + ".x", x);
            plugin.getConfig().set("spawns." + id + ".y", y);
            plugin.getConfig().set("spawns." + id + ".z", z);
            plugin.getConfig().set("spawns." + id + ".yaw", yaw);
            plugin.saveConfig();

            this.setWorld(w);
            this.loadSpawn(id);
      }

      public void setWorld(World world) {
            plugin.getConfig().set("ffaworld", world.getName());
            plugin.saveConfig();
      }

      public World getFFAWorld() {
            World world = Bukkit.getWorld(plugin.getConfig().getString("ffaworld"));
            return world;
      }

      public int grabRandomSpawnID() {
            Random rand = new Random();
            return rand.nextInt(plugin.spawns.size()) + 1;
      }

      public Location grabLocationFromSpawnID(int id) {
            SpawnData data = plugin.spawns.get(id);
            Location loc = new Location(this.getFFAWorld(), data.getX(), data.getY() + 2, data.getZ());
            loc.setYaw(data.getYaw());
            return loc;
      }
}
