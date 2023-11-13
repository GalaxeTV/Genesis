package tv.galaxe.genesis.runnable;

import org.bukkit.entity.Player;

public class SkeletonRunnable implements Runnable {
	private Player player;

	public SkeletonRunnable(Player p) {
		this.player = p;
	}

	@Override
	public void run() {
		player.sendMessage("Your Runnable is active!");
		player.getServer().getLogger().info(player.getName() + "'s Runnable is active!");
	}
}
