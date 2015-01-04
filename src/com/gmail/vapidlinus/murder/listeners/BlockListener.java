package com.gmail.vapidlinus.murder.listeners;

import com.gmail.vapidlinus.murder.main.Murder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
	private final Murder plugin;

	public BlockListener(Murder plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if ((plugin.getMatch().isStarted()) || (!event.getPlayer().isOp())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if ((plugin.getMatch().isStarted()) || (!event.getPlayer().isOp())) {
			event.setCancelled(true);
		}
	}
}