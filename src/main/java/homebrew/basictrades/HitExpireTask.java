package homebrew.basictrades;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HitExpireTask extends BukkitRunnable {
    private HitO hit;
    protected long delay;

    public HitExpireTask(HitO hit, long delay) {
        this.delay = delay;
        this.hit = hit;
        this.runTaskTimerAsynchronously(BasicTrades.instance, 0, 100);
    }

    @Override
    public void run() {
        if (delay < 100) {
            this.cancel();
            BasicTrades.hits.remove(hit.bounty);
            BasicTrades.success("Bounty on " + ChatColor.stripColor(hit.getBountyName()) + " has expired!");
            if (hit.owner != null) {
                BasicTrades.eHits.put(hit.owner, hit);
            }
            BasicTrades.save();
        } else {
            delay -= 100;
        }
    }
}
