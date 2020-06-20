package homebrew.basictrades.commands;

import homebrew.basictrades.BasicTrades;
import homebrew.basictrades.HitO;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

public class HitC implements CommandExecutor {
    BasicTrades plugin = BasicTrades.instance;
    Map<UUID, HitO> hits = plugin.hits;
    Map<Inventory, HitO> sHits = plugin.sHits;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player cmdSender = (Player)sender;
            switch (args.length) {
                case 0:
                    //When no player is specified
                    BasicTrades.fail(sender, "Please specify a player.");
                    break;
                case 1:
                    //When one person is specified
                    if (Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
                        OfflinePlayer hitUsr = Bukkit.getOfflinePlayer(args[0]);
                        boolean rc = !hits.containsKey(hitUsr);
                        if (rc) {
                            Inventory temp = cmdSender.openInventory(Bukkit.createInventory(cmdSender, 27, "Hit Price")).getTopInventory();
                            HitO hit = new HitO(null, hitUsr, temp);
                            sHits.put(temp, hit);
                        } else {
                            BasicTrades.fail(sender, "That player already has a hit >>:(");
                        }
                    } else {
                        BasicTrades.fail(sender, "That is not a player >:(");
                    }

                    break;
                default:
                    //when more than one person is specified
                    BasicTrades.fail(sender, "Excuse me, one hit at a time.");
                    break;
            }
        } else {
            BasicTrades.fail(sender, "This is a player command.");
        }
        return true;
    }
}
