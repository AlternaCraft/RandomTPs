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
package com.alternacraft.randomtps.Utils;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.CustomLinkedMap;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.MessageIntervals;
import com.alternacraft.aclib.utils.Randomizer;
import com.alternacraft.randomtps.API.Events.PlayerDroppedEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Listeners.HandleTeleport;
import static com.alternacraft.randomtps.Listeners.HandleTeleport.CANCELEDTP;
import com.alternacraft.randomtps.Main.Manager;
import static com.alternacraft.randomtps.Main.RandomTPs.PERFORMANCE;
import com.alternacraft.randomtps.Managers.ZoneManager;
import com.alternacraft.randomtps.Zone.DefinedZone;
import com.alternacraft.randomtps.Zone.Zone;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Management of validation.
 * <i>This class is useful to avoid stress the main thread.</i>
 * By using a task called each 'x' seconds once the previous task is finished.
 * 
 * @author AlternaCraft
 */
public class ZoneChecker {
    
    //<editor-fold defaultstate="collapsed" desc="VARIABLES + CONSTRUCTOR">
    private static final int MAX_TRIES = 50;
    
    private ValidatorTask run = null;
    
    private final int max_x, min_x, max_z, min_z, y;
    private final DefinedZone dz;
    private final Player player;
    
    private int tries = 0;
    
    private Location auxLocation = null;
    
    private boolean ended = false;
    
    public ZoneChecker(DefinedZone dz, Player player) {
        this.max_x = dz.getMaxX();
        this.min_x = dz.getMinX();
        this.y = dz.getY();
        this.max_z = dz.getMaxZ();
        this.min_z = dz.getMinZ();
        
        this.dz = dz;
        this.player = player;        
    }
    //</editor-fold>

    public void runValidateTask() {
        new ValidateTask().run();
    }
    
    public void runValidateInZoneTask(CustomLinkedMap<String, List<Zone>> subzones) {
        new ValidateInZoneTask(subzones).run();
    }
    
    public Location randomLocation() {
        return this.auxLocation;
    }
    
    public boolean hasEnded() {
        return this.ended;
    }
    
    public void stop() {
        this.run.stop();
    }
    
    //<editor-fold defaultstate="collapsed" desc="INNER CODE">
    private interface ZoneTask {
        public void run();
        public void end();
    }
    
    private class ValidateTask implements ZoneTask {

        public ValidateTask() {
             PERFORMANCE.start("Teleport to zone");
        }
        
        @Override
        public void run() {
            if (tries <= MAX_TRIES) {
                tries++;
                
                int randWorld = Randomizer.rand(
                        dz.getDestinations().size() - 1, 0
                );
                World destination = Bukkit.getServer().getWorld(
                        dz.getDestinations().get(randWorld)
                );

                auxLocation = new Location(destination,
                        Randomizer.rand(max_x, min_x), y,
                        Randomizer.rand(max_z, min_z));

                run = new ValidatorTask(this);
                Bukkit.getScheduler().runTaskLater(Manager.BASE.plugin(), run, 1);            
            } else {
                endOfValidation(false);
            }
        }        

        @Override
        public void end() {
            PERFORMANCE.recordValue("Teleport to zone");            
            endOfValidation(true);
        }
    }
    
    private class ValidateInZoneTask implements ZoneTask {

        private final CustomLinkedMap<String, List<Zone>> subzones;
        
        public ValidateInZoneTask(CustomLinkedMap<String, List<Zone>> subzones) {
            this.subzones = subzones;
            PERFORMANCE.start("Teleport to subzone");
        }
        
        @Override
        public void run() {
            if (tries <= MAX_TRIES) {
                tries++;
                
                int randworld = Randomizer.rand(subzones.size() - 1, 0);
                int randSubzone = Randomizer.rand(subzones.getValue(randworld).size() - 1, 0);
                Zone zone = subzones.get(subzones.getKey(randworld)).get(randSubzone);

                zone.getP1().setY(dz.getY());
                zone.getP2().setY(dz.getY());

                // Destination world
                World destination = Bukkit.getServer().getWorld(
                        subzones.getKey(randworld));

                auxLocation = zone.randomLocation(destination);

                run = new ValidatorTask(this);            
                Bukkit.getScheduler().runTaskLater(Manager.BASE.plugin(), run, 1); 
            } else {
                endOfValidation(false);
            }                
        }

        @Override
        public void end() {
            PERFORMANCE.recordValue("Teleport to subzone");
            endOfValidation(true);
        }
    }    
    
    private void endOfValidation(boolean successfully) {
        Langs lang = Localizer.getLocale(player);

        this.ended = true;

        if (!successfully) {
            CANCELEDTP.add(player.getUniqueId());
            MessageIntervals.sendMessage(player, GameInfo.PLAYER_CANT_BE_TELEPORTED, lang);
            player.teleport(HandleTeleport.ROLLBACK_LOCATION.get(player.getUniqueId()));
        } else {
            // We got it!!
            player.teleport(auxLocation);
            player.setGameMode(GameMode.SURVIVAL);

            MessageManager.sendPlayer(player, GameInfo.PLAYER_TELEPORTED.getText(lang));

            try {
                // Let's to propagate the "good news"
                Bukkit.getServer().getPluginManager().callEvent(
                        new PlayerDroppedEvent(player, dz.getZoneName()));            
            } catch (IllegalStateException ex) {
                MessageManager.sendPlayer(player, GameInfo.PLUGIN_ERROR_ON_TP.getText(lang));
            }
        }        
    }     
    
    private class ValidatorTask implements Runnable {

        private final long starttime;        
        private boolean finished, stop;
        
        private ZoneTask c = null;

        public ValidatorTask(ZoneTask c) {
            this.starttime = System.currentTimeMillis(); // Boot time            
            this.finished = false;    
            this.stop = false;
            this.c = c;
        }

        @Override
        public void run() {
            if (stop) {
                this.c.end();
            } else {
                this.finished = ZoneManager.validateZone(auxLocation, dz.getValidations());
                if (this.finished)
                    this.c.end();
                else
                    this.c.run();
            }
        }

        public long elapsedInMillis() {
            return System.currentTimeMillis() - this.starttime;
        }
        
        public void stop() {
            this.stop = true;
        }
    }
    //</editor-fold>
}
