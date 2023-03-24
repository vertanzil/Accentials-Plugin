package aceplugins.accentials.commands;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.logging.AccentialsLogger;
import aceplugins.accentials.players.AccentialsPlayer;

public class Head implements CommandExecutor {
	Accentials main = Accentials.getMain();
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AccentialsLogger log = aceplugins.accentials.Accentials.getLog();
		log.command(sender, commandLabel, args);
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(main.getMessageManager().getMessage("bad.console"));
			return true;
		}
		
		if(args.length == 1) {
			Player p = (Player) sender;
			AccentialsPlayer ap = main.getPlayerManager().getPlayer(p);
			if(ap.hasPermission("accentials.command.skull.other")) {
				ItemStack a = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
				SkullMeta m = (SkullMeta) a.getItemMeta();
				m.setOwner(args[0]);
				a.setItemMeta(m);
				p.getInventory().addItem(a);
				sender.sendMessage(main.getMessageManager().getMessage("skull.other").replace("%player%", args[0]));
			}else {
				sender.sendMessage(main.getMessageManager().getMessage("bad.permission"));
			}
		} else {
			Player p = (Player) sender;
			AccentialsPlayer ap = main.getPlayerManager().getPlayer(p);
			if(ap.hasPermission("accentials.command.skull")) {
				ItemStack a = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
				SkullMeta m = (SkullMeta) a.getItemMeta();
				m.setOwner(sender.getName());
				a.setItemMeta(m);
				p.getInventory().addItem(a);
				sender.sendMessage(main.getMessageManager().getMessage("skull.own").replace("%player%", p.getDisplayName()));
			}else {
				sender.sendMessage(main.getMessageManager().getMessage("bad.permission"));
			}
		}
		return true;
	}
}
