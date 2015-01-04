package com.gmail.vapidlinus.murder.listeners;

import com.gmail.vapidlinus.murder.main.Murder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
	private final Murder plugin;

	public InventoryListener(Murder plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event) {
		HumanEntity human = event.getWhoClicked();
		if ((human instanceof Player)) {
			Player player = (Player) human;
			if ((plugin.getMatch().isStarted()) || (!player.isOp())) {
				event.setCancelled(true);
				player.updateInventory();
			}
		}
	}
}