package tv.galaxe.genesis.event.enforcer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
            event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(16.0);
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.skeleton")) {
			plugin.getServer().getScheduler().cancelTask(taskMap.get(event.getPlayer()).getTaskId());
		}
	}

	@EventHandler
	public void onDrinkMilk(PlayerItemConsumeEvent event) {
		if (event.getPlayer().hasPermission("genesis.genus.skeleton")
				&& event.getItem().getType() == Material.MILK_BUCKET) {
			event.setItem(new ItemStack(Material.BUCKET));
			event.getPlayer().clearActivePotionEffects();
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
		}
	}
}