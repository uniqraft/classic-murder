package com.gmail.vapidlinus.murder.commandexecutors;

import com.gmail.vapidlinus.murder.main.Murder;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MCommandExecutor implements CommandExecutor {
	private Murder plugin;
	private HashMap<String, MCommand> commands;

	public MCommandExecutor(Murder plugin) {
		this.plugin = plugin;
		commands = new HashMap<String, MCommand>();
		registerCommands();
	}

	private void registerCommands() {
		commands.put("listplayers", new MCommandListMPlayers());
		commands.put("forcestart", new MCommandForceStartMatch());

		commands.put("listarenas", new MCommandListArenas());

		MCommandArena mca = new MCommandArena();
		commands.put("arena", mca);
		commands.put("a", mca);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp()) {
			return true;
		}
		if ((command.getName().equalsIgnoreCase("murder")) && (args.length > 0)) {
			for (String s : commands.keySet()) {
				if (args[0].equalsIgnoreCase(s)) {
					String[] newArgs = new String[args.length - 1];
					for (int i = 0; i < newArgs.length; i++) {
						newArgs[i] = args[(i + 1)];
					}
					((MCommand) commands.get(s)).execute(sender, newArgs, plugin);
				}
			}
		}
		return true;
	}
}