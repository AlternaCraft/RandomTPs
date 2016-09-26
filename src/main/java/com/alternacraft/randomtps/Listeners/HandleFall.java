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
package com.alternacraft.randomtps.Listeners;

import com.alternacraft.randomtps.Events.PlayerDroppedEvent;
import com.alternacraft.randomtps.Events.PlayerGodModeEvent;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Utils.Localization;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class HandleFall implements Listener {

    private final Map<UUID, String> dropped = new HashMap();
    
    @EventHandler
    public void onPlayerDropped(PlayerDroppedEvent e) {
        UUID uuid = e.player().getUniqueId();
        if (!dropped.containsKey(uuid)) {
            dropped.put(uuid, e.zoneName());
        }
    }

    @EventHandler
    public void onPlayerSmashed(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        
        Player p = (Player) e.getEntity();

        for (Map.Entry<UUID, String> entry : dropped.entrySet()) {
            UUID player = entry.getKey();
            String zone = entry.getValue();
            if (player.equals(p.getUniqueId())) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);                    
                    Localization loc = Manager.INSTANCE.getLocalizationByName(zone);
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerGodModeEvent(p, loc));
                    break;
                }
            }
        }
    }
}
