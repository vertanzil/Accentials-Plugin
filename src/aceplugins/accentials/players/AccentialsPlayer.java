package aceplugins.accentials.players;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import aceplugins.AP;
import aceplugins.accentials.Accentials;
import aceplugins.accentials.config.Configuration;
import aceplugins.accentials.groups.Group;

public class AccentialsPlayer {
	// Set by class
	File userFile;
	Player playerID;
	PermissionAttachment permissionAttach = null;
	boolean offline = false;
	boolean noob = false;
	
	// Global variables
	Configuration config;
	String channel = "global";
	
	// Initiating
	public AccentialsPlayer(Player arg0){
		String player = null;
		boolean online = Boolean.parseBoolean(Accentials.getMain().getAceConfig().get("server.online"));
		if(online)
			player = arg0.getUniqueId().toString();
		else if(!online)
			player = arg0.getName();
		
		userFile = new File(Accentials.getMain().getDataFolder(), "/players/" +  player + ".plyr");
		playerID = arg0;
		
		config = new Configuration(userFile);
	}
	
	public AccentialsPlayer(OfflinePlayer arg0) {
		String player = null;
		boolean online = Boolean.parseBoolean(Accentials.getMain().getAceConfig().get("server.online"));
		if(online)
			player = arg0.getUniqueId().toString();
		else if(!online)
			player = arg0.getName();
		
		userFile = new File(Accentials.getMain().getDataFolder(), "/players/" +  player + ".plyr");
		config = new Configuration(userFile);
		offline = true;
	}
	
	public void load() {
		config.load();
		if(!offline) {
			playerID.setCustomName(AP.color(getPrefixedNickname()));
			playerID.setDisplayName(AP.color(getPrefixedNickname()));
		}
	}
	
	public void loadPermissions() {
		if(offline)
			return;
		
		if(permissionAttach != null)
			playerID.removeAttachment(permissionAttach);
		
		permissionAttach = playerID.addAttachment(Accentials.getMain());
		for(String g : config.getList("groups")) { // Load group permissions
			Group group = Accentials.getMain().getGroupManager().getGroup(g);
			String[] gPerms = group.getPermissions();
			for(String p : gPerms) {
				permissionAttach.setPermission(p, true);
			}
		}
		for(String p : config.getList("permissions")) { // Additional user permissions
			permissionAttach.setPermission(p, true);
		}
	}
	
	// Writing files
	public void register(){
		try {
			config.copy(AccentialsPlayer.class.getResourceAsStream("default.txt"));
		} catch(IOException e) {
			Accentials.getLog().info("Failed to copy default player config from plugin...");
		}
		config.set("realname", playerID.getName());
		config.set("nickname", playerID.getName());
		config.set("groups", new String[] {Accentials.getMain().getAceConfig().get("default.group")});
		config.set("economy.global", Accentials.getMain().getAceConfig().get("default.balance") + "");
		config.write();
		config.reload();
		noob = true;
	}
	
	public boolean isNew() {
		return noob;
	}
	
	// Get values
	public boolean hasPlayed() {
		return config.exists();
	}
	
	public String getPrefix() {
		if(config.get("prefix").equalsIgnoreCase("%none%") && config.getList("groups").length > 0) {
			String ret = "";
			for(String g : config.getList("groups")) { // Load group permissions
				Group group = Accentials.getMain().getGroupManager().getGroup(g);
				ret = ret +  group.getPrefix();
			}
			return AP.color(ret);
		}
		
		if(config.getList("groups").length == 0 && config.get("prefix").equalsIgnoreCase("%none%")) {
			return AP.color("&c%ERROR%");
		}
		return AP.color(config.get("prefix"));
	}
	
	public String getSuffix() {
		if(config.get("suffix").equalsIgnoreCase("%none%") || config.getList("groups").length > 0) {
			String ret = "";
			for(String g : config.getList("groups")) { // Load group permissions
				Group group = Accentials.getMain().getGroupManager().getGroup(g);
				ret += group.getSuffix();
			}
			return AP.color(ret);
		}
		return AP.color(config.get("suffix"));
	}
	
	public boolean hasPermission(String arg0) {
		if(offline)
			return false;
		
		if(playerID.isOp() || playerID.hasPermission(arg0))
			return true;
		String x = arg0;
		while(x.contains(".")) {
			String i = x.substring(0, x.lastIndexOf(".")) + ".*";
			if(playerID.hasPermission(i))
				return true;
			x = x.substring(0, x.lastIndexOf("."));
		}
		return false;
	}
	
	public String getNickname() {
		return AP.color(config.get("nickname"));
	}
	
	public double getBalance() {
		if(Accentials.getMain().getAceConfig().get("economy.global").contains("f")) {
			if(offline)
				return 0.00;
			return getBalance(playerID.getWorld().getName().toLowerCase());
		} else {
			if(config.keyExists("economy.global"))
				return Double.parseDouble(config.get("economy.global"));
		}
		return 0.00;
	}
	
