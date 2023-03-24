package aceplugins.accentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.logging.AccentialsLogger;
import aceplugins.accentials.players.AccentialsPlayer;

public class SocialSpy implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AccentialsLogger log = aceplugins.accentials.Accentials.getLog();
		log.command(sender, commandLabel, args);
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Accentials.getMain().getMessageManager().getMessage("bad.console"));
			return true;
		}
		
		Player p = (Player) sender;
		AccentialsPlayer ap = Accentials.getMain().getPlayerManager().getPlayer(p);
		if(ap.hasPermission("accentials.command.socialspy")) {
			if(socialSpyStatus(p)) {
				Accentials.getMain().delSocialSpy(p);
			} else {
				Accentials.getMain().addSocialSpy(p);
			}
		}
		return true;
	}
	
	private boolean socialSpyStatus(Player p) {
		Player[] plyrs = Accentials.getMain().getSocialSpy();
		for(Player x : plyrs) {
			if(x.getName().equals(p.getName()))
				return true;
		}
		return false;
	}
}
