package tv.galaxe.genesis;

import net.luckperms.api.LuckPerms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tv.galaxe.genesis.cmd.SelectGUI;
import tv.galaxe.genesis.event.enforcer.Axolotl;
import tv.galaxe.genesis.event.enforcer.Enderman;
import tv.galaxe.genesis.event.enforcer.Phantom;
import tv.galaxe.genesis.event.enforcer.Sculk;
import tv.galaxe.genesis.event.enforcer.Shulker;
import tv.galaxe.genesis.event.enforcer.Skeleton;

public final class Core extends JavaPlugin implements Listener {
	public static LuckPerms lp;
	public static Plugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getConsoleSender().sendMessage("Genesis Plugin Enabled");
		saveDefaultConfig();

		// LuckPerms
		lp = getServer().getServicesManager().load(LuckPerms.class);

		// Commands
		getCommand("genesis").setExecutor(new SelectGUI());
		getCommand("genesisreload").setExecutor(new CommandExecutor() {
			public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
				reloadConfig();
				return true;
			}
		});

		// Class Enforcers
		getServer().getPluginManager().registerEvents(new Skeleton(), this);
		getServer().getPluginManager().registerEvents(new Enderman(), this);
		getServer().getPluginManager().registerEvents(new Phantom(), this);
		getServer().getPluginManager().registerEvents(new Axolotl(), this);
		getServer().getPluginManager().registerEvents(new Sculk(), this);
		getServer().getPluginManager().registerEvents(new Shulker(), this);
	}

	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("Genesis Plugin Disabled");
	}
}
