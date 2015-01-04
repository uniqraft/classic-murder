package com.gmail.vapidlinus.murder.tools;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Tools {
	public static String locationToString(Location loc) {
		String locString = "";
		locString = locString + loc.getWorld().getName() + " ";
		locString = locString + loc.getX() + " ";
		locString = locString + loc.getY() + " ";
		locString = locString + loc.getZ() + " ";
		locString = locString + loc.getYaw() + " ";
		locString = locString + loc.getPitch() + " ";
		return locString;
	}

	public static Location stringToLocation(String loc, Server server) {
		try {
			String[] split = loc.split(" ");
			if (Bukkit.getWorld(split[0]) == null) {
				Bukkit.createWorld(new WorldCreator(split[0]));
			}
			World world = server.getWorld(split[0]);
			double x = Double.parseDouble(split[1]);
			double y = Double.parseDouble(split[2]);
			double z = Double.parseDouble(split[3]);
			float yaw = Float.parseFloat(split[4]);
			float pitch = Float.parseFloat(split[5]);

			return new Location(world, x, y, z, yaw, pitch);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static float get2DDistance(Location loc1, Location loc2) {
		int x1 = loc1.getBlockX();
		int x2 = loc2.getBlockX();
		int z1 = loc1.getBlockZ();
		int z2 = loc2.getBlockZ();

		int x = x1 - x2;
		int z = z1 - z2;
		return (float) Math.sqrt(x * x + z * z);
	}

	public static void sendMessageAll(Server server, String message) {
		sendMessageAll(server, message, null);
	}

	public static void sendMessageAll(Server server, String message,
			Player ignored) {
		Player[] arrayOfPlayer;
		int j = (arrayOfPlayer = (Player[]) server.getOnlinePlayers().toArray()).length;
		for (int i = 0; i < j; i++) {
			Player player = arrayOfPlayer[i];
			if (player != ignored) {
				player.sendMessage(message);
			}
		}
	}

	public static ItemStack setItemStackName(ItemStack itemStack, String name) {
		return setItemStackName(itemStack, name, null);
	}

	public static ItemStack setItemStackName(ItemStack itemStack,
			List<String> lore) {
		return setItemStackName(itemStack, null, lore);
	}

	public static ItemStack setItemStackName(ItemStack itemStack, String name,
			List<String> lore) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(name);
		}
		if (lore != null) {
			meta.setLore(lore);
		}
		itemStack.setItemMeta(meta);
		return itemStack;
	}
}