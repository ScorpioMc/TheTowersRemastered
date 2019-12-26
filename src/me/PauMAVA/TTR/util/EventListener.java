/*
 * TheTowersRemastered (TTR)
 * Copyright (c) 2019-2020  Pau Machetti Vallverdu
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

package me.PauMAVA.TTR.util;

import me.PauMAVA.TTR.TTRCore;
import me.PauMAVA.TTR.ui.TeamSelector;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(TTRCore.getInstance().enabled()) {
            Inventory playerInventory = event.getPlayer().getInventory();
            playerInventory.clear();
            playerInventory.setItem(0, new ItemStack(Material.BLACK_BANNER));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(TTRCore.getInstance().enabled() && !TTRCore.getInstance().getCurrentMatch().isOnCourse()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent event) {
        if(TTRCore.getInstance().enabled() && !TTRCore.getInstance().getCurrentMatch().isOnCourse()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerClickEvent(PlayerInteractEvent event) {
        if(TTRCore.getInstance().enabled() && !TTRCore.getInstance().getCurrentMatch().isOnCourse()) {
            event.setCancelled(true);
            if(event.getItem() != null && event.getItem().getType() == Material.BLACK_BANNER) {
                new TeamSelector(event.getPlayer()).openSelector();
            }
        }
    }

    @EventHandler
    public void placeBlockEvent(BlockPlaceEvent event) {
        if(TTRCore.getInstance().enabled() && !TTRCore.getInstance().getCurrentMatch().isOnCourse()) {
            event.getPlayer().sendMessage(TTRPrefix.TTR_GAME + "" + ChatColor.GRAY + "You cannot break that block!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void breakBlockEvent(BlockBreakEvent event) {
        if(TTRCore.getInstance().enabled() && !TTRCore.getInstance().getCurrentMatch().isOnCourse()) {
            event.getPlayer().sendMessage(TTRPrefix.TTR_GAME + "" + ChatColor.GRAY + "You cannot place a block there!");
            event.setCancelled(true);
        }
    }


}