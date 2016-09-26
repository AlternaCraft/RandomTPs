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

import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.NumbersUtils;
import com.alternacraft.aclib.utils.Randomizer;
import com.alternacraft.randomtps.Langs.GameInfo;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 *
 * @author Julian
 */
public class Zone {

    public static final short DISTANCE = 10;
    private static final int CHUNK = 16;

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

    public Location getVisitLocation(World w) {
        Vector v = p1.getMidpoint(p2);

        int distance = NumbersUtils.differenceBetween(p1.getBlockZ(), p2.getBlockZ());
        do {
            distance /= 2;
        } while (distance > CHUNK * DISTANCE);
        v.setZ((NumbersUtils.getLower(p1.getBlockZ(), p2.getBlockZ())) - distance);

        return v.toLocation(w);
    }

    public Location randomLocation(World world) {
        return Randomizer.randBetweenVector(p1, p2).toLocation(world);
    }

    /*
    public boolean isEmpty(World world) {
        int x, z;
        int minx, minz;

        if (p1.getX() >= p2.getX()) {
            x = p1.getBlockX();
            minx = p2.getBlockX();
        } else {
            x = p2.getBlockX();
            minx = p1.getBlockX();
        }
        if (p1.getZ() >= p2.getZ()) {
            z = p1.getBlockZ();
            minz = p2.getBlockZ();
        } else {
            z = p2.getBlockZ();
            minz = p1.getBlockZ();
        }

        for (int i = minx; i <= x; i++) {
            for (int j = 0; j <= 65; j++) {
                for (int k = minz; k <= z; k++) {
                    Location l = new Location(world, (i < 0) ? (i - 1) : i, j, (k < 0) ? (k - 1) : k);
                    if (l.getBlock().isLiquid()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
     */
    public String toClickString(Langs l) {
        String title = GameInfo.ZONE_GO_TITLE.getText(l);
        String p1 = "*** P1 (" + this.p1.getBlockX() + ", " + this.getP1().getBlockY()
                + ", " + this.getP1().getBlockZ() + ")";
        String p2 = "*** P2 (" + this.getP2().getBlockX() + ", " + this.getP2().getBlockY()
                + ", " + this.getP2().getBlockZ() + ")";
        String info = GameInfo.ZONE_GO_CLICK.getText(l);

        /*int longer = StringsUtils.getHigherLength(title, p1, p2, info);
        String[] values = StringsUtils.copyLength(
                longer, ' ', StringsUtils.POSITION.CENTER, title, p1, p2, info);

        return values[0] + "\n" + values[1] + "\n" + values[2] + "\n" + values[3];*/
        return title + "\n" + p1 + "\n" + p2 + "\n" + info;
    }
}
