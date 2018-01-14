/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.randomtps.Zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.util.Vector;

/**
 * Pre defined zone.
 * 
 * @author AlternaCraft
 */
public class PreDefinedZone {

    private final String zone;    
    private final boolean redefine;

    private Vector c1;
    private Vector c2;
    private String origin;

    private final List<String> dests = new ArrayList();

    private final Map<String, List<Zone>> subzones = new HashMap();
    private Vector cs1;

    // Prezone
    private boolean coord1 = false;
    private boolean coord2 = false;

    private boolean subzone = false;

    private boolean askWorld = false;    
    private boolean askSubzone = false;
    // End Prezone
    
    public PreDefinedZone(String zone, boolean redefine) {
        this.zone = zone;
        this.redefine = redefine;
    }    
    
    public String getZoneName() {
        return zone;
    }

    public boolean redefine() {
        return redefine;
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
    
    public boolean coord1Saved() {
        return coord1;
    }

    public void setCoord1(boolean coord1) {
        this.coord1 = coord1;
    }

    public boolean coord2Saved() {
        return coord2;
    }

    public void setCoord2(boolean coord2) {
        this.coord2 = coord2;
    }

    public void setSubzone(boolean b) {
        this.subzone = b;
    }

    public boolean isSubzone() {
        return this.subzone;
    }

    public boolean askWorld() {
        return askWorld;
    }

    public void askWorld(boolean world) {
        this.askWorld = world;
    }

    public boolean askSubzone() {
        return askSubzone;
    }

    public void askSubzone(boolean subzone) {
        this.askSubzone = subzone;
    }

    public void resetValues() {
        this.coord1 = false;
        this.coord2 = false;
    }    

    public DefinedZone toDefinedZone() {
        if (this.askSubzone()) {
            return new DefinedZone(zone, new Zone(c1, c2), origin, subzones);
        } else {
            return new DefinedZone(zone, new Zone(c1, c2), origin, dests);
        }
    }    
}
