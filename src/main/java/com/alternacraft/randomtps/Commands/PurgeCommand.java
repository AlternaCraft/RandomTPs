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
package com.alternacraft.randomtps.Commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.exceptions.PluginException;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Main.Manager;
import org.bukkit.command.CommandSender;

/**
 * Purge inactive zones.
 * 
 * @author AlternaCraft
 */
public class PurgeCommand implements SubCommandExecutor {

    @Override
    public boolean execute(CommandSender cs, String[] args) {

        Lang lang = Localizer.getLocale(cs);

        try {
            int n = Manager.INSTANCE.getZonesDB().purge();
            MessageManager.sendPluginMessage(cs, GameInfo.PURGE_ZONES.getText(lang)
                    .replace("%QUANT%", String.valueOf(n)));            
        } catch (PluginException ex) {
            MessageManager.logArrayError(ex.getCustomStacktrace());
        }        

        return true;
    }
}
