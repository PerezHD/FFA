package com.harry5573.ffa;

import com.harry5573.ffa.killstreaks.Killstreak;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.harry5573.ffa.listeners.EventListener;
import com.harry5573.ffa.listeners.ItemHandler;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;
    
    public HashMap<Player, Integer> timer = new HashMap();
    public static HashMap<String, ItemStack[]> inventoryContents = new HashMap();
    public static HashMap<String, ItemStack[]> inventoryArmorContents = new HashMap();
    public HashMap<String, ItemStack> cursor = new HashMap();
    public HashMap<Player, Integer> killstreak = new HashMap();
    
    public static HashMap<Player, ArrayList<Block>> inFFA = new HashMap();
    public String prefix = null;
    public EventListener elistener;
    public ItemHandler ih;

    //Start Plugin
    @Override
    public void onEnable() {
        plugin = this;
        
        timer.clear();
        inventoryContents.clear();
        inventoryArmorContents.clear();
        cursor.clear();
        killstreak.clear();

        this.elistener = new EventListener(this);
        this.ih = new ItemHandler(this);

        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(new EventListener(this), this);
        pm.registerEvents(new Killstreak(this), this);
        
        System.out.println("[FFA] Plugin Has Been Started!");

        saveDefaultConfig();
        saveConfig();

        prefix = getConfig().getString("prefix").replaceAll("(&([a-f0-9]))", "\u00A7$2");
    }
    //Disable Plugin 

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            this.removeFromFFA(p);
        }

        System.out.println("[FFA] Plugin Has Been Stopped And All Players In FFA Removed!");
        saveConfig();
    }

    //JoinFFA
    public void joinFFA(final Player p) {
        int i = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                if (Main.plugin.timer.containsKey(p)) {
                    //Save the players real world items
                    saveInventory(p);
                    saveArmor(p);
                    saveCursor(p);
                    //Send the player a message to say there stuff has been saved
                    p.sendMessage(prefix + ChatColor.GRAY + " Your old inventory has been saved");

                    //Give them there new items and potions
                    ih.giveItems(p);
                    ih.giveStuff(p);

                    Bukkit.broadcastMessage(prefix + ChatColor.DARK_AQUA + " " + p.getName() + " Has joined FFA! Type /ffa to join them");

                    plugin.timer.remove(p);

                    randomLocation(p);

                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

                    plugin.killstreak.put(p, Integer.valueOf(0));

                    p.updateInventory();

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "fly " + p.getName() + " off");

                    p.setLevel(0);
                    p.setAllowFlight(false);

                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 2));
                }
            }
        }, 100L);
        plugin.timer.put(p, Integer.valueOf(i));
    }

    public void removeFromFFA(final Player p) {
        if ((plugin.killstreak.containsKey(p))) {

            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            if (p.getItemOnCursor() == null) {
                return;
            }
            p.setItemOnCursor(null);

            p.setHealth(0);

            restorePlayerStuff(p);

            plugin.inventoryArmorContents.remove(p.getName());
            plugin.inventoryContents.remove(p.getName());
            plugin.cursor.remove(p.getName());
            plugin.killstreak.remove(p);

            p.sendMessage(prefix + ChatColor.GREEN + " You were removed from FFA due to the plugin shutting down or reloading!");
        }
    }

    public void respawnFFA(final Player p) {
        if (plugin.killstreak.containsKey(p)) {

            for (PotionEffect pf : p.getActivePotionEffects()) {
                p.removePotionEffect(pf.getType());
            }
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);

            if (p.getItemOnCursor() == null) {
                return;
            }
            p.setItemOnCursor(null);

            restorePlayerStuff(p);

            plugin.inventoryArmorContents.remove(p.getName());
            plugin.inventoryContents.remove(p.getName());
            plugin.cursor.remove(p.getName());
            plugin.killstreak.remove(p);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (command.getName().equalsIgnoreCase("ffa")) {

            if (p == null) {
                sender.sendMessage("[FFA] You must be a player to do that!");
                return true;
            }
            if (args.length == 0) {
                p.sendMessage(prefix + ChatColor.AQUA + " Your about to join FFA! Get ready for Battle.");
                Player player = (Player) sender;
                if (p.getGameMode().equals(GameMode.CREATIVE)) {
                    p.setGameMode(GameMode.SURVIVAL);
                }
                joinFFA(player);
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("shutdown")) {
                    if (sender.hasPermission("iffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    sender.sendMessage(this.prefix + ChatColor.GREEN + " Plugin shutdown. Please RESTART the server to enable it.");
                    getServer().getPluginManager().disablePlugin(this);
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!p.hasPermission("iffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    p.sendMessage(prefix + ChatColor.GREEN + " Config reloaded");
                    System.out.println("[FFA] Config Reloading...");
                    reloadConfig();
                    return true;
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("setspawn")) {
                    if (!p.hasPermission("iffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    //Get The Players Location
                    Location loc = p.getLocation();
                    int blockX = loc.getBlockX();
                    int blockY = loc.getBlockY();
                    int blockZ = loc.getBlockZ();
                    //Set The Spawn
                    if (args[1].equals("1")) {
                        getConfig().set("Spawns.1", blockX + "," + blockY + "," + blockZ);
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 1 set @ your location.");
                        this.saveConfig();
                        return true;
                    }
                    if (args[1].equals("2")) {
                        getConfig().set("Spawns.2", blockX + "," + blockY + "," + blockZ);
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 2 set @ your location.");
                        this.saveConfig();
                        return true;
                    }
                    if (args[1].equals("3")) {
                        getConfig().set("Spawns.3", blockX + "," + blockY + "," + blockZ);
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 3 set @ your location.");
                        this.saveConfig();
                        return true;
                    }
                    if (args[1].equals("4")) {
                        getConfig().set("Spawns.4", blockX + "," + blockY + "," + blockZ);
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 4 set @ your location.");
                        this.saveConfig();
                        return true;
                    } else {
                        p.sendMessage(this.prefix + ChatColor.RED + " Usage: /ffa setspawn <1/4>");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("removespawn")) {
                    if (!p.hasPermission("iffa.admin")) {
                        sender.sendMessage(prefix + " " + ChatColor.RED + "No permission.");
                        return true;
                    }
                    if (args[1].equals("1")) {
                        getConfig().set("Spawns.1", " ");
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 1 set to null.");
                        this.saveConfig();
                        return true;
                    }
                    if (args[1].equals("2")) {
                        getConfig().set("Spawns.2", " ");
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 2 set to null.");
                        this.saveConfig();
                        return true;
                    }
                    if (args[1].equals("3")) {
                        getConfig().set("Spawns.3", " ");
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 3 set to null.");
                        this.saveConfig();
                        return true;
                    }
                    if (args[1].equals("4")) {
                        getConfig().set("Spawns.4", " ");
                        p.sendMessage(this.prefix + ChatColor.GOLD + " Spawn 4 set to null.");
                        this.saveConfig();
                        return true;
                    } else {
                        p.sendMessage(this.prefix + ChatColor.RED + " Usage: /ffa removespawn <1/4>");
                        return true;
                    }
                } else {
                    p.sendMessage(this.prefix + ChatColor.RED + " Usage: /ffa set||removespawn <1/4>");
                }
            }
        }
        return false;
    }

    public void randomLocation(Player p) {
        Random random = new Random();
        switch (random.nextInt(3)) {
            case 0:
                //Spawn 1
                Location l = null;
                String spawn1 = getConfig().getString("Spawns.1");
                String[] spawnfinal1 = spawn1.split(",");
                if (spawnfinal1.length == 3) {
                    int x = Integer.parseInt(spawnfinal1[0]);
                    int y = Integer.parseInt(spawnfinal1[1]);
                    int z = Integer.parseInt(spawnfinal1[2]);
                    l = new Location(getServer().getWorld(p.getWorld().getName()), x, y, z);
                }
                p.teleport(l, PlayerTeleportEvent.TeleportCause.COMMAND);
                break;
            case 1:
                //Spawn 2
                Location l2 = null;
                String spawn2 = getConfig().getString("Spawns.2");
                String[] spawnfinal2 = spawn2.split(",");
                if (spawnfinal2.length == 3) {
                    int x = Integer.parseInt(spawnfinal2[0]);
                    int y = Integer.parseInt(spawnfinal2[1]);
                    int z = Integer.parseInt(spawnfinal2[2]);
                    l2 = new Location(getServer().getWorld(p.getWorld().getName()), x, y, z);
                }
                p.teleport(l2, PlayerTeleportEvent.TeleportCause.COMMAND);
                break;
            case 2:
                //Spawn 3
                Location l3 = null;
                String spawn3 = getConfig().getString("Spawns.3");
                String[] spawnfinal3 = spawn3.split(",");
                if (spawnfinal3.length == 3) {
                    int x = Integer.parseInt(spawnfinal3[0]);
                    int y = Integer.parseInt(spawnfinal3[1]);
                    int z = Integer.parseInt(spawnfinal3[2]);
                    l3 = new Location(getServer().getWorld(p.getWorld().getName()), x, y, z);
                }
                p.teleport(l3, PlayerTeleportEvent.TeleportCause.COMMAND);
                break;
            case 3:
                //Spawn 4
                Location l4 = null;
                String spawn4 = getConfig().getString("Spawns.4");
                String[] spawnfinal4 = spawn4.split(",");
                if (spawnfinal4.length == 3) {
                    int x = Integer.parseInt(spawnfinal4[0]);
                    int y = Integer.parseInt(spawnfinal4[1]);
                    int z = Integer.parseInt(spawnfinal4[2]);
                    l4 = new Location(getServer().getWorld(p.getWorld().getName()), x, y, z);
                }
                p.teleport(l4, PlayerTeleportEvent.TeleportCause.COMMAND);
                break;
            default:
                break;
        }
    }

    public void saveArmor(Player p) {
        System.out.println("[FFA] Saving players armour...");
        plugin.inventoryArmorContents.put(p.getName(), p.getInventory().getArmorContents());
    }

    public void saveCursor(Player p) {
        System.out.println("[FFA] Saving players armour...");

        if (p.getItemOnCursor() == null) {
            return;
        }
        plugin.cursor.put(p.getName(), p.getItemOnCursor());
    }

    public void saveInventory(Player p) {
        System.out.println("[FFA] Saving players inventory...");
        plugin.inventoryContents.put(p.getName(), p.getInventory().getContents());
    }

    public void restorePlayerStuff(Player p) {
        p.getInventory().setContents((ItemStack[]) inventoryContents.get(p.getName()));
        p.getInventory().setArmorContents((ItemStack[]) inventoryArmorContents.get(p.getName()));

        if (cursor.containsKey(p.getName())) {
            p.getInventory().addItem((ItemStack) cursor.get(p.getName()));
        }
    }
}
