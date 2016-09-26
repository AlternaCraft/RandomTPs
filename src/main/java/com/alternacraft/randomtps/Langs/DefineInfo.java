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
import java.util.HashMap;

public enum DefineInfo implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="MESSAGES">
    ALREADY_DEFINING(
            "&cUsted ya está definiendo una zona",
            ""
    ),
    REDEFINING(
            "&cEsa zona ya existe por lo que se reajustaran los valores",
            ""
    ),
    STOP_DEFINING(
            "&aCreación de zona cancelada",
            ""
    ),
    NOT_DEFINING(
            "&cNo está definiendo ninguna zona",
            ""
    ),
    SELECTION(
            "&aBloque (%BLOCK_X%, %BLOCK_Y%, %BLOCK_Z%) seleccionado.",
            ""
    ),
    NO_SELECTED(
            "&c¡No ha seleccionado ningún punto!",
            ""
    ),
    INVALID_WORLD(
            "&c¡El mundo %WORLD% no existe!",
            ""
    ),
    HOW_TO_CANCEL(
            "&6Recuerde que puede cancelar la creación de la zona escribiendo: \"%CANCEL%\"",
            ""
    ),
    POINT_1(
            "&ePor favor, seleccione la primera coordenada y escriba: '%SELECT%'",
            ""
    ),
    POINT_2(
            "&eBien, ahora seleccione la segunda coordenada y escriba: '%SELECT%'",
            ""
    ),
    SUBZONE(
            "&e¿Quieres definir una subzona? (y/n)",
            ""
    ),
    WORLDS(
            "&e¡Perfecto!. Ahora indique el mundo, o los mundos (<mundo1> <mundo2> etc), de destino",
            ""
    ),
    ZONE_CREATED(
            "&aZona creada con exito",
            ""
    );
    // </editor-fold>

    private final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     */
    private DefineInfo(String es, String en) {
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
                ? this.locales.get(Langs.EN) : this.locales.get(lang);

        String v = LangManager.getValueFromFile(lang, this);

        return (v == null) ? value : v;
    }
}
