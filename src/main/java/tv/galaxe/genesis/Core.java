package tv.galaxe.genesis;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tv.galaxe.genesis.event.enforcer.Enderman;

public final class Core extends JavaPlugin implements Listener {
	private static Plugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getConsoleSender().sendMessage("Genesis Plugin Enabled");
		saveDefaultConfig();

		// Commands
		// getCommand("genesis").setExecutor(new GenesisGUI());

		// Genus Enforcers
		getServer().getPluginManager().registerEvents(new Skeleton(), this);
		getServer().getPluginManager().registerEvents(new Enderman(), this);
	}

	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("Genesis Plugin Disabled");
	}

	public static Plugin getInstance() {
		return plugin;
	}
}
