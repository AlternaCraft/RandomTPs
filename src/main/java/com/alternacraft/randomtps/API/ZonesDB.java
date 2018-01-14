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

import com.alternacraft.aclib.exceptions.PluginException;
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
     * @return List with the defined zones 
     * @throws com.alternacraft.aclib.exceptions.PluginException
     */
    public List<DefinedZone> getDefinedZones() throws PluginException;

    /**
     * Gets a specific defined zone.
     * 
     * @param zone defined zone name
     * 
     * @return Defined zone
     * @throws com.alternacraft.aclib.exceptions.PluginException
     */
    public DefinedZone getDefinedZone(String zone) throws PluginException;

    /**
     * Gets a subzone from a defined zone.
     * 
     * @param zone defined zone name
     * 
     * @return Map with the available subzones for that defined zone
     * @throws com.alternacraft.aclib.exceptions.PluginException
     */
    public Map<String, List<Zone>> getSubzones(String zone) throws PluginException;

    /**
     * Gets a zone (3D vector)
     * 
     * @param zone Zone name
     * 
     * @return Zone
     * @throws com.alternacraft.aclib.exceptions.PluginException
     */
    public Zone getZone(String zone) throws PluginException;

    /**
     * Saves a defined zone.
     * 
     * @param dz defined zone
     * @throws com.alternacraft.aclib.exceptions.PluginException
     */
    public void saveDefinedZone(DefinedZone dz) throws PluginException;

    /**
     * Sets a defined zone as active.
     * 
     * @param zone defined zone name
     * @throws com.alternacraft.aclib.exceptions.PluginException
     */
    public void enableDefinedZone(String zone) throws PluginException;

    /**
     * Sets a defined zone as inactive.
     * 
     * @param zone defined zone name
     * @throws com.alternacraft.aclib.exceptions.PluginException
     */
    public void disableDefinedZone(String zone) throws PluginException;    
    
    /**
     * Cleans inactive defined zones.
     * 
     * @return Number of deleted defined zones
     * @throws com.alternacraft.aclib.exceptions.PluginException
     */
    public int purge() throws PluginException;
}
