package aceplugins.accentials.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import aceplugins.AP;
import aceplugins.accentials.Accentials;
import aceplugins.accentials.players.AccentialsPlayer;

public class SignUse implements Listener {
	@EventHandler
	public void useEvent(PlayerInteractEvent e) {
		if(!(e.getClickedBlock() != null))
			return;
			
		Block b = e.getClickedBlock();
		if(!(b.getState() instanceof Sign))
			return;
		
		Sign s = (Sign) b.getState();
		String[] l = s.getLines();
		if(l[0].equals(ChatColor.GREEN + "[Buy]")) {
			Buy(l, e.getPlayer());
		} else if(l[0].equals(ChatColor.GREEN + "[Sell]")) {
			Sell(l, e.getPlayer());
		} else if(l[0].equals(ChatColor.GREEN + "[Trash]")) {
			Trash(l, e.getPlayer());
		} else if(l[0].equals(ChatColor.GREEN + "[Free]")) {
			Free(l, e.getPlayer());
		} else if(l[0].equals(ChatColor.GREEN + "[Warp]")) {
			e.getPlayer().chat("/warp " + l[1]);
		} else if(l[0].equals(ChatColor.GREEN + "[Kit]")) {
			e.getPlayer().chat("/kit " + l[1]);
		}
	}
	
	public void Buy(String[] l, Player p) {
		AccentialsPlayer ap = Accentials.getMain().getPlayerManager().getPlayer(p);
		if(ap.hasPermission("accentials.sign.use.buy")) {
			if(ap.getBalance() > parsePrice(l[3])) {
				ap.setBalance(ap.getBalance() - parsePrice(l[3]));
				Material m = parseMaterial(l[2]);
				int data = 0;
				if(l[2].contains(":")) {
					String[] a = l[2].split(":");
					m = parseMaterial(a[0]);
					data = parseInt(a[1]);
				}
				int amount = parseInt(l[1]);
				ItemStack is = new ItemStack(m, amount);
				is.setDurability((short) data);
				p.getInventory().addItem(is);
				String msg = Accentials.getMain().getMessageManager().getMessage("sign.use.buy");
				msg = msg.replace("%item%", l[2]);
				msg = msg.replace("%amount%", amount + "");
				msg = msg.replace("%cost%", l[3]);
				p.sendMessage(msg);
				
			} else {
				p.sendMessage(Accentials.getMain().getMessageManager().getMessage("bad.balance"));
			}
		} else {
			p.sendMessage(Accentials.getMain().getMessageManager().getMessage("bad.permission"));
		}
	}
	
	public void Sell(String[] l, Player p) {
		AccentialsPlayer ap = Accentials.getMain().getPlayerManager().getPlayer(p);
		if(ap.hasPermission("accentials.sign.use.sell")) {
			
			Material m = parseMaterial(l[2]);
			int data = 0;
			if(l[2].contains(":")) {
				String[] a = l[2].split(":");
				m = parseMaterial(a[0]);
				data = parseInt(a[1]);
			}
			int amount = parseInt(l[1]);
			ItemStack is = new ItemStack(m, amount);
			is.setDurability((short) data);
			if(p.getInventory().containsAtLeast(is, amount)) {
				ap.setBalance(ap.getBalance() + parsePrice(l[3]));
				p.getInventory().removeItem(is);
				String msg = Accentials.getMain().getMessageManager().getMessage("sign.use.sell");
				msg = msg.replace("%item%", l[2]);
				msg = msg.replace("%amount%", amount + "");
				msg = msg.replace("%cost%", l[3]);
				p.sendMessage(msg);
			} else {
				
			}
		} else {
			p.sendMessage(Accentials.getMain().getMessageManager().getMessage("bad.permission"));
		}
	}
	
	public void Free(String[] l, Player p) {
		AccentialsPlayer ap = Accentials.getMain().getPlayerManager().getPlayer(p);
		if(ap.hasPermission("accentials.sign.use.free")) {
			Material m = parseMaterial(l[1]);
			int data = 0;
			if(l[1].contains(":")) {
				String[] a = l[1].split(":");
				m = parseMaterial(a[0]);
				data = parseInt(a[1]);
			}
			ItemStack is = new ItemStack(m, 64);
			is.setDurability((short) data);
			
			Inventory inv = Bukkit.createInventory(p, 27,
					ChatColor.DARK_PURPLE + "Free " + l[1]);
			for(int i = 0; i != 27; i++) {
				inv.addItem(is);
			}
			
			p.openInventory(inv);
			String msg = Accentials.getMain().getMessageManager().getMessage("sign.use.free");
			msg = msg.replace("%item%", l[1]);
			p.sendMessage(msg);
		}
	}
	
	public void Trash(String[] l, Player p) {
		AccentialsPlayer ap = Accentials.getMain().getPlayerManager().getPlayer(p);
		if(ap.hasPermission("accentials.sign.use.trash")) {
			p.openInventory(Bukkit.createInventory(p, 36, AP.color("&cTrash - Be careful")));
			String msg = Accentials.getMain().getMessageManager().getMessage("sign.use.trash");
			p.sendMessage(msg);
		}
	}
	
	public Material parseMaterial(String l) {
		String a = l;
		l = l.replace(" ", "_");
		l = l.toUpperCase();
		return Material.matchMaterial(a);
	}
	
	public int parseInt(String l) {
		try {
			return Integer.parseInt(l);
		} catch(Exception e) { }
		return 0;
	}
	
	public double parsePrice(String l) {
		String x = l.replace(Accentials.getMain().getAceConfig().get("economy.currency"), "");
		x = x.replaceAll(",", "");
		return Double.parseDouble(x);
	}
}
