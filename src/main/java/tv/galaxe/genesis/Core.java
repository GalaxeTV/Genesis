package tv.galaxe.genesis;

import java.net.MalformedURLException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tv.galaxe.genesis.cmd.SelectGUI;
import tv.galaxe.genesis.event.enforcer.Phantom;

public final class Core extends JavaPlugin implements Listener {
	public static Plugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getConsoleSender().sendMessage("Genesis Plugin Enabled");
		saveDefaultConfig();

		// Commands
		try {
			getCommand("genesis").setExecutor(new SelectGUI());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Genus Enforcers
		// getServer().getPluginManager().registerEvents(new Skeleton(), this);
		// getServer().getPluginManager().registerEvents(new Enderman(), this);
		getServer().getPluginManager().registerEvents(new Phantom(), this);
	}

	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("Genesis Plugin Disabled");
	}
}
