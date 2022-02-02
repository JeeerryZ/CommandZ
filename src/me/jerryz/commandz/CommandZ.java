package me.jerryz.commandz;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.jerryz.commandz.api.Config;
import me.jerryz.commandz.api.MessageType;
import me.jerryz.commandz.api.StringUtils;
import me.jerryz.commandz.command.CommandAPI;
import me.jerryz.commandz.listeners.PlayerCommandPreprocessListener;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class CommandZ extends JavaPlugin{
	
	public static CommandZ getInstance() {
		return (CommandZ) Bukkit.getPluginManager().getPlugin("CommandZ");
	}
	
	public Config config = new Config("plugins/CommandZ", "config.yml", this);
	public Config times = new Config("plugins/CommandZ", "players.yml", this);
	private static Economy econ = null;
    private static Permission perms = null;
	
	
	public void onEnable() {
		if (!setupEconomy() ) {
            StringUtils.log(MessageType.ERROR, "Vault/Economy não encontrado, desabilitando o plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
		CommandAPI.loadCommands();
		CommandAPI.loadTimes();
		getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(), this);
	}

	public void onDisable() {
		CommandAPI.saveTimes();
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
    public static Economy getEconomy() {
        return econ;
    }
    
    public static Permission getPermissions() {
        return perms;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	Player p = (Player) sender;
    	if(label.equalsIgnoreCase("cmdz")) {
    		 if(args.length < 1) {
    			 long seconds = p.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
    			 long minutes = seconds / 60;
    			 long hours = minutes / 60;

    			 if(seconds < 60 ) {
    				 p.sendMessage(ChatColor.GRAY + "" +  seconds + " segundo(s).");
    			 }else if(seconds >= 60 && seconds < 3600) {
    				 p.sendMessage(ChatColor.GRAY + "" + minutes + " minuto(s) e " + (seconds % 60) + " segundo(s)");
    			 }else if(seconds >= 3600 && seconds < 86400) {
    				 p.sendMessage(ChatColor.GRAY + "" + hours + " hora(s), " + (minutes % 60) + "minuto(s) e " + (seconds % 60) + " segundo(s)");
    			 }
    			 
    			 StringUtils.sendMessage(p, MessageType.WARN, "Use /cmdz resetall <nomeDoComando> ou /cmdz reset <nomeDoComando> <nomeDoJogador>");
    			 return false;
    		 }
    		
    		if(args[0].equalsIgnoreCase("resetall")) {
    			if(args.length == 2) {
    				
    				try {
    					me.jerryz.commandz.command.Command c = CommandAPI.getCommand(args[1]);
    					for(String keys : times.getConfig().getConfigurationSection("Commands." + args[1]).getKeys(false)) {
    						times.getConfig().set("Commands." + args[1] + "." + keys, c.getTimes());
    						times.saveConfig();
    					}
    					for(String s : c.getPlayerTimes().keySet()) {
    						CommandAPI.updateTimes(s, c);
    					}
    					StringUtils.sendMessage(p, MessageType.INFO, "Comando " + ChatColor.AQUA + args[1] + ChatColor.GREEN + " foi resetado para " + ChatColor.AQUA + c.getTimes());
    					return false;
    				}catch(Exception e) {
    					StringUtils.sendMessage(p, MessageType.ERROR, "Erro, comando inexistente.");
    					return false;
    				}
    				
    				
    			}else {
    				StringUtils.sendMessage(p, MessageType.WARN, "Use /cmdz resetall <nomeDoComando>");
    				return false;
    			}
    			
    			
    		}
    		
    		if(args[0].equalsIgnoreCase("reset")) {
    			if(args.length == 3) {
    				
    				try {
    					me.jerryz.commandz.command.Command c = CommandAPI.getCommand(args[1]);
    					String name = args[2];
    					times.getConfig().set("Commands." + args[1] + "." + name, c.getTimes());
    					times.saveConfig();
    					StringUtils.sendMessage(p, MessageType.INFO, "Comando " + ChatColor.AQUA + args[1] + ChatColor.GREEN + " foi resetado para " + ChatColor.AQUA + c.getTimes() + ChatColor.GREEN + " para o jogador " + ChatColor.AQUA + args[2]);
    					CommandAPI.updateTimes(name, c);
    					return false;
    				}catch(Exception e) {
    					StringUtils.sendMessage(p, MessageType.ERROR, "Erro, comando inexistente.");
    					return false;
    				}
    				
    			}else {
    				StringUtils.sendMessage(p, MessageType.WARN, "Use /cmdz reset <nomeDoComando> <nomeDoJogador>");
    				return false;
    			}
    		}
    		
    		
    		
    	}
    	return false;
    }
    
}
