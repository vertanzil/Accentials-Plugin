package aceplugins.accentials.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import aceplugins.AP;
import aceplugins.accentials.Accentials;
import aceplugins.accentials.logging.AccentialsLogger;
import aceplugins.accentials.players.AccentialsPlayer;

public class Balance implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AccentialsLogger log = aceplugins.accentials.Accentials.getLog();
		log.command(sender, commandLabel, args);
		
		if(args.length == 1) {
			boolean p = false;
			if(!(sender instanceof Player))
				p = true;
			else
				p = Accentials.getMain().getPlayerManager().getPlayer((Player) sender).hasPermission("accentials.command.balance.other");
			
			if(p) {
				if(isPlayer(args[0])) {
					String b = Accentials.getMain().getMessageManager().getMessage("balance.other");
					b = b.replace("%player%", getPlayer(args[0]).getPrefixedNickname());
					b = b.replace("%balance%", getPlayer(args[0]).getStringBalance());
					sender.sendMessage(AP.color(b));
				} else {
					
				}
			} else {
				sender.sendMessage(AP.color(Accentials.getMain().getMessageManager().getMessage("bad.permission")));
			}
		} else {
			if(!(sender instanceof Player)) {
				String b = Accentials.getMain().getMessageManager().getMessage("balance.self");
				sender.sendMessage(b.replace("%balance%", "Infinite"));
				return true;
			}
			
			Player p = (Player) sender;
			AccentialsPlayer ap = Accentials.getMain().getPlayerManager().getPlayer(p);
			if(ap.hasPermission("accentials.command.balance")) {
				String b = Accentials.getMain().getMessageManager().getMessage("balance.self");
				b = b.replace("%balance%", ap.getStringBalance());
				p.sendMessage(AP.color(b));
			} else {
				p.sendMessage(AP.color(Accentials.getMain().getMessageManager().getMessage("bad.permission")));
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public AccentialsPlayer getPlayer(String arg0) {
		List<Player> online = (List<Player>) Bukkit.getOnlinePlayers();
		for(Player p : online) {
			if(p.getName().equalsIgnoreCase(arg0))
				return Accentials.getMain().getPlayerManager().getPlayer(p);
		}
		
		OfflinePlayer[] offline = Bukkit.getOfflinePlayers();
		for(OfflinePlayer p : offline) {
			if(p.getName().equalsIgnoreCase(arg0))
				return Accentials.getMain().getPlayerManager().getPlayer(p);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isPlayer(String arg0) {
		List<Player> online = (List<Player>) Bukkit.getOnlinePlayers();
		for(Player p : online) {
			if(p.getName().equalsIgnoreCase(arg0))
				return true;
		}
		
		OfflinePlayer[] offline = Bukkit.getOfflinePlayers();
		for(OfflinePlayer p : offline) {
			if(p.getName().equalsIgnoreCase(arg0))
				return true;
		}
		
		return false;
	}
}
