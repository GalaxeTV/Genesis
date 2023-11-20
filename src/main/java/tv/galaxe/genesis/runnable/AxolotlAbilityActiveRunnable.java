package tv.galaxe.genesis.runnable;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Axolotl;

public class AxolotlAbilityActiveRunnable implements Runnable {
	private Player player;

	public AxolotlAbilityActiveRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		if (Axolotl.abilityMap.get(player) <= 1) {
			player.removePotionEffect(PotionEffectType.SLOW);
			player.removePotionEffect(PotionEffectType.REGENERATION);
			Axolotl.abilityInactiveTaskMap.put(player, Core.plugin.getServer().getScheduler().runTaskTimer(Core.plugin,
					new AxolotlAbilityInactiveRunnable(player), 1, 1));
			int taskID = Axolotl.abilityActiveTaskMap.get(player).getTaskId();
			Axolotl.abilityActiveTaskMap.remove(player);
			Core.plugin.getServer().getScheduler().cancelTask(taskID);
		}
		Axolotl.abilityMap.put(player, Axolotl.abilityMap.get(player) - 1);
		player.sendActionBar(Component.text("Ability Charge: " + Axolotl.abilityMap.get(player)));
	}
}