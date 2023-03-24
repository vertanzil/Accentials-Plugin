package aceplugins;

import org.bukkit.ChatColor;

import aceplugins.accentials.Accentials;

public class AP {
	/*
	 * Hello there,
	 * I am AcePlugins, also known as Michelle Winters
	 */
	
	public static String color(String l) {
		String a = l;
		a = a.replace("&&", "%and%");
		a = a.replace("&", ChatColor.COLOR_CHAR + "");
		a = a.replace("%and%", "&");
		return a;
	}
	
	public static Accentials m() {
		return Accentials.getMain();
	}
}
