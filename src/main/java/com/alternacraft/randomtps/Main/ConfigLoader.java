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
package com.alternacraft.randomtps.Main;

import com.alternacraft.aclib.config.ConfigDataInterface;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.randomtps.Utils.ConfigDataStore;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLoader extends ConfigDataStore implements ConfigDataInterface {

    @Override
    public void loadParams(FileConfiguration fc) { 
        // Defaults
        Manager.BASE.definePluginPrefix(fc.getString("prefix"));
        
        this.setMetrics(fc.getBoolean("metrics"));
        
        this.setAlert(fc.getBoolean("alert"));
        this.setUpdate(fc.getBoolean("update"));
        
        Manager.BASE.defineErrorFormat((short) fc.getInt("errorFormat"));
        Manager.BASE.defineMainLanguage(Langs.valueOf(fc.getString("defaultLang")));
        
        // Building options
        this.setSelection(fc.getString("selection.complete"));
        this.setCancel(fc.getString("selection.cancel"));
        
        this.setInstant(fc.getBoolean("building.instant"));

        // Coordinates
        this.setX(new int[]{fc.getInt("coords.x.max"), fc.getInt("coords.x.min")});
        this.setY(fc.getInt("coords.y"));
        this.setZ(new int[]{fc.getInt("coords.z.max"), fc.getInt("coords.z.min")});
        
        // Extras
        this.setTime(fc.getInt("defaultExtras.time"));
        this.setBroadcast_as_exp(fc.getBoolean("defaultExtras.broadcast.show_as_exp"));
        this.setPotions_effects(fc.getStringList("defaultExtras.effects.potions"));
    }

}
