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

import com.alternacraft.randomtps.Localizations.Localization;
import com.alternacraft.randomtps.Localizations.Zone;
import java.util.List;
import java.util.Map;

/**
 * Database management.
 * 
 * @author AlternaCraft
 */
public interface ZonesDB {

    /**
     * Gets all localizations.
     * 
     * @return List
     */
    public List<Localization> getLocalizations();

    /**
     * Gets a specified localization.
     * 
     * @param localization Localization name
     * 
     * @return Localization
     */
    public Localization getLocalization(String localization);

    /**
     * Gets a subzone from a localization.
     * 
     * @param localization Localization name
     * 
     * @return Map with the available subzones for that localization
     */
    public Map<String, List<Zone>> getSubzones(String localization);

    /**
     * Gets a zone (3D vector)
     * 
     * @param localization Localization name
     * 
     * @return Zone
     */
    public Zone getZone(String localization);

    /**
     * Saves a localization.
     * 
     * @param l Localization
     */
    public void saveLocalization(Localization l);

    /**
     * Sets a localization as active.
     * 
     * @param localization Localization name
     */
    public void enableLocalization(String localization);

    /**
     * Sets a localization as inactive.
     * 
     * @param localization Localization name
     */
    public void disableLocalization(String localization);    
    
    /**
     * Cleans inactive localizations.
     * 
     * @return Number of deleted localizations
     */
    public int purge();
}
