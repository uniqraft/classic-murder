package com.gmail.vapidlinus.murder.commandexecutors;

import com.gmail.vapidlinus.murder.main.Murder;
import org.bukkit.command.CommandSender;

public abstract class MCommand {
	public abstract void execute(CommandSender paramCommandSender, String[] paramArrayOfString, Murder paramMurder);
}