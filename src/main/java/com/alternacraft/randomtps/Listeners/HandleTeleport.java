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
import com.alternacraft.aclib.utils.MessageIntervals;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.Randomizer;
import com.alternacraft.randomtps.API.Events.PlayerDroppedEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Localizations.Localization;
import com.alternacraft.randomtps.Localizations.Zone;
import com.alternacraft.randomtps.Main.Manager;
import static com.alternacraft.randomtps.Main.RandomTPs.PERFORMANCE;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
    private static final int MAX_TRIES = 100;

    public static final HashSet<UUID> CANCELEDTP = new HashSet<>();
    private final Map<UUID, Location> rollbackLocation = new HashMap();

    public HandleTeleport() {
    }

    // <editor-fold defaultstate="collapsed" desc="PLAYER MOVE">
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        List<Localization> zones = Manager.INSTANCE.getLocalizations();

        Player player = e.getPlayer();
        Langs lang = Localizer.getLocale(player);

        for (int i = 0; i < zones.size(); i++) {
            Localization localization = zones.get(i);

            if (HandleBuild.DISABLED.containsKey(localization.getZoneName())) {
                continue; // Nothing to do
            }

            // Same world
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(localization.getOrigin())) {
                try {
                    Location location = null;

                    // Is the player in the zone?
                    if (localization.isInsideOfMe(player.getLocation().toVector())) {
                        // Check subzones to go
                        boolean resul = false;

                        // Here comes the hard part, to get the location...
                        CustomLinkedMap<String, List<Zone>> subzones = new CustomLinkedMap();
                        subzones.putAll(localization.getSubzones());

                        int maxtries = 0;

                        if (subzones.isEmpty()) {
                            PERFORMANCE.start("Teleport to zone");
                            // Destination world
                            int randWorld = Randomizer.rand(
                                    localization.getDestinations().size() - 1, 0);
                            World destination = Bukkit.getServer().getWorld(
                                    localization.getDestinations().get(randWorld));

                            int max_x = Manager.INSTANCE.loader().getMaxX();
                            int min_x = Manager.INSTANCE.loader().getMinX();
                            int y = Manager.INSTANCE.loader().getY();
                            int max_z = Manager.INSTANCE.loader().getMaxZ();
                            int min_z = Manager.INSTANCE.loader().getMinZ();

                            do {
                                location = new Location(destination,
                                        Randomizer.rand(max_x, min_x), y,
                                        Randomizer.rand(max_z, min_z));
                                resul = Localization.isValidZone(location);
                                maxtries++;
                            } while (!resul && maxtries < MAX_TRIES);

                            PERFORMANCE.recordValue("Teleport to zone");
                        } else {
                            PERFORMANCE.start("Teleport to subzone");
                            int previousworld = -1, randSubzone = -1;

                            do {
                                int randworld = Randomizer.rand(subzones.size() - 1, 0);
                                if (randworld == previousworld && subzones.size() > 1) {
                                    continue; // No repeat
                                }
                                previousworld = randworld;

                                int trandSubzone = Randomizer.rand(subzones.getValue(randworld).size() - 1, 0);
                                if (trandSubzone == randSubzone && subzones.getValue(randworld).size() > 1) {
                                    continue; // No repeat
                                }
                                randSubzone = trandSubzone;

                                Zone zone = subzones.get(subzones.getKey(previousworld))
                                        .get(randSubzone);

                                zone.getP1().setY(Manager.INSTANCE.loader().getY());
                                zone.getP2().setY(Manager.INSTANCE.loader().getY());

                                // Destination world
                                World destination = Bukkit.getServer().getWorld(
                                        subzones.getKey(previousworld));

                                location = zone.randomLocation(destination);
                                resul = Localization.isValidSubZone(location, zone);

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

                        // Let's to propagate the good news
                        Bukkit.getServer().getPluginManager().callEvent(
                                new PlayerDroppedEvent(player, localization.getZoneName()));

                        return;
                    }
                } catch (IllegalStateException ex) {
                    MessageManager.sendPlayer(player, GameInfo.PLUGIN_ERROR_ON_TP.getText(lang));
                }
            }
        }

        // At the end to prevent that the player gets stuck
        Location cLocation = new Location(player.getLocation().getWorld(),
                player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1,
                player.getLocation().getBlockZ());

        if (!cLocation.getBlock().isEmpty()) {
            rollbackLocation.put(player.getUniqueId(), player.getLocation());
        }
    }
    // </editor-fold>
}
