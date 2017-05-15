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

import com.alternacraft.randomtps.API.ZoneValidation;
import com.alternacraft.randomtps.Utils.ZoneUtils;
import com.alternacraft.randomtps.Zone.Zone;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 *
 * @author AlternaCraft
 */
public class ZoneManager {
    
    public enum DEFAULT_VALIDATIONS {
        FALLING,
        FLUIDS,
        PLAYERS,
        FACTIONS,
        WGREGIONS
    }
    
    private static final Map<String, ZoneValidation> VALIDATIONS = new HashMap();
    
    /**
     * Register a zone validation.
     * 
     * @param name Name of validation.
     * @param zv Zone validation.
     */
    public static void registerValidation(String name, ZoneValidation zv) {
        if (!VALIDATIONS.containsKey(name)) {
            VALIDATIONS.put(name, zv);
        }
    }
    
    /**
     * Checks if a location meets the requirements.
     * <ul>
     *  <li>Chunks has not liquids</li>
     *  <li>Chunks has not players</li>
     *  <li>etc</li>
     * </ul>
     *
     * @param l Location where player will be teleported
     * @param todo List of validations to apply
     * @return True if it is: False if not
     */
    public static boolean validateZone(Location l, List<String> todo) {
        boolean valid = true;
        
        Chunk[] collidant_chunks = ZoneUtils.getCollidantChunks(l);
        Chunk[] chunks_to_unload = load(collidant_chunks);
        
        for (String v_name : todo) {
            if (VALIDATIONS.containsKey(v_name)) {
                if (!VALIDATIONS.get(v_name).valid(l, collidant_chunks)) {
                    valid = false;
                    break;
                }
            }
        }
        
        unload(chunks_to_unload);
        
        return valid;
    }    

    /**
     * Checks if a location meets the requirements.
     * <ul>
     *  <li>Chunks has not liquids</li>
     *  <li>Chunks has not players</li>
     *  <li>etc</li>
     * </ul>
     *
     * @param l Location where player will be teleported
     * @param zone Zone
     * @param todo List of validations to apply
     * @return True if it is: False if not
     */
    public static boolean validateSubZone(Location l, Zone zone, List<String> todo) {
        boolean valid = true;
        
        Chunk[] collidant_chunks = ZoneUtils.getCollidantChunks(l);
        Chunk[] chunks_to_unload = load(collidant_chunks);
        
        for (String v_name : todo) {
            if (VALIDATIONS.containsKey(v_name)) {
                if (!VALIDATIONS.get(v_name).validInsideZone(l, collidant_chunks, zone)) {
                    valid = false;
                    break;
                }
            }
        }

        unload(chunks_to_unload);
        
        return valid;
    }    
    
    private static Chunk[] load(Chunk[] toload) {
        Chunk[] loads = new Chunk[toload.length];
        int counter = 0;
        
        for (Chunk chunk : toload) {
            if (!chunk.isLoaded()) {
                chunk.load();
                loads[counter++] = chunk;
            }
        }
        
        return loads;
    }
    
    private static void unload(Chunk[] tounload) {
        for (Chunk chunk : tounload) {
            if (chunk == null) return;
            chunk.unload();
        }
    }
}
