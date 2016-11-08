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

import com.alternacraft.aclib.utils.Timer;
import com.alternacraft.randomtps.Managers.MetricsManager;
import com.alternacraft.randomtps.Managers.UpdaterManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of the plugin.
 *
 * @author AlternaCraft
 * @version 1.0
 */
public class RandomTPs extends JavaPlugin {

    public static final Timer PERFORMANCE = new Timer();
    
    @Override
    public void onEnable() {
        Manager pluginManager = Manager.INSTANCE;

        // Set up before start
        Manager.BASE.definePluginPrefix("&1[&bRandomTPs&1]");        
        
        // Plugin manager init
        if (!pluginManager.setup(this)) {
            this.setEnabled(false);
            return;
        }

        // Later tasks
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                MetricsManager.load(Manager.BASE.plugin());
                UpdaterManager.testUpdate(Manager.BASE.plugin(), getFile());
            }
        });

        // Sends enabled message
        this.getLogger().info("RandomTPs has been enabled!");
    }

    @Override
    public void onDisable() {       
        /* METRICS CONFIGURATION */
        PERFORMANCE.saveToLog("performance.txt");
        
        // Sends disable message
        this.getLogger().info("RandomTPs has been disabled!");
    }
}
