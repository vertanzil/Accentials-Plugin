package aceplugins.accentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import aceplugins.AP;
import aceplugins.accentials.Accentials;
import aceplugins.accentials.logging.AccentialsLogger;
import aceplugins.accentials.players.AccentialsPlayer;

public class SetSpawn implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AccentialsLogger log = aceplugins.accentials.Accentials.getLog();
		log.command(sender, commandLabel, args);
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(AP.m().getMessageManager().getMessage("bad.console").replaceAll("&", ChatColor.COLOR_CHAR + ""));
			return true;
		}
		
		Player p = (Player) sender;
		AccentialsPlayer ap = AP.m().getPlayerManager().getPlayer(p);
		if(ap.hasPermission("accentials.command.setspawn")) {
			AP.m().getWarpManager().deleteWarp("spawn");
			AP.m().getWarpManager().createWarp("spawn", p.getLocation());
			p.getLocation().getWorld().setSpawnLocation(p.getLocation());
			p.sendMessage(AP.color(Accentials.getMain().getMessageManager().getMessage("spawn.set")));
		} else {
			p.sendMessage(AP.color(Accentials.getMain().getMessageManager().getMessage("bad.permission")));
		}
		return true;
	}
}
