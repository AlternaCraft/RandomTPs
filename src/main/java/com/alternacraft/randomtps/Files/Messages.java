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
package com.alternacraft.randomtps.Files;

import static com.alternacraft.aclib.langs.LangManager.DIRECTORY;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.utils.StrUtils;
import java.io.File;
import java.util.HashMap;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Messages implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="MESSAGES">
    PLUGIN_ENABLED(
            "Plugin activado correctamente",
            "Plugin activated successfully"
    ),
    PLUGIN_DISABLED(
            "Plugin desactivado correctamente",
            "Plugin disabled!"
    ),
    PLUGIN_RELOAD(
            "&6Plugin recargado correctamente",
            "&6Plugin recharged properly"
    );
    // </editor-fold>

    private final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     */
    private Messages(String es, String en) {
        this.locales.put(Langs.ES, es);
        this.locales.put(Langs.EN, en);
    }

    @Override
    public String getText(Langs lang) {
        return StrUtils.translateColors(getDefaultText(lang));
    }

    @Override
    public String getDefaultText(Langs lang) {
        // Save the value from this (internally)
        String value = (this.locales.get(lang) == null)
                ? this.locales.get(Langs.EN) : this.locales.get(lang);

        // File access to get custom message (if exists)
        File langFile = new File(DIRECTORY + "messages_" + lang.name().replace("CUSTOM_", "") + ".yml");
        YamlConfiguration langConf = YamlConfiguration.loadConfiguration(langFile);

        // Value from the file (externally)
        if (langConf != null && langConf.contains(this.name())) {
            value = langConf.getString(this.name());
        }

        return value;
    }
}
