package tv.galaxe.genesis.runnable;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class EndermanRunnable implements Runnable {
	private Player player;

	public EndermanRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (player.getGameMode().equals(GameMode.SURVIVAL)
				&& ((player.isInRain() && player.getEquipment().getHelmet() == null) || player.isInWater())) {
			player.damage(0.5);
		}
	}
}
