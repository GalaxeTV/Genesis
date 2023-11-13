package tv.galaxe.genesis.runnable;

import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class SkeletonScheduler implements Consumer<BukkitTask> {
	private final Player player;

	public SkeletonScheduler(Player p) {
		this.player = p;
	}

	@Override
	public void accept(BukkitTask task) {
		// TODO: Checks to perform while player is online
	}
}
