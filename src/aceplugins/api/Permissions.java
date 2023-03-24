package aceplugins.api;

import org.bukkit.entity.Player;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.players.AccentialsPlayer;

public class Permissions {
	AccentialsPlayer playerID;
	Player player;
	public Permissions(AccentialsPlayer arg0, Player arg1) {
		playerID = arg0;
		player = arg1;
	}
	
	public static Permissions getPlayer(Player player) {
		return new Permissions(Accentials.getMain().getPlayerManager().getPlayer(player), player);
	}
	
	public boolean hasPermission(String arg0) {
		if(player.isOp() || player.hasPermission(arg0))
			return true;
		//test.permission.ok
		//test.permission.*
		String x = arg0;
		while(x.contains(".")) {
			String i = x.substring(0, x.lastIndexOf(".")) + ".*";
			if(player.hasPermission(i))
				return true;
			x = x.substring(0, x.lastIndexOf("."));
		}
		return false;
	}
}
