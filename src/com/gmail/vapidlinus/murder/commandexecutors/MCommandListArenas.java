package com.gmail.vapidlinus.murder.commandexecutors;

import com.gmail.vapidlinus.murder.main.Arena;
import com.gmail.vapidlinus.murder.main.Murder;
import com.gmail.vapidlinus.murder.tools.ChatContext;
import org.bukkit.command.CommandSender;

public class MCommandListArenas extends MCommand {
	public void execute(CommandSender sender, String[] args, Murder plugin) {
		String[] arenas = Arena.getPossibleArenas();
		if (arenas.length > 0) {
			for (int i = 0; i < arenas.length; i++) {
				sender.sendMessage(ChatContext.PREFIX_PLUGIN + arenas[i]);
			}
		} else {
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "No arenas!");
		}
	}
}