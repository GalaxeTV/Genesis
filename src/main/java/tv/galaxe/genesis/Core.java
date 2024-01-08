package tv.galaxe.genesis;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import net.luckperms.api.LuckPerms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tv.galaxe.genesis.cmd.ResetClass;
import tv.galaxe.genesis.cmd.SelectGUI;
import tv.galaxe.genesis.event.enforcer.Axolotl;
import tv.galaxe.genesis.event.enforcer.Enderman;
import tv.galaxe.genesis.event.enforcer.Human;
import tv.galaxe.genesis.event.enforcer.Phantom;
import tv.galaxe.genesis.event.enforcer.Sculk;
import tv.galaxe.genesis.event.enforcer.Shulker;
import tv.galaxe.genesis.event.enforcer.Skeleton;

public final class Core extends JavaPlugin implements Listener {
	public static LuckPerms lp;
	public static Plugin plugin;
	public static ApplicableRegionSet disableSet;
	public static StateFlag GENESIS_EFFECTS;

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getConsoleSender().sendMessage("Genesis Plugin Enabled");
		saveDefaultConfig();

		// LuckPerms
		lp = getServer().getServicesManager().load(LuckPerms.class);

		// Commands
		getCommand("genesis").setExecutor(new SelectGUI());
		getCommand("genesisreset").setExecutor(new ResetClass());
		getCommand("genesisreload").setExecutor(new CommandExecutor() {
			public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
				reloadConfig();
				return true;
			}
		});

		// Class Enforcers
		getServer().getPluginManager().registerEvents(new Human(), this);
		getServer().getPluginManager().registerEvents(new Skeleton(), this);
		getServer().getPluginManager().registerEvents(new Enderman(), this);
		getServer().getPluginManager().registerEvents(new Phantom(), this);
		getServer().getPluginManager().registerEvents(new Axolotl(), this);
		getServer().getPluginManager().registerEvents(new Sculk(), this);
		getServer().getPluginManager().registerEvents(new Shulker(), this);
	}

	@Override
	public void onLoad() {
		// WorldGuard
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		try {
			StateFlag flag = new StateFlag("genesis-effects", true);
			registry.register(flag);
			GENESIS_EFFECTS = flag;
		} catch (FlagConflictException e) {
			Flag<?> existing = registry.get("genesis-effects");
			if (existing instanceof StateFlag) {
				GENESIS_EFFECTS = (StateFlag) existing;
			} else {
				// Fail silently shouldn't happen ... will make more robust in patch 1
			}
		}
	}

	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("Genesis Plugin Disabled");
	}
}
