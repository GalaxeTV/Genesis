package tv.galaxe.genesis.event.enforcer;

import java.util.EnumSet;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.SculkRunnable;

public class Sculk implements Listener {
	private static HashMap<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();

	EnumSet<Material> nonVegetarian = EnumSet.of(Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_COD,
			Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_RABBIT, Material.COOKED_SALMON,
			Material.BEEF, Material.CHICKEN, Material.COD, Material.MUTTON, Material.PORKCHOP, Material.RABBIT,
			Material.SALMON, Material.ROTTEN_FLESH, Material.TROPICAL_FISH, Material.PUFFERFISH, Material.RABBIT_STEW);

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.sculk")) {
			taskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new SculkRunnable((Player) event.getPlayer()), 0, 20));
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)
					.setBaseValue(Core.plugin.getConfig().getDouble("classes.sculk.max-health"));
		}
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,
				PotionEffect.INFINITE_DURATION, 0, true, false, false));
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.sculk")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			Core.plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}

	@EventHandler
	public void actionKey(PlayerSwapHandItemsEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.sculk")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			event.setCancelled(true);
			double healthDiff = (Core.plugin.getConfig().getDouble("classes.sculk.max-health")
					- event.getPlayer().getHealth()) / 2.0;
			if (event.getPlayer().getLevel() > 0) {
				if (healthDiff >= 1.0) {
					event.getPlayer()
							.giveExpLevels(-Core.plugin.getConfig().getInt("classes.sculk.ability.level-cost"));
					event.getPlayer().setHealth(event.getPlayer().getHealth()
							+ Core.plugin.getConfig().getDouble("classes.sculk.ability.health-reward"));
					event.getPlayer().getWorld().playSound(((Entity) event.getPlayer()), Sound.BLOCK_ANVIL_LAND, 0.5F,
							1.5F);
				} else if (healthDiff != 0.0) {
					event.getPlayer()
							.giveExpLevels(-Core.plugin.getConfig().getInt("classes.sculk.ability.level-cost"));
					event.getPlayer().setHealth(Core.plugin.getConfig().getDouble("classes.sculk.max-health"));
					event.getPlayer().getWorld().playSound(((Entity) event.getPlayer()), Sound.BLOCK_ANVIL_LAND, 0.5F,
							1.5F);
				}
			}
		}
	}

	@EventHandler
	public void onMobTarget(EntityTargetLivingEntityEvent event) {
		if (event.getTarget() != null && event.getTarget().hasPermission("genesis.classes.sculk")
				&& event.getEntity() instanceof Warden) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onConsume(PlayerItemConsumeEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.sculk")
				&& nonVegetarian.contains(event.getItem().getType())) {
			event.setCancelled(true);
			event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
		}
	}
}