	public double getBalance(String arg0) {
		if(config.keyExists("economy." + arg0.toLowerCase()))
			return Double.parseDouble(config.get("economy." + arg0.toLowerCase()));
		return 0.00;
	}
	
	public String getPrefixedNickname() {
		if(config.get("realname").equals(getNickname()))
			return config.get("realname");
		String prefix = Accentials.getMain().getAceConfig().get("server.chat.nickname-prefix");
		String nickname = config.get("nickname");
		if(nickname.substring(0, 1).contains("&")) {
			nickname = nickname.substring(0, 2) + prefix + nickname.substring(2);
		} else {
			nickname = prefix + nickname;
		}
		return nickname;
	}
	
	public String getStringBalance() {
		DecimalFormat df = new DecimalFormat(",##0.00");
		String r = df.format(getBalance());
		if(Accentials.getMain().getAceConfig().get("economy.cur-place").equalsIgnoreCase("after")) {
			r = r + Accentials.getMain().getAceConfig().get("economy.currency");
		} else {
			r = Accentials.getMain().getAceConfig().get("economy.currency") + r;
		}
		return r;
	}
	
	public void setBalance(double amount) {
		DecimalFormat df = new DecimalFormat("0.00");
		config.set("economy.global", df.format(amount));
		config.write();
	}
	
	public long kitCooldown(String arg0) {
		if(config.getList("cooldowns") != null) {
			String[] cooldowns = config.getList("cooldowns");
			for(String x : cooldowns) {
				if(x.toLowerCase().contains(arg0))
					return Long.parseLong(x.substring(x.lastIndexOf(":") + 1));
			}
		}
		return 0;
	}
	
	public boolean isBanned() {
		return Boolean.parseBoolean(config.get("ban.active"));
	}
	
	public long getBanTime() {
		return Long.parseLong(config.get("ban.time"));
	}
	
	public String getBanReason() {
		return config.get("ban.reason");
	}
	
	public String getChannel() {
		return channel;
	}
	
	// Setting values
	public void setPrefix(String arg0) {
		config.set("prefix", arg0.replace(ChatColor.COLOR_CHAR + "", "&"));
		config.write();
	}
	
	public void setSuffix(String arg0) {
		config.set("suffix", arg0.replace(ChatColor.COLOR_CHAR + "", "&"));
		config.write();
	}
	
	public void addPermission(String arg0) {
		ArrayList<String> addPerms = new ArrayList<>();
		for(String x : config.getList("permissions")) {
			addPerms.add(x);
		}
		addPerms.add(arg0);
		config.set("permissions", addPerms.toArray(new String[] {}));
		config.write();
		if(!offline)
			loadPermissions();
	}
	
	public void removePermission(String arg0) {
		ArrayList<String> addPerms = new ArrayList<>();
		for(String x : config.getList("permissions")) {
			addPerms.add(x);
		}
		if(addPerms.contains(arg0)) {
			addPerms.remove(arg0);
			config.set("permissions", addPerms.toArray(new String[] {}));
			config.write();
			if(!offline)
				loadPermissions();
		}
	}
	
	public void setNickname(String arg0) {
		config.set("nickname", arg0.replaceAll(ChatColor.COLOR_CHAR + "", "&"));
		config.write();
		if(!offline) {
			playerID.setCustomName(AP.color(getPrefixedNickname()));
			playerID.setDisplayName(AP.color(getPrefixedNickname()));
		}
	}
	
	public void setKitCooldown(String arg0, long arg1) {
		ArrayList<String> newCool = new ArrayList<>();
		for(String x : config.getList("cooldowns")) {
			if(x.contains(arg0 + ":"))
				newCool.add(arg0 + ":" + arg1);
			else
				newCool.add(x);
		}
		if(!newCool.contains(arg0 + ":" + arg1))
			newCool.add(arg0 + ":" + arg1);
		config.set("cooldowns", newCool.toArray(new String[] {}));
		config.write();
	}
	
	public void clearKitCooldown(String arg0) {
		ArrayList<String> newCool = new ArrayList<>();
		for(String x : config.getList("cooldowns")) {
			if(!x.contains(arg0 + ":"))
				newCool.add(x);
		}
		config.set("cooldowns", newCool.toArray(new String[] {}));
		config.write();
	}
	
	public void ban(long arg0, String arg1) {
		config.set("ban.active", "true");
		config.set("ban.time", arg0 + "");
		config.set("ban.reason", arg1);
		config.write();
	}
	
	public void unban() {
		config.set("ban.active", false + "");
		config.set("ban.time", "0");
		config.set("ban.reason", "%");
		config.write();
	}
	
	public void setChannel(String arg0) {
		channel = arg0;
	}
}
