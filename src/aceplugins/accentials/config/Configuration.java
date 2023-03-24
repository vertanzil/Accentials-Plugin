package aceplugins.accentials.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import aceplugins.accentials.Accentials;

public class Configuration {
	File config;
	HashMap<String, Object> vals = new HashMap<>();
	public Configuration(File arg0) {
		config = arg0;
	}
							
	public boolean keyExists(String arg0) {
		return vals.containsKey(arg0);
	}
	
	public void set(String key, Object value) {
		vals.remove(key);
		vals.put(key, value);
	}
	
	public void write() {
		try {
			Scanner in = new Scanner(config);
			
			File newFile = new File(config.getAbsolutePath() + ".new");
			newFile.delete(); newFile.createNewFile();
			PrintStream out = new PrintStream(newFile);
			
			int indents = 0;
			String parent = "";
			while(in.hasNextLine()) {
				String l = in.nextLine();
				for(int i = 0; i != 2; i++) { // Double pass to make it work
					// Check blank line
					if(l.replaceAll(" ", "").length() == 0) {
						out.println();
						break;
					}
					
					// Check if comment
					if(l.replaceAll(" ", "").substring(0, 1).equals("#")) {
						out.println(l);
						break;
					}
					
					// Check if it was part of list
					if(l.replaceAll(" ", "").substring(0, 1).contains("-"))
						break;
					
					// Check if leaving a parent / list
					if(l.substring(0, indents).replaceAll(" ", "").length() != 0 && indents > 0) {
						indents -= 2;
						if(parent.contains("."))
							parent = parent.substring(0, parent.lastIndexOf("."));
						else
							parent = "";
					}
					
					// Check if entering a parent / list
					if(l.substring(l.indexOf(":") + 1).replaceAll(" ", "").length() == 0) {
						indents += 2;
						if(parent.length() == 0)
							parent = l.replaceAll(" ", "").replace(":", "");
						else
							parent += "." + l.replaceAll(" ", "").replace(":", "");
						
						if(vals.containsKey(parent)) {
							if(vals.get(parent) instanceof String[]) {
								out.println(l);
								for(String x : (String[]) vals.get(parent)) {
									if(!parent.contains("."))
										out.println(addIndents(indents, "- " + x));
									else
										out.println(addIndents(indents + 2, "- " + x));
								}
							}else {
								out.println(l + " " + ((String) vals.get(parent)));
							}
							
							indents -= 2;
							if(parent.contains("."))
								parent = parent.substring(0, parent.lastIndexOf("."));
							else
								parent = "";
						}else {
							out.println(l);
						}
						break;
					}
					
					// Get a value when not in a list
					if(l.contains(":")) {
						String key = l.substring(indents, l.indexOf(":"));
						if(vals.containsKey(parent + "." + key)) {
							if(vals.get(parent + "." + key) instanceof String[]) {
								out.println(addIndents(indents, key + ":"));
								for(String x : (String[]) vals.get(parent + "." + key)) {
									out.println(addIndents(indents + 2, "- " + x));
								}
							}else {
								out.println(addIndents(indents, key + ": " + (String) vals.get(parent + "." + key)));
							}
						}else if(vals.containsKey(key)){
							if(vals.get(key) instanceof String[]) {
								out.println(addIndents(indents, key + ":"));
								for(String x : (String[]) vals.get(key)) {
									out.println(addIndents(indents + 2, "- " + x));
								}
							}else {
								out.println(addIndents(indents, key + ": " + (String) vals.get(key)));
							}
						}else {
							out.println(l);
						}
						break;
					}
					
					out.println(l);
				}
			}
			
			in.close();
			out.flush();
			out.close();
			copy(newFile, config);
		}catch(Exception e) {
			Accentials.getLog().info("There was an writing a player config");
			Accentials.getLog().error(e);
		}
	}
	
	public String addIndents(int arg0, String arg1) {
		String ret = "";
		for(int i = 0; i != arg0; i++) {
			ret += " ";
		}
		ret += arg1;
		return ret;
	}
	
