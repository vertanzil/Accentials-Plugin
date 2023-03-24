package aceplugins.accentials.groups;

import java.io.File;
import java.util.HashMap;

import aceplugins.accentials.Accentials;

public class GroupManager {
	HashMap<String, Group> groups;
	public GroupManager() {
		groups = new HashMap<>();
		load();
	}
	
	private void load() {
		File groupDir = new File(Accentials.getMain().getDataFolder(), "/groups/");
		Accentials.getLog().info("[Groups] Finding groups in folder...");
		String[] groupList = groupDir.list();
		for(String groupName : groupList) {
			Accentials.getLog().info("");
			Accentials.getLog().info("[Groups] Loading group " + groupName);
			Group group = new Group(groupName.replace(".yml", ""));
			if(group.isLoaded()) {
				groups.put(groupName.replace(".yml", ""), group);
				Accentials.getLog().info("[Groups] Loaded");
			}
		}
	}
	
	public void reload() {
		groups.clear();
		load();
	}
	
	public Group getGroup(String arg0) {
		if(groups.containsKey(arg0))
			return groups.get(arg0);
		Accentials.getLog().info("Error: getGroup() method was called with invalid group. Returning null");
		return null;
	}
}
