package homebrew.basictrades.hit;

import homebrew.basictrades.tools.ConfigManagement;
import homebrew.basictrades.tools.HitTools;
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
        this.owner = HitTools.fileToUUID(loadHit.getName());
    }

    @Override
    public void saveHit() {
        saveHit(0);
    }
    public void saveHit(int count) {
        ConfigManagement configMan = new ConfigManagement("Expired" + File.separator + owner + count + ".yml");
        FileConfiguration savedHit = configMan.getConfig();


        savedHit.set("Bounty", bounty.toString());

        //Save Inventory
        ItemStack[] contents = prize.getContents();
        for (int i = 0; i < contents.length; i++) {
            savedHit.set("Inventory." + String.valueOf(i), contents[i]);
        }
        configMan.saveConfig();
    }

    @Override
    protected void makeTask(long delay) {

    }
}
