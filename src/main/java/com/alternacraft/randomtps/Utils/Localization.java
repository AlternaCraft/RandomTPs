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
     * <li>Chunks has not liquids</li>
     * <li>Chunks has not players</li>
     * </ul>
     *
     * @param l Location
     * @return boolean
     */
    public static boolean isValidZone(Location l) {
        Chunk[] chunks = getCollidantChunks(l);

        for (Chunk chunk : chunks) {
            boolean wasloaded = true;
            if (!chunk.isLoaded()) {
                wasloaded = false;
                chunk.load();
            }

            if (hasPlayers(chunk.getEntities()) || hasLiquidBlocks(chunk.getTileEntities())) {
                if (!wasloaded) {
                    chunk.unload();
                }
                return false;
            }
        }

        return true;
    }

    /**
     * Method for checking if a location meets the requirements
     * <ul>
     * <li>Chunks has not liquids</li>
     * <li>Chunks has not players</li>
     * </ul>
     *
     * @param l Location
     * @param zone Subzone
     * @return boolean
     */
    public static boolean isValidSubZone(Location l, Zone zone) {
        Chunk[] chunks = getCollidantChunks(l);
        
        for (Chunk chunk : chunks) {
            boolean wasloaded = true;
            if (!chunk.isLoaded()) {
                wasloaded = false;
                chunk.load();
            }

            if (hasPlayersInto(chunk.getEntities(), zone)
                    || hasLiquidBlocksInto(chunk.getTileEntities(), zone)) {
                if (!wasloaded) {
                    chunk.unload();
                }
                return false;
            }
        }

        return true;
    }

    /**
     * Method for checking if there are players inside of a zone
     *
     * @param entities Entity[]
     * @param z Zone
     *
     * @return boolean
     */
    private static boolean hasPlayersInto(Entity[] entities, Zone z) {
        for (Entity e : entities) {
            if (e.getType() == PLAYER && isInsideOfSubzone(e.getLocation().toVector(), z)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method for checking if there are players inside of chunks
     *
     * @param entities Entity[]
     *
     * @return boolean
     */
    private static boolean hasPlayers(Entity[] entities) {
        // Search another player into the chunk
        for (Entity e : entities) {
            if (e.getType() == PLAYER) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method for checking if there are liquids inside of a zone
     *
     * @param entities BlockState[]
     * @param z Zone
     *
     * @return boolean
     */
    private static boolean hasLiquidBlocksInto(BlockState[] blocks, Zone z) {
        for (BlockState b : blocks) {
            if (b.getBlock().isLiquid() && isInsideOfSubzone(b.getLocation().toVector(), z)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method for checking if there are liquids inside of chunks
     *
     * @param entities BlockState[]
     * @param z Zone
     *
     * @return boolean
     */
    private static boolean hasLiquidBlocks(BlockState[] blocks) {
        // Check if it has liquid blocks
        for (BlockState b : blocks) {
            if (b.getBlock().isLiquid()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method for checking if a player is inside of a subzone
     *
     * @param p Player coordenates
     * 
     * @return true if he is & false if not
     */
    private static boolean isInsideOfSubzone(Vector p, Zone z) {
        Vector max = Vector.getMaximum(z.getP1(), z.getP2());
        Vector min = Vector.getMinimum(z.getP1(), z.getP2());
        
        int xplayer = (p.getBlockX() < 0) ? p.getBlockX() + 1:p.getBlockX();
        int zplayer = (p.getBlockZ() < 0) ? p.getBlockZ() + 1:p.getBlockZ();
        
        return ((xplayer <= max.getBlockX() && xplayer >= min.getBlockX()) 
                && (zplayer <= max.getBlockZ() && zplayer >= min.getBlockZ()));
    }

    /**
     * Method for getting collidant chunks by location
     *
     * @param l Location
     *
     * @return Chunk[]
     */
    private static Chunk[] getCollidantChunks(Location l) {
        Chunk[] chunks = new Chunk[9];

        Chunk origin = l.getChunk();
        
        int xmin = origin.getX() - 1;
        int zmin = origin.getZ() - 1;

        int diff = 3;

        int xtemp = xmin;
        int ztemp = zmin;

        for (int i = 0; i < chunks.length; i++) {
            if (ztemp < zmin + diff) {
                chunks[i] = l.getWorld().getChunkAt(xtemp, ztemp);
                ztemp++;
            } else {
                xtemp++;
                ztemp = zmin;
                i--;
            }
        }

        return chunks;
    }
}