	private void copy(File a, File b) {
		try {
			b.delete(); b.createNewFile();
			Scanner in = new Scanner(a);
			PrintStream out = new PrintStream(b);
			while(in.hasNextLine()) {
				out.println(in.nextLine());
			}
			out.flush();
			out.close();
			in.close();
			a.delete();
			config = b;
			reload();
		}catch(Exception e) {
			Accentials.getLog().info("There was an writing a player file");
			Accentials.getLog().error(e);
		}
	}
	
	public void update() {
		// Coming soon
	}
	
	public void load() {
		try {
			load(new Scanner(config));
		}catch(Exception e) {
			Accentials.getLog().info("There was an error loading a player file");
			Accentials.getLog().error(e);
		}
	}
	
	public void reload() {
		vals.clear();
		load();
	}
	
	private void load(Scanner arg0) {
		try {
			Scanner in = arg0;
			
			int indents = 0;
			String parent = "";
			boolean list = false;
			ArrayList<String> listCreator = new ArrayList<>();
			while(in.hasNextLine()) {
				String l = in.nextLine();
				for(int i = 0; i != 2; i++) { // Double pass to make it work
					// Check blank line
					if(l.replaceAll(" ", "").length() == 0)
						break;
					
					// Check if comment
					if(l.replaceAll(" ", "").substring(0, 1).contains("#"))
						break;
					
					// Check if leaving a parent / list
					if(l.substring(0, indents).replaceAll(" ", "").length() != 0 && indents > 0) {
						if(list) {
							if(!vals.containsKey(parent))
								vals.put(parent, listCreator.toArray(new String[] {}));
							listCreator.clear();
							list = false;
						}
						indents -= 2;
						if(parent.contains("."))
							parent = parent.substring(0, parent.lastIndexOf("."));
						else
							parent = "";
					}
					
					// Check if entering a parent / list
					if(l.substring(l.indexOf(":") + 1).replaceAll(" ", "").length() == 0) {
						indents += 2;
						if(parent.length() == 0)
							parent = l.replaceAll(" ", "").replace(":", "");
						else
							parent += "." + l.replaceAll(" ", "").replace(":", "");
						break;
					}
					
					// Check if in list
					if(!list && l.substring(indents, indents + 1).contains("-")) {
						list = true;
					}
					
					// Get a value if in list 
					if(list && l.substring(indents, indents + 1).contains("-")) {
						listCreator.add(l.substring(l.indexOf("-") + 2));
						break;
					}
					
					// Get a value when not in a list
					if(l.contains(":")) {
						String key = l.substring(indents, l.indexOf(":", 0));
						String value = l.substring(l.indexOf(":", 0) + 2);
						if(parent.length() == 0) {
							if(!vals.containsKey(key))
								vals.put(key, value);
						}else {
							if(!vals.containsKey(parent + "." + key))
								vals.put(parent + "." + key, value);
						}
					}
				}
			}
			
			in.close();
		}catch(Exception e) {
			Accentials.getLog().info("There was an error downloading file from our system");
			Accentials.getLog().error(e);
		}
	}
	
	public String get(String arg0) {
		if(arg0.equalsIgnoreCase("economy.global") && config.getName().contains(".yml"))
			return "true";
		
		if(vals.containsKey(arg0)) {
			if(vals.get(arg0) instanceof String)
				return (String) vals.get(arg0);
		}
		return null;
	}
	
	public String[] getList(String arg0) {
		if(vals.containsKey(arg0)) {
			if(vals.get(arg0) instanceof String[])
				return (String[]) vals.get(arg0);
		}
		return null;
	}
	
	public boolean exists() {
		return config.exists();
	}
	
	public void copy(InputStream in) throws IOException {
		config.delete(); config.createNewFile();
		PrintStream ps = new PrintStream(config);
		Scanner s = new Scanner(in);
		while(s.hasNextLine()) {
			ps.println(s.nextLine());
		}
		ps.flush(); ps.close();
		s.close();
	}
}
