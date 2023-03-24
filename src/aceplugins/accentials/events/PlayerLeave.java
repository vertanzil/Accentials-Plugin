package aceplugins.accentials.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import aceplugins.accentials.Accentials;

public class PlayerLeave implements Listener{
	@EventHandler
	public void PlayerLeaveEvent(PlayerQuitEvent event) {
		Accentials.getLog().leave(event);
		Accentials accentials = Accentials.getMain();
		Player p = event.getPlayer();
		
		// Unload player from ram
		accentials.getPlayerManager().unloadPlayer(p);
		
		// Set the quit message
		String message = accentials.getAceConfig().get("server.leave");
		message = message.replaceAll("&", ChatColor.COLOR_CHAR + "");
		message = message.replaceAll("%player%", p.getDisplayName());
		event.setQuitMessage(message);
	}
}
