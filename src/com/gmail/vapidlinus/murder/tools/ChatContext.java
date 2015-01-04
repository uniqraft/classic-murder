package com.gmail.vapidlinus.murder.tools;

import org.bukkit.ChatColor;

public class ChatContext {
	private ChatContext() {}

	public static final String COLOR_MAIN;
	public static final String COLOR_LOWLIGHT;
	public static final String COLOR_HIGHLIGHT;
	public static final String COLOR_WARNING;
	public static final String COLOR_ERROR;
	public static final String COLOR_BYSTANDER;
	public static final String COLOR_MURDER;
	public static final String PREFIX_PLUGIN;
	public static final String PREFIX_DEBUG;
	public static final String ERROR_TOOMANYARGUMENTS;
	public static final String ERROR_NOTENOUGHARGUMENTS;
	public static final String ERROR_ARENANOTFOUND;
	public static final String ERROR_NOTENOUGHPLAYERSTOCONTINUE;
	public static final String ERROR_NOTENOUGHPLAYERSTOSTART;

	static {
		// Haha, this came out like a giant mess from the decompiler
		
		COLOR_MAIN = (new StringBuilder()).append(ChatColor.GRAY).toString();
		COLOR_LOWLIGHT = (new StringBuilder()).append(ChatColor.BLUE)
				.toString();
		COLOR_HIGHLIGHT = (new StringBuilder()).append(ChatColor.GREEN)
				.toString();
		COLOR_WARNING = (new StringBuilder()).append(ChatColor.YELLOW)
				.toString();
		COLOR_ERROR = (new StringBuilder()).append(ChatColor.RED).toString();
		COLOR_BYSTANDER = (new StringBuilder()).append(ChatColor.DARK_AQUA)
				.toString();
		COLOR_MURDER = (new StringBuilder()).append(ChatColor.DARK_RED)
				.toString();
		PREFIX_PLUGIN = (new StringBuilder()).append(ChatColor.DARK_GREEN)
				.append("[Murder] ").append(COLOR_MAIN).toString();
		PREFIX_DEBUG = (new StringBuilder()).append(ChatColor.DARK_GREEN)
				.append("[Murder] ").append(ChatColor.GOLD).append(" [DEBUG] ")
				.append(COLOR_MAIN).toString();
		ERROR_TOOMANYARGUMENTS = (new StringBuilder(
				String.valueOf(PREFIX_PLUGIN))).append(COLOR_WARNING)
				.append("Too many arguments.").toString();
		ERROR_NOTENOUGHARGUMENTS = (new StringBuilder(
				String.valueOf(PREFIX_PLUGIN))).append(COLOR_WARNING)
				.append("Not enough arguments.").toString();
		ERROR_ARENANOTFOUND = (new StringBuilder(String.valueOf(PREFIX_PLUGIN)))
				.append(COLOR_WARNING).append("Arena not found.").toString();
		ERROR_NOTENOUGHPLAYERSTOCONTINUE = (new StringBuilder(
				String.valueOf(PREFIX_PLUGIN))).append(COLOR_HIGHLIGHT)
				.append("Not enough player to continue the match.").toString();
		ERROR_NOTENOUGHPLAYERSTOSTART = (new StringBuilder(
				String.valueOf(PREFIX_PLUGIN))).append(COLOR_HIGHLIGHT)
				.append("Not enough player to start the match.").toString();
	}
}