package com.gmail.vapidlinus.murder.commandexecutors;

import com.gmail.vapidlinus.murder.main.Murder;
import com.gmail.vapidlinus.murder.tools.ChatContext;
import com.gmail.vapidlinus.murder.tools.Tools;
import org.bukkit.command.CommandSender;

public class MCommandForceStartMatch extends MCommand {
	public void execute(CommandSender sender, String[] args, Murder plugin) {
		if (plugin.getMatch().isStarted()) {
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "Match is already started.");
			return;
		}
		Tools.sendMessageAll(plugin.getServer(), ChatContext.PREFIX_PLUGIN + "Match was forced to start.");
		plugin.getMatch().startMatch();
	}
}