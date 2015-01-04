package com.gmail.vapidlinus.murder.runnable;

import com.gmail.vapidlinus.murder.main.Match;
import com.gmail.vapidlinus.murder.main.Murder;

import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

@SuppressWarnings("unused")
public class EndMatchCountdown implements Runnable {
	private Murder plugin;
	private int ID;
	private int time = 8;

	public EndMatchCountdown(Murder plugin) {
		this.plugin = plugin;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void run() {
		if (time == 0) {
			plugin.getMatch().finishMatch();
			plugin.getServer().getScheduler().cancelTask(ID);
		}
		time -= 1;
	}
}