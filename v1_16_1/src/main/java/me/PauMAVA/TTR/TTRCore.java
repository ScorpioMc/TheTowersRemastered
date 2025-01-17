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
package me.PauMAVA.TTR;

import me.PauMAVA.TTR.commands.EnableDisableCommand;
import me.PauMAVA.TTR.commands.StartMatchCommand;
import me.PauMAVA.TTR.commands.StopMatchCommand;
import me.PauMAVA.TTR.config.TTRConfigManager;
import me.PauMAVA.TTR.lang.LanguageManager;
import me.PauMAVA.TTR.match.AutoStarter;
import me.PauMAVA.TTR.match.MatchStatus;
import me.PauMAVA.TTR.match.TTRMatch;
import me.PauMAVA.TTR.teams.TTRTeamHandler;
import me.PauMAVA.TTR.ui.TTRScoreboard;
import me.PauMAVA.TTR.util.EventListener;
import me.PauMAVA.TTR.util.PacketInterceptor;
import me.PauMAVA.TTR.world.TTRWorldHandler;
import me.PauMAVA.TTR.world.TTRWorldSetup;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TTRCore extends JavaPlugin {

    private static TTRCore instance;
    private boolean enabled = false;
    private TTRMatch match;
    private TTRTeamHandler teamHandler;
    private TTRConfigManager configManager;
    private TTRWorldHandler worldHandler;
    private TTRScoreboard scoreboard;
    private AutoStarter autoStarter;
    private LanguageManager languageManager;
    private PacketInterceptor packetInterceptor;
    private TTRWorldSetup setup;
    private boolean isCounting = false;

    @Override
    public void onLoad() {
        this.setup = new TTRWorldSetup();
        saveDefaultConfig();
        setup.deleteWorld("world");
        setup.copyWorld("TTRDefaultMap", "world");
    }

    @Override
    public void onEnable() {
        instance = this;
        if (this.getConfig().getBoolean("enable_on_start")) {
            enabled = true;
        } else {
            getLogger().warning("TTR is disabled on server start. Use /ttrenable to enable it on start. Make sure to be using the desired map.");
        }
        this.configManager = new TTRConfigManager(this.getConfig());
        //this.languageManager = new LanguageManager(this);
        this.packetInterceptor = new PacketInterceptor(this);
        for (Player player : this.getServer().getOnlinePlayers()) {
            this.packetInterceptor.addPlayer(player);
        }
        if (enabled) {
            this.scoreboard = new TTRScoreboard();
            this.match = new TTRMatch(MatchStatus.PREGAME);
            this.teamHandler = new TTRTeamHandler();
            this.teamHandler.setUpDefaultTeams();
            this.worldHandler = new TTRWorldHandler(this, this.getServer().getWorlds().get(0));
            this.worldHandler.setUpWorld();
            this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        } else {
            this.match = new TTRMatch(MatchStatus.DISABLED);
        }

        this.getCommand("ttrstart").setExecutor(new StartMatchCommand(this));
        EnableDisableCommand enableDisableCommand = new EnableDisableCommand(this);
        this.getCommand("ttrenable").setExecutor(enableDisableCommand);
        this.getCommand("ttrdisable").setExecutor(enableDisableCommand);
        this.getCommand("ttrstop").setExecutor(new StopMatchCommand());
        this.autoStarter = new AutoStarter(this, this.getConfig());
    }

    @Override
    public void onDisable() {
        try {
            this.scoreboard.removeScoreboard();
            for (Player player : this.getServer().getOnlinePlayers()) {
                this.packetInterceptor.removePlayer(player);
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static TTRCore getInstance() {
        return instance;
    }

    public boolean enabled() {
        return this.enabled;
    }

    public TTRMatch getCurrentMatch() {
        return this.match;
    }

    public TTRTeamHandler getTeamHandler() {
        return this.teamHandler;
    }

    public TTRConfigManager getConfigManager() {
        return this.configManager;
    }

    public TTRWorldHandler getWorldHandler() {
        return worldHandler;
    }

    public TTRScoreboard getScoreboard() {
        return scoreboard;
    }

    public boolean isCounting() {
        return isCounting;
    }

    public void setCounting(boolean counting) {
        isCounting = counting;
    }

    public AutoStarter getAutoStarter() {
        return autoStarter;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public PacketInterceptor getPacketInterceptor() {
        return packetInterceptor;
    }

    public String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
