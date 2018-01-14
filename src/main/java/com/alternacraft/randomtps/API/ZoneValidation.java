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
package com.alternacraft.randomtps.API;

import com.alternacraft.randomtps.Zone.Zone;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 *
 * @author AlternaCraft
 */
public interface ZoneValidation {
    
    /**
     * Validate a location.
     * 
     * @param l Location where player will be teleported
     * @param affected_chunks Array of chunks which should be checked
     * 
     * @return True if is valid; False if not
     */
    public boolean isValid(Location l, Chunk[] affected_chunks);
    
    /**
     * Validate a location inside of a zone.
     * 
     * @param l Location where player will be teleported
     * @param affected_chunks Array of chunks which should be checked
     * @param zone Zone limits
     * 
     * @return True if is valid; False if not
     */
    public boolean isValidInsideSubzone(Location l, Chunk[] affected_chunks, Zone zone);
    
}
