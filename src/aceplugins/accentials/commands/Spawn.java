package aceplugins.accentials.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.logging.AccentialsLogger;

public class Spawn implements CommandExecutor {
	Accentials main = Accentials.getMain();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AccentialsLogger log = aceplugins.accentials.Accentials.getLog();
		log.command(sender, commandLabel, args);
		
		if(args.length == 1) {
			if(checkPermission(sender, "accentials.command.spawn.other")) {
				if(isOnline(args[0])) {
					getPlyer(args[0]).teleport(main.getWarpManager().getWarp("spawn"));
					sender.sendMessage(main.getMessageManager().getMessage("spawn.other")
							.replace("%other%", args[0]));
				}else {
					sender.sendMessage(main.getMessageManager().getMessage("bad.player"));
				}
			} else {
				sender.sendMessage(main.getMessageManager().getMessage("bad.permission"));
			}
		}else {
			if(!(sender instanceof Player)) {
				sender.sendMessage(main.getMessageManager().getMessage("bad.console"));
				return true;
			}
			
			Player p = (Player) sender;
			if(checkPermission(sender, "accentials.command.spawn")) {
				p.teleport(main.getWarpManager().getWarp("spawn"));
				sender.sendMessage(main.getMessageManager().getMessage("spawn.own")
						.replace("%player%", p.getDisplayName()));
			} else {
				sender.sendMessage(main.getMessageManager().getMessage("bad.permission"));
			}
		}
		return true;
	}
	
	public boolean checkPermission(CommandSender sender, String permission) {
		if(sender instanceof Player) {
			return main.getPlayerManager().getPlayer((Player) sender).hasPermission(permission);
		} else {
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Player getPlyer(String arg0) {
		List<Player> online = (List<Player>) Bukkit.getOnlinePlayers();
		for(Player p : online) {
			if(p.getName().equalsIgnoreCase(arg0))
				return p;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isOnline(String arg0) {
		List<Player> online = (List<Player>) Bukkit.getOnlinePlayers();
		for(Player p : online) {
			if(p.getName().equalsIgnoreCase(arg0))
				return true;
		}
		return false;
	}
}
