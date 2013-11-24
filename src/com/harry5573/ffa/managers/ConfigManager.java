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
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Harry5573
 */
public class ConfigManager {

    FreeForAll plugin;
    
    private FileConfiguration messagesConfig = null;
    private File messagesConfigFile = null;
    
    private FileConfiguration rewardsConfig = null;
    private File rewardsConfigFile = null;
    
    private FileConfiguration itemsConfig = null;
    private File itemsConfigFile = null;

    public ConfigManager(FreeForAll instance) {
        this.plugin = instance;
    }

    /**
     * Loads all the needed configurations
     */
    public void load() {
        plugin.saveDefaultConfig();

        if (messagesConfigFile == null) {
            messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        }

        if (rewardsConfigFile == null) {
            rewardsConfigFile = new File(plugin.getDataFolder(), "rewards.yml");
        }

        if (itemsConfigFile == null) {
            itemsConfigFile = new File(plugin.getDataFolder(), "items.yml");
        }

        if (!messagesConfigFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        if (!rewardsConfigFile.exists()) {
            plugin.saveResource("rewards.yml", false);
        }

        if (!itemsConfigFile.exists()) {
            plugin.saveResource("items.yml", false);
        }

        rewardsConfig = YamlConfiguration.loadConfiguration(rewardsConfigFile);
        messagesConfig = YamlConfiguration.loadConfiguration(messagesConfigFile);
        itemsConfig = YamlConfiguration.loadConfiguration(itemsConfigFile);
    }

    /**
     * Reloads all the needed configurations
     */
    public void reload() {
        plugin.reloadConfig();
        
        rewardsConfig = YamlConfiguration.loadConfiguration(rewardsConfigFile);
        messagesConfig = YamlConfiguration.loadConfiguration(messagesConfigFile);
        itemsConfig = YamlConfiguration.loadConfiguration(itemsConfigFile);
    }
    
    /**
     * Returns the FileConfiguration of the messages config
     * @return 
     */
    public FileConfiguration getMessagesConfig() {
        return this.messagesConfig;
    }
    
    /**
     * Returns the FileConfiguration of the rewards config
     * @return 
     */
    public FileConfiguration getRewardsConfig() {
        return this.rewardsConfig;
    }

    /**
     * Returns the FileConfiguration of the rewards config
     * @return 
     */
    public FileConfiguration getItemsConfig() {
        return this.itemsConfig;
    }
}
