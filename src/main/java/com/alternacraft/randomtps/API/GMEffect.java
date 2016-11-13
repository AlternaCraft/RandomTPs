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
package com.alternacraft.randomtps.API;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author AlternaCraft
 */
public interface GMEffect {

    /**
     * Set an effect to a player for 'x' seconds
     *
     * @param player Player
     * @param time Time in seconds
     * @param effect_data Extra data for the effect
     *
     * @return True if it works; False if not
     */
    public boolean start(Player player, int time, Object effect_data);

    /**
     * Stops a player's effect
     *
     * @param offlinePlayer Offline player
     */
    public void stop(OfflinePlayer offlinePlayer);

}
