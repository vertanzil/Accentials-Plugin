package aceplugins.accentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import aceplugins.accentials.logging.AccentialsLogger;
import aceplugins.accentials.players.AccentialsPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class Accentials implements CommandExecutor{
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		aceplugins.accentials.Accentials main = aceplugins.accentials.Accentials.getMain();
		AccentialsLogger log = aceplugins.accentials.Accentials.getLog();
		log.command(sender, commandLabel, args);
		// TODO GUI
		// / ######################### Groups ######################### \
		// | /accentials group [group] addperm [perm] add     # 5 Args  |
		// | /accentials group [group] delperm [perm] delete  # 5 Args  |
		// | /accentials group [group] prefix [val / remove]  # 4 Args  |
		// | /accentials group [group] suffix [val / remove]  # 4 Args  |
		// | /accentials group [group] delete confirm         # 4 Args  |
		// | /accentials group [group] delete                 # 3 Args  |
		// \ ########################################################## /
		
		// / ########################## Player ########################## \
		// | /accentials player [player] addperm [perm]         # 4 Args  |
		// | /accentials player [player] delperm [perm]         # 4 Args  |
		// | /accentials player [player] prefix [val / remove]  # 4 Args  |
		// | /accentials player [player] suffix [val / remove]  # 4 args  |
		// | /accentials player [player] cooldown [kit]         # 4 args  |
		// | /accentials player [player] setbal [balance]       # 4 args  |
		// | /accentials player [player] takebal [amount]       # 4 args  |
		// | /accentials player [player] addbal [amount]        # 4 args  |
		// | /accentials player [player] balance                # 3 args  |
		// \ ############################################################ /
		
		// / ###################### Kits ###################### \
		// | /accentials kit [kit] edit               # 3 Args  |
		// | /accentials kit [kit] cooldown [amount]  # 4 Args  |
		// | /accentials kit [kit] delete             # 3 Args  |
		// | /accentials kit create [kit]             # 3 Args  |
		// | /accentials kit [kit] delete             # 3 Args  |
		// | /accentials kit reload                   # 2 Args  |
		// \ ################################################## /
		
		// /accentials reload
		if(!(sender instanceof Player)) {
			sender.sendMessage(main.getMessageManager().getMessage("bad.console").replaceAll("&", ChatColor.COLOR_CHAR + ""));
			return true;
		}
		
		Player p = (Player) sender;
		AccentialsPlayer ap = main.getPlayerManager().getPlayer(p);
		
		if(!sender.hasPermission("accentials.command.accentials") || !sender.isOp()) {
			sender.sendMessage(main.getMessageManager().getMessage("bad.permission"));
			return true;
		}
		
		if(args.length > 0) {
			String firstArg = args[0];
			String lastArg = args[args.length - 1];
			if(lastArg.equalsIgnoreCase("exit")) {
				ap.setChannel("global");
				main.unmuteChat(p);
				clearChat(p);
				sender.sendMessage(ChatColor.GRAY + "Chat resumed");
			}
			
			if(firstArg.equalsIgnoreCase("player")) { // Edit a player
				
			}else if(firstArg.equalsIgnoreCase("kit")) { // Editing a kit
				
			}else if(firstArg.equalsIgnoreCase("group")) { // Editing a permission group
				
			} else if(firstArg.equalsIgnoreCase("version")) {
				showTitle(p, ChatColor.GREEN + "Accentials version " + main.getVersion(), ChatColor.YELLOW + "https://dev.bukkit.org/projects/accentials/");
				showHelp(p, ap);
			} else if(firstArg.equalsIgnoreCase("reload")) {
				main.reload();
				showTitle(p, ChatColor.GREEN + "Successfully reloaded", "");
				ap = main.getPlayerManager().getPlayer(p);
				showHelp(p, ap);
			}
		} else {
			showHelp(p, ap);
		}
		return true;
	}
	
	public void editPlayers(Player p, String[] args) {
		if(args.length == 1) {
			clearChat(p);
		}
	}
	
	public void showHelp(Player p, AccentialsPlayer ap) {
		aceplugins.accentials.Accentials main = aceplugins.accentials.Accentials.getMain();
		
		ap.setChannel("cmd: /accentials");
		clearChat(p);
		main.muteChat(p);
		p.sendMessage(ChatColor.GRAY + "Chat muted. Type exit at any time to leave.");
		p.sendMessage(ChatColor.YELLOW + "Please click what to edit:");
		CommandText(p, " Edit Player", ChatColor.YELLOW, "/accentials player", "Edit a player", ChatColor.GRAY);
		CommandText(p, " Edit Group", ChatColor.YELLOW, "/accentials group", "Edit a permission group", ChatColor.GRAY);
		CommandText(p, " Edit Kit", ChatColor.YELLOW, "/accentials kit", "Edit a kit", ChatColor.GRAY);
		CommandText(p, " Version", ChatColor.YELLOW, "/accentials version", "Check the version of accentials", ChatColor.GRAY);
		CommandText(p, " Reload", ChatColor.YELLOW, "/accentials reload", "Reload all of Accentials", ChatColor.GRAY);
	}
	
	public void showTitle(Player p, String title, String subtitle) {
		CraftPlayer cp = (CraftPlayer) p;
		cp.sendTitle(title, subtitle);
	}
	
	public void console(CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "Console mode ACTIVE");
	}
	
	public void clearChat(CommandSender arg0) {
		for(int i = 0; i != 100; i++) {
			arg0.sendMessage(" ");
		}
	}
	
	public void CommandText(Player p, String text, ChatColor textColor, String cmd, String hover, ChatColor hoverColor) {
		IChatBaseComponent comp = ChatSerializer.a("[\"\",{"
				+ "\"text\":\" " + textColor + text.replace("\"", "\\\"") + "\","
				+ "\"color\":\"" + textColor.name() + "\","
				+ "\"clickEvent\":{"
					+ "\"action\":\"run_command\","
					+ "\"value\":\"" + cmd + "\"},"
				+ "\"hoverEvent\":{"
					+ "\"action\":\"show_text\","
					+ "\"value\":{"
						+ "\"text\":\"\","
						+ "\"extra\":[{"
							+ "\"text\":\""+ hoverColor + hover.replace("\"", "\\\"") + "\","
							+ "\"color\":\"" + hoverColor.name() + "\""
						+ "}]"
					+ "}"
				+ "}}]");
	    PacketPlayOutChat packet = new PacketPlayOutChat(comp);
	    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
}
