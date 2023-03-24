package aceplugins.accentials.messages;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import aceplugins.AP;
import aceplugins.accentials.Accentials;
import aceplugins.accentials.config.Configuration;

public class MessageManager {
	Configuration messages;
	public MessageManager(){
		messages = new Configuration(new File(Accentials.getMain().getDataFolder(), "messages.yml"));
		if(!messages.exists())
			try {
				messages.copy(MessageManager.class.getResourceAsStream("defaults.txt"));
			} catch(IOException e) {
				Accentials.getLog().info("Failed to copy default message config from plugin...");
			}
		messages.load();
	}
	
	public String getMessage(String arg0) {
		try {
			return AP.color(messages.get(arg0));
		}catch(NullPointerException e) { }
		
		try {
			Random r = new Random(System.currentTimeMillis());
			String[] a = messages.getList(arg0);
			return AP.color(a[r.nextInt(a.length)]);
		}catch(NullPointerException e) { }
		return "%";
	}
	
	public void reload() {
		messages.reload();
	}
	
	public void setMessage(String key, String value) {
		messages.set(key, value);
		messages.write();
	}
}
