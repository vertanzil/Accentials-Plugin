package aceplugins.accentials.callback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import aceplugins.accentials.Accentials;

public class CallBack implements Runnable{
	BukkitTask task;
	
	public CallBack() {
		String e = Accentials.getMain().getAceConfig().get("metrics.active");
		if(e.toLowerCase().contains("t")) {
			task = Accentials.getMain().getServer().getScheduler().runTaskAsynchronously(Accentials.getMain(), this);
		}
	}
	
	public void stop() {
		String req = "request=shutdown&port=" + Bukkit.getServer().getPort();
		
		get(req);
		task.cancel();
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				ping();
				TimeUnit.MINUTES.sleep(9);
			}
		} catch(Exception e) {
			Accentials.getLog().info("Error sending metric info. See log.txt");
			Accentials.getLog().error(e);
		}
	}
	
	public void ping() {
		String req = "request=ping&";
		req +=       "port=" + Bukkit.getServer().getPort() + "&";
		req +=       "players=" + Bukkit.getServer().getOnlinePlayers().size() + "&";
		req +=       "max=" + Bukkit.getServer().getMaxPlayers();
		get(req);
	}
	
	public String get(String info) {
		try {
			Socket socket = new Socket("accentials.pw", 80);
			
			PrintStream out = new PrintStream(socket.getOutputStream());
			
			out.print("GET /callback/callback.php?" + info + " HTTP/1.0\r\n");
			out.print("Host: accentials.pw\r\n");
			out.print("\r\n");
			out.flush();
			
			Scanner in = new Scanner(socket.getInputStream());
			
			for(int i = 0; i != 6; i++) {
				in.nextLine();
			}
			
			String ret = in.nextLine();
			out.flush();
			out.close();
			in.close();
			socket.close();
			return ret;
		}catch(Exception e) {
			if(e.getMessage().contains("unreachable"))
				Accentials.getLog().info("Unable to callback due to no network");
			else
				Accentials.getLog().error(e);
		}
		return "ERROR";
	}
	
	public String getIP() {
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine();
			in.close();
			return ip;
		}catch(Exception e) {
			if(e.getMessage().contains("unreachable"))
				Accentials.getLog().info("Unable to get IP due to no network");
			else
				Accentials.getLog().error(e);
		}
		return Bukkit.getServer().getIp();
	}
}
