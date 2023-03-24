package aceplugins.accentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.logging.AccentialsLogger;
import aceplugins.accentials.players.AccentialsPlayer;

public class SetWarp implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AccentialsLogger log = aceplugins.accentials.Accentials.getLog();
		log.command(sender, commandLabel, args);
		
		if(!(sender instanceof Player))
			return true;
		
		// Initiate player
		Player p = (Player) sender;
		AccentialsPlayer accentialsPlayer = Accentials.getMain().getPlayerManager().getPlayer(p);
		
		// Check they have permission
		if(accentialsPlayer.hasPermission("accentials.command.setwarp")) {
			if(args.length == 1) { // Check they put a name
				if(Accentials.getMain().getWarpManager().createWarp(args[0], p.getLocation())) {
					p.sendMessage(ChatColor.COLOR_CHAR + "aWarp successfully set");
				}else {
					// There was an error :/
					p.sendMessage(ChatColor.COLOR_CHAR + "cSorry, there was an error creating the warp.");
					p.sendMessage(ChatColor.COLOR_CHAR + "cContact the owner to check log.txt");
				}
			}else {
				p.sendMessage(ChatColor.COLOR_CHAR + "cUsage: /setwarp [name]");
			}
		}else {
			p.sendMessage(ChatColor.COLOR_CHAR + "cSorry, you do not have permission to run this command");
		}
		return true;
	}
}