package com.gmail.vapidlinus.murder.listeners;

import com.gmail.vapidlinus.murder.main.MPlayer;
import com.gmail.vapidlinus.murder.main.Murder;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.util.Vector;

public class EntityListener implements Listener {
	private final Murder plugin;

	public EntityListener(Murder plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		Entity entity = event.getEntity();
		if ((entity instanceof Player)) {
			Player player = (Player) entity;
			MPlayer mplayer = plugin.getMatch().getMPlayer(player);
			if (mplayer.isMurderer()) {
				player.setFoodLevel(20);
			} else {
				player.setFoodLevel(2);
			}
		}
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!plugin.getMatch().isGameStarted()) {
			event.setCancelled(true);
			return;
		}
		Player attacker = null;
		Player attacked = null;
		if ((event.getEntity() instanceof Player)) {
			attacked = (Player) event.getEntity();
		}
		if ((event.getDamager() instanceof Player)) {
			attacker = (Player) event.getDamager();
		}
		MPlayer mAttacked = null;
		if (attacked != null) {
			mAttacked = plugin.getMatch().getMPlayer(attacked);
		}
		MPlayer mAttacker = null;
		if (attacker != null) {
			mAttacker = plugin.getMatch().getMPlayer(attacker);
		}
		if (mAttacked == null) {
			return;
		}
		if (mAttacked.isSpectator()) {
			Entity entityDamager = event.getDamager();
			if ((entityDamager instanceof Arrow)) {
				Arrow arrow = (Arrow) entityDamager;
				attacked.teleport(attacked.getLocation().add(0.0D, 5.0D, 0.0D));

				Arrow newArrow = attacked.getWorld().spawnArrow(
						arrow.getLocation(),
						arrow.getVelocity(),
						(float) arrow.getVelocity().distance(
								new Vector(0, 0, 0)), 0.0F);
				newArrow.setBounce(false);

				event.setCancelled(true);
				arrow.remove();
			}
			return;
		}
		if ((event.getDamager() instanceof Arrow)) {
			if (mAttacked.isMurderer()) {
				attacked.setHealth(0.0D);
				return;
			}
			Arrow arrow = (Arrow) event.getDamager();
			if ((arrow.getShooter() instanceof Player)) {
				Player player = (Player) arrow.getShooter();
				MPlayer mplayer = plugin.getMatch().getMPlayer(player);
				if (mplayer != null) {
					mplayer.setGunBan(60);
					mplayer.setGunner(false);
				}
				if (mAttacked != mplayer) {
					attacked.setHealth(0.0D);
					return;
				}
			}
			return;
		}
		if (mAttacker == null) {
			return;
		}
		if (mAttacker.isMurderer()) {
			if (mAttacker.getPlayer().getInventory().getItemInHand().getType() == Material.IRON_SWORD) {
				attacked.setHealth(0.0D);
				return;
			}
		}
		event.setCancelled(true);
	}
}