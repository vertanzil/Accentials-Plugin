package aceplugins.accentials.groups;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.bukkit.ChatColor;

import aceplugins.accentials.Accentials;

public class Group {
	File groupFile;
	boolean loaded = false;
	String name;
	String prefix = "%none%";
	String suffix = "%none%";
	ArrayList<String> perms = new ArrayList<>();
	
	public Group(String name) {
		groupFile = new File(Accentials.getMain().getDataFolder(), "/groups/" + name + ".yml");
		this.name =  name;
		load();
	}
	
	// Loading file
	public void load() {
		Scanner s = null;
		try {
			s = new Scanner(groupFile);
		}catch(Exception e) {
			Accentials.getLog().info("Error loading a group " + name + ". See log.txt for details");
			Accentials.getLog().error(e);
			return;
		}
		
		while(s.hasNextLine()) {
			String l = s.nextLine();
			if(l.contains("prefix"))
				prefix = l.substring(l.indexOf(":") + 2);
			if(l.contains("suffix"))
				suffix = l.substring(l.indexOf(":") + 2);
			if(l.contains("  - "))
				perms.add(l.substring(l.indexOf("-") + 2));
		}
		
		s.close();
		loaded = true;
	}
	
	// Creating / writing to file
	public void registerGroup() {
		perms.add("accentials.example");
		write();
	}
	
	private void write(){
		try {
			groupFile.delete();
			groupFile.createNewFile();
			PrintStream out = new PrintStream(new FileOutputStream(groupFile));
			out.println("prefix: " + prefix);
			out.println("suffix: " + suffix);
			out.println("permissions: []");
			for(String i : perms) {
				out.println("  - " + i);
			}
			out.flush();
			out.close();
		}catch(Exception e) {
			Accentials.getLog().info("There was an error registering group " + name + ". See log.txt for details");
			Accentials.getLog().error(e);
		}
	}
	
	// Get values
	public String getPrefix() {
		if(prefix.equalsIgnoreCase("%none%"))
			return "";
		return prefix.replaceAll("&", ChatColor.COLOR_CHAR + "");
	}
	
	public String getSuffix() {
		if(suffix.equalsIgnoreCase("%none%"))
			return "";
		return suffix.replaceAll("&", ChatColor.COLOR_CHAR + "");
	}
	
	public String[] getPermissions() {
		return perms.toArray(new String[] {});
	}
	
	public boolean hasPermission(String arg0) {
		String x = arg0;
		while(x.contains(".")) {
			String i = x.substring(0, x.lastIndexOf(".")) + ".*";
			if(perms.contains(i))
				return true;
			x = x.substring(0, x.lastIndexOf("."));
		}
		return false;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	// Set and edit values
	public void addPermission(String arg0) {
		perms.add(arg0);
		try {
			write();
		}catch(Exception e) {
			Accentials.getLog().info("There was an error adding a permission to group " + name + ". See log.txt for details");
			Accentials.getLog().error(e);
		}
	}
	
	public void removePermission(String arg0) {
		if(perms.contains(arg0))
			perms.remove(arg0);
		try {
			write();
		}catch(Exception e) {
			Accentials.getLog().info("There was an error adding a permission to group " + name + ". See log.txt for details");
			Accentials.getLog().error(e);
		}
	}
	
	public void setPrefix(String arg0) {
		prefix = arg0.replaceAll(ChatColor.COLOR_CHAR + "", "&");
	}
}
