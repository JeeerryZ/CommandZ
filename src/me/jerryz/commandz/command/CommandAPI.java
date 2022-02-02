package me.jerryz.commandz.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

import me.jerryz.commandz.CommandZ;
import me.jerryz.commandz.api.Config;
import me.jerryz.commandz.api.MessageType;
import me.jerryz.commandz.api.StringUtils;

public class CommandAPI {

	private static ArrayList<Command> commands = new ArrayList<Command>();
	
	public static void createCommand(String name, String permission, double price, int times, int cooldown, List<String> message, String command, List<String> commands2) {
		Command cmd = new Command(name, permission, price, times, cooldown, message, command, commands2);
		commands.add(cmd);
	}
	
	public static Command getCommand(String name) {
		for(Command command : commands) {
			if(command.getName().equalsIgnoreCase(name)) {
				return command;
			}
		}
		return null;
	}

	public static ArrayList<Command> getAllCommands(){
		return commands;
	}
	
	@SuppressWarnings("unchecked")
	public static void loadCommands() {
		Config config = CommandZ.getInstance().config;
		
		int i = 0;
		
		for(String keys : config.getConfig().getConfigurationSection("Commands").getKeys(false)){
			i++;
			String name = keys;
			String permission = config.getConfig().getString("Commands." + keys + ".Permission");
			double price = config.getConfig().getDouble("Commands." + keys + ".Price");
			int times = config.getConfig().getInt("Commands." + keys + ".Times");
			int cooldown = config.getConfig().getInt("Commands." + keys + ".Cooldown");
			String command = config.getConfig().getString("Commands." + keys + ".Command");
			List<String> preMessages = (List<String>) config.getConfig().getList("Commands." + keys + ".Messages");
			List<String> commands = (List<String>) config.getConfig().getList("Commands." + keys + ".Commands");
			List<String> formattedMessages = new ArrayList<String>();
			for(String s : preMessages) {
				formattedMessages.add(StringUtils.colorize(s));
			}
			
			CommandAPI.createCommand(name, permission, price, times, cooldown, formattedMessages, command, commands);
			StringUtils.log(MessageType.WARN, "Carregando comando: " + ChatColor.AQUA + name);
		}
		
		StringUtils.log(MessageType.INFO, "Foram carregados " + ChatColor.AQUA + String.valueOf(i) + ChatColor.GREEN + " comandos com sucesso.");
	}
	
	public static void saveTimes() {
		Config config = CommandZ.getInstance().times;
		for(Command c : getAllCommands()) {
			for(Entry<String, Integer> key : c.getPlayerTimes().entrySet()) {
				config.getConfig().set("Commands."+c.getName() + "." + key.getKey(), Integer.parseInt(String.valueOf(key.getValue())));
				config.saveConfig();
			}
		}
	}
	
	public static void updateTimes(String name, Command c) {
		c.getPlayerTimes().put(name, c.getTimes());
	}
	
	public static void loadTimes() {
		StringUtils.log(MessageType.INFO, "Carregando dados...");
		Config times = CommandZ.getInstance().times;
		try {
			for(String commands : times.getConfig().getConfigurationSection("Commands").getKeys(false)) {
				for(String names : times.getConfig().getConfigurationSection("Commands." + commands).getKeys(false)) {
					getCommand(commands).getPlayerTimes().put(names, times.getConfig().getInt("Commands." + commands + "." + names));
				}
				
			}
		} catch (Exception e) {
			StringUtils.log(MessageType.ERROR, "Nenhum dado encontrado.");
		}
		
	}
}
