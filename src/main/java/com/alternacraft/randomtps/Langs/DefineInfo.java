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
package com.alternacraft.randomtps.Langs;

import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.randomtps.Main.Manager;
import java.util.HashMap;

/**
 * Messages about zone defining.
 *
 * @author AlternaCraft
 */
public enum DefineInfo implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="MESSAGES">
    ALREADY_DEFINING(
            "&cUsted ya está definiendo una zona",
            "&cYou are already defining a zone"
    ),
    REDEFINING(
            "&cEsa zona ya existe por lo que se creará de nuevo",
            "&cThat zone already exists so you are redefining the zone"
    ),
    STOP_DEFINING(
            "&aCreación de zona cancelada",
            "&aZone creation canceled"
    ),
    NOT_DEFINING(
            "&cNo está definiendo ninguna zona",
            "&cYou are not defining any zone"
    ),
    SELECTED(
            "&aBloque (%BLOCK_X%, %BLOCK_Y%, %BLOCK_Z%) seleccionado",
            "&aBlock (%BLOCK_X%, %BLOCK_Y%, %BLOCK_Z%) selected"
    ),
    UNSELECTED(
            "&c¡No ha seleccionado ninguna coordenada!",
            "&cYou haven not selected any coordinate!"
    ),
    INVALID_WORLD(
            "&c¡El mundo %WORLD% no existe!",
            "&cWorld %WORLD% does not exist!"
    ),
    HOW_TO_CANCEL(
            "&6Recuerde que puede cancelar la creación de la zona escribiendo: \"%CANCEL%\"",
            "&6You remember you can cancel a zone creation by writing: \"%CANCEL%\""
    ),
    COORD_1(
            "&ePor favor, seleccione la primera coordenada con el click derecho y escriba: \"%SELECT%\"",
            "&ePlease, select the first coordinate with right click of the mouse and write: \"%SELECT%\""
    ),
    COORD_2(
            "&eBien, ahora seleccione la segunda coordenada y escriba: \"%SELECT%\"",
            "&eCool. Now, you have to select the second coordinate and write: \"%SELECT%\""
    ),
    SUBZONE(
            "&e¿Quieres definir una subzona? (y/n)",
            "&eDo you want to define a subzone? (y/n)"
    ),
    WORLDS(
            "&e¡Perfecto!. Ahora indique el mundo, o los mundos (<mundo1> <mundo2> etc), de destino",
            "&ePerfect!, Now you have to indicate the destination world/s (<world1> <world2> etc)"
    ),
    ZONE_CREATED(
            "&aZona creada con exito",
            "&aZone created successfully"
    );
    // </editor-fold>// </editor-fold>

    private final HashMap<Lang, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     */
    private DefineInfo(String es, String en) {
        this.locales.put(Lang.ES, es);
        this.locales.put(Lang.EN, en);
    }

    @Override
    public String getText(Lang lang) {
        return StringsUtils.translateColors(getDefaultText(lang));
    }

    @Override
    public String getDefaultText(Lang lang) {
        Lang main = Manager.BASE.getMainLanguage();
        String v = LangManager.getValueFromFile(lang, this);
        v = (v == null) ? this.locales.get(lang) : v;
        v = (v == null) ? LangManager.getValueFromFile(main, this) : v;
        v = (v == null) ? this.locales.get(main) : v;
        return v;
    }
}
