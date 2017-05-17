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

import static com.alternacraft.aclib.PluginBase.TPS;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Utils.CustomLinkedMap;
import com.alternacraft.randomtps.Utils.ZoneChecker;
import com.alternacraft.randomtps.Zone.DefinedZone;
import com.alternacraft.randomtps.Zone.Zone;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * Handle player location.
 *
 * @author AlternaCraft
 */
public class HandleTeleport implements Listener {

    public static final HashSet<UUID> CANCELEDTP = new HashSet<>();
    public static final Map<UUID, Location> ROLLBACK_LOCATION = new HashMap();

    public static final Map<UUID, ZoneChecker> TELEPORTING = new HashMap();

    public enum COLLISION_EFFECT {
        NONE((short) 1), 
        SLOW_DOWN((short) 0), 
        BOUNCE((short) -5), 
        SPEED_UP((short) 5);
        
        private short mvm;

        COLLISION_EFFECT(short mvm) {
            this.mvm = mvm;
        }

        public short velocity() {
            return this.mvm;
        }
    }

    public HandleTeleport() {
    }

    @EventHandler
    public void onPlayerSmashed(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (TELEPORTING.containsKey(p.getUniqueId())) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        List<DefinedZone> zones = Manager.INSTANCE.getDefinedZones();

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        boolean nextStep = true;

        if (TELEPORTING.containsKey(uuid)) {
            if (TELEPORTING.get(uuid).hasEnded()) {
                player.setNoDamageTicks(0);
                TELEPORTING.remove(uuid);
            }
            return;
        }

        for (int i = 0; i < zones.size(); i++) {
            DefinedZone definedZone = zones.get(i);

            if (HandleBuild.DISABLED.containsKey(definedZone.getZoneName())
                    || !player.getLocation().getWorld().getName().equalsIgnoreCase(definedZone.getOrigin())) {
                continue; // Nothing to do
            }

            // Is the player in the zone?
            if (definedZone.isInsideOfMe(player.getLocation().toVector())) {

                // Here comes the hard part, to get the location...
                CustomLinkedMap<String, List<Zone>> subzones = new CustomLinkedMap();
                subzones.putAll(definedZone.getSubzones());

                // If there are not subzones
                ZoneChecker zc = new ZoneChecker(definedZone, player);
                if (subzones.isEmpty()) {
                    zc.runValidateTask();
                } else {
                    zc.runValidateInZoneTask(subzones);
                }

                player.setNoDamageTicks(TPS * 3600);
                TELEPORTING.put(uuid, zc);

                // Slow down
                Vector vec = player.getLocation().getDirection();
                player.setVelocity(new Vector(vec.getX(),
                        player.getVelocity().getY() * definedZone
                                .getCollisionEffect().velocity(), vec.getZ()));

                nextStep = false;
            }
        }

        // At the end to prevent that the player gets stuck
        if (nextStep) {
            Location cLocation = new Location(player.getLocation().getWorld(),
                    player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1,
                    player.getLocation().getBlockZ());

            if (!cLocation.getBlock().isEmpty()
                    && !cLocation.getBlock().isLiquid()
                    && !cLocation.getBlock().getType().equals(Material.SLIME_BLOCK)) {
                ROLLBACK_LOCATION.put(uuid, player.getLocation());
            }
        }
    }
}
