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
import java.util.Iterator;
import java.util.List;
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
     * Warmup Task ID for player
     */
    public HashMap<Player, Integer> warmupTasks = new HashMap();
    
    /**
     * Player storage
     */
    public HashMap<Player, ItemStack[]> playerInventoryContents = new HashMap();
    public HashMap<Player, ItemStack[]> playerArmorContents = new HashMap();
    public HashMap<Player, ItemStack> playerCursorStore = new HashMap();
    public HashMap<Player, Integer> playerKillstreak = new HashMap();
    public HashMap<Player, Float> playerExp = new HashMap();
    
    /**
     * Players in ffa
     */
    public List<Player>playerInFFA = new ArrayList<>();
    
    /**
     * Regions
     */
    public HashMap<Player, ArrayList<Block>> inFFA = new HashMap();
    /**
     * Spawns
     */
    public HashMap<Integer, SpawnData> spawns = new HashMap();

    /**
     * Message Storage
     */
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
    
    @Override
    public void onEnable() {
        plugin = this;
        
        log("Plugin version " + getVersion() + " starting!");
        
        this.checkDependencies();
        
        warmupTasks.clear();
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

        log("Plugin started!");
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            pmanager.removeFromFFA(p, true);
        }
        log("Plugin stopped safely!");
        saveConfig();
    }
    
    /**
     * Sets up commands
     * @return 
     */
    public void setupCommands() {
        FFACommandHandler handler = new FFACommandHandler(this);
        this.getCommand("ffa").setExecutor(handler);
        //Then we register the rest
        handler.registerCommand("join", new CommandJoin(this));
        handler.registerCommand("enable", new CommandEnable(this));
        handler.registerCommand("reload", new CommandReload(this));
        handler.registerCommand("define", new CommandDefine(this));
        handler.registerCommand("setspawn", new CommandSetSpawn(this));
        handler.registerCommand("removespawn", new CommandRemoveSpawn(this));
    }

    /**
     * Checks that all the needed dependencies are installed on the server
     */
    public void checkDependencies() {
        PluginManager pm = this.getServer().getPluginManager();
        for (Iterator<String> it = this.getDescription().getSoftDepend().iterator(); it.hasNext();) {
            String plugin = it.next();
            if (pm.getPlugin(plugin) == null) {
                this.log("Could not find dependencie " + plugin + " plugin shutting down!");
                this.shutdown();
            } else {
                this.log("Registered Dependencie " + plugin + "!");
            }
        }
    }

    /**
     * Vault method to check that we have an economy plugin installed on the server
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
        Bukkit.getServer().getPluginManager().disablePlugin(this);
    }
    
    /**
     * Returns the version of the plugin
     * @return 
     */
    public String getVersion() {
        return this.getDescription().getVersion();
    }
    
    /**
     * Logs a message to the server log/console
     * @param msg 
     */
    public void log(String msg) {
        this.getLogger().info(msg);
    }
}
