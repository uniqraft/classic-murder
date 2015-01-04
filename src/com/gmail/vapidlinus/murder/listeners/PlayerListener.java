package com.gmail.vapidlinus.murder.listeners;

import com.gmail.vapidlinus.murder.main.MPlayer;
import com.gmail.vapidlinus.murder.main.Murder;
import com.gmail.vapidlinus.murder.tools.ChatContext;
import com.gmail.vapidlinus.murder.tools.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
	private final Murder plugin;

	public PlayerListener(Murder plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		MPlayer mplayer = new MPlayer(player.getName(), false, false, plugin);
		plugin.getMatch().addMPlayer(mplayer);
		event.getPlayer().getWorld().setSpawnLocation(0, 64, 0);
		if (plugin.getMatch().isStarted()) {
			if (plugin.getMatch().isGameStarted()) {
				mplayer.setSpectator(true, true);
			}
			player.teleport(plugin.getMatch().getArena().getRandomSpawn());
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.getMatch().removeMPlayer(plugin.getMatch().getMPlayer(player));
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (plugin.getMatch().isStarted()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		MPlayer mplayer = plugin.getMatch().getMPlayer(player);
		if (mplayer.isSpectator()) {
			event.setCancelled(true);
			for (MPlayer mp : plugin.getMatch().getMPlayers()) {
				if (mp.isSpectator()) {
					mp.getPlayer().sendMessage(
							ChatColor.GRAY + "<" + player.getName() + ">" + " "
									+ event.getMessage());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage(null);

		Player player = event.getEntity();
		MPlayer mplayer = plugin.getMatch().getMPlayer(player);
		if (player.isDead()) {
			mplayer.setSpectator(true, false);
			plugin.getMatch().checkForGameEnd();
			return;
		}
		if (plugin.getMatch().isStarted()) {
			event.setDroppedExp(0);
			event.getDrops().clear();
			if (plugin.getMatch().isGameStarted()) {
				event.setDroppedExp(0);
				event.getDrops().clear();
				if (mplayer.isMurderer()) {
					Tools.sendMessageAll(plugin.getServer(),
							ChatContext.PREFIX_PLUGIN
									+ ChatContext.COLOR_MURDER + "The Murderer"
									+ ChatContext.COLOR_WARNING
									+ " has been killed!");
					mplayer.setMurderer(false, true);
				}
				mplayer.setSpectator(true, false);
				plugin.getMatch().checkForGameEnd();
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (plugin.getMatch().isStarted()) {
			event.setCancelled(true);

			Player player = event.getPlayer();
			MPlayer mplayer = plugin.getMatch().getMPlayer(player);
			if (mplayer.isSpectator()) {
				return;
			}
			if (!mplayer.isMurderer()) {
				if (event.getItem().getItemStack().getType() == Material.BOW) {
					if (mplayer.isGunner()) {
						return;
					}
					if (mplayer.getGunBan() < 1) {
						mplayer.setGunner(true);
						event.getItem().remove();
						return;
					}
					return;
				}
				if (event.getItem().getItemStack().getType() == Material.IRON_INGOT) {
					if (mplayer.isGunner()) {
						return;
					}
					ItemStack stack = player.getInventory().getItem(8);
					if ((stack == null)
							|| (stack.getType() != Material.IRON_INGOT)) {
						player.getInventory().setItem(8,
								new ItemStack(Material.IRON_INGOT));
					} else {
						stack.setAmount(stack.getAmount() + 1);
					}
					if ((stack != null) && (stack.getAmount() == 5)) {
						mplayer.setGunner(true);
						player.getInventory().setItem(8, null);
						Tools.sendMessageAll(plugin.getServer(),
								ChatContext.PREFIX_PLUGIN
										+ ChatContext.COLOR_BYSTANDER
										+ "An Innocent"
										+ ChatContext.COLOR_WARNING
										+ " crafted a gun!");
					}
				}
			} else if (event.getItem().getItemStack().getType() == Material.BOW) {
				return;
			}
			player.getWorld().playSound(player.getLocation(),
					Sound.ITEM_PICKUP, 1.0F, 0.5F);
			event.getItem().remove();
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (plugin.getMatch().isStarted()) {
			final PlayerRespawnEvent e = event;
			plugin.getServer().getScheduler()
					.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Player player = e.getPlayer();
							plugin.getMatch().updateSpectators();
							player.teleport(plugin.getMatch().getArena()
									.getRandomSpawn());
							player.setSaturation(0.0F);
						}
					}, 20L);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		MPlayer gunner = plugin.getMatch().getMPlayer(player);
		Action eventAction = event.getAction();
		if (gunner.isSpectator()) {
			event.setCancelled(true);
			return;
		}
		if ((event.getClickedBlock() != null)
				&& (eventAction == Action.RIGHT_CLICK_BLOCK)
				&& (event.getClickedBlock().getType() == Material.SOIL)
				&& (player.isOp())) {
			return;
		}
		if ((event.getClickedBlock() != null)
				&& ((event.getClickedBlock().getType().equals(Material.SOIL)) || (player
						.getWorld()
						.getBlockAt(
								event.getClickedBlock().getLocation()
										.add(0.0D, 1.0D, 0.0D)).getType()
						.equals(Material.FIRE)))) {
			event.setCancelled(true);
			return;
		}
		if (!plugin.getMatch().isStarted()) {
			return;
		}
		if ((eventAction == Action.RIGHT_CLICK_AIR)
				|| (eventAction == Action.RIGHT_CLICK_BLOCK)) {
			if (gunner.getName() == player.getName()) {
				Arrow arrow;
				if (player.getInventory().getItemInHand().getType() == Material.BOW) {
					if (gunner.getFireNext() <= 0) {
						event.setCancelled(true);

						Location arrowLocaction = player.getLocation();
						arrowLocaction.add(0.0D, 1.75D, 0.0D);
						arrowLocaction.add(player.getLocation().getDirection()
								.normalize().multiply(1.5D));
						arrowLocaction.add(player.getVelocity().multiply(1.5D));
						arrow = player.getWorld()
								.spawnArrow(arrowLocaction,
										player.getLocation().getDirection(),
										4.0F, 0.0F);
						arrow.setBounce(false);
						arrow.setShooter(player);
						player.getWorld().playSound(arrowLocaction,
								Sound.FIREWORK_BLAST, 2.5F, 0.5F);

						gunner.reload();
					}
				} else if (player.getInventory().getItemInHand().getType() == Material.ENDER_PEARL) {
					event.setCancelled(true);
					player.getInventory().setItemInHand(
							new ItemStack(Material.AIR, 1));
					Tools.sendMessageAll(plugin.getServer(),
							ChatContext.PREFIX_PLUGIN
									+ ChatContext.COLOR_MURDER
									+ "The Murderer "
									+ ChatContext.COLOR_WARNING
									+ " used the teleportation device!");
					for (MPlayer mp : plugin.getMatch().getMPlayers()) {
						mp.getPlayer().teleport(
								plugin.getMatch().getArena().getRandomSpawn());
					}
				}
			}
		}
	}
}