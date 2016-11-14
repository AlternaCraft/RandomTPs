/*
 * Copyright (C) 2016 AlternaCraft
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
package com.alternacraft.randomtps.API.Events;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event thrown when someone tries to define a localization.
 * 
 * @author AlternaCraft
 */
public class DefineZoneEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private OfflinePlayer player = null;
    private String zone = null;
    private boolean redefine = false;
    private boolean cancelled = false;

    public DefineZoneEvent(OfflinePlayer player, String zone, boolean redefine) {
        this.player = player;
        this.zone = zone;
        this.redefine = redefine;
    }

    public OfflinePlayer offplayer() {
        return player;
    }

    public Player player() {
        return (Player) player;
    }

    public String getZoneName() {
        return zone;
    }

    /**
     * Gets if the localization is already defined
     * 
     * @return True if it exists; False if not
     */
    public boolean isRedefine() {
        return redefine;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean bln) {
        this.cancelled = bln;
    }
}
