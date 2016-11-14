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
package com.alternacraft.randomtps.Langs;

import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.randomtps.Main.Manager;
import java.util.HashMap;

/**
 * Messages about general information.
 * 
 * @author AlternaCraft
 */
public enum GeneralInfo implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="MESSAGES">
    PLUGIN_ENABLED(
            "Plugin activado correctamente",
            "Plugin enabled correctly"
    ),
    PLUGIN_DISABLED(
            "Plugin desactivado correctamente",
            "Plugin disabled correctly"
    ),
    PLUGIN_RELOAD(
            "&aPlugin recargado correctamente",
            "&aPlugin reloaded successfully"
    ),
    PLUGIN_CLEANED(
            "&6Plugin limpiado correctamente",
            "&6Plugin purged successfully"
    ),
    PLUGIN_NO_CLEANED(
            "&4No se ha podido purgar la configuración",
            "&4You could not purge the configuration"
    ),
    COMMAND_FORBIDDEN(
            "&4¡No puedes ejecutar ese comando!",
            "&4You can not execute that command!"
    ),
    COMMAND_ARGUMENTS(
            "&4¡Te faltan/sobran argumentos!",
            "&4You spare arguments!"            
    );
    // </editor-fold>

    private final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     */
    private GeneralInfo(String es, String en) {
        this.locales.put(Langs.ES, es);
        this.locales.put(Langs.EN, en);
    }

    @Override
    public String getText(Langs lang) {
        return StringsUtils.translateColors(getDefaultText(lang));
    }

    @Override
    public String getDefaultText(Langs lang) {        
        String value = (this.locales.get(lang) == null)
                ? this.locales.get(Manager.BASE.getMainLanguage()) : this.locales.get(lang);

        String v = LangManager.getValueFromFile(lang, this);

        return (v == null) ? value : v;
    }
}
