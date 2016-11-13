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
import com.alternacraft.aclib.utils.Metrics;
import com.alternacraft.aclib.utils.Metrics.Graph;
import com.alternacraft.aclib.utils.PluginLog;
import com.alternacraft.randomtps.Main.Manager;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.plugin.java.JavaPlugin;

public class MetricsManager {

    private static Metrics metrics;

    public static void setGraphs() {
        PluginLog pl = new PluginLog("performance.txt");
        pl.importLog();

        Map<Date, List<String>> loginfo = PluginLog.getValuesPerDate(pl.getMessages());

        for (Map.Entry<Date, List<String>> entry : loginfo.entrySet()) {
            List<String> reports = entry.getValue();
            Graph graph = metrics.createGraph("General statistics");

            for (String report : reports) {
                Pattern p = Pattern.compile("(.*) \\- (\\d+)");
                Matcher m = p.matcher(report);

                String type = "";
                int value = 0;

                if (m.find()) {
                    type = m.group(1);
                    value = Integer.valueOf(m.group(2));
                }

                addPlotter(graph, type, value);
            }
        }
        
        pl.delete();
    }

    public static void load(final JavaPlugin plugin) {
        try {
            if (Manager.INSTANCE.loader().isMetrics()) {
                metrics = new Metrics(plugin);
                setGraphs();
                metrics.start();
            }
        } catch (IOException e) {
            MessageManager.logError(e.getMessage());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="CLASS STUFF">
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
}
