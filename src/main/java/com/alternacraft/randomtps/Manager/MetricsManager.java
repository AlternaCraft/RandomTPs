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
package com.alternacraft.randomtps.Manager;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.randomtps.Main.Manager;
import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class MetricsManager {

    // <editor-fold defaultstate="collapsed" desc="GRAPHS">
    // </editor-fold>

    public static void sendData(final JavaPlugin plugin) {
        try {
            if (Manager.INSTANCE.loader().isMetrics()) {
                Metrics metrics = new Metrics(plugin);
                
                // Graphs //
                
                metrics.start();
            }
        } catch (IOException e) {
            MessageManager.logError(e.getMessage());
        }
    }
}
