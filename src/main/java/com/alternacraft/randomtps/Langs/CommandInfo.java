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
package com.alternacraft.randomtps.Langs;

import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.langs.LangInterface;
import java.util.HashMap;
import java.util.Map;

/**
 * Messages about commands.
 * 
 * @author AlternaCraft
 */
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
    COMMAND_ZONE_DESC(
            "Gestiona una region",
            "Manage a region"
    ),
    COMMAND_ZONE_LIST_DESC(
            "Lista de regiones activas",
            "Region list"
    ),
    COMMAND_ZONE_TOGGLEACTIVE_DESC(
            "Activa/Desactiva una region",
            "Enable/Disable a region"
    ),
    COMMAND_ZONE_GO_DESC(
            "Ir a una zona",
            "Go to a zone"
    ),
    COMMAND_ZONE_CREATE_DESC(
            "Crea/Modifica una zona",
            "Create/Modify a region"
    ),
    COMMAND_ZONE_SHOW_DESC(
            "Visualiza una zona",
            "Show a region"
    ),
    COMMAND_ZONE_HIDE_DESC(
            "Oculta una zona",
            "Manage a region"
    ),
    COMMAND_ZONE_RESTOREALL_DESC(
            "Restaura todas las regiones",
            "Restore all regions"
    ),
    COMMAND_PURGE_DESC(
            "Para limpiar las zonas inactivas",
            "Remove inactive zones"
    ),
    COMMAND_RELOAD_DESC(
            "Para recargar el config del plugin",
            "Reload the plugin configuration"
    );
    // </editor-fold>

    private final HashMap<Lang, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     */
    private CommandInfo(String es, String en) {
        this.locales.put(Lang.ES, es);
        this.locales.put(Lang.EN, en);
    }
    
    @Override
    public Map<Lang, String> getLocales() {
        return this.locales;
    }

    @Override
    public Enum getEnum() {
        return this;
    }
}
