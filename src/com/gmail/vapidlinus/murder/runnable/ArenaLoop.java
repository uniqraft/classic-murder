package com.gmail.vapidlinus.murder.runnable;

import com.gmail.vapidlinus.murder.main.MPlayer;
import com.gmail.vapidlinus.murder.main.Match;
import com.gmail.vapidlinus.murder.main.Murder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArenaLoop implements Runnable {
	private Murder plugin;
	private int tick = 0;

	public ArenaLoop(Murder plugin) {
		this.plugin = plugin;
	}

	public void run() {
		tick += 1;
		if ((tick != 0) && (tick % 20 == 0)) {
			spawnParts();
		}
		MPlayer mplayer = plugin.getMatch().getMurderer();
		Player player = mplayer.getPlayer();
		if (mplayer != null) {
			MPlayer nearest = plugin.getMatch().getClostestBystander(player.getLocation());
			if (nearest != null) {
				player.setCompassTarget(nearest.getPlayer().getLocation());
			}
		}
		plugin.getMatch().updateSpectators();
	}

	void spawnParts() {
		Match match = plugin.getMatch();
		for (int i = 0; i < 3; i++) {
			Location loc = match.getArena().getRandomPartSpawn();
			loc.getWorld().dropItem(loc, new ItemStack(Material.IRON_INGOT));
		}
	}
}