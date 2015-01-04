package com.gmail.vapidlinus.murder.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gmail.vapidlinus.murder.tools.CustomYaml;
import com.gmail.vapidlinus.murder.tools.Tools;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class Arena {
	public static final String PATH_ARENAS = "plugins/Murder/Arenas";
	
	private final Murder plugin;
	
	private List<Location> spawns;
	private List<Location> partspawns;
	private String path;
	
	private Random r = new Random();
	
	public Arena(Murder plugin, String path) {
		this.plugin = plugin;
		this.path = path;
		
		Load();
	}

	void Load() {
		// Load spawns
		YamlConfiguration config = CustomYaml.loadConfig(path);
		spawns = new ArrayList<Location>();
		
		// Convert spawns
		List<?> spawnsList = config.getList("spawns");
		for (Object i : spawnsList) {
			Object loc = Tools.stringToLocation(i.toString(), plugin.getServer());
			if (loc instanceof Location) spawns.add((Location) loc);
		}
		partspawns = new ArrayList<Location>();
		List<?> partSpawnsList = config.getList("partspawns");
		for (Object i : partSpawnsList) {
			Object loc = Tools.stringToLocation(i.toString(), plugin.getServer());
			if (loc instanceof Location) partspawns.add((Location) loc);
		}
	}

	public Location getRandomSpawn() {
		if (spawns != null) {
			int spawn = r.nextInt(spawns.size());
			return (Location) spawns.get(spawn);
		}
		return null;
	}

	public Location getRandomPartSpawn() {
		if (partspawns != null) {
			int spawn = r.nextInt(partspawns.size());
			return (Location) partspawns.get(spawn);
		}
		return null;
	}

	public static String[] getPossibleArenas() {
		return CustomYaml.getYAMLFilesInPath("plugins/Murder/Arenas");
	}

	public static Arena getArenaFromFile(Murder plugin, String path) {
		return new Arena(plugin, path);
	}
}
