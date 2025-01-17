/*
 *  TheTowersRemastered (TTR)
 *  Copyright (c) 2019-2021  Pau Machetti Vallverdú
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package me.PauMAVA.TTR.chat;

import me.PauMAVA.TTR.TTRCore;
import me.PauMAVA.TTR.teams.TTRTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TTRChatManager {

    public static void sendMessage(Player sender, String originalMessage) {
        if (originalMessage.startsWith("!")) {
            dispatchGlobalMessage(originalMessage);
        } else {
            dispatchTeamMessage(originalMessage, sender);
        }
    }

    private static void dispatchGlobalMessage(String string) {
        String global = TTRCore.getInstance().translate("&7&l[&r&5&lGLOBAL&r&7&l]&r ");
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.sendMessage(global + "" + ChatColor.GRAY + string);
        }
    }

    private static void dispatchTeamMessage(String string, Player sender) {
        TTRTeam playerTeam = TTRCore.getInstance().getTeamHandler().getPlayerTeam(sender);
        String team = TTRCore.getInstance().translate("&7&l[&r&b&lTEAM&r&7&l]&r ");
        if (playerTeam == null) {
            return;
        }
        for (Player p : playerTeam.getPlayers()) {
            p.sendMessage(team + "" + ChatColor.GRAY + string);
        }
    }


}
