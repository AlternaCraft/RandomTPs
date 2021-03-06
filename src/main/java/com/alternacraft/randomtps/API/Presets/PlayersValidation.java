/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.randomtps.API.Presets;

import com.alternacraft.randomtps.API.ZoneValidation;
import com.alternacraft.randomtps.Utils.ZoneUtils;
import com.alternacraft.randomtps.Zone.Zone;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import static org.bukkit.entity.EntityType.PLAYER;

/**
 *
 * @author AlternaCraft
 */
public class PlayersValidation implements ZoneValidation {

    @Override
    public boolean isValid(Location l, Chunk[] affected_chunks) {
        for (Chunk chunk : affected_chunks) {
            for (Entity e : chunk.getEntities()) {
                if (e.getType() == PLAYER) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isValidInsideSubzone(Location l, Chunk[] affected_chunks, Zone zone) {
        // Search another player into the chunk
        for (Chunk chunk : affected_chunks) {
            for (Entity e : chunk.getEntities()) {
                if (e.getType() == PLAYER
                        && ZoneUtils.isInsideOfSubzone(e.getLocation().toVector(), zone)) {
                    return false;
                }
            }    
        }        
        return true;
    }    
}
