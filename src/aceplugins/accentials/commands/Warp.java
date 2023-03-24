package aceplugins.accentials.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.logging.AccentialsLogger;
import aceplugins.accentials.messages.MessageManager;
import aceplugins.accentials.players.AccentialsPlayer;
import aceplugins.accentials.warps.Warps;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class Warp implements CommandExecutor {
	private Accentials main = Accentials.getMain();
	private Warps warps = main.getWarpManager();
	private MessageManager messages = main.getMessageManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AccentialsLogger log = aceplugins.accentials.Accentials.getLog();
		log.command(sender, commandLabel, args);
		
		// Check atleast one warp is there
		if(warps.getWarps().size() == 0) {
			sender.sendMessage(messages.getMessage("warp.none").replaceAll("%player%", sender.getName()));
			return true;
		}
		
		if(!(sender instanceof Player)) { // Check sender isn't a player
			consoleSender(sender, args);
			return true;
		}
		
		Player p = (Player) sender;
		AccentialsPlayer player = main.getPlayerManager().getPlayer(p);
		
		// Player executing command
		if(args.length == 1) {
			if(warps.warpExists(args[0])) {
				// It exists lets check they can warp there
				if(player.hasPermission("accentials.command.warp." + args[0].toLowerCase())) {
					p.teleport(warps.getWarp(args[0]));
					p.sendMessage(messages.getMessage("warp.own")
							.replaceAll("%warp%", args[0]).replaceAll("%player%", p.getDisplayName()));
				}else {
					p.sendMessage(messages.getMessage("bad.permission").replaceAll("%player%", p.getDisplayName()));
				}
			}else {
				p.sendMessage(messages.getMessage("warp.exist").replaceAll("%player%", p.getDisplayName())
						.replaceAll("%warp%", args[0]));
			}
		}else if(args.length == 2) {
			if(warps.warpExists(args[0])) {
				// It exists lets check they can warp there
				if(player.hasPermission("accentials.command.warp." + args[0].toLowerCase() + ".other")) {
					if(isOnline(args[1])) {
						Player other = Bukkit.getPlayer(args[1]);
						other.teleport(warps.getWarp(args[0]));
						p.sendMessage(messages.getMessage("warp.other").replaceAll("%player%", p.getDisplayName())
								.replaceAll("%other%", other.getDisplayName()).replaceAll("%warp%", args[0]));
					}else {
						p.sendMessage(messages.getMessage("bad.player").replaceAll("%player%", p.getDisplayName())
								.replaceAll("%other%", args[1]));
					}
				}else {
					p.sendMessage(messages.getMessage("bad.permission").replaceAll("%player%", p.getDisplayName()));
				}
			}else {
				p.sendMessage(messages.getMessage("warp.exist").replaceAll("%player%", p.getDisplayName())
						.replaceAll("%warp%", args[0]));
			}
		}else {
			if(player.hasPermission("accentials.command.warp.list")) {
				p.sendMessage(messages.getMessage("warp.list").replaceAll("%player%", p.getDisplayName()));
				if(warps.getWarps(player).size() > 5) {
					// List warps
					p.sendMessage(parser(messages.getMessage("warp.warp-prefix").toUpperCase()) + parseList(warps.getWarps(player)));
				}else {
					// Clikable warps
					for(String s : warps.getWarps(player)) {
						CommandText(p, s, parser(messages.getMessage("warp.warp-prefix").toUpperCase()), "/warp " + s, "Click to warp to " + s, parser(messages.getMessage("warp.warp-prefix").toUpperCase()));
					}
				}
			}else {
				p.sendMessage(messages.getMessage("bad.permission").replaceAll("%player%", p.getDisplayName()));
			}
		}
		return true;
	}
	
	public ChatColor parser(String arg0) {
		ChatColor ret;
		switch (arg0) {
		case "0":
			ret = ChatColor.BLACK;
			break;
		case "1":
			ret = ChatColor.DARK_BLUE;
			break;
		case "2":
			ret = ChatColor.DARK_GREEN;
			break;
		case "3":
			ret = ChatColor.DARK_AQUA;
			break;
		case "4":
			ret = ChatColor.DARK_RED;
			break;
		case "5":
			ret = ChatColor.DARK_PURPLE;
			break;
		case "6":
			ret = ChatColor.GOLD;
			break;
		case "7":
			ret = ChatColor.GRAY;
			break;
		case "8":
			ret = ChatColor.DARK_GRAY;
			break;
		case "9":
			ret = ChatColor.BLUE;
			break;
		case "A":
			ret = ChatColor.GREEN;
			break;
		case "B":
			ret = ChatColor.AQUA;
			break;
		case "C":
			ret = ChatColor.RED;
			break;
		case "D":
			ret = ChatColor.LIGHT_PURPLE;
			break;
		case "E":
			ret = ChatColor.YELLOW;
			break;
		case "F":
			ret = ChatColor.WHITE;
			break;
		default:
			ret = ChatColor.BLACK;
			break;
		}
		return ret;
	}
	
	// This is to manage when console sends commands
	public void consoleSender(CommandSender sender, String[] args) {
		if(args.length == 2) { // Warp other player "/warp spawn AcePlugins"
			if(warps.warpExists(args[0])) {
				// It exists lets check the players on
				if(isOnline(args[1])) {
					Player other = Bukkit.getPlayer(args[1]);
					other.teleport(warps.getWarp(args[0]));
					sender.sendMessage(messages.getMessage("warp.other").replaceAll("%player%", sender.getName())
							.replaceAll("%other%", other.getDisplayName()).replaceAll("%warp%", args[0]));
				}else {
					sender.sendMessage(messages.getMessage("bad.player").replaceAll("%player%", sender.getName())
							.replaceAll("%other%", args[1]));
				}
			}else {
				sender.sendMessage(messages.getMessage("warp.exist").replaceAll("%player%", sender.getName())
						.replaceAll("%warp%", args[0]));
			}
		}else{
			sender.sendMessage(messages.getMessage("warp.list").replaceAll("%player%", sender.getName()));
			sender.sendMessage(ChatColor.valueOf(messages.getMessage("warp.warp-prefix")) + parseList(warps.getWarps()));
		}
	}
	
	public String parseList(List<String> arg0) {
		return arg0.toString().substring(1, arg0.toString().length() - 1);
	}
	
	public void CommandText(Player p, String text, ChatColor textColor, String cmd, String hover, ChatColor hoverColor) {
		IChatBaseComponent comp = ChatSerializer.a("[\"\",{"
				+ "\"text\":\" " + text.replace("\"", "\\\"") + "\","
				+ "\"color\":\"" + textColor.name() + "\","
				+ "\"clickEvent\":{"
					+ "\"action\":\"run_command\","
					+ "\"value\":\"" + cmd + "\"},"
				+ "\"hoverEvent\":{"
					+ "\"action\":\"show_text\","
					+ "\"value\":{"
						+ "\"text\":\"\","
						+ "\"extra\":[{"
							+ "\"text\":\""+ hover.replace("\"", "\\\"") + "\","
							+ "\"color\":\"" + hoverColor.name() + "\""
						+ "}]"
					+ "}"
				+ "}}]");
	    PacketPlayOutChat packet = new PacketPlayOutChat(comp);
	    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
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