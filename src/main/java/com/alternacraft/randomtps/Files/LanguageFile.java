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

import com.alternacraft.aclib.PluginManager;
import com.alternacraft.aclib.files.PluginFile;
import java.io.File;

public class LanguageFile extends PluginFile {
    public static enum LangValues {
        // <editor-fold defaultstate="collapsed" desc="Language values">
        CHUNK_ALREADY_CLAIMED("faction.already-claimed", "&4This terrain has been claimed by another faction!");
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Internal stuff">
        private String path = null;
        private String value = null;

        private LangValues(String path, String value) {
            this.path = path;
            this.value = value;
        }

        public String path() {
            return path;
        }

        public String value() {
            return value;
        }
        // </editor-fold>
    }

    public LanguageFile() {
        String fullpath = PluginManager.instance.plugin().getDataFolder()
                + "/lang.yml";

        file = new File(fullpath);

        if (!file.exists()) {
            for (LangValues value : LangValues.values()) {
                values.put(value.path(), value.value());
            }

            super.createNewYamlConfiguration(file);
        } else {
            super.loadYamlConfiguration(file);
        }
    }

    public String getMessage(LangValues message) {
        return (String) getNode(message.path());
    }
}
