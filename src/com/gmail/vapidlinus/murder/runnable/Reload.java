package com.gmail.vapidlinus.murder.runnable;

import com.gmail.vapidlinus.murder.main.MPlayer;
import com.gmail.vapidlinus.murder.main.Match;
import com.gmail.vapidlinus.murder.main.Murder;
import java.util.List;
import org.bukkit.entity.Player;

public class Reload implements Runnable {
	private Murder plugin;

	public Reload(Murder plugin) {
		this.plugin = plugin;
	}

	public void run() {
		Match match = plugin.getMatch();
		if (match.isStarted()) {
			List<MPlayer> bystanders = match.getBystanders(true);
			for (MPlayer mplayer : bystanders) {
				mplayer.setGunBan(mplayer.getGunBan() - 1);
			}
			List<MPlayer> gunners = match.getGunners();
			if (gunners != null) {
				for (MPlayer gunner : gunners) {
					Player player = gunner.getPlayer();
					int fireNext = gunner.getFireNext();
					if (fireNext > 0) {
						gunner.setFireNext(fireNext - 10);
						player.setExp(1.0F - fireNext / 100.0F);
						if (gunner.getFireNext() > 0) {
							player.setLevel(0);
						} else {
							player.setLevel(1);
							player.setExp(0.999999F);
						}
					}
				}
			}
			MPlayer murderer = match.getMurderer();
			if (murderer != null) {
				Player player = murderer.getPlayer();
				int fireNext = murderer.getFireNext();
				if (fireNext > 0) {
					murderer.setFireNext(fireNext - 30);
					player.setExp(1.0F - fireNext / 100.0F);
					if (murderer.getFireNext() > 0) {
						player.setLevel(0);
					} else {
						player.setLevel(1);
						player.setExp(0.999999F);
					}
				}
			}
		}
	}
}