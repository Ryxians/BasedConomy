package homebrew.basictrades.hit;

import homebrew.basictrades.tools.ConfigManagement;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class HitE extends HitA {

    public HitE(OfflinePlayer bountyOwner, OfflinePlayer bounty, Inventory inventory) {
        this.owner = bountyOwner.getUniqueId();
        this.bounty = bounty.getUniqueId();
        this.prize = inventory;
    }

    public HitE(File loadHit) {
        super(loadHit);
    }

    @Override
    public void saveHit() {
        ConfigManagement configMan = new ConfigManagement("Expired" + File.separator + owner + ".yml");
        FileConfiguration savedHit = configMan.getConfig();

        //Save owner
        if (owner != null) {
            savedHit.set("Owner", owner.toString());
        }

        //Save Inventory
        ItemStack[] contents = prize.getContents();
        for (int i = 0; i < contents.length; i++) {
            savedHit.set("Inventory." + String.valueOf(i), contents[i]);
        }
        configMan.saveConfig();
    }
}
