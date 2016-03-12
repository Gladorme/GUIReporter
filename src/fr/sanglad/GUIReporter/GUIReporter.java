package fr.sanglad.GUIReporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIReporter extends JavaPlugin {
	
	public static GUIReporter instance;
	
	public Inventory inventoryReporter;
	public HashMap<Player, UUID> players = new HashMap<Player, UUID>();
	public HashMap<Player, Integer> cooldown = new HashMap<Player, Integer>();
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		instance=this;
		getServer().getPluginManager().registerEvents(new GUI_Listener(this), (this));
		
		// Déclaration des commandes
		getCommand("guireporter").setExecutor(new GUI_Command(this));
		getCommand("report").setExecutor(new GUI_CommandReport(this));
		
		saveDefaultConfig();	
		
		// Création de l'inventaire
		inventoryReporter = Bukkit.createInventory(null, 54, "Report player");
		int i = 10;
		if (getConfig().getMapList("items").size() < 22) {
			for (Map<?, ?> m: getConfig().getMapList("items")) {
				if (m.get("item") instanceof String && m.get("name") instanceof String && m.get("lore") instanceof String) {
					inventoryReporter.setItem(i, addItem(
							new ItemStack(Material.getMaterial(Integer.parseInt(((String) m.get("item")).split(":")[0])), 1, Byte.parseByte(((String) m.get("item")).split(":")[1])), 
							ChatColor.translateAlternateColorCodes('&', (String) m.get("name")), 
							ChatColor.translateAlternateColorCodes('&', (String) m.get("lore"))));
					i++;
					if (i == 17) i=19;
					if (i == 26) i=28;
				} else {
					Bukkit.broadcastMessage("§cError: Problem with the config !");
					this.getPluginLoader().disablePlugin(this);
				}			
			}		
		} else {
			Bukkit.broadcastMessage("§cError: Too many reasons in the config !");
			this.getPluginLoader().disablePlugin(this);
		}
		inventoryReporter.setItem(49, addItem(new ItemStack(Material.getMaterial(324), 1), "§cCancel", ""));
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	for (Entry<Player, Integer> entry : cooldown.entrySet()) {
            	    Player player = entry.getKey();
            	    Integer time = entry.getValue();
            	    if (time <= 1) {
            	    	cooldown.remove(player);
            	    } else {
            	    	entry.setValue(time-1);
            	    }            	                	   
            	}
            }
        }, 0L, 20L);
		System.out.print("Start of GUIReporter");
	}
	
	@Override
	public void onDisable() {
		System.out.print("End of GUIReporter");
	}
		
	public ItemStack addItem(ItemStack item, String displayname, String lore) {
		ItemMeta meta = item.getItemMeta();
		if (displayname != "")
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));

		if (lore != "") {
			lore = ChatColor.translateAlternateColorCodes('&', lore);
			String[] loreString = lore.split("\n");
			List<String> loreItem = new ArrayList<String>();
			loreItem.clear();
			for (String loreStringS : loreString) {
				loreItem.add(loreStringS);
			}

			meta.setLore(loreItem);
		}
		
		item.setItemMeta(meta);
		return item;
	}

}
