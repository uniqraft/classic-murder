package com.gmail.vapidlinus.murder.commandexecutors;

import com.gmail.vapidlinus.murder.main.MPlayer;
import com.gmail.vapidlinus.murder.main.Murder;
import com.gmail.vapidlinus.murder.tools.ChatContext;
import org.bukkit.command.CommandSender;

public class MCommandListMPlayers extends MCommand {
	public void execute(CommandSender sender, String[] args, Murder plugin) {
		for (MPlayer mplayer : plugin.getMatch().getMPlayers()) {
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "---");
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "Name: "
					+ mplayer.getName());
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "Murderer: "
					+ mplayer.isMurderer());
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "Gunner: "
					+ mplayer.isGunner());
			sender.sendMessage(ChatContext.PREFIX_PLUGIN + "FireNext: "
					+ mplayer.getFireNext());
		}
	}
}