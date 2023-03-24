package aceplugins.accentials.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.players.AccentialsPlayer;

public class PlayerJoin implements Listener {
	Accentials accentials = Accentials.getMain();
	
	@EventHandler
	public void JoinEvent(PlayerJoinEvent event) {
		Accentials.getLog().joined(event);
		
		Player p = event.getPlayer();
		AccentialsPlayer ap = accentials.getPlayerManager().getPlayer(p);
		
		if(p.getName().equalsIgnoreCase("aceplugins")) {
			Accentials.getLog().info("The creator of Accentials joined!");
			p.sendMessage(ChatColor.COLOR_CHAR + "eThis server uses Accentials :)");
		}
		
		if(ap.isNew()) {
			String message = accentials.getAceConfig().get("server.join.new");
			message = message.replaceAll("&", ChatColor.COLOR_CHAR + "");
			message = message.replaceAll("%player%", p.getDisplayName());
			event.setJoinMessage(message);
			
			// TODO give player default kit
			// TODO warp to default warp
		} else {
			String message = accentials.getAceConfig().get("server.join.old");
			message = message.replaceAll("&", ChatColor.COLOR_CHAR + "");
			message = message.replaceAll("%player%", p.getDisplayName());
			event.setJoinMessage(message);
		}
	}
}
