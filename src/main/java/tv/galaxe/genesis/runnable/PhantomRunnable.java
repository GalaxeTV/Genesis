package tv.galaxe.genesis.runnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Phantom;

public class PhantomRunnable implements Runnable {
	private Player player;

	public PhantomRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		ApplicableRegionSet set = Phantom.phantomQuery.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
		if (player.getGameMode().equals(GameMode.SURVIVAL)
				&& set.testState(WorldGuardPlugin.inst().wrapPlayer(player), Core.GENESIS_EFFECTS)
				&& player.getWorld().isDayTime() && !(player.isInWaterOrRain())
				&& !(player.getLocation().getBlock().getLightFromSky() < 15)
				&& player.getEquipment().getHelmet() == null) {
			player.setFireTicks(Core.plugin.getConfig().getInt("classes.phantom.daylight-fire-ticks"));
		}
	}
}
