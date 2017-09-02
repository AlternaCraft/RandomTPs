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
package com.alternacraft.randomtps.Main;

import com.alternacraft.aclib.config.ConfigDataInterface;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.randomtps.Utils.ConfigDataStore;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Config loader.
 * 
 * @author AlternaCraft
 */
public class ConfigLoader extends ConfigDataStore implements ConfigDataInterface {

    @Override
    public void loadParams(FileConfiguration fc) { 
        // Defaults
        Manager.BASE.definePluginPrefix(fc.getString("prefix"));
        Manager.BASE.defineErrorFormat((short) fc.getInt("errorFormat"));
        Manager.BASE.defineMainLanguage(Lang.valueOf(fc.getString("defaultLang")));
        
        // General
        this.setMetrics(fc.getBoolean("metrics"));
        
        this.setAlert(fc.getBoolean("alert"));
        this.setUpdate(fc.getBoolean("update"));
        
        // Building options
        this.setSelection(fc.getString("selection.add"));
        this.setCancel(fc.getString("selection.cancel"));
        
        this.setInstant(fc.getBoolean("building.instant"));

        // EXTRAS
        this.setX(new int[] {
            fc.getInt("defaults.limits.x.max"), fc.getInt("defaults.limits.x.min")
        });        
        this.setZ(new int[] {
            fc.getInt("defaults.limits.z.max"), fc.getInt("defaults.limits.z.min")
        });
        this.setY(fc.getInt("defaults.height"));
        this.setCollisionEffect(fc.getString("defaults.collision"));
        this.setTime(fc.getInt("defaults.time"));                
        this.setBroadcast_as_exp(fc.getBoolean("defaults.broadcast.as_experience"));
        this.setPotions_effects(fc.getStringList("defaults.effects.potions"));
        this.setValidations(fc.getStringList("defaults.validations"));
    }
}
