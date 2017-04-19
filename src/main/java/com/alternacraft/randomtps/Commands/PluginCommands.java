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

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.commands.CommandListener;
import com.alternacraft.aclib.commands.SubCommand;
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.Main.Manager;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commands list.
 * 
 * @author AlternaCraft
 */
public class PluginCommands implements SubCommandExecutor {

    public static String USAGE = "rtp";

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        Langs lang = Localizer.getLocale(cs);

        CommandListener cl = Manager.INSTANCE.getMainCommand().cmdListener();

        cs.sendMessage("");
        cs.sendMessage(PluginBase.INSTANCE.pluginPrefix() + ChatColor.AQUA
                + " v" + cl.plugin().getDescription().getVersion() + ChatColor.RESET);

        for (Map.Entry<SubCommand, SubCommandExecutor> entry : cl.arguments().entrySet()) {
            SubCommand key = entry.getKey();

            // Don't show if he does not have permission
            if (cs instanceof Player && !key.getCommand().isEmpty()) { 
                String permission = cl.prefix() + "." + key.getCommand();
                if (!((Player) cs).hasPermission(permission)) {
                    continue;
                }
            }

            cs.sendMessage("  /" + ChatColor.BLUE + key.getUsage() + ChatColor.RESET
                    + " - " + ChatColor.GRAY + key.getDescription(lang));
        }

        cs.sendMessage("■ " + ChatColor.AQUA + "Created By "
                + cl.plugin().getDescription().getAuthors().get(0)
                + ChatColor.RESET + " ■");

        return true;
    }

}
