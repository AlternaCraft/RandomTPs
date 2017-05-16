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
package com.alternacraft.randomtps.Managers;

import com.alternacraft.aclib.utils.Metrics;
import com.alternacraft.aclib.utils.PluginLog;
import com.alternacraft.randomtps.Main.Manager;
import static com.alternacraft.randomtps.Main.RandomTPs.PERFORMANCE_FILE;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Metrics manager.
 *
 * @author AlternaCraft
 */
public class MetricsManager {

    public static void setGraphs(Metrics metrics) {

        metrics.addCustomChart(new Metrics.SimplePie("default_language") {
            @Override
            public String getValue() {
                return Manager.INSTANCE.loader().getDefaultLang().name();
            }
        });

        metrics.addCustomChart(new Metrics.SimplePie("cancel_command") {
            @Override
            public String getValue() {
                return Manager.INSTANCE.loader().getCancel();
            }
        });

        metrics.addCustomChart(new Metrics.SimplePie("add_command") {
            @Override
            public String getValue() {
                return Manager.INSTANCE.loader().getSelection();
            }
        });
        metrics.addCustomChart(new Metrics.SimplePie("building_mode") {
            @Override
            public String getValue() {
                return (Manager.INSTANCE.loader().doInstantly()) ? "Instant" : "No instant";
            }
        });

        metrics.addCustomChart(new Metrics.SimplePie("teletransportation_height") {
            @Override
            public String getValue() {
                return String.valueOf(Manager.INSTANCE.loader().getY());
            }
        });

        metrics.addCustomChart(new Metrics.SimplePie("enabled_validations") {
            @Override
            public String getValue() {
                String result = "";
                List<String> validations = Manager.INSTANCE.loader().getValidations();
                for (int i = 0; i < validations.size(); i++) {
                    result += validations.get(i);
                    if (i < validations.size() - 1) {
                        result += ", ";
                    }
                }
                return (result.isEmpty()) ? "None" : result;
            }
        });

        metrics.addCustomChart(new Metrics.AdvancedPie("general_statistics") {
            @Override
            public HashMap<String, Integer> getValues(HashMap<String, Integer> valueMap) {
                PluginLog pl = new PluginLog(PERFORMANCE_FILE);
                pl.importLog();

                Map<Date, List<String>> loginfo = PluginLog.getValuesPerDate(pl.getMessages());

                for (Map.Entry<Date, List<String>> entry : loginfo.entrySet()) {
                    List<String> reports = entry.getValue();
                    for (String report : reports) {
                        Pattern p = Pattern.compile("(.*) \\- (\\d+)");
                        Matcher m = p.matcher(report);

                        String type = "";
                        int value = 0;

                        if (m.find()) {
                            type = m.group(1);
                            value = Integer.valueOf(m.group(2));
                        }

                        valueMap.put(type, value);
                    }
                }

                pl.delete();

                return valueMap;
            }
        });
    }

    public static void load(final JavaPlugin plugin) {
        if (Manager.INSTANCE.loader().isMetrics()) {
            Metrics metrics = new Metrics(plugin);
            setGraphs(metrics);
        }
    }
}
