package aceplugins.accentials.kits;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.players.AccentialsPlayer;

public class KitManager {
	// TODO List
	// Add names to allow capitals
	// Add exactname search
	// Add kitexists
	// Add get kit
	
	HashMap<String, Kit> kits = new HashMap<>();
	public KitManager() {
		load();
	}
	
	// Use for loading
	public void load() {
		try {
			File folder = new File(Accentials.getMain().getDataFolder(), "/kits/");
			for(File i : folder.listFiles()) {
				String name = i.getName().substring(0, i.getName().length() - 4);
				kits.put(name, new Kit(name));
			}
		}catch(Exception e) {
			Accentials.getLog().info("There was an error loading kits. See log.txt for more info");
			Accentials.getLog().error(e);
		}
	}
	
	// Use for reloading kits
	public void reload() {
		kits.clear();
		load();
	}
	
	// Use for getting all kits
	public String[] getKits() {
		ArrayList<String> ret = new ArrayList<>();
		for(Map.Entry<String, ?> i : kits.entrySet()) {
			ret.add(i.getKey());
		}
		return ret.toArray(new String[] {});
	}
	
	// Use for getting kits for player
	public String[] getKits(AccentialsPlayer arg0) {
		ArrayList<String> ret = new ArrayList<>();
		for(Map.Entry<String, ?> i : kits.entrySet()) {
			if(arg0.hasPermission("accentials.command.kit." + i.getKey())) {
				ret.add(i.getKey());
			}
		}
		return ret.toArray(new String[] {});
	}
	
	// Kit exists
	public boolean kitExist(String arg0) {
		for(String i : getKits()) {
			if(i.equalsIgnoreCase(arg0))
				return true;
		}
		return false;
	}
	
	// Exact name
	public String exactName(String arg0) {
		for(String i : getKits()) {
			if(i.equalsIgnoreCase(arg0))
				return i;
		}
		return null;
	}
}
