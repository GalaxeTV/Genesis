package tv.galaxe.genesis.runnable;

import org.bukkit.entity.Player;

public class SkeletonRunnable implements Runnable {
	private final Player player;

	public SkeletonRunnable(Player p) {
		this.player = p;
	}

	@Override
	public void run() {
		// TODO: Checks to perform while player is online
	}
}
