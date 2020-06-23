package homebrew.basictrades.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class Messages {
    public static void logInfo(String str) {
        Bukkit.getLogger().log(Level.INFO, str);
    }

    public static void success(CommandSender sender, String str) {
        sender.sendMessage(ChatColor.DARK_GREEN + "[BasedHits] " + ChatColor.GREEN + str);
    }

    public static void fail(CommandSender sender, String str) {
        sender.sendMessage(ChatColor.DARK_RED + "[BasedHits] " + ChatColor.RED + str);
    }

    public static void success(String str) {
        String st = (ChatColor.DARK_GREEN + "[BasedHits] " + ChatColor.GREEN + str);
        Bukkit.getOnlinePlayers().forEach(i ->
                {
                    if (i.hasPermission("BasedHits.broadcast")) {
                        i.sendMessage(st);
                    }
                }
        );
        logInfo(st);
    }
}
