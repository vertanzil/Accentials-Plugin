package aceplugins.accentials;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import aceplugins.accentials.callback.CallBack;
import aceplugins.accentials.config.Configuration;
import aceplugins.accentials.events.ChatEvent;
import aceplugins.accentials.events.PlayerJoin;
import aceplugins.accentials.events.PlayerLeave;
import aceplugins.accentials.events.PlayerLogin;
import aceplugins.accentials.events.SignCreate;
import aceplugins.accentials.events.SignUse;
import aceplugins.accentials.firstboot.FirstBoot;
import aceplugins.accentials.folders.FolderManager;
import aceplugins.accentials.groups.GroupManager;
import aceplugins.accentials.kits.KitManager;
import aceplugins.accentials.logging.AccentialsLogger;
import aceplugins.accentials.messages.MessageManager;
import aceplugins.accentials.metrics.Metrics;
import aceplugins.accentials.players.PlayerManager;
import aceplugins.accentials.warps.Warps;

public class Accentials extends JavaPlugin{
	AccentialsLogger log;
	Configuration config;
	private static Accentials main;
	PlayerManager playerManager;
	GroupManager groupManager;
	CallBack callBack;
	ArrayList<Player> mutedChat;
	Warps warpManager;
	KitManager kitManager;
	MessageManager messageManager;
	ArrayList<Player> socialspy;
	
	@Override
	public void onEnable() {
		main = this;
		Accentials.getMain().getDataFolder().mkdir();
		
		//Load the logger
		log = new AccentialsLogger();
		
		// Make sure all the folders created
		new FolderManager();
		
		// Load the config
		try {
			config = new Configuration(new File(getDataFolder(), "config.yml"));
			if(!config.exists()) {
				Accentials.getLog().info("Creating files...");
				try {
					config.copy(getClass().getResourceAsStream("default-config.txt"));
				} catch(IOException e) {
					Accentials.getLog().info("Failed to copy default plugin config from plugin...");
				}
				new FirstBoot();
			}
			config.load();
			log.info("plugin.version = " + config.get("plugin.version"));
		} catch (Exception e) {
			log.error(e);
		}
		
		// Load groups
		try {
			groupManager = new GroupManager();
		}catch(Exception e) {
			log.error(e);
		}
		
		// Load PlayerManager
		playerManager = new PlayerManager();
		
		// Of course.. metrics
		try{
	    	Metrics metrics = new Metrics(this);
	    	metrics.start();
		}catch (IOException e){
			log.error(e);
		}
		
		// Now Accentials CallBack
		callBack = new CallBack();
		
		// Warp manager
		warpManager = new Warps();
		
		// Muted chat
		mutedChat = new ArrayList<>();
		
		// Kit manager
		kitManager = new KitManager();
		
		// Message Manager
		messageManager = new MessageManager();
		
		// Register events
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
		getServer().getPluginManager().registerEvents(new ChatEvent(), this);
		getServer().getPluginManager().registerEvents(new SignCreate(), this);
		getServer().getPluginManager().registerEvents(new SignUse(), this);
		getServer().getPluginManager().registerEvents(new PlayerLogin(), this);
		
		// Register commands
		getCommand("accentials").setExecutor(new aceplugins.accentials.commands.Accentials());
		getCommand("setwarp").setExecutor(new aceplugins.accentials.commands.SetWarp());
		getCommand("warp").setExecutor(new aceplugins.accentials.commands.Warp());
		getCommand("setspawn").setExecutor(new aceplugins.accentials.commands.SetSpawn());
		getCommand("spawn").setExecutor(new aceplugins.accentials.commands.Spawn());
		getCommand("head").setExecutor(new aceplugins.accentials.commands.Head());
		getCommand("balance").setExecutor(new aceplugins.accentials.commands.Balance());
		
		// Loaded message
		Accentials.getLog().info("|====================================|");
		Accentials.getLog().info("|             Accentials             |");
		Accentials.getLog().info("| Welcome!                     Gen 2 |");
		Accentials.getLog().info("| If you have just downloaded        |");
		Accentials.getLog().info("| Accentials, please go through the  |");
		Accentials.getLog().info("| configs before using the plugin.   |");
		Accentials.getLog().info("| file path: /plugins/Accentials     |");
		Accentials.getLog().info("|                                    |");
		Accentials.getLog().info("| Docs: https://accentials.pw/docs/  |");
		Accentials.getLog().info("| And More: https://accential.pw     |");
		Accentials.getLog().info("|====================================|");
		Accentials.getLog().info("");
	}
	
