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
package com.alternacraft.randomtps.Zone;

import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.NumbersUtils;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Managers.ZoneManager;
import static com.alternacraft.randomtps.Utils.ConfigDataStore.MAX;
import static com.alternacraft.randomtps.Utils.ConfigDataStore.MIN;
import static com.alternacraft.randomtps.Utils.ZoneUtils.CHUNK;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Defined zone.
 * 
 * @author AlternaCraft
 */
public class DefinedZone extends Zone {

    private final static short DISTANCE = 10;
    
    private String zoneName = null;
    private String origin = null;
    private final List<String> dests = new ArrayList();

    private Map<String, List<Zone>> subzones = new HashMap();

    // Limits
    protected int[] x = {10000, -5000};
    protected int y = 150;
    protected int[] z = {10000, -5000};
    
    // Effect time    
    private int time = 10;
    // Broadcast
    private boolean show_as_exp = true;
    // Effects
    private final Map<String, Integer> potion_effects = new HashMap();
    // Validations
    private final List<String> validations = new ArrayList();

    public DefinedZone(String zonename, Zone zone, String origin,
            List<String> destinations) {
        super(zone.getP1(), zone.getP2());

        this.zoneName = zonename;
        this.origin = origin;

        this.dests.addAll(destinations);
    }

    public DefinedZone(String zonename, Zone zone, String origin,
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

    public List<String> getValidations() {
        return validations;
    }
    
    public void setValidations(List<String> validations) {
        for (String validation : validations) {
            switch(validation) {
                case "falling_safe":
                    this.validations.add(ZoneManager.DEFAULT_VALIDATIONS.FALLING.name());
                    break;
                case "no_players":
                    this.validations.add(ZoneManager.DEFAULT_VALIDATIONS.PLAYERS.name());
                    break;
                case "no_fluids":
                    this.validations.add(ZoneManager.DEFAULT_VALIDATIONS.FLUIDS.name());
                    break;
                case "no_factions":
                    this.validations.add(ZoneManager.DEFAULT_VALIDATIONS.FACTIONS.name());
                    break;
                case "no_wgregions":
                    this.validations.add(ZoneManager.DEFAULT_VALIDATIONS.WGREGIONS.name());
                    break;
                default:
                    this.validations.add(validation);
            }
        }
    }

    public int[] getX() {
        return x;
    }
    
    public int getMaxX() {
        return x[MAX];
    }

    public int getMinX() {
        return x[MIN];
    }    

    public void setX(int[] x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[] getZ() {
        return z;
    }
    
    public int getMaxZ() {
        return z[MAX];
    }

    public int getMinZ() {
        return z[MIN];
    }    

    public void setZ(int[] z) {
        this.z = z;
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
    
    /**
     * Gets the visit location.
     * 
     * @param w World
     * 
     * @return Location to visit
     */
    public Location getVisitLocation(World w) {
        Vector v = p1.getMidpoint(p2);

        int distance = NumbersUtils.differenceBetween(p1.getBlockZ(), p2.getBlockZ());
        do {
            distance /= 2;
        } while (distance > CHUNK * DISTANCE);
        v.setZ((NumbersUtils.getLower(p1.getBlockZ(), p2.getBlockZ())) - distance);

        return v.toLocation(w);
    }

    /**
     * Gets a formatted string.
     * 
     * @param l Player's language
     * 
     * @return The formatted string
     */
    public String toClickString(Langs l) {
        String title = GameInfo.ZONE_GO_TITLE.getText(l);
        String coord1 = "*** P1 (" + this.p1.getBlockX() + ", " + this.getP1().getBlockY()
                + ", " + this.getP1().getBlockZ() + ")";
        String coord2 = "*** P2 (" + this.getP2().getBlockX() + ", " + this.getP2().getBlockY()
                + ", " + this.getP2().getBlockZ() + ")";
        String info = GameInfo.ZONE_GO_CLICK.getText(l);

        return title + "\n" + coord1 + "\n" + coord2 + "\n" + info;
    }    
}
