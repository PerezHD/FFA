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
import com.harry5573.ffa.api.PlayersInFFAChangeEvent;
import com.harry5573.ffa.managers.MessageManager.MessageType;
import com.harry5573.ffa.task.WarmupTask;
import com.harry5573.ffa.utilitys.MessageUtil;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 *
 * @author Harry5573
 */
public class PlayerManager {
    
    FreeForAll plugin;
    
    public PlayerManager(FreeForAll instance) {
        this.plugin = instance;
    }
    
    /**
     * Call this when we want a player to begin the process of joining ffa
     * @param p 
     */
    public void joinFFA(final Player p) {
        plugin.playerInFFA.add(p);

        p.sendMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.PREJOIN));
        if (p.getGameMode().equals(GameMode.CREATIVE)) {
            p.setGameMode(GameMode.SURVIVAL);
        }

        BukkitTask thread = new WarmupTask(p, plugin, plugin.getConfig().getInt("teleportdelay")).runTaskTimer(plugin, 0L, 20L);
        plugin.warmupTasks.put(p, thread.getTaskId());
    }

    /**
     * Should be called after the warmup process
     * @param p 
     */
    public void joinFFAAfterTimer(final Player p) {
        if (p.getOpenInventory() != null) {
            p.getOpenInventory().close();
        }
        
        p.sendMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.OLDSAVE));
        this.teleportPlayerToRandomLocation(p);

        this.savePlayerToInternalStorage(p);
        
        for (PotionEffect pf : p.getActivePotionEffects()) {
            p.removePotionEffect(pf.getType());
        }

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        p.setAllowFlight(false);
        p.setFlying(false);
        p.setGameMode(GameMode.SURVIVAL);

        plugin.playerKillstreak.put(p, 0);
        plugin.warmupTasks.remove(p);

        plugin.itemman.givePlayerStarterKits(p);

        this.updateScoreboard(p);

        if (plugin.getConfig().getBoolean("broadcast.join")) {
            Bukkit.broadcastMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.BROADCASTJOIN).replaceAll("PLAYER", p.getName()));
        }

        if (plugin.getConfig().getBoolean("effect.joinblind")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 2));
        }

        if (plugin.getConfig().getBoolean("effect.joinsound")) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 0);
        }

        PlayersInFFAChangeEvent changeevent = new PlayersInFFAChangeEvent(plugin.gameman.getAmountPlayersInFFA());
        Bukkit.getServer().getPluginManager().callEvent(changeevent);
    }

    /**
     * Removes a player from FFA
     *
     * @param p
     * @param alive
     * @param online
     */
    public void removeFromFFA(Player p, boolean online) {
        if (plugin.warmupTasks.containsKey(p)) {
            Bukkit.getScheduler().cancelTask(plugin.warmupTasks.get(p));

            plugin.warmupTasks.remove(p);
            plugin.playerInFFA.remove(p);
        }

        if (!plugin.playerKillstreak.containsKey(p)) {
            return;
        }

        plugin.playerKillstreak.remove(p);
        plugin.playerInFFA.remove(p);

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setItemOnCursor(null);

        for (PotionEffect pf : p.getActivePotionEffects()) {
            p.removePotionEffect(pf.getType());
        }

        p.setHealth(0);

        if (online) {
            p.sendMessage(plugin.messageman.getPrefix() + " " + plugin.messages.get(MessageType.THANKS));
            this.removeScoreboard(p);
        }

        this.restorePlayerFromInternalStore(p);
        
        PlayersInFFAChangeEvent changeevent = new PlayersInFFAChangeEvent(plugin.gameman.getAmountPlayersInFFA());
        Bukkit.getServer().getPluginManager().callEvent(changeevent);
    }

    /**
     * Stores a players stuff in internal storage
     *
     * @param p
     */
    public boolean savePlayerToInternalStorage(Player p) {
        plugin.playerInventoryContents.put(p, p.getInventory().getContents());
        plugin.playerArmorContents.put(p, p.getInventory().getArmorContents());
        plugin.playerExp.put(p, p.getExp());
        
        HashMap<PotionEffectType, Integer> pots = new HashMap();
        
        for (PotionEffect pf : p.getActivePotionEffects()) {
            pots.put(pf.getType(), pf.getAmplifier());
        }

        if (p.getItemOnCursor() != null) {
            plugin.playerCursorStore.put(p, p.getItemOnCursor());
        }
        return true;
    }

    /**
     * Restores a player after being in FFA
     *
     * @param p
     * @param teleport
     */
    public void restorePlayerFromInternalStore(final Player p) {
        p.getInventory().setContents((ItemStack[]) plugin.playerInventoryContents.get(p));
        p.getInventory().setArmorContents((ItemStack[]) plugin.playerArmorContents.get(p));
        p.setExp(plugin.playerExp.get(p));
        
        if (plugin.playerCursorStore.containsKey(p)) {
            p.getInventory().addItem(plugin.playerCursorStore.get(p));
        }
        
        plugin.playerInventoryContents.remove(p);
        plugin.playerArmorContents.remove(p);
        plugin.playerExp.remove(p);
        plugin.playerCursorStore.remove(p);
    }

    /**
     * Teleports a player to random location from FFA spawns
     *
     * @param p
     */
    public void teleportPlayerToRandomLocation(Player p) {
        p.teleport(plugin.spawnman.grabLocationFromSpawnID(plugin.spawnman.grabRandomSpawnID()), TeleportCause.PLUGIN);
    }

    /**
     * Gives a player money via VAULT
     * @param p
     * @param amount 
     */
    public void giveMoney(Player p, double amount) {
        p.sendMessage(plugin.messages.get(MessageType.MONEYGIVE).replaceAll("MONEY", String.valueOf(Integer.valueOf((int) amount))));
        FreeForAll.econ.depositPlayer(p.getName(), amount);
    }
    
    /**
     * Tells us if a player is in FFA
     * @param p
     * @return 
     */
    public boolean playerIsInFFA(Player p) {
        if (plugin.playerInFFA.contains(p)) {
            return true;
        }
        return false;
    }
    
    /**
     * Returns a players killstreak if they are in FFA
     * @param p
     * @return 
     */
    public int getPlayerKillstreak(Player p) {
        int amt = plugin.playerKillstreak.get(p);
        
        if (amt != 0) {
            return amt;
        }
        
        return 0;
    }
 
    /**
     * Sets the players scoreboard to whatever we want!
     * @param p 
     */
    public void updateScoreboard(Player p) {
        ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
        p.setScoreboard(scoreboardManager.getNewScoreboard());
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(MessageUtil.translateToColorCode(plugin.getConfig().getString("scoreboard.title")));

        Score players = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Players:"));
        players.setScore(plugin.gameman.getAmountPlayersInFFA());

        Score killstreak = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Killstreak:"));
        killstreak.setScore(this.getPlayerKillstreak(p));
        
        p.setScoreboard(scoreboard);
    }

    /**
     * Removes a players scoreboard after FFA
     * @param p 
     */
    public void removeScoreboard(Player p) {
        ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
        p.setScoreboard(scoreboardManager.getNewScoreboard());
    }
    
    /**
     * Sends a message to all players in FFA
     * @param msg 
     */
    public void messageAllPlayersInFFA(String msg) {
        for (Player p : plugin.playerInFFA) {
            p.sendMessage(msg);
        }
    }
}
