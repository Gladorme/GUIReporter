package fr.sanglad.GUIReporter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI_Listener implements Listener {
	
	private GUIReporter plugin;
	
	public GUI_Listener(GUIReporter main) {
		plugin = main;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			Inventory inv = event.getClickedInventory();
			ItemStack item = event.getCurrentItem();
			if (inv != null && item != null) {
				if (inv.getName().equals("Report player")) {
					event.setCancelled(true);
					player.closeInventory();
					if (event.getSlot() == 49) {
						plugin.players.remove(player);
					} else {
						player.sendMessage(plugin.getConfig().getString("MsgConfirm").replace("&", "§"));
						for (Player online : Bukkit.getOnlinePlayers()) {
							if (online.isOp() || online.hasPermission("guireporter.notif")) {
								online.sendMessage(plugin.getConfig().getString("MsgNotif").replace("&", "§")
									.replace("%t", Bukkit.getOfflinePlayer(plugin.players.get(player)).getName())
									.replace("%p", player.getName())
									.replace("%r", item.getItemMeta().getDisplayName()));
							}
						}
					}
				}
			}
		}
	}
}
