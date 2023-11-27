package tv.galaxe.genesis.runnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Enderman;

public final class EndermanRunnable implements Runnable {
	private Player player;

	public EndermanRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		ApplicableRegionSet set = Enderman.endermanQuery
				.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
		if (player.getGameMode().equals(GameMode.SURVIVAL)
				&& set.testState(WorldGuardPlugin.inst().wrapPlayer(player), Core.GENESIS_EFFECTS)
				&& ((player.isInRain() && player.getEquipment().getHelmet() == null) || player.isInWater())) {
			player.damage(Core.plugin.getConfig().getDouble("classes.enderman.water-damage"));
		}
	}
}
