package me.jerryz.commandz.command;

import java.util.HashMap;
import java.util.List;

public class Command {
	
	private String name;
	private String permission;
	private double price;
	private int times;
	private int cooldown;
	private List<String> message;
	private String command;
	private HashMap<String, Integer> playerTimes;
	private List<String> commands;
	
	public Command(String name, String permission, double price, int times, int cooldown, List<String> message, String command, List<String> commands) {
		super();
		this.name = name;
		this.permission = permission;
		this.price = price;
		this.times = times;
		this.cooldown = cooldown;
		this.message = message;
		this.command = command;
		this.playerTimes = new HashMap<String, Integer>();
		this.commands = commands;
	}
	
	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
	
	public HashMap<String, Integer> getPlayerTimes() {
		return playerTimes;
	}

	public void reducePlayerTime(String name) {
		playerTimes.put(name, playerTimes.get(name)-1);
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

}
