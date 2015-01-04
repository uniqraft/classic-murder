package com.gmail.vapidlinus.murder.runnable;

import com.gmail.vapidlinus.murder.main.Match;
import com.gmail.vapidlinus.murder.main.Murder;
import com.gmail.vapidlinus.murder.tools.ChatContext;
import com.gmail.vapidlinus.murder.tools.Tools;

public class MatchCountdown implements Runnable {
	private Match match;
	private Murder plugin;
	private int time = 20;

	public MatchCountdown(Match match, Murder plugin) {
		this.match = match;
		this.plugin = plugin;
	}

	public void run() {
		match.clearItems(match.getArena().getRandomSpawn().getWorld());
		if ((time > 0) && ((time < 5) || (time % 5 == 0))) {
			Tools.sendMessageAll(plugin.getServer(), ChatContext.PREFIX_PLUGIN
					+ ChatContext.COLOR_HIGHLIGHT + time
					+ ChatContext.COLOR_LOWLIGHT
					+ " seconds until match starts.");
		} else if (time == 0) {
			match.kickDeadPlayers();
			if (match.canMatchStart(false)) {
				match.setHasGameStarted(true);
				Tools.sendMessageAll(plugin.getServer(),
						ChatContext.PREFIX_PLUGIN + ChatContext.COLOR_HIGHLIGHT
								+ "The match has started!");
			} else {
				Tools.sendMessageAll(plugin.getServer(),
						ChatContext.ERROR_NOTENOUGHPLAYERSTOCONTINUE);
				match.endMatch();
			}
		}
		time -= 1;
	}
}