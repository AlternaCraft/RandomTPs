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

import com.alternacraft.aclib.utils.Randomizer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Logic zone.
 * 
 * @author AlternaCraft
 */
public class Zone {

    protected final Vector p1;
    protected final Vector p2;

    public Zone(Vector p1, Vector p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Vector getP1() {
        return p1;
    }

    public Vector getP2() {
        return p2;
    }

    /**
     * Gets a random location inside of the zone.
     *
     * @param world World
     *
     * @return The random location
     */
    public Location randomLocation(World world) {
        return Randomizer.randBetweenVector(this.p1, this.p2).toLocation(world);
    }
    
    /**
     * Checks if a player is inside of a zone.
     *
     * @param p Player coordinates
     * @return True if he is; False if not
     */
    public boolean isInsideOfMe(Vector p) {
        return p.isInAABB(Vector.getMinimum(p1, p2), Vector.getMaximum(p1, p2));
    }    
}
