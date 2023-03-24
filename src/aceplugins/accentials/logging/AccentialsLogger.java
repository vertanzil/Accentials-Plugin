package aceplugins.accentials.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import aceplugins.AP;
import aceplugins.accentials.Accentials;
import aceplugins.accentials.config.Configuration;

public class AccentialsLogger {
	File logFile;
	PrintStream logOut;
	public AccentialsLogger() {
		logFile = new File(Accentials.getMain().getDataFolder(), "log.txt");
		try {
			logFile.delete();
			logFile.createNewFile();
			logOut = new PrintStream(new FileOutputStream(logFile));
			logOut.println("[" + t() + "] Logging started");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		info("Server stopped");
		logOut.close();
	}
	
	public void info(String arg0) {
		System.out.println(arg0);
		logOut.println("[" + t() + "] " + arg0);
		logOut.flush();
	}
	
	public void error(Exception e) {
		logOut.println("\nStack trace occured:");
		e.printStackTrace(logOut);
		logOut.println();
		logOut.flush();
	}
	
	public void joined(PlayerJoinEvent e) {
		info("Player \"" + e.getPlayer().getName() + "\" joined on IP \"" 
				+ e.getPlayer().getAddress().getHostName() + "\"");
	}
	
	public void leave(PlayerQuitEvent e) {
		info("Player \"" + e.getPlayer().getName() + "\" left the server");
	}
	
	public void command(CommandSender sender, String commandLabel, String[] args) {
		String cmd = commandLabel;
		
		Configuration cfg = Accentials.getMain().getAceConfig();
		if(cfg.getList("socialspy") != null) {
			String[] x = cfg.getList("socialspy");
			for(String i : x) {
				if(commandLabel.equalsIgnoreCase(i)) {
					String msg = Accentials.getMain().getMessageManager().getMessage("socialspy.in");
					msg = msg.replace("%player%", sender.getName());
					
					String fullcmd = commandLabel;
					if(args.length > 0) {
						for(String j : args) {
							fullcmd += " " + j;
						}
					}
					
					msg = msg.replace("%command%", fullcmd);
					for(Player p : Accentials.getMain().getSocialSpy()) {
						p.sendMessage(AP.color(msg));
					}
				}
			}
		}
		
		// TODO add social spy
		if(args.length > 0)
			for(String i : args)
				cmd += " " + i;
		info("\"" + sender.getName() + "\" executed command: \"" + cmd + "\"");
	}
	
	private String t() {
		return new SimpleDateFormat("HH:mm").format(new Date());
	}
}
