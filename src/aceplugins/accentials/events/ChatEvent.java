package aceplugins.accentials.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import aceplugins.AP;
import aceplugins.accentials.Accentials;
import aceplugins.accentials.players.AccentialsPlayer;
import aceplugins.api.Permissions;

public class ChatEvent implements Listener {
	Accentials accentials = Accentials.getMain();
	@EventHandler
	public void Chat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		AccentialsPlayer aceP = accentials.getPlayerManager().getPlayer(p);
		
		// Log the message
		Accentials.getLog().info("Player \"" + p.getName() + "\" stated \"" + event.getMessage() + "\"");
		
		//Remove those with muted chat
		for(Player plyr : accentials.getMutedChat()) {
			Accentials.getLog().info("Player \"" + plyr.getName() + "\" isn't recieving messages");
			event.getRecipients().remove(plyr);
		}
		
		if(aceP.getChannel().equalsIgnoreCase("global")) {
			// Get message format from config
			String message = accentials.getAceConfig().get("server.chat.format");
			message = AP.color(message); // Replace & for color code
			
			// Check if player has prefix and put it in
			if(aceP.getPrefix() != null)
				message = message.replace("%prefix%", aceP.getPrefix());
			else
				message = message.replace("%prefix%", "");
			
			// Set player to %s (Bukkit states I have to :/)
			if(p.isOp()) {
				message = message.replace("%player%", ChatColor.COLOR_CHAR + accentials.getAceConfig().get("server.chat.op-prefix") + "%s");
			}else {
				message = message.replace("%player%", "%s");
			}
			
			// Check if player has a suffic and put it in
			if(aceP.getSuffix() != null)
				message = message.replace("%suffix%", aceP.getSuffix());
			else
				message = message.replace("%suffix%", "");
			
			// Set message to %s (Bukkit really -__-)
			message = message.replace("%message%", "%s");
			
			// Check if player can speak in color. If so change & for color code
			if(Permissions.getPlayer(p).hasPermission("accentials.chat.color"))
				event.setMessage(AP.color(event.getMessage()));
			
			// Finish by setting format
			event.setFormat(AP.color(message));
		}else {
			// If there not in global chat cancel it
			event.setCancelled(true);
		}
		
		// Allow commands
		if(aceP.getChannel().contains("cmd: ")) { // Check if channel is a command channel
			String cmd = aceP.getChannel().substring(aceP.getChannel().indexOf("/") + 1);
			cmd += " " + event.getMessage(); // Proccess the command
			p.chat("/" + cmd); // Get player to send command
		}
	}
}
