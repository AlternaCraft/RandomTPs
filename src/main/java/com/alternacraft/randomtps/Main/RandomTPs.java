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
package com.alternacraft.randomtps.Main;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.extras.Recorder;
import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.randomtps.Langs.GameInfo;
import static com.alternacraft.randomtps.Listeners.HandleBuild.DISABLED;
import com.alternacraft.randomtps.Managers.MetricsManager;
import com.alternacraft.randomtps.Managers.UpdatesManager;
import com.alternacraft.randomtps.Utils.ZoneBuilder;
import com.alternacraft.randomtps.Utils.ZoneBuilder.BlocksReplacer;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class.
 *
 * @author AlternaCraft
 */
public class RandomTPs extends JavaPlugin {
    
    public static final Recorder METER = new Recorder();

    // Log files...
    public static final String PERFORMANCE_FILE = "performance.txt";
    
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
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            MetricsManager.load(Manager.BASE.plugin());
            UpdatesManager.testUpdate(Manager.BASE.plugin(), getFile());
        });

        // Sends enabled message
        this.getLogger().info("RandomTPs has been enabled!");
    }
    
    @Override
    public void onDisable() {
        /* METRICS CONFIGURATION */
        METER.saveToLog(PERFORMANCE_FILE);
        
        CommandSender cs = this.getServer().getConsoleSender();

        /* Rollback zones */
        for (Map.Entry<String, ZoneBuilder> entry : DISABLED.entrySet()) {
            ZoneBuilder zonebuilder = entry.getValue();
            
            if (!zonebuilder.isFinished()) {
                zonebuilder.stop();
            }
            
            BlocksReplacer br = zonebuilder.new BlocksReplacer(true, cs, zonebuilder);
            br.forceInstant();
            
            MessageManager.sendCommandSender(cs,
                        GameInfo.ZONE_START_ROLLBACK.getText(Localizer.getLocale(cs))
                                .replace("%NAME%", zonebuilder.zoneName())); 
            
            br.run();
            
            synchronized (br) {
                String time = StringsUtils.splitToComponentTimes(zonebuilder.elapsedTime() / 1000);
                MessageManager.sendCommandSender(cs, GameInfo.ZONE_END_ROLLBACK
                    .getText(Localizer.getLocale(cs)).replace("%TIME%", time)
                        .replace("%NAME%", zonebuilder.zoneName()));
            }
        }
        
        DISABLED.clear();

        // Sends disable message
        this.getLogger().info("RandomTPs has been disabled!");
    }
}
