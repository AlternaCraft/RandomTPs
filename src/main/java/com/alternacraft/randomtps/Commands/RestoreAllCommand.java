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
import org.bukkit.command.CommandSender;
import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.Langs.GameInfo;
import static com.alternacraft.randomtps.Listeners.HandleBuild.DISABLED;
import com.alternacraft.randomtps.Utils.ZoneBuilder;
import java.util.Map;

public class RestoreAllCommand implements ArgumentExecutor {

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        Langs lang = Localizer.getLocale(cs);
        
        if (!DISABLED.isEmpty()) {
            MessageManager.sendCommandSender(cs, GameInfo.RESET_ZONES.getText(lang));
            for (Map.Entry<String, ZoneBuilder> entry : DISABLED.entrySet()) {
                ZoneBuilder zonebuilder = entry.getValue();
                if (zonebuilder.isFinished()) {
                    zonebuilder.hide(cs);
                } else {
                    MessageManager.sendCommandSender(cs,
                            GameInfo.ZONE_IN_USE.getText(lang));
                }
            }
        } else {
            MessageManager.sendCommandSender(cs,
                    GameInfo.NO_ZONES_TO_RESET.getText(lang));
        }
        
        return true;
    }
}
