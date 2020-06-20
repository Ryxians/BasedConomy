package homebrew.basictrades;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Permission;
import java.util.logging.Level;

public class ConfigManagement {
    //Things that aren't changed
    private BasicTrades plugin = BasicTrades.instance;

    //Things that are
    private File configFile;
    private FileConfiguration fileConfig;

    public ConfigManagement(String fileName) {
        configFile = new File(plugin.getDataFolder(), fileName);
    }

    public ConfigManagement(File configFile) {
        this.configFile = configFile;
    }

    public void reloadConfig() {
        fileConfig = YamlConfiguration.loadConfiguration(configFile);

        //Look for defaults
        InputStream defConfigStream = plugin.getResource(configFile.getName());
        if (defConfigStream != null) {
            YamlConfiguration def = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            fileConfig.setDefaults(def);
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfig == null) {
            reloadConfig();
        }
        return fileConfig;
    }

    public void saveConfig() {
        if (fileConfig != null && configFile != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            this.plugin.saveResource(configFile.getName(), false);
        }
    }

    public File getFile() {
        return configFile;
    }
}
