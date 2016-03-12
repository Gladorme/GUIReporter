package fr.sanglad.GUIReporter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GUI_Command implements CommandExecutor {

	private GUIReporter plugin;
	
	public GUI_Command(GUIReporter main) {
		plugin = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			plugin.reloadConfig();
			player.sendMessage("§bGUIReporter§a restarted successfully !");
		}			
		return true;
	}

}
