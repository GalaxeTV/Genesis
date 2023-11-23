package tv.galaxe.genesis.runnable;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ShulkerRunnable implements Runnable {
	private Player player;

	public ShulkerRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (player.getGameMode().equals(GameMode.SURVIVAL)) {

		}
	}
}