	public void reload() {
		Accentials.getLog().info("");
		Accentials.getLog().info("|====================================|");
		Accentials.getLog().info("|             Accentials             |");
		Accentials.getLog().info("| Reloading                    Gen 2 |");
		Accentials.getLog().info("| We understand that from time to    |");
		Accentials.getLog().info("| time you will update the configs   |");
		Accentials.getLog().info("| through ftp, or such. However, we  |");
		Accentials.getLog().info("| suggest using the /accentials      |");
		Accentials.getLog().info("| command to make edits as this is   |");
		Accentials.getLog().info("| safer.                             |");
		Accentials.getLog().info("|====================================|");
		Accentials.getLog().info("");
		
		mutedChat.clear(); // Clear all muted chat
		config.reload(); // Reload config
		groupManager.reload(); // Reload groups
		playerManager.reload(); // Reload players
		kitManager.reload(); // Reload kits
		messageManager.reload(); // Reload messages
	}
	
	@Override
	public void onDisable() {
		Accentials.getLog().info("");
		Accentials.getLog().info("|====================================|");
		Accentials.getLog().info("|             Accentials             |");
		Accentials.getLog().info("| Goodbye!                     Gen 2 |");
		Accentials.getLog().info("| We hope that you are enjoying      |");
		Accentials.getLog().info("| Accentials, generation 2. If not   |");
		Accentials.getLog().info("| let us know how we can improve at  |");
		Accentials.getLog().info("| https://froums.accentials.pw/      |");
		Accentials.getLog().info("|                                    |");
		Accentials.getLog().info("| If you are stuck on the configs    |");
		Accentials.getLog().info("| plase visit:                       |");
		Accentials.getLog().info("| https://accentials.pw/docs/        |");
		Accentials.getLog().info("| or get in touch at:                |");
		Accentials.getLog().info("| https://accentials.pw/contact.php  |");
		Accentials.getLog().info("|====================================|");
		
		callBack.stop();
		log.close();
		
		log = null;
		config = null;
		playerManager = null;
		groupManager = null;
		main = null;
		callBack = null;
	}
	
	// For external access
	public static Accentials getMain() {
		return main;
	}
	
	// Getting log from main
	public AccentialsLogger getLogging() {
		return log;
	}
	
	//SocialSpy
	public void addSocialSpy(Player p) {
		socialspy.add(p);
	}
	
	public void delSocialSpy(Player p) {
		socialspy.remove(p);
	}
	
	public Player[] getSocialSpy() {
		return socialspy.toArray(new Player[] {});
	}
	
	// Getting log externally
	public static AccentialsLogger getLog() {
		return Accentials.getMain().getLogging();
	}
	
	// Getting version from main
	// Will be used in updating configs
	public String getVersion() {
		return "G2V1";
	}
	
	// Assorted api
	// Used for getting main config from main
	public Configuration getAceConfig() {
		return config;
	}
	
	// Getting message manager from main
	public MessageManager getMessageManager() {
		return messageManager;
	}
	
	// Used for getting groups from main
	public GroupManager getGroupManager(){
		return groupManager;
	}
	
	// Used for getting AccentialsPlayer data from main
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	// Used for getting warps from main
	public Warps getWarpManager() {
		return warpManager;
	}
	
	public KitManager getKitManager() {
		return kitManager;
	}
	
	// Player chat based
	// Used to mute a players chat (Accentials command main use)
	public void muteChat(Player arg0) {
		mutedChat.add(arg0);
	}
	
	// Used to stop blocking messages to that player
	public void unmuteChat(Player arg0) {
		mutedChat.remove(arg0);
	}
	
	// Gets all players with chat disabled
	public Player[] getMutedChat() {
		return mutedChat.toArray(new Player[] {});
	}
}
