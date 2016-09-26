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

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.Randomizer;
import com.alternacraft.randomtps.Events.PlayerDroppedEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Utils.ElapsedTime;
import com.alternacraft.randomtps.Utils.Localization;
import com.alternacraft.aclib.utils.CustomLinkedMap;
import com.alternacraft.randomtps.Utils.Zone;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Julián
 */
public class HandleTeleport implements Listener {

    public HandleTeleport() {
    }

    // <editor-fold defaultstate="collapsed" desc="PLAYER MOVE">
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        List<Localization> zones = Manager.INSTANCE.getLocalizations();

        Player player = e.getPlayer();

        for (int i = 0; i < zones.size(); i++) {
            Localization localization = zones.get(i);

            if (HandleBuild.DISABLED.containsKey(localization.getZoneName())) {
                continue; // Nothing to do
            }

            // Same world
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(localization.getOrigin())) {
                Location playerLocation = new Location(player.getLocation().getWorld(),
                        player.getLocation().getX(), player.getLocation().getBlockY(),
                        player.getLocation().getBlockZ());

                try {
                    Location location = null;

                    // Is the player in the zone?
                    if (localization.isInsideOfMe(playerLocation.toVector())) {
                        // Check subzones to go
                        boolean resul = false;

                        // Here comes the hard part, to get the location...
                        ElapsedTime et = new ElapsedTime() {
                            {
                                this.startCount();
                            }
                        };

                        CustomLinkedMap<String, List<Zone>> subzones = new CustomLinkedMap();
                        subzones.putAll(localization.getSubzones());

                        if (subzones.isEmpty()) {
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
                                resul = Localization.isValid(location);
                            } while (!resul);

                            ElapsedTime.recordValue("zone", et.getValue());
                        } else {
                            int tries = Manager.INSTANCE.loader().getTries();
                            int randWorld = -1, randSubzone = -1;

                            do {
                                int trandWorld = Randomizer.rand(subzones.size() - 1, 0);
                                if (trandWorld == randWorld && subzones.size() > 0) {
                                    continue; // No repeat
                                }
                                randWorld = trandWorld;

                                int trandSubzone = Randomizer.rand(subzones.getValue(trandWorld).size() - 1, 0);
                                if (trandSubzone == randSubzone && subzones.getValue(trandWorld).size() > 0) {
                                    continue; // No repeat
                                }
                                randSubzone = trandSubzone;

                                Zone zone = subzones.get(subzones
                                        .getKey(randWorld)).get(randSubzone);
                                
                                zone.getP1().setY(Manager.INSTANCE.loader().getY());
                                zone.getP2().setY(Manager.INSTANCE.loader().getY());
                                
                                // Destination world
                                World destination = Bukkit.getServer().getWorld(
                                        subzones.getKey(randWorld));

                                int intent = 0;
                                do {
                                    location = zone.randomLocation(destination);
                                    resul = Localization.isValid(location);
                                    intent++;
                                } while (intent < tries && !resul);
                            } while (!resul);

                            ElapsedTime.recordValue("subzone", et.getValue());
                        }

                        // We got it!!
                        player.teleport(location);
                        player.setGameMode(GameMode.SURVIVAL);

                        MessageManager.sendPlayer(player, GameInfo.PLAYER_TELEPORTED.getText(
                                Localizer.getLocale(player))
                        );

                        // Let's to propagate the good new
                        Bukkit.getServer().getPluginManager().callEvent(
                                new PlayerDroppedEvent(player, localization.getZoneName()));
                    }
                } catch (Exception ex) {
                    MessageManager.sendPlayer(player, GameInfo.PLUGIN_ERROR_TP.getText(
                            Localizer.getLocale(player)
                    ));
                }
            }
        }
    }
    // </editor-fold>
}
