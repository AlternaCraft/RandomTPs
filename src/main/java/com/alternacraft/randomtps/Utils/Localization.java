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
package com.alternacraft.randomtps.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import static org.bukkit.entity.EntityType.PLAYER;
import org.bukkit.util.Vector;

public class Localization extends Zone {

    private String zoneName = null;
    private String origin = null;
    private final List<String> dests = new ArrayList();

    private Map<String, List<Zone>> subzones = new HashMap();

    // Basics    
    private int time = 10;
    private boolean inmortal = true;
    // Broadcast
    private boolean show_as_exp = true;
    // Effects
    private final Map<String, Integer> potion_effects = new HashMap();

    public Localization(String zonename, Zone zone, String origin,
            List<String> destinations) {
        super(zone.getP1(), zone.getP2());

        this.zoneName = zonename;
        this.origin = origin;
        
        this.dests.addAll(destinations);
    }

    public Localization(String zonename, Zone zone, String origin,
             Map<String, List<Zone>> subzones) {
        super(zone.getP1(), zone.getP2());

        this.zoneName = zonename;
        this.origin = origin;
        
        this.subzones = subzones;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZone(String zone) {
        this.zoneName = zone;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<String> getDestinations() {
        return dests;
    }

    public void setDestination(List<String> arrives) {
        this.dests.clear();
        this.dests.addAll(arrives);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int no_pvp) {
        this.time = no_pvp;
    }

    public boolean isInmortal() {
        return inmortal;
    }

    public void setInmortal(boolean b) {
        this.inmortal = b;
    }

    public boolean broadcastAsEXP() {
        return show_as_exp;
    }

    public void setBroadcastAsEXP(boolean show_as_exp) {
        this.show_as_exp = show_as_exp;
    }

    public Map<String, Integer> getPotionEffects() {
        return potion_effects;
    }

    public void setPotion_effects(List<String> effects) {
        for (String effect : effects) {
            String[] values = effect.split(" ");
            potion_effects.put(values[0], Integer.valueOf(values[1]));
        }
    }

    public void addSubzone(String w, Zone z) {
        if (!this.subzones.containsKey(w)) {
            if (this.subzones.get(w) == null) {
                this.subzones.put(w, new ArrayList());
            }
            this.subzones.get(w).add(z);
        }
    }

    public boolean hasSubzones() {
        return this.subzones.size() > 0;
    }
    
    public Map<String, List<Zone>> getSubzones() {
        return subzones;
    }
    
    /**
     * Method for checking if a player is in a location
     *
     * @param p Player coordenates
     * @return true if he is & false if not
     */
    public boolean isInsideOfMe(Vector p) {
        return p.isInAABB(Vector.getMinimum(p1, p2), Vector.getMaximum(p1, p2));
    }

    /**
     * Method for checking if a location meets the requirements
     * <ul>
     * <li>Chunk has not liquids</li>
     * <li>Chunk has not players</li>
     * </ul>
     *
     * @param l Location
     * @return boolean
     */
    public static boolean isValid(Location l) {
        Chunk chunk = l.getChunk();

        boolean loaded = true;
        if (!chunk.isLoaded()) {
            loaded = false;
            chunk.load();
        }

        // Search another player into the chunk
        Entity[] entities = chunk.getEntities();
        for (Entity e : entities) {
            if (e.getType() == PLAYER) {
                if (!loaded) {
                    chunk.unload(); // Maintenance
                }
                return false;
            }
        }

        // Check if it has liquid blocks
        for (BlockState b : chunk.getTileEntities()) {
            if (b.getBlock().isLiquid()) {
                if (!loaded) {
                    chunk.unload(); // Maintenance
                }
                return false;
            }
        }

        return true;
    }
}
