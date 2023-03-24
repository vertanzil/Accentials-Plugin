package aceplugins.accentials.events;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import aceplugins.AP;
import aceplugins.accentials.Accentials;
import aceplugins.accentials.players.AccentialsPlayer;

public class SignCreate implements Listener{
	Accentials main = Accentials.getMain();
	@EventHandler
	public void signCreate(SignChangeEvent e) {
		String[] l = e.getLines();
		AccentialsPlayer ap = main.getPlayerManager().getPlayer(e.getPlayer());
		
		if(l[0].equalsIgnoreCase("[buy]")) {
			if(isInteger(l[1]) && isMaterial(l[2]) && isDouble(l[3]) && ap.hasPermission("accentials.sign.create.buy")) {
				e.setLine(0, ChatColor.GREEN + "[Buy]");
				e.setLine(2, exactMaterial(l[2]));
				e.setLine(3, addCur(Double.parseDouble(l[3])));
				e.getPlayer()
					.sendMessage(main.getMessageManager().getMessage("sign.create.success")
							.replace("%type%", "Buy"));
			} else {
				e.setLine(0, ChatColor.RED + "[Buy]");
				e.getPlayer().sendMessage(main.getMessageManager().getMessage("sign.create.failed"));
			}
		} else if(l[0].equalsIgnoreCase("[sell]")) {
			if(isInteger(l[1]) && isMaterial(l[2]) && isDouble(l[3]) && ap.hasPermission("accentials.sign.create.sell")) {
				if(addCur(Double.parseDouble(l[3])).length() > 15) {
					e.setLine(0, ChatColor.RED + "[Sell]");
					e.getPlayer().sendMessage(main.getMessageManager().getMessage("sign.create.failed"));
					return;
				}
				e.setLine(0, ChatColor.GREEN + "[Sell]");
				e.setLine(2, exactMaterial(l[2]));
				e.setLine(3, addCur(Double.parseDouble(l[3])));
				e.getPlayer()
					.sendMessage(main.getMessageManager().getMessage("sign.create.success").
							replace("%type%", "Sell"));
			} else {
				e.setLine(0, ChatColor.RED + "[Sell]");
				e.getPlayer().sendMessage(main.getMessageManager().getMessage("sign.create.failed"));
			}
		} else if(l[0].equalsIgnoreCase("[free]")) {
			if(isMaterial(l[1]) && ap.hasPermission("accentials.sign.create.free")) {
				e.setLine(0, ChatColor.GREEN + "[Free]");
				e.setLine(1, exactMaterial(l[1]));
				e.setLine(2, "");
				e.setLine(3, "");
				e.getPlayer()
				.sendMessage(main.getMessageManager().getMessage("sign.create.success").replace("%type%", "Sell"));
			} else {
				e.setLine(0, ChatColor.RED + "[Free]");
				e.getPlayer().sendMessage(main.getMessageManager().getMessage("sign.create.failed"));
			}
		} else if(l[0].equalsIgnoreCase("[trash]")) {
			if(ap.hasPermission("accentials.sign.create.trash")) {
				e.setLine(0, ChatColor.GREEN + "[Trash]");
				e.getPlayer()
				.sendMessage(main.getMessageManager().getMessage("sign.create.success").replace("%type%", "Trash"));
			} else {
				e.setLine(0, ChatColor.RED + "[Trash]");
				e.getPlayer().sendMessage(main.getMessageManager().getMessage("sign.create.failed"));
			}
		} else if(l[0].equalsIgnoreCase("[warp]")) {
			if(Accentials.getMain().getWarpManager().warpExists(l[1]) && ap.hasPermission("accentials.sign.create.warp")) {
				e.setLine(0, ChatColor.GREEN + "[Warp]");
				e.setLine(1, Accentials.getMain().getWarpManager().exactName(l[1]));
				e.getPlayer()
				.sendMessage(main.getMessageManager().getMessage("sign.create.success").replace("%type%", "Warp"));
			}else {
				e.setLine(0, ChatColor.RED + "[Warp]");
				e.getPlayer().sendMessage(main.getMessageManager().getMessage("sign.create.failed"));
			}
		} else if(l[0].equalsIgnoreCase("[kit]")) {
			if(Accentials.getMain().getKitManager().kitExist(l[1]) && ap.hasPermission("accentials.sign.create.kit")) {
				e.setLine(0, ChatColor.GREEN + "[Kit]");
				e.setLine(1, Accentials.getMain().getKitManager().exactName(l[1]));
				e.getPlayer()
				.sendMessage(main.getMessageManager().getMessage("sign.create.success").replace("%type%", "Kit"));
			}else {
				e.setLine(0, ChatColor.RED + "[Kit]");
				e.getPlayer().sendMessage(main.getMessageManager().getMessage("sign.create.failed"));
			}
		}
		
		if(ap.hasPermission("accentials.sign.create.color")) {
			for(int i = 0; i != 4; i++) {
				e.setLine(i, AP.color(l[i]));
			}
		}
	}
	
	public String addCur(double price) {
		DecimalFormat df = new DecimalFormat(",##0.00");
		if(Accentials.getMain().getAceConfig().get("economy.cur-place").equalsIgnoreCase("after")) {
			return df.format(price) + Accentials.getMain().getAceConfig().get("economy.currency");
		} else {
			return Accentials.getMain().getAceConfig().get("economy.currency") + df.format(price);
		}
	}
	
	@SuppressWarnings("deprecation")
	public String exactMaterial(String arg0) {
		if(arg0.contains(":")) {
			String a = arg0.substring(0, arg0.indexOf(":"));
			String b = arg0.substring(arg0.indexOf(":") + 1);
			return exactMaterial(a) + ":" + b;
		}
		if(!isMaterial(arg0))
			return "Air";
		Material m = null;
		if(isInteger(arg0))
			m = Material.getMaterial(Integer.parseInt(arg0));
		else
			m = Material.matchMaterial(arg0.toUpperCase());
		String name = m.name().replace("_", " ");
		String[] ret = upperFirst(name.split(" "));
		String finalRet = "";
		for(String a : ret) {
			finalRet += a + " ";
		}
		return finalRet.substring(0, finalRet.length() - 1);
	}
	
	@SuppressWarnings("deprecation")
	public boolean isMaterial(String arg0) {
		if(arg0.contains(":")) {
			String a = arg0.substring(0, arg0.indexOf(":"));
			return isMaterial(a);
		}
		Material m = null;
		if(isInteger(arg0))
			m = Material.getMaterial(Integer.parseInt(arg0));
		else
			m = Material.matchMaterial(arg0.toUpperCase());
		if(m != null)
			return true;
		return false;
	}
	
	public String[] upperFirst(String[] arg0) {
		String[] ret = arg0;
		for(int i = 0; i != ret.length; i++) {
			String cur = ret[i];
			String a = cur.substring(0, 1).toUpperCase();
			String b = cur.substring(1).toLowerCase();
			ret[i] = a + b;
		}
		return ret;
	}
	
	public boolean isInteger(String arg0) {
		try {
			Integer.parseInt(arg0);
		}catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public boolean isDouble(String arg0) {
		try {
			Double.parseDouble(arg0);
		}catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
}
