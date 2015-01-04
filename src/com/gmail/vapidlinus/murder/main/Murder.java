package com.gmail.vapidlinus.murder.main;

import com.gmail.vapidlinus.murder.commandexecutors.MCommandExecutor;
import com.gmail.vapidlinus.murder.listeners.BlockListener;
import com.gmail.vapidlinus.murder.listeners.EntityListener;
import com.gmail.vapidlinus.murder.listeners.InventoryListener;
import com.gmail.vapidlinus.murder.listeners.PlayerListener;
import com.gmail.vapidlinus.murder.runnable.NextMatchCountdown;
import com.gmail.vapidlinus.murder.runnable.Reload;
import com.gmail.vapidlinus.murder.tools.TeleportFix;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Murder extends JavaPlugin {
	public static final int GUN_BAN_TIME = 60;
	private Match match;
	public int nextMatchCountdownID = -1;

	public void onEnable() {
		match = new Match(this);
		Player[] arrayOfPlayer;
		int j = (arrayOfPlayer = (Player[]) getServer().getOnlinePlayers().toArray()).length;
		for (int i = 0; i < j; i++) {
			Player player = arrayOfPlayer[i];
			match.addMPlayer(new MPlayer(player.getName(), false, false, this));
		}
		registerListeners();
		startLoops();

		getCommand("murder").setExecutor(new MCommandExecutor(this));
	}

	public void startNextMatchCountdown() {
		nextMatchCountdownID = getServer().getScheduler().scheduleSyncRepeatingTask(this, new NextMatchCountdown(this),	20L, 20L);
	}

	public void stopNextMatchCountdown() {
		if (nextMatchCountdownID != -1) {
			getServer().getScheduler().cancelTask(nextMatchCountdownID);
			nextMatchCountdownID = -1;
		}
	}

	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new EntityListener(this), this);
		pm.registerEvents(new InventoryListener(this), this);
		pm.registerEvents(new TeleportFix(this, Bukkit.getServer()), this);
		pm.registerEvents(new BlockListener(this), this);
	}

	private void startLoops() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Reload(this), 10L, 10L);
	}

	public Match getMatch() {
		return match;
	}
}