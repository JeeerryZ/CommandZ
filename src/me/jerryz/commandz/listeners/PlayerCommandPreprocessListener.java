package me.jerryz.commandz.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.jerryz.commandz.CommandZ;
import me.jerryz.commandz.api.Cooldown;
import me.jerryz.commandz.command.Command;
import me.jerryz.commandz.command.CommandAPI;

public class PlayerCommandPreprocessListener implements Listener{
	
	@EventHandler
	public void onPreprocess(PlayerCommandPreprocessEvent e) {
		
		for(Command c : CommandAPI.getAllCommands()) {
			if(e.getMessage().equalsIgnoreCase(c.getCommand().replaceAll("%player%", e.getPlayer().getName()))){
				
				if(!c.getPlayerTimes().containsKey(e.getPlayer().getName())) {
					c.getPlayerTimes().put(e.getPlayer().getName(), c.getTimes());
				}
				
				//CHECANDO SE TEM PERMISSÃO
				if(!e.getPlayer().hasPermission(c.getPermission())) {
					e.getPlayer().sendMessage(ChatColor.RED + "Você não tem permissão.");
					e.setCancelled(true);
					return;
				}
				
				//CHECANDO SE AINDA TEM USOS RESTANTES
				if(c.getPlayerTimes().get(e.getPlayer().getName()) == 0) {
					Cooldown cd = new Cooldown(e.getPlayer().getUniqueId(), c.getName(), c.getCooldown());
					if(Cooldown.isInCooldown(e.getPlayer().getUniqueId(), c.getName())) {
						e.getPlayer().sendMessage(ChatColor.RED + "Seus usos expiraram, aguarde mais " + ChatColor.AQUA + Cooldown.getTimeLeft(e.getPlayer().getUniqueId(), c.getName()) + ChatColor.RED + " segundos para usar novamente.");
						e.setCancelled(true);
						return;
					}
					cd.start();
					e.getPlayer().sendMessage(ChatColor.RED + "Seus usos expiraram, aguarde mais " + ChatColor.AQUA + Cooldown.getTimeLeft(e.getPlayer().getUniqueId(), c.getName()) + ChatColor.RED + " segundos para usar novamente.");
					e.setCancelled(true);
					return;
				}
				
				//CHECANDO SE TEM DINHEIRO SUFICIENTE
				if(CommandZ.getEconomy().getBalance(e.getPlayer()) < c.getPrice()) {
					e.getPlayer().sendMessage(ChatColor.RED + "Você não tem dinheiro suficiente.");
					e.setCancelled(true);
					return;
				}
				
				CommandZ.getEconomy().withdrawPlayer(e.getPlayer(), c.getPrice());

				for(String s : c.getMessage()) {
					String ss = s.replaceAll("%player%", e.getPlayer().getName());
					e.getPlayer().sendMessage(ss);
				}
				
				c.reducePlayerTime(e.getPlayer().getName());
				
				
				for(String s : c.getCommands()) {
					String ss = s.replaceAll("/", "");
					String cmd = ss.replaceAll("%player%", e.getPlayer().getName());
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
				}
				CommandAPI.saveTimes();
				e.setCancelled(true);
				return;
			}
		}
		
	}

}
