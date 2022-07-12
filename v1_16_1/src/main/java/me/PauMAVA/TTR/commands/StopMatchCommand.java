package me.PauMAVA.TTR.commands;

import static org.bukkit.ChatColor.*;

import me.PauMAVA.TTR.TTRCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StopMatchCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String label, String[] args) {
        if (!theSender.hasPermission("ttr.stop")) {
            theSender.sendMessage(DARK_RED + "You are not permitted to use this command.");
        }

        String game = TTRCore.getInstance().translate("&7&l[&r&a&lThe Towers&r&7&l]&r ");
        if (!TTRCore.getInstance().getCurrentMatch().isOnCourse()) {
            theSender.sendMessage(game + RED + "Game hasn't been started, restarting server.");
        }

        if (TTRCore.getInstance().getCurrentMatch().isOnCourse()) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.setGameMode(GameMode.SPECTATOR);
            }
            Bukkit.broadcastMessage(RED + "Game has been ended by " + UNDERLINE + theSender.getName()
                    + RESET + RED + ", restarting server in 10 seconds.");
        }
        new BukkitRunnable() {
            int i = 10;
            @Override
            public void run() {
                i--;
                if (i == 5) {
                    Bukkit.broadcastMessage(RED + "Restarting server in 5 seconds.");
                }
                if (i == 0) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        all.kickPlayer(RED + "Game stopped, restarting server.");
                    }
                    Bukkit.shutdown();
                }
            }
        }.runTaskTimer(TTRCore.getInstance(), 20, 20);
        return false;
    }
}
