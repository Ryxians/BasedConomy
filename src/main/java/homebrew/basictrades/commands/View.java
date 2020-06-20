package homebrew.basictrades.commands;

import homebrew.basictrades.BasicTrades;
import homebrew.basictrades.HitO;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

public class View implements CommandExecutor {

    public static Map<UUID, HitO> hits = BasicTrades.hits;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                UUID hitUsr = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
                boolean rc = hits.containsKey(hitUsr);
                if (rc) {
                    Inventory view = hits.get(hitUsr).prize;
                    ((Player) sender).openInventory(view);
                } else {
                    BasicTrades.fail(sender, "There is not an existing hit for this player.");
                }
            } else {
                ((Player)sender).openInventory(BasicTrades.hitsMenu);
            }
        }
        return true;
    }
}
