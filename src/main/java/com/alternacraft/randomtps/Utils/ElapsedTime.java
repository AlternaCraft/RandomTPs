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
package com.alternacraft.randomtps.Utils;

import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.randomtps.Managers.MetricsManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;

public class ElapsedTime {

    private static final Map<String, List<Long>> REGISTER = new HashMap();

    private long timeAtStart = 0;

    public void start() {
        timeAtStart = System.currentTimeMillis();
    }

    public long getStartTime() {
        return this.timeAtStart;
    }

    public static void recordValue(String id, long f) {
        if (!ElapsedTime.REGISTER.containsKey(id)) {
            ElapsedTime.REGISTER.put(id, new ArrayList());
        }
        ElapsedTime.REGISTER.get(id).add((System.currentTimeMillis() - f));
    }

    private static int getAverageInMillis(List<Long> times) {
        int x = 0;

        for (Long l : times) {
            x += l;
        }

        return (x /= times.size());
    }

    public static String showAverage() {
        String v = ChatColor.YELLOW + "(Average) Load time of each process...\n";

        for (Map.Entry<String, List<Long>> entry : REGISTER.entrySet()) {
            String key = entry.getKey();
            List<Long> times = entry.getValue();

            v += key + " (" + StringsUtils.splitToComponentTimes(
                    (int) (getAverageInMillis(times) / 1000)) + "); ";
        }

        return v;
    }

    public static void loadAverage() {
        for (Map.Entry<String, List<Long>> entry : REGISTER.entrySet()) {
            String type = entry.getKey();
            List<Long> time = entry.getValue();

            MetricsManager.sendTimings(type, getAverageInMillis(time));
        }
    }

}
