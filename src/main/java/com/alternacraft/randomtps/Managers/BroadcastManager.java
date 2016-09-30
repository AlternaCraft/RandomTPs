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
package com.alternacraft.randomtps.Managers;

import com.alternacraft.randomtps.Broadcasts.BroadcastAsExp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import com.alternacraft.randomtps.Broadcasts.Broadcast;
import org.bukkit.OfflinePlayer;

public class BroadcastManager {

    public enum TYPE {
        AS_EXP
    }

    public static final Map<UUID, Broadcast> BROADCASTERS = new HashMap();

    public static boolean callBroadcast(TYPE type, Player pl, int time) {
        Broadcast b;

        switch (type) {
            case AS_EXP:
                b = new BroadcastAsExp();
                break;
            default:
                return false;
        }

        if (b.showBroadcast(pl, time) < 0) {
            return false;
        }

        BROADCASTERS.put(pl.getUniqueId(), b);

        return true;
    }

    public static void stopBroadcast(OfflinePlayer player) {
        BROADCASTERS.get(player.getUniqueId()).stopBroadcast(player);
        BROADCASTERS.remove(player.getUniqueId());
    }

}
