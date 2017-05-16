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
package com.alternacraft.randomtps.Listeners;

import com.alternacraft.randomtps.API.Events.PlayerDroppedEvent;
import com.alternacraft.randomtps.API.Events.PlayerGodModeEvent;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Zone.DefinedZone;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle player falling.
 *
 * @author AlternaCraft
 */
public class HandleFall implements Listener {

    private static final Map<UUID, String> DROPPED = new HashMap();

    @EventHandler
    public void onPlayerDropped(PlayerDroppedEvent e) {
        UUID uuid = e.player().getUniqueId();
        if (!DROPPED.containsKey(uuid)) {
            if (HandleTeleport.CANCELEDTP.contains(uuid)) {
                HandleTeleport.CANCELEDTP.remove(uuid);
            }
            DROPPED.put(uuid, e.zoneName());
        }
    }

    @EventHandler
    public void onPlayerSmashed(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();

        // Avoiding instakill on tp
        if (HandleTeleport.CANCELEDTP.contains(p.getUniqueId())) {
            e.setCancelled(true);
            HandleTeleport.CANCELEDTP.remove(p.getUniqueId());
        } else if (DROPPED.containsKey(p.getUniqueId())) {
            String zone = DROPPED.get(p.getUniqueId());
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
                DefinedZone loc = Manager.INSTANCE.getDefinedZoneByName(zone);
                Bukkit.getServer().getPluginManager().callEvent(new PlayerGodModeEvent(p, loc));
                DROPPED.remove(p.getUniqueId());
            }
        }
    }
    
    @EventHandler
    public void onPlayerFakeSmashed(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (DROPPED.containsKey(p.getUniqueId())) {
            Block next = p.getWorld().getBlockAt(e.getTo().getBlockX(), e.getTo().getBlockY()-1, e.getTo().getBlockZ());
            if (e.getTo().getBlock().isLiquid() || next.getType().equals(Material.SLIME_BLOCK)) {
                String zone = DROPPED.get(p.getUniqueId());
                DefinedZone loc = Manager.INSTANCE.getDefinedZoneByName(zone);
                Bukkit.getServer().getPluginManager().callEvent(new PlayerGodModeEvent(p, loc));
                DROPPED.remove(p.getUniqueId());
            }
        }
    }
}
