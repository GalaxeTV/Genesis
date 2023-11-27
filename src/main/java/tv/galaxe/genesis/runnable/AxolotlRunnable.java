package tv.galaxe.genesis.runnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.entity.Player;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Axolotl;

public class AxolotlRunnable implements Runnable {
	private Player player;

	public AxolotlRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		ApplicableRegionSet set = Axolotl.axolotlQuery.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
		if (player.getRemainingAir() == 300 && !player.isInWaterOrRain()
				&& set.testState(WorldGuardPlugin.inst().wrapPlayer(player), Core.GENESIS_EFFECTS)) {
			player.damage(2.0);
		}
	}
}
