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
package com.alternacraft.randomtps.API;

import com.alternacraft.randomtps.Utils.Localization;
import com.alternacraft.randomtps.Utils.Zone;
import java.util.List;
import java.util.Map;

public interface ZonesDB {

    /**
     * Method for getting all localizations
     * 
     * @return List
     */
    public List<Localization> getLocalizations();

    /**
     * Method for getting a localization
     * 
     * @param localization Localization name
     * 
     * @return Localization
     */
    public Localization getLocalization(String localization);

    /**
     * Method for getting a subzone from a localization
     * 
     * @param localization Localization name
     * 
     * @return Map with the available subzones for that localization
     */
    public Map<String, List<Zone>> getSubzones(String localization);

    /**
     * Method for getting a zone (3D vector)
     * 
     * @param localization Localization name
     * 
     * @return Zone (3D vector)
     */
    public Zone getZone(String localization);

    /**
     * Method for saving a zone
     * 
     * @param l Localization
     */
    public void saveLocalization(Localization l);

    /**
     * Method for setting a localization as active
     * 
     * @param localization Localization name
     */
    public void enableLocalization(String localization);

    /**
     * Method for setting a localization as inactive
     * 
     * @param localization Localization name
     */
    public void disableLocalization(String localization);    
    
    /**
     * Method for cleaning inactive localizations
     * 
     * @return Number of deleted records
     */
    public int purge();
}
