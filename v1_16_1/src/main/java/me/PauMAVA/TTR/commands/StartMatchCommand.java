/*
 * TheTowersRemastered (TTR)
 * Copyright (c) 2019-2021  Pau Machetti Vallverdú
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.PauMAVA.TTR.commands;

import static org.bukkit.ChatColor.*;

import me.PauMAVA.TTR.TTRCore;
import me.PauMAVA.TTR.lang.PluginString;
import me.PauMAVA.TTR.teams.TTRTeam;
import me.PauMAVA.TTR.util.XPBarTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class StartMatchCommand implements CommandExecutor {

    private TTRCore plugin;

    public StartMatchCommand(TTRCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String label, String[] args) {
        if (!theSender.hasPermission("ttr.start")) {
            theSender.sendMessage(DARK_RED + "You are not permitted to use this command.");
            return true;
        }

        String game = plugin.translate("&7&l[&r&a&lThe Towers&r&7&l]&r ");
        if (!TTRCore.getInstance().enabled()) {
            theSender.sendMessage(game + RED + "You need to do '/ttrenable' first before trying to start.");
            return true;
        }

        for (TTRTeam team : TTRCore.getInstance().getTeamHandler().getTeams()) {
            if (team.getPlayers().size() < 1) {
                theSender.sendMessage(game + RED + "Game needs at least one player per team.");
                return true;
            }
        }

        if (TTRCore.getInstance().getCurrentMatch().isOnCourse()) {
            theSender.sendMessage(game + RED + "Game already started");
            return true;
        }

        int timer = 10;
        try {
            new XPBarTimer(timer, plugin.getCurrentMatch().getClass().getMethod("startMatch")).runTaskTimer(TTRCore.getInstance(), 0L, 20L);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Bukkit.broadcastMessage(game + GREEN + "Game will start in " + timer + " seconds.");
        return false;
    }
}
