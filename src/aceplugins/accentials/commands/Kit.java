package aceplugins.accentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class Kit implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Re-write required
		return true;
	}
	
	public void CommandText(Player p, String text, ChatColor textColor, String cmd, String hover, ChatColor hoverColor) {
		IChatBaseComponent comp = ChatSerializer.a("[\"\",{"
				+ "\"text\":\" " + text.replace("\"", "\\\"") + "\","
				+ "\"color\":\"" + textColor.name() + "\","
				+ "\"clickEvent\":{"
					+ "\"action\":\"run_command\","
					+ "\"value\":\"" + cmd + "\"},"
				+ "\"hoverEvent\":{"
					+ "\"action\":\"show_text\","
					+ "\"value\":{"
						+ "\"text\":\"\","
						+ "\"extra\":[{"
							+ "\"text\":\""+ hover.replace("\"", "\\\"") + "\","
							+ "\"color\":\"" + hoverColor.name() + "\""
						+ "}]"
					+ "}"
				+ "}}]");
	    PacketPlayOutChat packet = new PacketPlayOutChat(comp);
	    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
}
