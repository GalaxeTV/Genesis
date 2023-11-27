package tv.galaxe.genesis.cmd;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import tv.galaxe.genesis.Core;
import tv.galaxe.genesis.event.enforcer.Axolotl;
import tv.galaxe.genesis.event.enforcer.Enderman;
import tv.galaxe.genesis.event.enforcer.Phantom;
import tv.galaxe.genesis.event.enforcer.Sculk;
import tv.galaxe.genesis.event.enforcer.Shulker;
import tv.galaxe.genesis.event.enforcer.Skeleton;

public class SelectGUI implements CommandExecutor {
	private static Gui selectGui;
	private static Gui reviewGui;
	private static Player player;

	public SelectGUI() {
		selectGui = Gui.gui().title(Component.text("Class Selection")).rows(3).create();
		reviewGui = Gui.gui().title(Component.text("Review Selection")).rows(3).create();
		PermissionNode endermanNode = PermissionNode.builder("genesis.classes.enderman").negated(false).build();
		PermissionNode skeletonNode = PermissionNode.builder("genesis.classes.skeleton").negated(false).build();
		PermissionNode phantomNode = PermissionNode.builder("genesis.classes.phantom").negated(false).build();
		PermissionNode axolotlNode = PermissionNode.builder("genesis.classes.axolotl").negated(false).build();
		PermissionNode sculkNode = PermissionNode.builder("genesis.classes.sculk").negated(false).build();
		PermissionNode shulkerNode = PermissionNode.builder("genesis.classes.shulker").negated(false).build();
		PermissionNode disableGuiNode = PermissionNode.builder("genesis.gui").negated(true).build();

		selectGui.setDefaultClickAction(event -> {
			event.setCancelled(true);
		});
		reviewGui.setDefaultClickAction(event -> {
			event.setCancelled(true);
		});
		ItemStack bg = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta bgMeta = bg.getItemMeta();
		bgMeta.displayName(Component.text(""));
		bg.setItemMeta(bgMeta);
		selectGui.getFiller().fill(new GuiItem(bg));
		reviewGui.getFiller().fill(new GuiItem(bg));

		// Tooltip
		ItemStack review = new ItemStack(Material.NETHER_STAR);
		ItemMeta reviewMeta = review.getItemMeta();
		reviewMeta.displayName(Component.text("Select a Class!").decoration(TextDecoration.ITALIC, false));
		review.setItemMeta(reviewMeta);
		selectGui.setItem(2, 5, new GuiItem(review));

		// Cancel Button
		ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.displayName(
				Component.text("Cancel").decoration(TextDecoration.ITALIC, false).color(TextColor.color(255, 0, 0)));
		cancel.setItemMeta(cancelMeta);

		// Confirm Button
		ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta confirmMeta = confirm.getItemMeta();
		confirmMeta.displayName(
				Component.text("Confirm").decoration(TextDecoration.ITALIC, false).color(TextColor.color(0, 255, 0)));
		confirm.setItemMeta(confirmMeta);

		// Skeleton
		ItemStack skeleton = new ItemStack(Material.SKELETON_SKULL);
		ItemMeta skeletonMeta = skeleton.getItemMeta();
		skeletonMeta.displayName(Component.text("Skeleton Class").decoration(TextDecoration.ITALIC, false));
		skeleton.setItemMeta(skeletonMeta);
		selectGui.setItem(2, 1, new GuiItem(skeleton, event -> {
			reviewGui.setItem(1, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(2, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(3, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(1, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(skeletonNode);
				});
				Skeleton.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(2, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(skeletonNode);
				});
				Skeleton.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(3, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(skeletonNode);
				});
				Skeleton.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.open(event.getWhoClicked());
		}));

		// Enderman
		ItemStack enderman = createHead("7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf");
		ItemMeta endermanMeta = enderman.getItemMeta();
		endermanMeta.displayName(Component.text("Enderman Class").decoration(TextDecoration.ITALIC, false));
		enderman.setItemMeta(endermanMeta);
		selectGui.setItem(1, 2, new GuiItem(enderman, event -> {
			reviewGui.setItem(1, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(2, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(3, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(1, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(endermanNode);
				});
				Enderman.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(2, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(endermanNode);
				});
				Enderman.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(3, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(endermanNode);
				});
				Enderman.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.open(event.getWhoClicked());
		}));

		// Axolotl
		ItemStack axolotl = createHead("d704254139a0b1a926e7552482dd67679c6ae0dc8335c980dbd1c0d99634a708");
		ItemMeta axolotlMeta = axolotl.getItemMeta();
		axolotlMeta.displayName(Component.text("Axolotl Class").decoration(TextDecoration.ITALIC, false));
		axolotl.setItemMeta(axolotlMeta);
		selectGui.setItem(2, 3, new GuiItem(axolotl, event -> {
			reviewGui.setItem(1, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(2, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(3, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(1, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(axolotlNode);
				});
				Axolotl.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(2, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(axolotlNode);
				});
				Axolotl.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(3, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(axolotlNode);
				});
				Axolotl.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.open(event.getWhoClicked());
		}));

		// Sculk
		ItemStack sculk = createHead("bc9c84349742164a22971ee54516fff91d868da72cdcce62069db128c42154b2");
		ItemMeta sculkMeta = sculk.getItemMeta();
		sculkMeta.displayName(Component.text("Sculk Class").decoration(TextDecoration.ITALIC, false));
		sculk.setItemMeta(sculkMeta);
		selectGui.setItem(1, 4, new GuiItem(sculk, event -> {
			reviewGui.setItem(1, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(2, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(3, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(1, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(sculkNode);
				});
				Sculk.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(2, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(sculkNode);
				});
				Sculk.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(3, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(sculkNode);
				});
				Sculk.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.open(event.getWhoClicked());
		}));

		// Shulker
		ItemStack shulker = createHead("1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44");
		ItemMeta shulkerMeta = shulker.getItemMeta();
		shulkerMeta.displayName(Component.text("Shulker Class").decoration(TextDecoration.ITALIC, false));
		shulker.setItemMeta(shulkerMeta);
		selectGui.setItem(3, 6, new GuiItem(shulker, event -> {
			reviewGui.setItem(1, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(2, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(3, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(1, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(shulkerNode);
				});
				Shulker.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(2, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(shulkerNode);
				});
				Shulker.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(3, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(shulkerNode);
				});
				Shulker.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.open(event.getWhoClicked());
		}));

		// Strider
		// selectGui.setItem(2, 7,
		// new
		// GuiItem(createHead("a13cb566124aed3b5d86bfaf1d1b01f69526645622ed8510aa86a66d57096fe4")));

		// Phantom
		ItemStack phantom = createHead("7e95153ec23284b283f00d19d29756f244313a061b70ac03b97d236ee57bd982");
		ItemMeta phantomMeta = phantom.getItemMeta();
		phantomMeta.displayName(Component.text("Phantom Class").decoration(TextDecoration.ITALIC, false));
		phantom.setItemMeta(phantomMeta);
		selectGui.setItem(3, 8, new GuiItem(phantom, event -> {
			reviewGui.setItem(1, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(2, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(3, 9, new GuiItem(cancel, cancelEvent -> {
				selectGui.open(event.getWhoClicked());
			}));
			reviewGui.setItem(1, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(phantomNode);
				});
				Phantom.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(2, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(phantomNode);
				});
				Phantom.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.setItem(3, 1, new GuiItem(confirm, confirmEvent -> {
				reviewGui.close(confirmEvent.getWhoClicked());
				Core.lp.getUserManager().modifyUser(confirmEvent.getWhoClicked().getUniqueId(), (User user) -> {
					user.data().add(disableGuiNode);
					user.data().add(phantomNode);
				});
				Phantom.newUser(((Player) confirmEvent.getWhoClicked()));
			}));
			reviewGui.open(event.getWhoClicked());
		}));

		// Piglin
		// selectGui.setItem(2, 9, new GuiItem(Material.PIGLIN_HEAD));

		// To be announced
		ItemStack TBD = createHead("d5d20330da59c207d78352838e91a48ea1e42b45a9893226144b251fe9b9d535");
		ItemMeta TBDMeta = TBD.getItemMeta();
		TBDMeta.displayName(Component.text("To Be Announced").decoration(TextDecoration.ITALIC, false));
		TBD.setItemMeta(TBDMeta);
		selectGui.setItem(2, 9, new GuiItem(TBD));
		selectGui.setItem(2, 7, new GuiItem(TBD));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be used as a player!");
			return true;
		}
		player = (Player) sender;
		selectGui.open(player);
		return true;
	}

	public static ItemStack createHead(String TextureID) {
		PlayerProfile headProfile = Core.plugin.getServer().createProfile(UUID.randomUUID());
		PlayerTextures headTextures = headProfile.getTextures();
		try {
			headTextures.setSkin(new URL("https://textures.minecraft.net/texture/" + TextureID));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		headProfile.setTextures(headTextures);
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = ((SkullMeta) skull.getItemMeta());
		meta.setPlayerProfile(headProfile);
		skull.setItemMeta(meta);
		return skull;
	}
}
