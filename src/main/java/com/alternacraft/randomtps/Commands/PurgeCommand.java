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
package com.alternacraft.randomtps.Commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Main.Manager;
import org.bukkit.command.CommandSender;

public class PurgeCommand implements ArgumentExecutor {

    @Override
    public boolean execute(CommandSender cs, String[] args) {

        Langs lang = Localizer.getLocale(cs);

        int n = Manager.INSTANCE.getZonesDB().purge();
        MessageManager.sendCommandSender(cs, GameInfo.PURGE_ZONES.getText(lang)
                .replace("%QUANT%", String.valueOf(n)));

        return true;
    }
}
