package com.gmail.vapidlinus.murder.main;

import com.gmail.vapidlinus.murder.runnable.ArenaLoop;
import com.gmail.vapidlinus.murder.runnable.EndMatchCountdown;
import com.gmail.vapidlinus.murder.runnable.MatchCountdown;
import com.gmail.vapidlinus.murder.tools.ChatContext;
import com.gmail.vapidlinus.murder.tools.Tools;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class Match {
	private Murder plugin;
	private boolean hasStarted = false;
	private boolean hasGameStarted = false;
	private Arena arena = null;
	private int matchCountdownID = -1;
	private int arenaLoopID = -1;
	private List<MPlayer> mplayers;

	public Match(Murder plugin) {
		this.plugin = plugin;
		mplayers = new ArrayList<MPlayer>();
	}

	public MPlayer getClostestBystander(Location locationFrom) {
		MPlayer nearest = null;
		float distance = 2048.0F;
		for (MPlayer mplayer : mplayers) {
			if ((mplayer.isBystander(false)) && (!mplayer.getPlayer().isDead())) {
				float d = Tools.get2DDistance(locationFrom, mplayer.getPlayer()
						.getLocation());
				if (d < distance) {
					distance = d;
					nearest = mplayer;
				}
			}
		}
		return nearest;
	}

	public void clearItems(World world) {
		List<Entity> entList = world.getEntities();
		for (Entity current : entList) {
			if ((current instanceof Item)) {
				current.remove();
			}
		}
	}

	public void startMatch() {
		hasStarted = true;
		hasGameStarted = false;

		Random random = new Random();

		String[] possibleArenas = Arena.getPossibleArenas();
		int a = random.nextInt(possibleArenas.length);
		arena = Arena.getArenaFromFile(plugin, possibleArenas[a]);

		kickDeadPlayers();
		for (int i = 0; i < mplayers.size(); i++) {
			if (((MPlayer) mplayers.get(i)).getPlayer().isDead()) {
				((MPlayer) mplayers.get(i)).getPlayer().kickPlayer(
						"Don't idle!");
			} else {
				((MPlayer) mplayers.get(i)).reload();

				Player player = ((MPlayer) mplayers.get(i)).getPlayer();
				player.setGameMode(GameMode.ADVENTURE);
				player.setFlying(false);
				player.teleport(arena.getRandomSpawn());
			}
		}
		matchCountdownID = plugin
				.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(plugin, new MatchCountdown(this, plugin), 20L, 20L);
		arenaLoopID = plugin
				.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(plugin, new ArenaLoop(plugin), 420L, 20L);
	}

	public void endMatch() {
		if (!isStarted()) {
			return;
		}
		if (matchCountdownID != -1) {
			plugin.getServer().getScheduler().cancelTask(matchCountdownID);
			matchCountdownID = -1;
		}
		if (arenaLoopID != -1) {
			plugin.getServer().getScheduler().cancelTask(arenaLoopID);
			arenaLoopID = -1;
		}
		EndMatchCountdown countdown = new EndMatchCountdown(plugin);
		int ID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, countdown, 20L, 20L);
		setHasGameStarted(false);
		countdown.setID(ID);
	}

	public boolean canMatchStart(boolean showMessages) {
		if (mplayers.size() < 2) {
			if (showMessages) {
				Tools.sendMessageAll(plugin.getServer(), ChatContext.ERROR_NOTENOUGHPLAYERSTOSTART);
			}
			return false;
		}
		return true;
	}

	public void finishMatch() {
		setHasStarted(false);
		plugin.startNextMatchCountdown();
		Location loc = new Location(plugin.getServer().getWorld("world"), 0.0D, 64.0D, 0.0D);
		for (int i = 0; i < mplayers.size(); i++) {
			((MPlayer) mplayers.get(i)).setGunner(false);
			((MPlayer) mplayers.get(i)).setMurderer(false);
			((MPlayer) mplayers.get(i)).setSpectator(false);

			Player player = ((MPlayer) mplayers.get(i)).getPlayer();
			player.setExp(0.0F);
			player.setLevel(0);

			player.teleport(loc);
		}
	}

	public void selectActors() {
		SecureRandom random = new SecureRandom();

		int players = mplayers.size();

		int r1 = random.nextInt(mplayers.size());
		int r2 = r1;
		if (players > 1) {
			while (r2 == r1) {
				r2 = random.nextInt(mplayers.size());
			}
		}
		for (int i = 0; i < players; i++) {
			((MPlayer) mplayers.get(i)).setGunner(i == r1);
			if (players > 1) {
				((MPlayer) mplayers.get(i)).setMurderer(i == r2);
			}
		}
	}

	public void updateSpectators() {
		for (MPlayer mplayer : getMPlayers()) {
			if (mplayer.isSpectator()) {
				mplayer.getPlayer().setGameMode(GameMode.CREATIVE);
				for (MPlayer mplayerOther : getMPlayers()) {
					if (mplayerOther != mplayer) {
						Player spec = mplayer.getPlayer();
						Player other = mplayerOther.getPlayer();

						other.hidePlayer(spec);
					}
				}
			} else {
				for (MPlayer mplayerOther : getMPlayers()) {
					mplayer.getPlayer().setGameMode(GameMode.ADVENTURE);
					if (mplayerOther != mplayer) {
						Player me = mplayer.getPlayer();
						Player other = mplayerOther.getPlayer();

						other.showPlayer(me);
					}
				}
			}
		}
	}

	public void checkForGameEnd() {
		if ((!isStarted()) || (!hasGameStarted)) {
			return;
		}
		if (getMurderer() == null) {
			Tools.sendMessageAll(plugin.getServer(), ChatContext.PREFIX_PLUGIN
					+ ChatContext.COLOR_BYSTANDER + "The Innocent"
					+ ChatContext.COLOR_WARNING + " wins the match!");
			plugin.getMatch().endMatch();
		} else if (getBystanders(false).size() < 1) {
			Tools.sendMessageAll(plugin.getServer(), ChatContext.PREFIX_PLUGIN
					+ ChatContext.COLOR_MURDER + "The Murderer"
					+ ChatContext.COLOR_WARNING + " has won!");
			plugin.getMatch().endMatch();
		}
	}

	public boolean isStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	public boolean isGameStarted() {
		return hasGameStarted;
	}

	public void kickDeadPlayers() {
		for (int i = 0; i < mplayers.size(); i++) {
			if (((MPlayer) mplayers.get(i)).getPlayer().isDead()) {
				((MPlayer) mplayers.get(i)).getPlayer().kickPlayer(
						"You were kicked due to being in the respawn screen.");
			}
		}
	}

	public void setHasGameStarted(boolean hasGameStarted) {
		this.hasGameStarted = hasGameStarted;
		if (hasGameStarted) {
			plugin.getServer().getScheduler().cancelTask(matchCountdownID);
			matchCountdownID = -1;
			selectActors();
		}
	}

	public List<MPlayer> getGunners() {
		List<MPlayer> gunners = new ArrayList<MPlayer>();
		for (MPlayer p : mplayers) {
			if (p.isGunner()) {
				gunners.add(p);
			}
		}
		return gunners;
	}

	public MPlayer getMurderer() {
		for (MPlayer p : mplayers) {
			if (p.isMurderer()) {
				return p;
			}
		}
		return null;
	}

	public void addMPlayer(MPlayer mplayer) {
		mplayers.add(mplayer);
	}

	public void removeMPlayer(MPlayer mplayer) {
		if (mplayers.contains(mplayer)) {
			if (isStarted()) {
				mplayer.isGunner();

				mplayer.isMurderer();
			}
			mplayers.remove(mplayer);
		}
		checkForGameEnd();
	}

	public List<MPlayer> getMPlayers() {
		return mplayers;
	}

	public MPlayer getMPlayer(Player player) {
		for (MPlayer mplayer : mplayers) {
			if (mplayer.getName() == player.getName()) {
				return mplayer;
			}
		}
		return null;
	}

	public List<MPlayer> getBystandesr() {
		return getBystanders(false);
	}

	public List<MPlayer> getBystanders(boolean ignoreGunner) {
		List<MPlayer> bystanders = new ArrayList<MPlayer>();
		for (MPlayer mplayer : mplayers) {
			if (mplayer.isBystander(ignoreGunner)) {
				bystanders.add(mplayer);
			}
		}
		return bystanders;
	}

	public Arena getArena() {
		return arena;
	}
}