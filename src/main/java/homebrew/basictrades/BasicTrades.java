package homebrew.basictrades;

import homebrew.basictrades.hit.HitE;
import homebrew.basictrades.hit.HitO;
import homebrew.basictrades.commands.HitC;
import homebrew.basictrades.commands.View;
import homebrew.basictrades.tools.HitTools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public final class BasicTrades extends JavaPlugin {
    public static BasicTrades instance;

    public static Map<Inventory, HitO> sHits = new HashMap<Inventory, HitO>();

    public static Map<UUID, HitO> hits = new HashMap<UUID, HitO>();

    public static Map<UUID, List<HitE>> eHits = new HashMap<UUID, List<HitE>>();

    public static Inventory hitsMenu;

    public static File dataFolder;

    public FileConfiguration config = getConfig();


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        dataFolder = BasicTrades.instance.getDataFolder();

        //get config.yml
        config.options().copyDefaults(true);
        saveDefaultConfig();

        HitTools.loadHits();
        HitTools.hitsToMenu();
        HitTools.loadExpiredHits();

        //Load commands
        getCommand("hit").setExecutor(new HitC());
        getCommand("view").setExecutor(new View());

        //Load listener
        getServer().getPluginManager().registerEvents(new BasicListen(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HitTools.saveHits();
        HitTools.saveExpiredHits();
    }

}
