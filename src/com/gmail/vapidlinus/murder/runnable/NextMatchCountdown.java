package com.gmail.vapidlinus.murder.runnable;

import com.gmail.vapidlinus.murder.main.Murder;
import com.gmail.vapidlinus.murder.tools.ChatContext;
import com.gmail.vapidlinus.murder.tools.Tools;

public class NextMatchCountdown implements Runnable {
	private Murder plugin;
	private int time = 20;

	public NextMatchCountdown(Murder plugin) {
		this.plugin = plugin;
	}

	public void run() {
		if ((time > 0) && ((time <= 3) || (time % 10 == 0))) {
			Tools.sendMessageAll(plugin.getServer(), ChatContext.PREFIX_PLUGIN
					+ ChatContext.COLOR_HIGHLIGHT + time
					+ ChatContext.COLOR_WARNING
					+ " seconds until match starts.");
		} else if (time == 0) {
			if (plugin.getMatch().canMatchStart(true)) {
				plugin.getMatch().startMatch();
				plugin.stopNextMatchCountdown();
			} else {
				time = 21;
			}
		}
		time -= 1;
	}
}