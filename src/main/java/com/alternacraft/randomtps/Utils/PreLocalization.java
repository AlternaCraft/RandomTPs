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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.util.Vector;

public class PreLocalization extends PreValues {

    private final String zone;

    private Vector c1;
    private Vector c2;
    private String origin;

    private final List<String> dests = new ArrayList();

    private final Map<String, List<Zone>> subzones = new HashMap();
    private Vector cs1;

    public PreLocalization(String zone) {
        this.zone = zone;
    }

    public String getZoneName() {
        return zone;
    }

    public void setC1(Vector c1) {
        this.c1 = c1;
    }

    public void setC2(Vector c2) {
        this.c2 = c2;
    }

    public void setOrigin(String or) {
        this.origin = or;
    }

    public void addDestination(String dest) {
        if (!this.dests.contains(dest)) {
            this.dests.add(dest);
        }
    }

    public void setCS1(Vector cs1) {
        this.cs1 = cs1;
    }

    public Vector getCS1() {
        return cs1;
    }

    public void addSubzone(String w, Zone z) {
        if (this.subzones.get(w) == null) {
            this.subzones.put(w, new ArrayList());
        }
        this.subzones.get(w).add(z);
    }

    public Localization toLocalization() {
        if (this.askSubzone()) {
            return new Localization(zone, new Zone(c1, c2), origin, subzones);
        } else {
            return new Localization(zone, new Zone(c1, c2), origin, dests);
        }

    }

}
