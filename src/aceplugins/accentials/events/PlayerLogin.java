package aceplugins.accentials.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import aceplugins.accentials.Accentials;

public class PlayerLogin implements Listener{
	Accentials accentials = Accentials.getMain();
	
	@EventHandler
	public void login(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		
		if(accentials.getPlayerManager().isRegistered(p)) {
			if(!accentials.getPlayerManager().loadPlayer(p))
				e.disallow(Result.KICK_OTHER,
						ChatColor.COLOR_CHAR + "cFailed to load user data, admins have been made aware");
		}else {
			if(!accentials.getPlayerManager().registerPlayer(p))
				e.disallow(Result.KICK_OTHER,
						ChatColor.COLOR_CHAR + "cFailed to create user data, admins have been made aware");
		}
	}
}
