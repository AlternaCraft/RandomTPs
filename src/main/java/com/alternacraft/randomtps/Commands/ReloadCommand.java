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
package com.alternacraft.randomtps.Commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.Langs.GeneralInfo;
import com.alternacraft.randomtps.Main.Manager;
import org.bukkit.command.CommandSender;

/**
 * Reload main configuration.
 * 
 * @author AlternaCraft
 */
public class ReloadCommand implements SubCommandExecutor {

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        Langs lang = Localizer.getLocale(cs);

        LangManager.clearMessages();
        
        // Reload main configuration
        Manager.BASE.init(Manager.BASE.plugin(), Manager.INSTANCE.loader());
        
        // Other things
        Manager.INSTANCE.registerDBs();        
        Manager.INSTANCE.loadLanguages();
        Manager.INSTANCE.loadLocalizations();

        MessageManager.sendCommandSender(cs, GeneralInfo.PLUGIN_RELOAD.getText(lang));
        
        return true;
    }
}
