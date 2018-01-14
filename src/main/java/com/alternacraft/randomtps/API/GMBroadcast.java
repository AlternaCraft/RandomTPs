/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.randomtps.API;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Behavior of broadcast when god mode is reached.
 * 
 * @author AlternaCraft
 */
public interface GMBroadcast {

    /**
     * Shows a broadcast to a player for 'x' seconds
     *
     * @param pl Player
     * @param time Time in seconds
     *
     * @return Task id
     */
    public int start(final Player pl, int time);

    /**
     * Stops the broadcast to a player
     *
     * @param player OfflinePlayer
     */
    public void stop(OfflinePlayer player);
}
