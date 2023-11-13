package tv.galaxe.genesis.event.enforcer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.SkeletonRunnable;

public final class Skeleton implements Listener {
	private Plugin plugin = Core.getInstance();
	private Map<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.skeleton")) {
			taskMap.put(event.getPlayer(), plugin.getServer().getScheduler().runTaskTimer(plugin,
					new SkeletonRunnable((Player) event.getPlayer()), 0, 20));
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.skeleton")) {
			plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}
}