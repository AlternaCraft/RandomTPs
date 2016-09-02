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
package com.alternacraft.randomtps.Main;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginManager;
import com.alternacraft.aclib.commands.CommandArgument;
import com.alternacraft.aclib.commands.CommandListener;
import com.alternacraft.aclib.utils.StrUtils;
import com.alternacraft.randomtps.Commands.ConfigCommand;
import com.alternacraft.randomtps.Commands.InfoCommand;
import com.alternacraft.randomtps.Commands.PurgeCommand;
import com.alternacraft.randomtps.Commands.ReloadCommand;
import com.alternacraft.randomtps.Commands.ZoneCommand;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of the plugin.
 *
 * @author AlternaCraft
 * @version 1.0
 */
public class RandomTPs extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginManager pluginManager = PluginManager.instance;

        // Plugin prefix definition
        String prefix = StrUtils.translateColors("&5[RandomTeleport] ");
        pluginManager.definePluginPrefix(prefix);

        // Plugin manager setup
        if (!pluginManager.setup(this)) {
            this.setEnabled(false);
        }

        // Registering commands
        CommandListener mainCommand = new CommandListener("randomtps", this);
        mainCommand.addArgument(new CommandArgument("info", "rt info"),
                new InfoCommand());
        mainCommand.addArgument(new CommandArgument("config", "rt config"),
                new ConfigCommand());
        mainCommand.addArgument(new CommandArgument("zone", "rt zone"),
                new ZoneCommand());
        mainCommand.addArgument(new CommandArgument("purge", "rt purge"),
                new PurgeCommand());
        mainCommand.addArgument(new CommandArgument("reload", "rt reload"),
                new ReloadCommand());
        
        // Sends enabled message
        MessageManager.log("RandomTeleport has been enabled!");
    }

    @Override
    public void onDisable() {
        // Sends disable message
        MessageManager.log("RandomTeleport has been disabled!");
    }

    // <editor-fold defaultstate="collapsed" desc="World guard plugin getter">
    public WorldGuardPlugin getWorldGuard() {
        Plugin wg = getServer().getPluginManager().getPlugin("WorldGuard");
        if (wg == null) {
            return null;
        }
        return (WorldGuardPlugin) wg;
    }
    // </editor-fold>
}
