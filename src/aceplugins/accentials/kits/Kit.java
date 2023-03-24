package aceplugins.accentials.kits;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import aceplugins.accentials.Accentials;

public class Kit {
	File file;
	String name;
	long time = 0;
	List<ItemStack> items = new ArrayList<>();
	
	// Main loader
	public Kit(String arg0) {
		name = arg0;
		file = new File(Accentials.getMain().getDataFolder(), name + ".yml");
	}
	
	// Load in kit
	@SuppressWarnings("deprecation")
	public void load() {
		try {
			Scanner scanner = new Scanner(file);
			ItemStack in = new ItemStack(Material.STONE);
			while(scanner.hasNextLine()) {
				String l = scanner.nextLine();
				for(int x = 0; x != 2; x++) {
					if(!l.contains("  ") && in != null) {
						items.add(in);
						in = null;
					}else if((l.contains(": []") || l.contains(":[]")) && !(in != null)) {
						String item = l.substring(0, l.lastIndexOf(":"));
						if(item.contains(":")) { // Contains data
							String[] itemData = item.split(":");
							if(isInt(itemData[0])) {
								in = new ItemStack(Material.getMaterial(Integer.parseInt(itemData[0])));
							}else {
								in = new ItemStack(Material.getMaterial(itemData[0]));
							}
							if(isInt(itemData[1])) {
								MaterialData data = in.getData();
								data.setData((byte) Integer.parseInt(itemData[1]));
								in.setData(data);
							}
						}else { // Doesn't contain data
							if(isInt(item)) {
								in = new ItemStack(Material.getMaterial(Integer.parseInt(item)));
							}else {
								in = new ItemStack(Material.getMaterial(item));
							}
						}
						break;
					}else if(in != null) {
						if(l.contains("name")) {
							ItemMeta meta = in.getItemMeta();
							meta.setDisplayName(l.substring(l.indexOf(":", 0) + 2).replace('&', ChatColor.COLOR_CHAR));
							in.setItemMeta(meta);
						}else if(l.contains("lore")) {
							ItemMeta meta = in.getItemMeta();
							List<String> lore = meta.getLore();
							lore.add(l.substring(l.indexOf(":", 0) + 2).replace('&', ChatColor.COLOR_CHAR));
							in.setItemMeta(meta);
						}else if(l.contains("enchantment")) {
							
						}else if(l.contains("amount")) {
							try {
								in.setAmount(Integer.parseInt(l.substring(l.indexOf(":") + 2)));
							}catch(NumberFormatException e) {
								Accentials.getLog().info("Invalid amount on kit item in kit " + name);
							}
						}
						break;
					}else {
						if(l.contains("timeout")) {
							try {
								time = Long.parseLong(l.substring(l.indexOf(":") + 2));
							}catch(NumberFormatException e) {
								Accentials.getLog().info("Invalid timeout on kit " + name);
							}
						}
					}
				}
			}
			scanner.close();
		}catch(Exception e) {
			Accentials.getLog().info("Kit named \"" + name + "\" failed to load.. See log.txt");
			Accentials.getLog().error(e);
		}
	}
	
	// TODO write this
	// Used to write the kit
	public void write() {
		try {
			// Delete the file so we can write
			file.delete();
			
			// Open the writer
			PrintStream ps = new PrintStream(file);
			
			// Write the timeout
			ps.println("timeout: " + time);
			
			// Write the items
			for(ItemStack e : items) {
				ps.println(e.getType().toString() + ":" + e.getData() + ": []");
				ps.println("  name: " + e.getItemMeta().getDisplayName());
				// TODO add enchantments
			}
			
			// Finish off
			ps.flush();
			ps.close();
		}catch(Exception e) {
			Accentials.getLog().info("Error writing kit " + name + ". See log.txt for detials");
			Accentials.getLog().error(e);
		}
	}
	
	// Used to update items in-game
	public void setItems(Inventory arg0) {
		items = new ArrayList<>();
		for(ItemStack i : arg0.getContents()) {
			items.add(i);
		}
		write();
	}
	
	// Used for updating timeout in game
	public void setTimeout(long arg0) {
		time = arg0;
		write();
	}
	
	// Reload the kit
	public void reload() {
		time = 0;
		items = new ArrayList<>();
		load();
	}
	
	// Used to give the player the kit
	public int give(Player player) {
		boolean drop = false;
		for(ItemStack i : items) {
			if(!drop) {
				if(player.getInventory().firstEmpty() == -1) {
					// Full ?
				}
				player.getInventory().addItem(i);
			}else {
				// TODO drop excess
			}
		}
		if(drop)
			return 0;
		return 1;
	}
	
	// Check if it is an int
	private boolean isInt(String arg0) {
		try {
			Integer.parseInt(arg0);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
}
