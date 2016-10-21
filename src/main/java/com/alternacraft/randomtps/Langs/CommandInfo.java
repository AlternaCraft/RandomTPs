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

public enum CommandInfo implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="MESSAGES">
    COMMAND_EMPTY(
            "Lista de comandos",
            "Command list"
    ),
    COMMAND_INFO_DESC(
            "Información del plugin",
            "Plugin information"
    ),
    COMMAND_DEFINE_DESC(
            "Define una zona de teletransporte",
            "Configure a teleport zone"
    ),
    COMMAND_REDEFINE_DESC(
            "Redefinir la configuracion de una zona",
            "Redefine the configuration of a zone"
    ),
    COMMAND_ZONE_DESC(
            "Gestiona una region",
            "Manage a region"
    ),
    COMMAND_RESTOREALL_DESC(
            "Restaura todas las regiones",
            "Restore all regions"
    ),
    COMMAND_PURGE_DESC(
            "Para limpiar las zonas inactivas",
            "Remove inactive zones"
    ),
    COMMAND_RELOAD_DESC(
            "Para recargar el config del plugin",
            "Reload all the plugin"
    ),
    COMMAND_STATS_DESC(
            "Para obtener informacion sobre el rendimiento del plugin",
            "For getting information about plugin perfomance"
    );
    // </editor-fold>

    private final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     */
    private CommandInfo(String es, String en) {
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
