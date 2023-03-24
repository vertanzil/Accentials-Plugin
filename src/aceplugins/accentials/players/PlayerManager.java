package aceplugins.accentials.players;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import aceplugins.accentials.Accentials;

public class PlayerManager {
	HashMap<String, AccentialsPlayer> loaded;
	public PlayerManager() {
		load();
	}
	
	// Core of PlayerManager
	@SuppressWarnings("unchecked")
	private void load() {
		loaded = new HashMap<>();
		
		// Load all online players
		List<Player> online = (List<Player>) Bukkit.getOnlinePlayers();
		for(Player p : online) {
			try {
				AccentialsPlayer ap = new AccentialsPlayer(p);
				if(!ap.hasPlayed()) {
					ap.register();
				}else {
					ap.load();
				}
				loaded.put(p.getName(), ap);
			}catch(Exception e) {
				Accentials.getLog().error(e);
			}
		}
	}
	
	// Used to reload
	@SuppressWarnings("unchecked")
	public void reload() {
		loaded.clear();
		
		// Load all online players
		List<Player> online = (List<Player>) Bukkit.getOnlinePlayers();
		for(Player p : online) {
			try {
				AccentialsPlayer ap = new AccentialsPlayer(p);
				if(!ap.hasPlayed()) {
					ap.register();
				}else {
					ap.load();
				}
				loaded.put(p.getName(), ap);
			}catch(Exception e) {
				Accentials.getLog().error(e);
			}
		}
	}
	
	// Use this if player is new
	public boolean registerPlayer(Player player) {
		try {
			AccentialsPlayer ap = new AccentialsPlayer(player);
			ap.register();
			loaded.put(player.getName(), ap);
		}catch(Exception e) {
			Accentials.getLog().error(e);
			return false;
		}
		return true;
	}
	
	// Load in a player (Never use)
	public boolean loadPlayer(Player player) {
		try {
			AccentialsPlayer ap = new AccentialsPlayer(player);
			ap.load();
			loaded.put(player.getName(), ap);
		}catch(Exception e) {
			Accentials.getLog().error(e);
			return false;
		}
		return true;
	}
	
	// Used when logging off
	public void unloadPlayer(Player player) {
		loaded.remove(player.getName());
	}
	
	// Get player from list
	public AccentialsPlayer getPlayer(Player player) {
		if(loaded.containsKey(player.getName()))
			return loaded.get(player.getName());
		try {
			loadPlayer(player);
			return loaded.get(player.getName());
		}catch(Exception e) {
			Accentials.getLog().error(e);
		}
		return null;
	}
	
	public AccentialsPlayer getPlayer(OfflinePlayer player) {
		AccentialsPlayer ap = new AccentialsPlayer(player);
		ap.load();
		return ap;
	}
	
	// Check if a player has played before
	public boolean isRegistered(Player player) {
		try {
			if(loaded.containsKey(player.getName()))
				return loaded.get(player.getName()).hasPlayed();
			loadPlayer(player);
			return loaded.get(player.getName()).hasPlayed();
		}catch(Exception e) {
			Accentials.getLog().error(e);
		}
		return false;
	}
	
	public boolean isRegistered(OfflinePlayer player) {
		AccentialsPlayer ap = new AccentialsPlayer(player);
		boolean r = ap.hasPlayed();
		ap = null;
		return r;
	}
}
