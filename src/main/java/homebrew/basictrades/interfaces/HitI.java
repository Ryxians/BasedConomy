package homebrew.basictrades.interfaces;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface HitI {
    public OfflinePlayer getOwner();

    public UUID getOwnerUUID();

    public String getOwnerName();

    public OfflinePlayer getBounty();

    public UUID getBountyUUID();

    public String getBountyName();

    public ItemStack getSkull();

    public ItemStack getChest();

    public void saveHit();
}
