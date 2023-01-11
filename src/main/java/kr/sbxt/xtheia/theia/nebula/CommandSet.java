package kr.sbxt.xtheia.theia.nebula;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.HashMap;
import java.util.List;

public class CommandSet implements CommandExecutor, TabCompleter
{
	private final static HashMap<String, >
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		NebulaItemModifier a;
		var argsRequired = a.getArguments();
		for (int i = 0; i < args.length; i++)
		{
			final var input = args[i];
			final var parsable = argsRequired[i].parsable(input);
			
		}
		return null;
	}
}
