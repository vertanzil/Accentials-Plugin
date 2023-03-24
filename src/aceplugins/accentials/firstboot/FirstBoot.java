package aceplugins.accentials.firstboot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Scanner;

import aceplugins.accentials.Accentials;

public class FirstBoot {
	public FirstBoot() {
		createGroups();
		createKits();
	}
	
	public void createGroups(){
		try {
			File a = new File(Accentials.getMain().getDataFolder(), "/groups/member.yml");
			try {
				copy(getClass().getResourceAsStream("group-member.txt"), a);
			}catch(Exception e) {
				Accentials.getMain().getLogging().info("Failed to load default member group from plugin. Trying download.");
				download(a, new URL("https://accentials.pw/plugin-files/"+ Accentials.getMain().getVersion() +"/groups/member.yml"));
			}
			
			File b = new File(Accentials.getMain().getDataFolder(), "/groups/mod.yml");
			try {
				copy(getClass().getResourceAsStream("group-mod.txt"), b);
			}catch(Exception e) {
				Accentials.getMain().getLogging().info("Failed to load default mod group from plugin. Trying download.");
				download(b, new URL("https://accentials.pw/plugin-files/"+ Accentials.getMain().getVersion() +"/groups/mod.yml"));
			}
		}catch(Exception e) {
			Accentials.getLog().info("There was an error downloading default groups");
			Accentials.getLog().error(e);
		}
	}

	public void createKits() {
		try {
			File a = new File(Accentials.getMain().getDataFolder(), "/kits/member.yml");
			try {
				copy(getClass().getResourceAsStream("kit-member.txt"), a);
			}catch(Exception e) {
				Accentials.getMain().getLogging().info("Failed to load default member group from plugin. Trying download.");
				download(a, new URL("https://accentials.pw/plugin-files/"+ Accentials.getMain().getVersion() +"/kits/member.yml"));
			}
			
			File b = new File(Accentials.getMain().getDataFolder(), "/kits/mod.yml");
			try {
				copy(getClass().getResourceAsStream("kit-mod.txt"), b);
			}catch(Exception e) {
				Accentials.getMain().getLogging().info("Failed to load default mod group from plugin. Trying download.");
				download(b, new URL("https://accentials.pw/plugin-files/"+ Accentials.getMain().getVersion() +"/kits/mod.yml"));
			}
		}catch(Exception e) {
			Accentials.getLog().info("There was an error downloading default groups");
			Accentials.getLog().error(e);
		}
	}
	
	public void copy(InputStream arg0, File arg1) throws IOException {
		arg1.delete(); arg1.createNewFile();
		PrintStream ps = new PrintStream(arg1);
		Scanner s = new Scanner(arg0);
		while(s.hasNextLine()) {
			ps.println(s.nextLine());
		}
		ps.flush(); ps.close();
		s.close();
	}
	
	private void download(File arg0, URL arg1) {
		try {
			arg0.delete();arg0.createNewFile();
			Scanner in = new Scanner(arg1.openStream());
			PrintStream ps = new PrintStream(arg0);
			
			while(in.hasNextLine()) {
				ps.println(in.nextLine());
			}
			ps.flush();
			ps.close();
			in.close();
		}catch(Exception e) {
			Accentials.getLog().info("There was an error downloading file from our system");
			Accentials.getLog().error(e);
		}
	}
}
