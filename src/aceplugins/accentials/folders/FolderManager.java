package aceplugins.accentials.folders;

import java.io.File;

import aceplugins.accentials.Accentials;

public class FolderManager {
	public FolderManager() {
		File core = Accentials.getMain().getDataFolder();
		File players = new File(core, "/players/");
		File warps = new File(core, "/warps/");
		File groups = new File(core, "/groups/");
		File kits = new File(core, "/kits/");
		core.mkdirs();
		players.mkdirs();
		warps.mkdirs();
		groups.mkdirs();
		kits.mkdirs();
	}
}
