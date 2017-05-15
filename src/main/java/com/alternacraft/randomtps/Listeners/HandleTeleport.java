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

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.CustomLinkedMap;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.MessageIntervals;
import com.alternacraft.aclib.utils.Randomizer;
import com.alternacraft.randomtps.API.Events.PlayerDroppedEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Main.Manager;
import static com.alternacraft.randomtps.Main.RandomTPs.PERFORMANCE;
import com.alternacraft.randomtps.Managers.ZoneManager;
import com.alternacraft.randomtps.Zone.DefinedZone;
import com.alternacraft.randomtps.Zone.Zone;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle player location.
 *
 * @author AlternaCraft
 */
public class HandleTeleport implements Listener {

    // This value is random
    private static final int MAX_TRIES = 1000;

    public static final HashSet<UUID> CANCELEDTP = new HashSet<>();
    private final Map<UUID, Location> rollbackLocation = new HashMap();

    public HandleTeleport() {
    }

    // <editor-fold defaultstate="collapsed" desc="PLAYER MOVE">
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        List<DefinedZone> zones = Manager.INSTANCE.getLocalizations();

        Player player = e.getPlayer();
        Langs lang = Localizer.getLocale(player);

        for (int i = 0; i < zones.size(); i++) {
            DefinedZone definedZone = zones.get(i);

            if (HandleBuild.DISABLED.containsKey(definedZone.getZoneName())
                    || !player.getLocation().getWorld().getName().equalsIgnoreCase(definedZone.getOrigin())) {
                continue; // Nothing to do
            }

            try {
                Location location = null;

                // Is the player in the zone?
                if (definedZone.isInsideOfMe(player.getLocation().toVector())) {
                    // Check subzones to go
                    boolean resul = false;

                    // Here comes the hard part, to get the location...
                    CustomLinkedMap<String, List<Zone>> subzones = new CustomLinkedMap();
                    subzones.putAll(definedZone.getSubzones());

                    int maxtries = 0;

                    // If there are not subzones
                    if (subzones.isEmpty()) {
                        PERFORMANCE.start("Teleport to zone");
                        int max_x = definedZone.getMaxX();
                        int min_x = definedZone.getMinX();
                        int y = definedZone.getY();
                        int max_z = definedZone.getMaxZ();
                        int min_z = definedZone.getMinZ();

                        do {
                            // Destination world
                            int randWorld = Randomizer.rand(
                                    definedZone.getDestinations().size() - 1, 0
                            );
                            World destination = Bukkit.getServer().getWorld(
                                    definedZone.getDestinations().get(randWorld)
                            );
                            
                            location = new Location(destination,
                                    Randomizer.rand(max_x, min_x), y,
                                    Randomizer.rand(max_z, min_z));
                            resul = ZoneManager.validateZone(location,
                                    definedZone.getValidations());
                            maxtries++;
                        } while (!resul && maxtries < MAX_TRIES);
                        PERFORMANCE.recordValue("Teleport to zone");
                    } else {
                        PERFORMANCE.start("Teleport to subzone");
                        do {
                            int randworld = Randomizer.rand(subzones.size() - 1, 0);
                            int randSubzone = Randomizer.rand(subzones.getValue(randworld).size() - 1, 0);
                            Zone zone = subzones.get(subzones.getKey(randworld)).get(randSubzone);

                            zone.getP1().setY(definedZone.getY());
                            zone.getP2().setY(definedZone.getY());

                            // Destination world
                            World destination = Bukkit.getServer().getWorld(
                                    subzones.getKey(randworld));

                            location = zone.randomLocation(destination);
                            resul = ZoneManager.validateSubZone(location,
                                    zone, definedZone.getValidations());

                            maxtries++;
                        } while (!resul && maxtries < MAX_TRIES);
                        PERFORMANCE.recordValue("Teleport to subzone");
                    }

                    // Avoiding he falls for ever
                    if (!resul) {
                        CANCELEDTP.add(player.getUniqueId());
                        MessageIntervals.sendMessage(player, GameInfo.PLAYER_CANT_BE_TELEPORTED, lang);
                        player.teleport(rollbackLocation.get(player.getUniqueId()));
                        return;
                    }

                    // We got it!!
                    player.teleport(location);
                    player.setGameMode(GameMode.SURVIVAL);

                    MessageManager.sendPlayer(player, GameInfo.PLAYER_TELEPORTED.getText(lang));

                    // Let's to propagate the "good news"
                    Bukkit.getServer().getPluginManager().callEvent(
                            new PlayerDroppedEvent(player, definedZone.getZoneName()));

                    return;
                }
            } catch (IllegalStateException ex) {
                MessageManager.sendPlayer(player, GameInfo.PLUGIN_ERROR_ON_TP.getText(lang));
            }
        }

        // At the end to prevent that the player gets stuck
        Location cLocation = new Location(player.getLocation().getWorld(),
                player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1,
                player.getLocation().getBlockZ());

        if (!cLocation.getBlock().isEmpty() 
                && !cLocation.getBlock().isLiquid()
                && !cLocation.getBlock().getType().equals(Material.SLIME_BLOCK)) {
            rollbackLocation.put(player.getUniqueId(), player.getLocation());
        }
    }
    // </editor-fold>
}
