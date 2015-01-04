package com.gmail.vapidlinus.murder.main;

import com.gmail.vapidlinus.murder.tools.ChatContext;
import com.gmail.vapidlinus.murder.tools.Tools;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MPlayer {
	private String name;
	private Murder plugin;
	private boolean isMurderer;
	private boolean isGunner;
	private boolean isSpectator;
	private int fireNext;
	private int gunBan = -1;

	public MPlayer(String name, boolean isMurderer, boolean isGunner,
			Murder plugin) {
		this.name = name;
		this.plugin = plugin;
		setMurderer(isMurderer);
		setGunner(isGunner, false);
		isSpectator = false;
	}

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return plugin.getServer().getPlayer(name);
	}

	public boolean isMurderer() {
		return isMurderer;
	}

	public void setMurderer(boolean isMurderer) {
		setMurderer(isMurderer, false);
	}

	public void setMurderer(boolean isMurderer, boolean showMessage) {
		this.isMurderer = isMurderer;
		if ((!isMurderer) && (showMessage)) {
			Tools.sendMessageAll(plugin.getServer(), ChatContext.PREFIX_PLUGIN
					+ ChatContext.COLOR_MURDER + name
					+ ChatContext.COLOR_WARNING + " was the murderer!");
		}
		updateInventory();
	}

	public boolean isGunner() {
		return isGunner;
	}

	public void setGunner(boolean isGunner) {
		setGunner(isGunner, true);
	}

	public void setGunner(boolean isGunner, boolean dropGun) {
		if ((this.isGunner) && (dropGun) && (!isGunner)) {
			getPlayer().getWorld().dropItem(getPlayer().getLocation(),
					new ItemStack(Material.BOW));
		}
		this.isGunner = isGunner;
		updateInventory();
	}

	public boolean isSpectator() {
		return isSpectator;
	}

	public boolean isBystander(boolean ignoreGunner) {
		if (isMurderer) {
			return false;
		}
		if ((isGunner) && (ignoreGunner)) {
			return false;
		}
		if (isSpectator) {
			return false;
		}
		return true;
	}

	public void setSpectator(boolean isSpectator) {
		setSpectator(isSpectator, true);
	}

	public void setSpectator(boolean isSpectator, boolean updateSpectators) {
		this.isSpectator = isSpectator;
		setMurderer(false);
		setGunner(false);
		if (updateSpectators) {
			plugin.getMatch().updateSpectators();
		}
	}

	public void updateInventory() {
		Player player = getPlayer();
		player.getInventory().clear();
		player.getInventory().setHeldItemSlot(0);
		if (isGunner()) {
			ItemStack item = new ItemStack(Material.BOW, 1);
			Tools.setItemStackName(item, "Pistol");
			player.getInventory().setItem(1, item);
		} else if (isMurderer()) {
			ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
			ItemStack item2 = new ItemStack(Material.COMPASS, 1);
			ItemStack item3 = new ItemStack(Material.IRON_INGOT, 1);
			ItemStack item4 = new ItemStack(Material.ENDER_PEARL, 1);
			Tools.setItemStackName(item, "Knife",
					Arrays.asList(new String[] { "Use this to murder!" }));
			Tools.setItemStackName(
					item2,
					"Compass",
					Arrays.asList(new String[] { "This points to the closer innocent!" }));
			Tools.setItemStackName(
					item4,
					"Teleporter",
					Arrays.asList(new String[] { "Consume this to teleport all players!" }));
			player.getInventory().setItem(1, item);
			player.getInventory().setItem(2, item2);
			player.getInventory().setItem(8, item3);
			player.getInventory().setItem(3, item4);
		}
		if (isMurderer()) {
			player.setFoodLevel(20);
		} else {
			player.setFoodLevel(2);
		}
		player.setHealth(20.0D);
	}

	public int getFireNext() {
		return fireNext;
	}

	public void setFireNext(int fireNext) {
		this.fireNext = fireNext;
	}

	public void reload() {
		fireNext = 100;
	}

	public int getGunBan() {
		return gunBan;
	}

	public void setGunBan(int gunBan) {
		this.gunBan = gunBan;
	}
}