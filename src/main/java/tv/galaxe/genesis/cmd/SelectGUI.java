package tv.galaxe.genesis.cmd;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionType;
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
	private static Gui reviewEnderman;
	private static Gui reviewSkeleton;
	private static Gui reviewPhantom;
	private static Gui reviewAxolotl;
	private static Gui reviewSculk;
	private static Gui reviewShulker;
	private static List<Gui> reviewGuis;

	public SelectGUI() {
		// Create GUI objects
		selectGui = Gui.gui().title(Component.text("Class Selection")).rows(3).create();
		reviewEnderman = Gui.gui().title(Component.text("Enderman Class")).rows(3).create();
		reviewSkeleton = Gui.gui().title(Component.text("Skeleton Class")).rows(3).create();
		reviewPhantom = Gui.gui().title(Component.text("Phantom Class")).rows(3).create();
		reviewAxolotl = Gui.gui().title(Component.text("Axolotl Class")).rows(3).create();
		reviewSculk = Gui.gui().title(Component.text("Sculk Class")).rows(3).create();
		reviewShulker = Gui.gui().title(Component.text("Shulker Class")).rows(3).create();
		reviewGuis = List.of(reviewEnderman, reviewSkeleton, reviewPhantom, reviewAxolotl, reviewSculk, reviewShulker);
		PermissionNode endermanNode = PermissionNode.builder("genesis.classes.enderman").negated(false).build();
		PermissionNode skeletonNode = PermissionNode.builder("genesis.classes.skeleton").negated(false).build();
		PermissionNode phantomNode = PermissionNode.builder("genesis.classes.phantom").negated(false).build();
		PermissionNode axolotlNode = PermissionNode.builder("genesis.classes.axolotl").negated(false).build();
		PermissionNode sculkNode = PermissionNode.builder("genesis.classes.sculk").negated(false).build();
		PermissionNode shulkerNode = PermissionNode.builder("genesis.classes.shulker").negated(false).build();
		PermissionNode disableGuiNode = PermissionNode.builder("genesis.gui").negated(true).build();

		// Default Clicks are Cancelled
		reviewGuis.forEach(gui -> {
			gui.setDefaultClickAction(event -> {
				event.setCancelled(true);
			});
		});
		selectGui.setDefaultClickAction(event -> {
			event.setCancelled(true);
		});

		// Set Backgrounds
		ItemStack bg = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta bgMeta = bg.getItemMeta();
		bgMeta.displayName(Component.text(""));
		bg.setItemMeta(bgMeta);
		GuiItem bgGuiItem = new GuiItem(bg);
		selectGui.getFiller().fill(bgGuiItem);
		reviewGuis.forEach(gui -> {
			gui.getFiller().fill(bgGuiItem);
		});

		// Set Cancel Buttons
		ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		cancelMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		cancelMeta.displayName(
				Component.text("Cancel").decoration(TextDecoration.ITALIC, false).color(TextColor.color(255, 0, 0)));
		cancel.setItemMeta(cancelMeta);
		GuiItem cancelGuiItem = new GuiItem(cancel, event -> {
			selectGui.open(event.getWhoClicked());
		});
		reviewGuis.forEach(gui -> {
			gui.setItem(1, 9, cancelGuiItem);
			gui.setItem(2, 9, cancelGuiItem);
			gui.setItem(3, 9, cancelGuiItem);
		});

		// Tooltip
		ItemStack review = new ItemStack(Material.NETHER_STAR);
		ItemMeta reviewMeta = review.getItemMeta();
		reviewMeta.displayName(Component.text("Select a Class!").decoration(TextDecoration.ITALIC, false));
		review.setItemMeta(reviewMeta);
		selectGui.setItem(2, 5, new GuiItem(review));

		// Confirm Button Template
		ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta confirmMeta = confirm.getItemMeta();
		confirmMeta.addEnchant(Enchantment.DURABILITY, 1, true);
		confirmMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		confirmMeta.displayName(
				Component.text("Confirm").decoration(TextDecoration.ITALIC, false).color(TextColor.color(0, 255, 0)));
		confirm.setItemMeta(confirmMeta);

		// Skeleton
		// Skeleton Head
		ItemStack skeleton = new ItemStack(Material.SKELETON_SKULL);
		ItemMeta skeletonMeta = skeleton.getItemMeta();
		skeletonMeta.displayName(Component.text("Skeleton Class").decoration(TextDecoration.ITALIC, false));
		skeleton.setItemMeta(skeletonMeta);
		GuiItem skeletonGuiItem = new GuiItem(skeleton, event -> {
			reviewSkeleton.open(event.getWhoClicked());
		});
		selectGui.setItem(2, 1, skeletonGuiItem);

		// Confirm Skeleton Gui Items
		GuiItem confirmSkeletonGuiItem = new GuiItem(confirm, event -> {
			reviewSkeleton.close(event.getWhoClicked());
			Core.lp.getUserManager().modifyUser(event.getWhoClicked().getUniqueId(), (User user) -> {
				user.data().add(disableGuiNode);
				user.data().add(skeletonNode);
			});
			Skeleton.newUser(((Player) event.getWhoClicked()));
		});
		reviewSkeleton.setItem(1, 1, confirmSkeletonGuiItem);
		reviewSkeleton.setItem(2, 1, confirmSkeletonGuiItem);
		reviewSkeleton.setItem(3, 1, confirmSkeletonGuiItem);

		// Brittle
		ItemStack brittle = new ItemStack(Material.BONE_MEAL);
		ItemMeta brittleMeta = brittle.getItemMeta();
		brittleMeta.displayName(Component.text("Brittle").decoration(TextDecoration.ITALIC, false));
		brittleMeta.lore(List.of(Component.text("-2 Max Hearts.").decoration(TextDecoration.ITALIC, false)));
		brittle.setItemMeta(brittleMeta);
		reviewSkeleton.setItem(2, 5, new GuiItem(brittle));

		// Undead
		ItemStack undead = new ItemStack(Material.SUNFLOWER);
		ItemMeta undeadMeta = undead.getItemMeta();
		undeadMeta.displayName(Component.text("Undead").decoration(TextDecoration.ITALIC, false));
		undeadMeta.lore(
				List.of(Component.text("Burn in sunlight without a helmet.").decoration(TextDecoration.ITALIC, false)));
		undead.setItemMeta(undeadMeta);
		reviewSkeleton.setItem(1, 4, new GuiItem(undead));

		// Co-Workers
		ItemStack coworkers = new ItemStack(Material.BONE);
		ItemMeta coworkersMeta = coworkers.getItemMeta();
		coworkersMeta.displayName(Component.text("Co-Workers").decoration(TextDecoration.ITALIC, false));
		coworkersMeta.lore(List
				.of(Component.text("All skeletons are now passive to you.").decoration(TextDecoration.ITALIC, false)));
		coworkers.setItemMeta(coworkersMeta);
		reviewSkeleton.setItem(1, 6, new GuiItem(coworkers));

		// All Bones
		ItemStack allBones = new ItemStack(Material.MILK_BUCKET);
		ItemMeta allBonesMeta = allBones.getItemMeta();
		allBonesMeta.displayName(Component.text("All Bones").decoration(TextDecoration.ITALIC, false));
		allBonesMeta.lore(List.of(
				Component.text("Drinking milk gives a regeneration effect.").decoration(TextDecoration.ITALIC, false),
				Component.text("+ Regeneration "
						+ (Core.plugin.getConfig().getInt("classes.skeleton.milk-buff.amplifier") + 1) + " for "
						+ (Core.plugin.getConfig().getInt("classes.skeleton.milk-buff.duration-ticks") / 20)
						+ " seconds.").decoration(TextDecoration.ITALIC, false)));
		allBones.setItemMeta(allBonesMeta);
		reviewSkeleton.setItem(3, 4, new GuiItem(allBones));

		// Sharpshooter
		ItemStack sharpshooter = new ItemStack(Material.BOW);
		ItemMeta sharpshooterMeta = sharpshooter.getItemMeta();
		sharpshooterMeta.displayName(Component.text("Sharpshooter").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		sharpshooterMeta.lore(List.of(
				Component.text("Gain proficiency with ranged weapons.").decoration(TextDecoration.ITALIC, false), 
				Component.text("+" + (((int) Core.plugin.getConfig().getDouble("classes.skeleton.arrow-buff") * 2))
						+ " attack damage with bows and crossbows.").decoration(TextDecoration.ITALIC, false)));
		// spotless:on
		sharpshooter.setItemMeta(sharpshooterMeta);
		reviewSkeleton.setItem(3, 6, new GuiItem(sharpshooter));

		// Enderman
		// Enderman Head
		ItemStack enderman = createHead("7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf");
		ItemMeta endermanMeta = enderman.getItemMeta();
		endermanMeta.displayName(Component.text("Enderman Class").decoration(TextDecoration.ITALIC, false));
		enderman.setItemMeta(endermanMeta);
		GuiItem endermanGuiItem = new GuiItem(enderman, event -> {
			reviewEnderman.open(event.getWhoClicked());
		});
		selectGui.setItem(1, 2, endermanGuiItem);

		// Confirm Enderman Gui Items
		GuiItem confirmEndermanGuiItem = new GuiItem(confirm, event -> {
			reviewEnderman.close(event.getWhoClicked());
			Core.lp.getUserManager().modifyUser(event.getWhoClicked().getUniqueId(), (User user) -> {
				user.data().add(disableGuiNode);
				user.data().add(endermanNode);
			});
			Enderman.newUser(((Player) event.getWhoClicked()));
		});
		reviewEnderman.setItem(1, 1, confirmEndermanGuiItem);
		reviewEnderman.setItem(2, 1, confirmEndermanGuiItem);
		reviewEnderman.setItem(3, 1, confirmEndermanGuiItem);

		// Eye of Ender
		ItemStack eyeOfEnder = new ItemStack(Material.ENDER_EYE);
		ItemMeta eyeOfEnderMeta = eyeOfEnder.getItemMeta();
		eyeOfEnderMeta.displayName(Component.text("Eye of Ender").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		eyeOfEnderMeta.lore(List.of(Component.text("Unlimited ender pearls.").decoration(TextDecoration.ITALIC, false),
									Component.text("Press your 'switch off hand item' key").color(TextColor.color(170, 170, 170)),
									Component.text("to activate this ability.").color(TextColor.color(170, 170, 170)),
									Component.text("By default this key is 'F.'").color(TextColor.color(170, 170, 170)),
									Component.text("Has a cooldown of "
										+ (Core.plugin.getConfig().getInt("classes.enderman.ability-cooldown")/20)
										+ " seconds.").color(TextColor.color(170, 170, 170))));
		// spotless:on
		eyeOfEnder.setItemMeta(eyeOfEnderMeta);
		reviewEnderman.setItem(2, 5, new GuiItem(eyeOfEnder));

		// Acid Rain
		ItemStack acidRain = new ItemStack(Material.TIPPED_ARROW);
		ItemMeta acidRainMeta = acidRain.getItemMeta();
		acidRainMeta.displayName(Component.text("Acid Rain").decoration(TextDecoration.ITALIC, false));
		acidRainMeta.lore(
				List.of(Component.text("Take damage from sources of water.").decoration(TextDecoration.ITALIC, false)));
		((PotionMeta) acidRainMeta).setBasePotionType(PotionType.AWKWARD);
		acidRainMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
		acidRain.setItemMeta(acidRainMeta);
		reviewEnderman.setItem(1, 4, new GuiItem(acidRain));

		// Gentle Hands
		ItemStack gentleHands = new ItemStack(Material.FEATHER);
		ItemMeta gentleHandsMeta = gentleHands.getItemMeta();
		gentleHandsMeta.displayName(Component.text("Gentle Hands").decoration(TextDecoration.ITALIC, false));
		gentleHandsMeta
				.lore(List.of(Component.text("Permanently have silk touch.").decoration(TextDecoration.ITALIC, false)));
		gentleHands.setItemMeta(gentleHandsMeta);
		reviewEnderman.setItem(1, 6, new GuiItem(gentleHands));

		// Cousins
		ItemStack cousins = new ItemStack(Material.ENDER_DRAGON_SPAWN_EGG);
		ItemMeta cousinsMeta = cousins.getItemMeta();
		cousinsMeta.displayName(Component.text("Cousins").decoration(TextDecoration.ITALIC, false));
		cousinsMeta.lore(List.of(
				Component.text("Enderman and Endermites are now passive.").decoration(TextDecoration.ITALIC, false)));
		cousins.setItemMeta(cousinsMeta);
		reviewEnderman.setItem(3, 4, new GuiItem(cousins));

		// Wrong Size
		ItemStack wrongSize = new ItemStack(Material.WARPED_FENCE);
		ItemMeta wrongSizeMeta = wrongSize.getItemMeta();
		wrongSizeMeta.displayName(Component.text("Wrong Size").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		wrongSizeMeta.lore(List.of(
				Component.text("Deal less damage with ranged weapons.").decoration(TextDecoration.ITALIC, false), 
				Component.text("-" + (((int) Core.plugin.getConfig().getDouble("classes.enderman.ranged-debuff") * 2))
						+ " attack damage with bows and crossbows.").decoration(TextDecoration.ITALIC, false)));
		// spotless:on
		wrongSize.setItemMeta(wrongSizeMeta);
		reviewEnderman.setItem(3, 6, new GuiItem(wrongSize));

		// Axolotl
		// Axolotl Head
		ItemStack axolotl = createHead("d704254139a0b1a926e7552482dd67679c6ae0dc8335c980dbd1c0d99634a708");
		ItemMeta axolotlMeta = axolotl.getItemMeta();
		axolotlMeta.displayName(Component.text("Axolotl Class").decoration(TextDecoration.ITALIC, false));
		axolotl.setItemMeta(axolotlMeta);
		GuiItem axolotlGuiItem = new GuiItem(axolotl, event -> {
			reviewAxolotl.open(event.getWhoClicked());
		});
		selectGui.setItem(2, 3, axolotlGuiItem);

		// Confirm Axolotl Gui Items
		GuiItem confirmAxolotlGuiItem = new GuiItem(confirm, event -> {
			reviewAxolotl.close(event.getWhoClicked());
			Core.lp.getUserManager().modifyUser(event.getWhoClicked().getUniqueId(), (User user) -> {
				user.data().add(disableGuiNode);
				user.data().add(axolotlNode);
			});
			Axolotl.newUser(((Player) event.getWhoClicked()));
		});
		reviewAxolotl.setItem(1, 1, confirmAxolotlGuiItem);
		reviewAxolotl.setItem(2, 1, confirmAxolotlGuiItem);
		reviewAxolotl.setItem(3, 1, confirmAxolotlGuiItem);

		// Gills
		ItemStack gills = new ItemStack(Material.AXOLOTL_BUCKET);
		ItemMeta gillsMeta = gills.getItemMeta();
		gillsMeta.displayName(Component.text("Gills").decoration(TextDecoration.ITALIC, false));
		gillsMeta.lore(List.of(Component.text("You air meter now functions as a water meter.")
				.decoration(TextDecoration.ITALIC, false)));
		gills.setItemMeta(gillsMeta);
		reviewAxolotl.setItem(2, 5, new GuiItem(gills));

		// Resilient
		ItemStack resilient = new ItemStack(Material.TURTLE_HELMET);
		ItemMeta resilientMeta = resilient.getItemMeta();
		resilientMeta.displayName(Component.text("Resilient").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		resilientMeta.lore(List.of(Component.text("Gain slowness and regeneration for a short time.").decoration(TextDecoration.ITALIC, false),
									Component.text("Press your 'switch off hand item' key").color(TextColor.color(170, 170, 170)),
									Component.text("to toggle this ability.").color(TextColor.color(170, 170, 170)),
									Component.text("By default this key is 'F.'").color(TextColor.color(170, 170, 170)),
									Component.text("Has a maximum duration of "
										+ (Core.plugin.getConfig().getInt("classes.axolotl.ability.charge")/20)
										+ " seconds before needing to recharge.").color(TextColor.color(170, 170, 170))));
		// spotless:on
		resilientMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		resilient.setItemMeta(resilientMeta);
		reviewAxolotl.setItem(1, 4, new GuiItem(resilient));

		// Natural Predator
		ItemStack naturalPredator = new ItemStack(Material.TRIDENT);
		ItemMeta naturalPredatorMeta = naturalPredator.getItemMeta();
		naturalPredatorMeta.displayName(Component.text("Natural Predator").decoration(TextDecoration.ITALIC, false));
		naturalPredatorMeta.lore(List.of(Component.text("Deal extra damage to all hostile aquatic mobs.")
				.decoration(TextDecoration.ITALIC, false)));
		naturalPredatorMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		naturalPredator.setItemMeta(naturalPredatorMeta);
		reviewAxolotl.setItem(1, 6, new GuiItem(naturalPredator));

		// Streamlined
		ItemStack streamlined = new ItemStack(Material.PRISMARINE_SHARD);
		ItemMeta streamlinedMeta = streamlined.getItemMeta();
		streamlinedMeta.displayName(Component.text("Streamlined").decoration(TextDecoration.ITALIC, false));
		streamlinedMeta.lore(
				List.of(Component.text("Gain permanent Dolphin's Grace.").decoration(TextDecoration.ITALIC, false)));
		streamlined.setItemMeta(streamlinedMeta);
		reviewAxolotl.setItem(3, 4, new GuiItem(streamlined));

		// Squishy
		ItemStack squishy = new ItemStack(Material.HEARTBREAK_POTTERY_SHERD);
		ItemMeta squishyMeta = squishy.getItemMeta();
		squishyMeta.displayName(Component.text("Squishy").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		squishyMeta.lore(List.of(
				Component.text("You have 8 hearts instead of 10.").decoration(TextDecoration.ITALIC, false)));
		// spotless:on
		squishy.setItemMeta(squishyMeta);
		reviewAxolotl.setItem(3, 6, new GuiItem(squishy));

		// Sculk
		// Sculk Head
		ItemStack sculk = createHead("bc9c84349742164a22971ee54516fff91d868da72cdcce62069db128c42154b2");
		ItemMeta sculkMeta = sculk.getItemMeta();
		sculkMeta.displayName(Component.text("Sculk Class").decoration(TextDecoration.ITALIC, false));
		sculk.setItemMeta(sculkMeta);
		GuiItem sculkGuiItem = new GuiItem(sculk, event -> {
			reviewSculk.open(event.getWhoClicked());
		});
		selectGui.setItem(1, 4, sculkGuiItem);

		// Confirm Sculk Gui Items
		GuiItem confirmSculkGuiItem = new GuiItem(confirm, event -> {
			reviewSculk.close(event.getWhoClicked());
			Core.lp.getUserManager().modifyUser(event.getWhoClicked().getUniqueId(), (User user) -> {
				user.data().add(disableGuiNode);
				user.data().add(sculkNode);
			});
			Sculk.newUser(((Player) event.getWhoClicked()));
		});
		reviewSculk.setItem(1, 1, confirmSculkGuiItem);
		reviewSculk.setItem(2, 1, confirmSculkGuiItem);
		reviewSculk.setItem(3, 1, confirmSculkGuiItem);

		// Nimble
		ItemStack nimble = new ItemStack(Material.FIREWORK_ROCKET);
		ItemMeta nimbleMeta = nimble.getItemMeta();
		nimbleMeta.displayName(Component.text("Nimble").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		nimbleMeta.lore(List.of(Component.text("Permanent Swiftsneak III.").decoration(TextDecoration.ITALIC, false),
									Component.text("Stacks with the Swiftsneak enchant.").color(TextColor.color(170, 170, 170))));
		// spotless:on
		nimbleMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
		nimble.setItemMeta(nimbleMeta);
		reviewSculk.setItem(2, 5, new GuiItem(nimble));

		// Parasitic
		ItemStack parasitic = new ItemStack(Material.EXPERIENCE_BOTTLE);
		ItemMeta parasiticMeta = parasitic.getItemMeta();
		parasiticMeta.displayName(Component.text("Parasitic").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		parasiticMeta.lore(List.of(Component.text("Exchange 1 level for 1 heart.").decoration(TextDecoration.ITALIC, false),
									Component.text("Press your 'switch off hand item' key").color(TextColor.color(170, 170, 170)),
									Component.text("to activate this ability.").color(TextColor.color(170, 170, 170)),
									Component.text("By default this key is 'F.'").color(TextColor.color(170, 170, 170))));
		// spotless:on
		parasitic.setItemMeta(parasiticMeta);
		reviewSculk.setItem(1, 4, new GuiItem(parasitic));

		// Like Family
		ItemStack likeFamily = new ItemStack(Material.SCULK_SENSOR);
		ItemMeta likeFamilyMeta = likeFamily.getItemMeta();
		likeFamilyMeta.displayName(Component.text("Like Family").decoration(TextDecoration.ITALIC, false));
		likeFamilyMeta.lore(
				List.of(Component.text("The Warden is passive to you.").decoration(TextDecoration.ITALIC, false)));
		likeFamily.setItemMeta(likeFamilyMeta);
		reviewSculk.setItem(1, 6, new GuiItem(likeFamily));

		// Low Lighting
		ItemStack lowLighting = new ItemStack(Material.DAMAGED_ANVIL);
		ItemMeta lowLightingMeta = lowLighting.getItemMeta();
		lowLightingMeta.displayName(Component.text("Low Lighting").decoration(TextDecoration.ITALIC, false));
		lowLightingMeta.lore(List.of(Component.text("Gain mining fatigue and slowness under direct sunlight.")
				.decoration(TextDecoration.ITALIC, false)));
		lowLighting.setItemMeta(lowLightingMeta);
		reviewSculk.setItem(3, 4, new GuiItem(lowLighting));

		// Plant Food
		ItemStack plantFood = new ItemStack(Material.KELP);
		ItemMeta plantFoodMeta = plantFood.getItemMeta();
		plantFoodMeta.displayName(Component.text("Plant Food").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		plantFoodMeta.lore(List.of(
				Component.text("Not able to eat meat.").decoration(TextDecoration.ITALIC, false), 
				Component.text("Includes not being able to eat fish.").color(TextColor.color(170, 170, 170))));
		// spotless:on
		plantFood.setItemMeta(plantFoodMeta);
		reviewSculk.setItem(3, 6, new GuiItem(plantFood));

		// Shulker
		// Shulker Head
		ItemStack shulker = createHead("1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44");
		ItemMeta shulkerMeta = shulker.getItemMeta();
		shulkerMeta.displayName(Component.text("Shulker Class").decoration(TextDecoration.ITALIC, false));
		shulker.setItemMeta(shulkerMeta);
		GuiItem shulkerGuiItem = new GuiItem(shulker, event -> {
			reviewShulker.open(event.getWhoClicked());
		});
		selectGui.setItem(3, 6, shulkerGuiItem);

		// Confirm Shulker Gui Items
		GuiItem confirmShulkerGuiItem = new GuiItem(confirm, event -> {
			reviewShulker.close(event.getWhoClicked());
			Core.lp.getUserManager().modifyUser(event.getWhoClicked().getUniqueId(), (User user) -> {
				user.data().add(disableGuiNode);
				user.data().add(shulkerNode);
			});
			Shulker.newUser(((Player) event.getWhoClicked()));
		});
		reviewShulker.setItem(1, 1, confirmShulkerGuiItem);
		reviewShulker.setItem(2, 1, confirmShulkerGuiItem);
		reviewShulker.setItem(3, 1, confirmShulkerGuiItem);

		// Packrat
		ItemStack packrat = new ItemStack(Material.SHULKER_SHELL);
		ItemMeta packratMeta = packrat.getItemMeta();
		packratMeta.displayName(Component.text("Packrat").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		packratMeta.lore(List.of(Component.text("Access a second inventory.").decoration(TextDecoration.ITALIC, false),
									Component.text("Press your 'switch off hand item' key").color(TextColor.color(170, 170, 170)),
									Component.text("to use this ability.").color(TextColor.color(170, 170, 170)),
									Component.text("By default this key is 'F.'").color(TextColor.color(170, 170, 170)),
									Component.text("CURRENTLY ACCESSES ENDERCHEST").decoration(TextDecoration.BOLD, true).color(TextColor.color(170, 0, 0))));
									// TODO: Update the above description once Packrat ability is fully implemented.
		// spotless:on
		packrat.setItemMeta(packratMeta);
		reviewShulker.setItem(2, 5, new GuiItem(packrat));

		// Keep Inventory
		ItemStack keepInventory = new ItemStack(Material.TOTEM_OF_UNDYING);
		ItemMeta keepInventoryMeta = keepInventory.getItemMeta();
		keepInventoryMeta.displayName(Component.text("Keep Inventory").decoration(TextDecoration.ITALIC, false));
		keepInventoryMeta.lore(List
				.of(Component.text("Keep your second inventory on death.").decoration(TextDecoration.ITALIC, false)));
		keepInventory.setItemMeta(keepInventoryMeta);
		reviewShulker.setItem(1, 4, new GuiItem(keepInventory));

		// Weighed Down
		ItemStack weighedDown = new ItemStack(Material.ANVIL);
		ItemMeta weighedDownMeta = weighedDown.getItemMeta();
		weighedDownMeta.displayName(Component.text("Weighed Down").decoration(TextDecoration.ITALIC, false));
		weighedDownMeta.lore(List.of(
				Component.text("Wearing heavy armor makes it impossible to move.").decoration(TextDecoration.ITALIC,
						false),
				Component.text("Heavy armor includes:").color(TextColor.color(170, 170, 170)),
				Component.text("Iron, Gold, Diamond, and Netherite").color(TextColor.color(170, 170, 170))));
		weighedDown.setItemMeta(weighedDownMeta);
		reviewShulker.setItem(1, 6, new GuiItem(weighedDown));

		// Bulky
		ItemStack bulky = new ItemStack(Material.NETHERITE_CHESTPLATE);
		ItemMeta bulkyMeta = bulky.getItemMeta();
		bulkyMeta.displayName(Component.text("Bulky").decoration(TextDecoration.ITALIC, false));
		bulkyMeta.lore(
				List.of(Component.text("You have 14 hearts instead of 10.").decoration(TextDecoration.ITALIC, false)));
		bulkyMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		bulky.setItemMeta(bulkyMeta);
		reviewShulker.setItem(3, 4, new GuiItem(bulky));

		// Strider
		// selectGui.setItem(2, 7,
		// new
		// GuiItem(createHead("a13cb566124aed3b5d86bfaf1d1b01f69526645622ed8510aa86a66d57096fe4")));

		// Phantom
		// Phantom Head
		ItemStack phantom = createHead("7e95153ec23284b283f00d19d29756f244313a061b70ac03b97d236ee57bd982");
		ItemMeta phantomMeta = phantom.getItemMeta();
		phantomMeta.displayName(Component.text("Phantom Class").decoration(TextDecoration.ITALIC, false));
		phantom.setItemMeta(phantomMeta);
		GuiItem phantomGuiItem = new GuiItem(phantom, event -> {
			reviewPhantom.open(event.getWhoClicked());
		});
		selectGui.setItem(3, 8, phantomGuiItem);

		// Confirm Phantom Gui Items
		GuiItem confirmPhantomGuiItem = new GuiItem(confirm, event -> {
			reviewPhantom.close(event.getWhoClicked());
			Core.lp.getUserManager().modifyUser(event.getWhoClicked().getUniqueId(), (User user) -> {
				user.data().add(disableGuiNode);
				user.data().add(phantomNode);
			});
			Phantom.newUser(((Player) event.getWhoClicked()));
		});
		reviewPhantom.setItem(1, 1, confirmPhantomGuiItem);
		reviewPhantom.setItem(2, 1, confirmPhantomGuiItem);
		reviewPhantom.setItem(3, 1, confirmPhantomGuiItem);

		// Wings
		ItemStack wings = new ItemStack(Material.ELYTRA);
		ItemMeta wingsMeta = wings.getItemMeta();
		wingsMeta.displayName(Component.text("Wings").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		wingsMeta.lore(List.of(Component.text("Gain a permanent Elytra.").decoration(TextDecoration.ITALIC, false),
									Component.text("Works with a chestplate.").color(TextColor.color(170, 170, 170))));
		// spotless:on
		wings.setItemMeta(wingsMeta);
		reviewPhantom.setItem(2, 5, new GuiItem(wings));

		// Undead
		reviewPhantom.setItem(1, 4, new GuiItem(undead));

		// Cousins
		ItemStack cousinsPhantom = new ItemStack(Material.PHANTOM_SPAWN_EGG);
		ItemMeta cousinsPhantomMeta = cousinsPhantom.getItemMeta();
		cousinsPhantomMeta.displayName(Component.text("Cousins").decoration(TextDecoration.ITALIC, false));
		cousinsPhantomMeta
				.lore(List.of(Component.text("Phantoms are passive to you.").decoration(TextDecoration.ITALIC, false)));
		cousinsPhantom.setItemMeta(cousinsPhantomMeta);
		reviewPhantom.setItem(1, 6, new GuiItem(cousinsPhantom));

		// Light as a Feather
		ItemStack lightFeather = new ItemStack(Material.FEATHER);
		ItemMeta lightFeatherMeta = lightFeather.getItemMeta();
		lightFeatherMeta.displayName(Component.text("Light as a Feather").decoration(TextDecoration.ITALIC, false));
		lightFeatherMeta.lore(
				List.of(Component.text("You no longer take fall damage.").decoration(TextDecoration.ITALIC, false)));
		lightFeather.setItemMeta(lightFeatherMeta);
		reviewPhantom.setItem(3, 4, new GuiItem(lightFeather));

		// Nightmare
		ItemStack nightmare = new ItemStack(Material.TNT);
		ItemMeta nightmareMeta = nightmare.getItemMeta();
		nightmareMeta.displayName(Component.text("Nightmare").decoration(TextDecoration.ITALIC, false));
		// spotless:off
		nightmareMeta.lore(List.of(
				Component.text("You are unable to sleep.").decoration(TextDecoration.ITALIC, false), 
				Component.text("Setting your spawn point at a bed is still possible.").color(TextColor.color(170, 170, 170))));
		// spotless:on
		nightmare.setItemMeta(nightmareMeta);
		reviewPhantom.setItem(3, 6, new GuiItem(nightmare));

		// Piglin
		// selectGui.setItem(2, 9, new GuiItem(Material.PIGLIN_HEAD));

		// To be announced
		ItemStack TBD = createHead("d5d20330da59c207d78352838e91a48ea1e42b45a9893226144b251fe9b9d535");
		ItemMeta TBDMeta = TBD.getItemMeta();
		TBDMeta.displayName(Component.text("To Be Announced").decoration(TextDecoration.ITALIC, false));
		TBD.setItemMeta(TBDMeta);
		selectGui.setItem(2, 9, new GuiItem(TBD));
		selectGui.setItem(2, 7, new GuiItem(TBD));
		reviewShulker.setItem(3, 6, new GuiItem(TBD));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be used as a player!");
			return true;
		}
		selectGui.open(((HumanEntity) sender));
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
