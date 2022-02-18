package me.PauMAVA.TTR.commands;

import me.PauMAVA.TTR.TTRCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StopMatchCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String label, String[] args) {
        if (!theSender.hasPermission("ttr.stop")) {
            theSender.sendMessage(ChatColor.DARK_RED + "You are not permitted to use this command.");
        }
        Bukkit.broadcastMessage(ChatColor.RED + "Game has been ended by " + ChatColor.UNDERLINE + theSender.getName()
                + ChatColor.RESET + "" + ChatColor.RED + ", restarting server in 10 seconds.");
        new BukkitRunnable() {
            int i = 10;
            @Override
            public void run() {
                i--;
                if (i == 5) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Restarting server in 5 seconds.");
                }
                if (i == 0) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.kickPlayer(ChatColor.RED + "Game stopped, restarting server.");
                    }
                    Bukkit.shutdown();
                }
            }
        }.runTaskTimer(TTRCore.getInstance(), 20, 20);
        return false;
    }
}
