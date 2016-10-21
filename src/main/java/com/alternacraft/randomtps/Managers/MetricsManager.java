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
package com.alternacraft.randomtps.Managers;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.randomtps.Main.Manager;
import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

public class MetricsManager {

    private static Metrics metrics;

    //<editor-fold defaultstate="collapsed" desc="PLOTTER">
    private static void addPlotter(Graph g, String plotter, final int number) {
        if (number == 0) {
            return;
        }
        g.addPlotter(new Metrics.Plotter(plotter) {
            @Override
            public int getValue() {
                return number;
            }
        });
    }
    //</editor-fold>    

    public static void send() {
        metrics.start();
    }
    
    public static void getGraphs() {
        
    }
    
    public static void load(final JavaPlugin plugin) {
        try {
            if (Manager.INSTANCE.loader().isMetrics()) {
                metrics = new Metrics(plugin);
                getGraphs();
                metrics.start();
            }
        } catch (IOException e) {
            MessageManager.logError(e.getMessage());
        }
    }

    public static void sendTimings(String type, int time) {
        if (time == 0)
            return;
        
        if (Manager.INSTANCE.loader().isMetrics()) {
            Graph graph = metrics.createGraph(type);
            addPlotter(graph, "Time average", time);
            metrics.addGraph(graph);            
        }
    }
}
