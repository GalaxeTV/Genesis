package tv.galaxe.genesis.event.enforcer;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.HashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.runnable.EndermanRunnable;

public final class Enderman implements Listener {
	private static HashMap<Player, BukkitTask> taskMap = new HashMap<Player, BukkitTask>();
	private static HashMap<Player, Integer> cooldownMap = new HashMap<Player, Integer>();
	public static RegionQuery endermanQuery;

	public Enderman() {
		endermanQuery = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
	}

	public static void newUser(Player player) {
		taskMap.put(player,
				Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin, new EndermanRunnable(player), 0, 20));
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
				.setBaseValue(Core.plugin.getConfig().getDouble("classes.enderman.max-health"));
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.enderman")) {
			taskMap.put(event.getPlayer(), Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new EndermanRunnable((Player) event.getPlayer()), 0, 20));
			event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)
					.setBaseValue(Core.plugin.getConfig().getDouble("classes.enderman.max-health"));
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.enderman")) {
			Core.plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}

	@EventHandler
	public void actionKey(PlayerSwapHandItemsEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.enderman")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			int currentTick = event.getPlayer().getServer().getCurrentTick();
			event.setCancelled(true);
			if (cooldownMap.getOrDefault(event.getPlayer(), currentTick) <= currentTick) {
				event.getPlayer().launchProjectile(EnderPearl.class);
				cooldownMap.put(event.getPlayer(), event.getPlayer().getServer().getCurrentTick()
						+ Core.plugin.getConfig().getInt("classes.enderman.ability-cooldown"));
			} else {
				event.getPlayer().sendActionBar(Component.text("You can use this ability in ")
						.append(Component.text(String.format("%.1f",
								(cooldownMap.get(event.getPlayer()) - event.getPlayer().getServer().getCurrentTick())
										/ 20.0)))
						.append(Component.text(" seconds!")));
			}
		}
	}

	@EventHandler
	public void onEnderPearl(PlayerTeleportEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.enderman")
				&& event.getCause() == TeleportCause.ENDER_PEARL) {
			event.setCancelled(true);
			event.getPlayer().teleport(event.getTo());
			event.getPlayer().getWorld().playSound(((Entity) event.getPlayer()), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F,
					1.0F);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getPlayer().hasPermission("genesis.classes.enderman")
				&& event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			ItemStack activeItem = (!event.getPlayer().getInventory().getItemInMainHand().isEmpty())
					? event.getPlayer().getInventory().getItemInMainHand().clone()
					: new ItemStack(Material.STICK);
			activeItem.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
			Location loc = event.getBlock().getLocation();
			event.setDropItems(false);
			event.setExpToDrop(0);
			event.getBlock().getDrops(activeItem).forEach((item) -> {
				event.getBlock().getWorld().dropItemNaturally(loc, item);
			});
		}
	}

	@EventHandler
	public void onMobTarget(EntityTargetLivingEntityEvent event) {
		if (event.getTarget() != null && event.getTarget().hasPermission("genesis.classes.enderman")
				&& (event.getEntity() instanceof org.bukkit.entity.Enderman
						|| event.getEntity() instanceof Endermite)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onShootBowDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof AbstractArrow // Prevent errors by checking if the Damager is an AbstractArrow
				&& ((Entity) ((AbstractArrow) event.getDamager()).getShooter())
						.hasPermission("genesis.classes.enderman")
				&& ((Player) ((AbstractArrow) event.getDamager()).getShooter()).getGameMode()
						.equals(GameMode.SURVIVAL)) {
			event.setDamage(event.getDamage() - Core.plugin.getConfig().getDouble("classes.enderman.ranged-debuff"));
		}
	}
}
