package tv.galaxe.genesis.runnable;

import org.bukkit.entity.Player;

public class AxolotlRunnable implements Runnable {
	private Player player;

	public AxolotlRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (player.getRemainingAir() == 300 && !player.isInWaterOrRain()) {
			player.damage(2.0);
		}
	}
}
