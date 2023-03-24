package aceplugins.accentials.warps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import aceplugins.accentials.Accentials;
import aceplugins.accentials.players.AccentialsPlayer;

public class Warps {
	HashMap<String, Location> warps = new HashMap<>();
	public Warps() {
		load();
		
		// Check if spawn warp exists
		if(!warps.containsKey("spawn"))
			createSpawn();
	}
	
	private void createSpawn() {
		// Using this for when server is created
		createWarp("spawn", Bukkit.getWorlds().get(0).getSpawnLocation());
	}
	
	private void load() {
		// Find all files in the folder
		File folder = new File(Accentials.getMain().getDataFolder(), "/warps/");
		for(File f : folder.listFiles()) {
			try {
				// Open the file reader
				Scanner s = new Scanner(f);
				
				// Contstants required to build location
				World world = null;
				double x = 0;
				double y = 0;
				double z = 0;
				float yaw = 0;
				float pitch = 0;
				
				// Go through every line
				while(s.hasNextLine()) {
					String line = s.nextLine();
					if(line.contains("world:")) // Set the world
						world = Bukkit.getWorld(line.substring(line.indexOf(":") + 2));
					else if(line.contains("x: ")) // Set the X
						x = Double.parseDouble(line.substring(line.indexOf(":") + 2));
					else if(line.contains("y: ")) // Set the Y
						y = Double.parseDouble(line.substring(line.indexOf(":") + 2));
					else if(line.contains("z: ")) // Set the Z
						z = Double.parseDouble(line.substring(line.indexOf(":") + 2));
					else if(line.contains("yaw")) // Set the yaw (left + right)
						yaw = Float.parseFloat(line.substring(line.indexOf(":") + 2));
					else if(line.contains("pitch")) // Set the pitch (up + down)
						pitch = Float.parseFloat(line.substring(line.indexOf(":") + 2));
				}
				// Close the reader
				s.close();
				
				// Read every line. Lets set it up
				Location l = new Location(world, x, y, z);
				l.setYaw(yaw);
				l.setPitch(pitch);
				
				// Put it in the warps HashMap
				warps.put(f.getName().substring(0, f.getName().lastIndexOf(".")), l);
			}catch(Exception e) {
				// Oops.. Error?
				Accentials.getLog().info("There was an error loading warp file " + f.getName());
				Accentials.getLog().error(e);
			}
		}
	}
	
	public boolean createWarp(String arg0, Location arg1) {
		try {
			// Set up the file
			File f = new File(Accentials.getMain().getDataFolder(), "/warps/" + arg0 + ".warp");
			
			// Delete the warp incase it exists
			deleteWarp(arg0);
			
			// Create the file
			f.createNewFile();
			
			// Open writer and write data required
			PrintStream out = new PrintStream(new FileOutputStream(f));
			out.println("world: " + arg1.getWorld().getName());
			out.println("x: " + arg1.getX());
			out.println("y: " + arg1.getY());
			out.println("z: " + arg1.getZ());
			out.println("yaw: " + arg1.getYaw());
			out.println("pitch: " + arg1.getPitch());
			
			// Flush and close
			out.flush();
			out.close();
		}catch(Exception e) {
			// Oops.. Error?
			Accentials.getLog().info("There was an error creating warp " + arg0);
			Accentials.getLog().error(e);
			return false;
		}
		
		// Make sure it's in the HashMap
		warps.put(arg0, arg1);
		return true;
	}
	
	public void deleteWarp(String arg0) {
		if(!warpExists(arg0))
			return; // Cancel it because it doesn't exist
		
		// Get the file and delete it
		File f = new File(Accentials.getMain().getDataFolder(), "/warps/" + exactName(arg0) + ".warp");
		if(f.exists())
			warps.remove(arg0.toLowerCase());
			f.delete();
	}
	
	public boolean warpExists(String arg0) {
		// Search HashMap for key
		for(Map.Entry<String, Location> e : warps.entrySet()) {
			if(e.getKey().equalsIgnoreCase(arg0)) {
				return true;
			}
		}
		return false;
	}
	
	public List<String> getWarps(){
		// Put all the kets in a List
		List<String> ret = new ArrayList<>();
		for(Map.Entry<String, ?> e : warps.entrySet()) {
			ret.add(e.getKey());
		}
		
		// Remove spawn (that's not classed as a warp)
		ret.remove("spawn");
		return ret;
	}
	
	public List<String> getWarps(AccentialsPlayer arg0){
		// Create list to put warps that player can access in
		List<String> ret = new ArrayList<>();
		for(Map.Entry<String, ?> e : warps.entrySet()) {
			if(arg0.hasPermission("accentials.command.warp." + e.getKey().toLowerCase())) {
				ret.add(e.getKey());
			}
		}
		
		// Remove spawn (that's not classed as a warp)
		ret.remove("spawn");
		return ret;
	}
	
	public Location getWarp(String arg0) {
		// Check warp exists..
		if(warpExists(arg0))
			return warps.get(exactName(arg0));
		
		// If this is sent the developer forgot to check the warp exists
		return null;
	}
	
	public String exactName(String arg0) {
		for(Map.Entry<String, ?> e : warps.entrySet()) {
			if(e.getKey().equalsIgnoreCase(arg0)) {
				return e.getKey();
			}
		}
		return null;
	}
}
