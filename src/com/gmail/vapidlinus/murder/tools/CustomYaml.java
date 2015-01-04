package com.gmail.vapidlinus.murder.tools;

import java.io.File;
import java.io.FilenameFilter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomYaml {
	public static YamlConfiguration loadConfig(String path) {
		try {
			File file = new File(path);
			FileConfiguration fileConfig = new YamlConfiguration();
			fileConfig.load(file);
			return (YamlConfiguration) fileConfig;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean saveConfig(YamlConfiguration config, String path) {
		try {
			config.save(new File(path));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String[] getYAMLFilesInPath(String path) {
		try {
			File[] files = new File(path).listFiles(new FilenameFilter() {
				public boolean accept(File dir, String fileName) {
					return fileName.endsWith(".yml");
				}
			});
			String[] paths = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				paths[i] = files[i].getPath();
			}
			if (paths.length == 0) {
				return null;
			}
			return paths;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}