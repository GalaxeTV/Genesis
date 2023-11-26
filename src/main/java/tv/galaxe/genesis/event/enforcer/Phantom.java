package tv.galaxe.genesis.event.enforcer;

import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitTask;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.PhantomRunnable;

public class Phantom implements Listener {
	private static HashMap<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.phantom")) {
			taskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new PhantomRunnable((Player) event.getPlayer()), 20, 20));
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)
					.setBaseValue(Core.plugin.getConfig().getDouble("classes.phantom.max-health"));
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.phantom")) {
			Core.plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}

	@EventHandler
	public void actionKey(PlayerSwapHandItemsEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.phantom")) {
			event.setCancelled(true);
			event.getPlayer().setGliding(!event.getPlayer().isGliding());
		}
	}

	@EventHandler
	public void glide(EntityToggleGlideEvent event) {
		if (event.getEntity().hasPermission("genesis.classes.phantom")
				&& ((Player) event.getEntity()).getGameMode().equals(GameMode.SURVIVAL)
				&& event.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR
				&& (((Player) event.getEntity()).getInventory().getChestplate() == null
						|| ((Player) event.getEntity()).getInventory().getChestplate().getType() != Material.ELYTRA)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onSleep(PlayerBedEnterEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.phantom")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			event.setCancelled(true);
			event.setUseBed(Event.Result.DENY);
			event.getPlayer().sendMessage("Phantoms can't sleep!");
		}
	}

	@EventHandler
	public void onPhantomTarget(EntityTargetLivingEntityEvent event) {
		if (event.getTarget() != null && event.getTarget().hasPermission("genesis.classes.phantom")
				&& event.getEntity() instanceof org.bukkit.entity.Phantom) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFallDamage(EntityDamageEvent event) {
		if (event.getEntity().hasPermission("genesis.classes.phantom") && event.getCause().equals(DamageCause.FALL)) {
			event.setCancelled(true);
			event.setDamage(
					event.getDamage() * Core.plugin.getConfig().getDouble("classes.phantom.fall-damage-modifier"));
		}
	}
}
