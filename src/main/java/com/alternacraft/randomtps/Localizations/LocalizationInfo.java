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
package com.alternacraft.randomtps.Localizations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Localization
 * 
 * @author AlternaCraft
 */
public class LocalizationInfo extends Localization {

    private String zoneName = null;
    private String origin = null;
    private final List<String> dests = new ArrayList();

    private Map<String, List<Zone>> subzones = new HashMap();

    // Basics    
    private int time = 10;
    // Broadcast
    private boolean show_as_exp = true;
    // Effects
    private final Map<String, Integer> potion_effects = new HashMap();

    public LocalizationInfo(String zonename, Zone zone, String origin,
            List<String> destinations) {
        super(zone.getP1(), zone.getP2());

        this.zoneName = zonename;
        this.origin = origin;

        this.dests.addAll(destinations);
    }

    public LocalizationInfo(String zonename, Zone zone, String origin,
            Map<String, List<Zone>> subzones) {
        super(zone.getP1(), zone.getP2());

        this.zoneName = zonename;
        this.origin = origin;

        this.subzones = subzones;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZone(String zone) {
        this.zoneName = zone;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<String> getDestinations() {
        return dests;
    }

    public void setDestination(List<String> arrives) {
        this.dests.clear();
        this.dests.addAll(arrives);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int no_pvp) {
        this.time = no_pvp;
    }

    public boolean broadcastAsEXP() {
        return show_as_exp;
    }

    public void setBroadcastAsEXP(boolean show_as_exp) {
        this.show_as_exp = show_as_exp;
    }

    public Map<String, Integer> getPotionEffects() {
        return potion_effects;
    }

    public void setPotion_effects(List<String> effects) {
        for (String effect : effects) {
            String[] values = effect.split(" ");
            potion_effects.put(values[0], Integer.valueOf(values[1]));
        }
    }

    public void addSubzone(String w, Zone z) {
        if (!this.subzones.containsKey(w)) {
            if (this.subzones.get(w) == null) {
                this.subzones.put(w, new ArrayList());
            }
            this.subzones.get(w).add(z);
        }
    }

    public boolean hasSubzones() {
        return this.subzones.size() > 0;
    }

    public Map<String, List<Zone>> getSubzones() {
        return subzones;
    }
}
