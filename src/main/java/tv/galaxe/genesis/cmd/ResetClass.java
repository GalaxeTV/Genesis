package tv.galaxe.genesis.cmd;

import net.kyori.adventure.text.Component;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Human;

// TODO: Rework this class to not have to kick the player, also investigate the LuckPerms API to see if there's a better way to do this.

public class ResetClass implements CommandExecutor {
	PermissionNode disableEndermanNode, disableSkeletonNode, disablePhantomNode, disableAxolotlNode, disableSculkNode,
			disableShulkerNode, enableGuiNode;

	public ResetClass() {
		disableEndermanNode = PermissionNode.builder("genesis.classes.enderman").negated(true).build();
		disableSkeletonNode = PermissionNode.builder("genesis.classes.skeleton").negated(true).build();
		disablePhantomNode = PermissionNode.builder("genesis.classes.phantom").negated(true).build();
		disableAxolotlNode = PermissionNode.builder("genesis.classes.axolotl").negated(true).build();
		disableSculkNode = PermissionNode.builder("genesis.classes.sculk").negated(true).build();
		disableShulkerNode = PermissionNode.builder("genesis.classes.shulker").negated(true).build();
		enableGuiNode = PermissionNode.builder("genesis.gui").negated(false).build();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be used as a player!");
			return true;
		}

		if (args.length == 0 || !args[0].equalsIgnoreCase("confirm")) {
			sender.sendMessage(
					"This command will kick you from the server to finalize the changes. Please confirm this by running /greset confirm");
			return true;
		}

		Core.lp.getUserManager().modifyUser(((Player) sender).getUniqueId(), (User user) -> {
			user.data().add(disableEndermanNode);
			user.data().add(disableSkeletonNode);
			user.data().add(disablePhantomNode);
			user.data().add(disableAxolotlNode);
			user.data().add(disableSculkNode);
			user.data().add(disableShulkerNode);
			user.data().add(enableGuiNode);
		});
		Human.newUser(((Player) sender));

		((Player) sender).kick(Component.text("Please re-connect to finalize resetting your class back to default!"));

		return true;
	}
}
