package fr.sanglad.GUIReporter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GUI_CommandReport implements CommandExecutor {

	private GUIReporter plugin;
	
	public GUI_CommandReport(GUIReporter main) {
		plugin = main;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				if (!args[0].equals(player.getName())) {
					if (Bukkit.getOfflinePlayer(args[0]) != null) {
						if (plugin.cooldown.containsKey(player)) {
							player.sendMessage(plugin.getConfig().getString("MsgCooldown").replace("&", "§").replace("%s", plugin.cooldown.get(player) + ""));
						} else {
							plugin.cooldown.put(player, plugin.getConfig().getInt("Cooldown"));
							plugin.players.put(player, Bukkit.getOfflinePlayer(args[0]).getUniqueId());
							player.openInventory(plugin.inventoryReporter);
						}
					} else {
						player.sendMessage(plugin.getConfig().getString("MsgUnknowPlayer").replace("&", "§"));
					}
				} else {
					player.sendMessage(plugin.getConfig().getString("MsgYourself").replace("&", "§"));
				}
			} else {
				player.sendMessage(plugin.getConfig().getString("MsgInvalidArg").replace("&", "§"));
			}
		}			
		return true;
	}

}
