/*
 * Copyright (C) 2017 AlternaCraft
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

import com.alternacraft.randomtps.API.GMBroadcast;
import com.alternacraft.randomtps.Utils.BroadcastAsExp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Broadcasts manager.
 * 
 * @author AlternaCraft
 */
public class BroadcastManager {

    public enum TYPE {
        AS_EXP
    }

    public static final Map<UUID, List<GMBroadcast>> BROADCASTERS = new HashMap();

    public static boolean callBroadcast(TYPE type, Player pl, int time) {
        GMBroadcast b;

        switch (type) {
            case AS_EXP:
                b = new BroadcastAsExp();
                break;
            default:
                return false;
        }

        if (b.start(pl, time) >= 0) {
            if (!BROADCASTERS.containsKey(pl.getUniqueId())) {
                BROADCASTERS.put(pl.getUniqueId(), new ArrayList<GMBroadcast>());
            }
            BROADCASTERS.get(pl.getUniqueId()).add(b);
            return true;
        }
        
        return false;
    }
    
    public static void stopBroadcast(OfflinePlayer player, TYPE type) {
        List<GMBroadcast> broadcasts = BROADCASTERS.get(player.getUniqueId());
        for (GMBroadcast broadcast : broadcasts) {
            switch(type) {
                case AS_EXP:
                    if (broadcast instanceof BroadcastAsExp) {
                        BROADCASTERS.get(player.getUniqueId()).remove(broadcast);
                        return;
                    }
            }
            broadcast.stop(player);
        }
    }

    public static void stopBroadcasts(OfflinePlayer player) {
        List<GMBroadcast> broadcasts = BROADCASTERS.get(player.getUniqueId());
        for (GMBroadcast broadcast : broadcasts) {
            broadcast.stop(player);
        }
        BROADCASTERS.remove(player.getUniqueId());
    }
}
