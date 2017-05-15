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
package com.alternacraft.randomtps.API;

import com.alternacraft.randomtps.Zone.DefinedZone;
import com.alternacraft.randomtps.Zone.Zone;
import java.util.List;
import java.util.Map;

/**
 * Database management.
 * 
 * @author AlternaCraft
 */
public interface ZonesDB {

    /**
     * Gets all defined zones.
     * 
     * @return List
     */
    public List<DefinedZone> getDefinedZone();

    /**
     * Gets a specific defined zone.
     * 
     * @param zone defined zone name
     * 
     * @return defined zone
     */
    public DefinedZone getDefinedZone(String zone);

    /**
     * Gets a subzone from a defined zone.
     * 
     * @param zone defined zone name
     * 
     * @return Map with the available subzones for that defined zone
     */
    public Map<String, List<Zone>> getSubzones(String zone);

    /**
     * Gets a zone (3D vector)
     * 
     * @param zone Zone name
     * 
     * @return Zone
     */
    public Zone getZone(String zone);

    /**
     * Saves a defined zone.
     * 
     * @param l defined zone
     */
    public void saveLocalization(DefinedZone l);

    /**
     * Sets a defined zone as active.
     * 
     * @param zone defined zone name
     */
    public void enableLocalization(String zone);

    /**
     * Sets a defined zone as inactive.
     * 
     * @param zone defined zone name
     */
    public void disableLocalization(String zone);    
    
    /**
     * Cleans inactive defined zones.
     * 
     * @return Number of deleted defined zones
     */
    public int purge();
}
