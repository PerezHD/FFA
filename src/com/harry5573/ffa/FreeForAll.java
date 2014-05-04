package com.harry5573.ffa;

import com.harry5573.ffa.command.CommandDefine;
import com.harry5573.ffa.command.CommandJoin;
import com.harry5573.ffa.command.CommandReload;
import com.harry5573.ffa.command.CommandRemoveSpawn;
import com.harry5573.ffa.command.CommandSetSpawn;
import com.harry5573.ffa.command.CommandEnable;
import com.harry5573.ffa.command.FFACommandHandler;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.harry5573.ffa.listeners.PlayerListener;
import com.harry5573.ffa.managers.ConfigManager;
import com.harry5573.ffa.managers.GameManager;
import com.harry5573.ffa.managers.ItemManager;
import com.harry5573.ffa.managers.MessageManager;
import com.harry5573.ffa.managers.MessageManager.MessageType;
import com.harry5573.ffa.managers.PlayerManager;
import com.harry5573.ffa.region.Region;
import com.harry5573.ffa.managers.RegionManager;
import com.harry5573.ffa.managers.RewardsManager;
import com.harry5573.ffa.utilitys.SpawnData;
import com.harry5573.ffa.managers.SpawnManager;
import com.harry5573.ffa.task.WarmupTask;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;

public class FreeForAll extends JavaPlugin implements Listener {

    public static FreeForAll plugin;

    /**
     * The ffa arena Region
     */
    public Region ffaregion;

    /**
     * Player storage
     */
    public HashMap<Player, ItemStack[]> playerInventoryContents = new HashMap();
    public HashMap<Player, ItemStack[]> playerArmorContents = new HashMap();
    public HashMap<Player, ItemStack> playerCursorStore = new HashMap();
    public HashMap<Player, Integer> playerKillstreak = new HashMap();
    public HashMap<Player, Float> playerExp = new HashMap();

    public List<Player> playerInFFA = new ArrayList<>();
    public HashMap<Player, ArrayList<Block>> inFFA = new HashMap();
    public HashMap<Integer, SpawnData> spawns = new HashMap();
    public HashMap<MessageType, String> messages = new HashMap();

    /**
     * Needed classes
     */
    public PlayerListener plistener;
    public RegionManager rus;
    public PlayerManager pmanager;
    public RegionManager regionman;
    public SpawnManager spawnman;
    public ConfigManager cfManager;
    public MessageManager messageman;
    public RewardsManager rewardman;
    public GameManager gameman;
    public ItemManager itemman;

    /**
     * Vault stuff
     */
    public static Economy econ = null;
    public static Permission permission = null;

    /**
     * Is the plugin enabled (Joinable?)
     */
    public boolean enabled = false;

    private static long startupTime;

    @Override
    public void onEnable() {
        startupTime = System.currentTimeMillis();
        plugin = this;

        log("====[ Plugin version " + getDescription().getVersion() + " starting up ]====");

        if (!checkDependencies()) {
            shutdown();
            return;
        }

        playerInventoryContents.clear();
        playerArmorContents.clear();
        playerCursorStore.clear();
        playerKillstreak.clear();

        this.plistener = new PlayerListener(this);
        this.rus = new RegionManager(this);
        this.pmanager = new PlayerManager(this);
        this.regionman = new RegionManager(this);
        this.cfManager = new ConfigManager(this);
        this.messageman = new MessageManager(this);
        this.spawnman = new SpawnManager(this);
        this.rewardman = new RewardsManager(this);
        this.gameman = new GameManager(this);
        this.itemman = new ItemManager(this);

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(this), this);

        cfManager.load();
        messageman.load();
        spawnman.loadSpawns();

        ffaregion = this.rus.getFFARegion();

        getServer().getScheduler().runTaskTimer(plugin, new WarmupTask(), 20L, 20L);

        if (!this.setupEconomy()) {
            this.log("No economy plugin installed! Plugin shutting down");
            this.shutdown();
            return;
        }

        if (!this.setupPermissions()) {
            this.log("No permission plugin installed! Plugin shutting down");
            this.shutdown();
            return;
        }

        this.setupCommands();

        log("====[ Plugin started up in " + (System.currentTimeMillis() - startupTime) + "ms ]====");
    }

    @Override
    public void onDisable() {
        log("====[ Plugin shutting down ]====");

        for (Player p : Bukkit.getOnlinePlayers()) {
            pmanager.removeFromFFA(p, true);
        }

        saveConfig();

        log("====[ Plugin shut down! ]====");
    }

    /**
     * Sets up commands
     */
    public void setupCommands() {
        FFACommandHandler handler = new FFACommandHandler(this);
        getCommand("ffa").setExecutor(handler);

        handler.registerCommand("join", new CommandJoin(this));
        handler.registerCommand("enable", new CommandEnable(this));
        handler.registerCommand("reload", new CommandReload(this));
        handler.registerCommand("define", new CommandDefine(this));
        handler.registerCommand("setspawn", new CommandSetSpawn(this));
        handler.registerCommand("removespawn", new CommandRemoveSpawn(this));
    }

    /**
     * Checks that all the needed dependencies are installed on the server
     *
     * @return
     */
    public boolean checkDependencies() {
        PluginManager pm = this.getServer().getPluginManager();
        for (String pluginName : this.getDescription().getSoftDepend()) {
            if (pm.getPlugin(pluginName) == null) {
                this.log("Could not find dependencie " + pluginName + " shutting down!");
                return false;
            } else {
                this.log("Registered Dependencie " + pluginName + "!");
            }
        }
        return true;
    }

    /**
     * Vault method to check that we have an economy plugin installed on the
     * server
     *
     * @return
     */
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * Vault method to check that we have a permission plugin installed
     */
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    /**
     * Shuts down the plugin
     */
    public void shutdown() {
        plugin.getServer().getPluginManager().disablePlugin(this);
        plugin.getServer().getScheduler().cancelTasks(this);
    }

    /**
     * Logs a message to the server log/console
     *
     * @param msg
     */
    public void log(String msg) {
        this.getLogger().info(msg);
    }
}
