package me.jerryz.commandz.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.jerryz.commandz.CommandZ;


public class StringUtils {

	public static String preffix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Command" + ChatColor.RED + "Z" + ChatColor.GRAY
			+ "] ";
	public static String consolePreffix = ChatColor.RED + "[" + ChatColor.GOLD + "COMMANDZ" + ChatColor.RED
			+ "] ";

	public static void log(MessageType type, String msg) {
		switch (type) {

		case ERROR:
			CommandZ.getInstance().getServer().getConsoleSender().sendMessage(consolePreffix + ChatColor.RED + msg);
			break;
		case INFO:
			CommandZ.getInstance().getServer().getConsoleSender().sendMessage(consolePreffix + ChatColor.GREEN + msg);
			break;
		case WARN:
			CommandZ.getInstance().getServer().getConsoleSender().sendMessage(consolePreffix + ChatColor.YELLOW + msg);
			break;
		default:
			break;
		}
	}

	public static void sendMessage(Player p, MessageType type, String msg) {
		switch (type) {

		case ERROR:
			p.sendMessage(preffix + ChatColor.RED + msg);
			break;
		case INFO:
			p.sendMessage(preffix + ChatColor.GREEN + msg);
			break;
		case WARN:
			p.sendMessage(preffix + ChatColor.YELLOW + msg);
			break;
		default:
			break;
		}
	}
	
	public static String colorize(String msg)
    {
        String coloredMsg = "";
        for(int i = 0; i < msg.length(); i++)
        {
            if(msg.charAt(i) == '&')
                coloredMsg += '§';
            else
                coloredMsg += msg.charAt(i);
        }
        return coloredMsg;
    }
	
}
