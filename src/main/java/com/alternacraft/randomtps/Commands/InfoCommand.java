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
import com.alternacraft.randomtps.Main.Manager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class InfoCommand implements ArgumentExecutor {

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        JavaPlugin plugin = Manager.BASE.plugin();
        
        cs.sendMessage("");
        MessageManager.sendCommandSender(
                cs,
                "&fPlugin developed by &9AlternaCraft"
        );
        MessageManager.sendCommandSender(
                cs,
                "Version: " + ChatColor.GRAY + plugin.getDescription().getVersion()
        );
        MessageManager.sendCommandSender(
                cs,
                "&fMore info in &9" + plugin.getDescription().getWebsite()
        );
        return true;
    }
}
