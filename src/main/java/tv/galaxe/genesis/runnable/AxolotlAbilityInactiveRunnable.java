package tv.galaxe.genesis.runnable;

import org.bukkit.entity.Player;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Axolotl;

public class AxolotlAbilityInactiveRunnable implements Runnable {
	private Player player;

	public AxolotlAbilityInactiveRunnable(Player p) {
		player = p;
	}

	@Override
	public void run() {
		// Check if at max ability points
		if (Axolotl.abilityMap.get(player) >= Core.plugin.getConfig().getInt("classes.axolotl.ability.cooldown") - 1) {
			// Hide boss bar
			player.hideBossBar(Axolotl.abilityBar.get(player));
			// Temporarily store taskID to cancel runnable after removing from map.
			int taskID = Axolotl.abilityInactiveTaskMap.get(player).getTaskId();
			Axolotl.abilityInactiveTaskMap.remove(player);
			Core.plugin.getServer().getScheduler().cancelTask(taskID);
		}

		// Increase charge and update bossbar
		Axolotl.abilityMap.put(player, Axolotl.abilityMap.get(player) + 1);
		Axolotl.abilityBar.get(player).progress((float) (Axolotl.abilityMap.get(player)
				/ Core.plugin.getConfig().getDouble("classes.axolotl.ability.cooldown")));
	}
}