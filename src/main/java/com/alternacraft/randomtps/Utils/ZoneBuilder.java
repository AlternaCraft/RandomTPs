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

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.API.Events.BuildCompletedEvent;
import com.alternacraft.randomtps.API.Events.BuildStartedEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Main.Manager;
import static com.alternacraft.randomtps.Main.RandomTPs.PERFORMANCE;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

public class ZoneBuilder {

    private final static long TPS = 20;

    private BlocksReplacer run = null;

    private final String zone;
    private final Vector p1, p2;
    private final Map<Location, Material> blocks = new LinkedHashMap();

    public ZoneBuilder(String zone, Vector p1, Vector p2) {
        this.zone = zone;
        this.p1 = p1;
        this.p2 = p2;
    }

    //<editor-fold defaultstate="collapsed" desc="Logic">
    public void loadStructure(World world) {
        int x, y, z;
        int minx, miny, minz;

        if (p1.getX() >= p2.getX()) {
            x = p1.getBlockX();
            minx = p2.getBlockX();
        } else {
            x = p2.getBlockX();
            minx = p1.getBlockX();
        }
        if (p1.getY() >= p2.getY()) {
            y = p1.getBlockY();
            miny = p2.getBlockY();
        } else {
            y = p2.getBlockY();
            miny = p1.getBlockY();
        }
        if (p1.getZ() >= p2.getZ()) {
            z = p1.getBlockZ();
            minz = p2.getBlockZ();
        } else {
            z = p2.getBlockZ();
            minz = p1.getBlockZ();
        }
        
        PERFORMANCE.start("Loading zone");
        
        for (int i = minx; i <= x; i++) {
            for (int j = miny; j <= y; j++) {
                for (int k = minz; k <= z; k++) {
                    if ( // Vertices
                            (i == minx || i == x) && (j == miny || j == y) && (k == minz || k == z)
                            || // Rows (side 1)
                            (i == minx || i == x) && (j == miny || j == y)
                            || // Rows (side 2)
                            (k == minz || k == z) && (j == miny || j == y)
                            || // Corners (Columns)
                            (j > miny && j < y) && (i == minx || i == x) && (k == minz || k == z)) {
                        // Fix for negative coords
                        Location l = new Location(world, (i < 0) ? (i - 1) : i, j, (k < 0) ? (k - 1) : k);
                        blocks.put(l, l.getBlock().getType());
                    }
                }
            }
        }
        
        PERFORMANCE.recordValue("Loading zone");
    }

    public void show(CommandSender cs, final Material m) {
        Langs l = Localizer.getLocale(cs);

        run = new BlocksReplacer(new ArrayList() {{ add(m); }}, false, cs, this);
        Bukkit.getScheduler().runTaskLater(Manager.BASE.plugin(), run, TPS);

        MessageManager.sendCommandSender(cs, GameInfo.ZONE_START_BUILD.getText(l)
                .replace("%NAME%", zone));

        Bukkit.getServer().getPluginManager().callEvent(new BuildStartedEvent(
                cs, this));
    }

    public void hide(CommandSender cs) {
        Langs l = Localizer.getLocale(cs);

        run = new BlocksReplacer(true, cs, this);
        Bukkit.getScheduler().runTaskLater(Manager.BASE.plugin(), run, TPS);

        MessageManager.sendCommandSender(cs, GameInfo.ZONE_START_ROLLBACK.getText(l)
                .replace("%NAME%", zone));

        Bukkit.getServer().getPluginManager().callEvent(new BuildStartedEvent(
                cs, this));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Build runnable">
    class BlocksReplacer implements Runnable {

        private final short TIMEPERBLOCK = (Manager.INSTANCE.loader().doInstantly()) ? (short) 0 : (short) 1;

        private final Collection mats;
        private final boolean rollback;

        private final CommandSender cs;
        private final ZoneBuilder builder;

        private long starttime;
        private boolean stop;
        private int lastBlock;
        private boolean finished;

        public BlocksReplacer(Collection mats, boolean rollback,
                CommandSender cs, ZoneBuilder builder) {
            this.finished = false;
            this.stop = false;
            this.starttime = System.currentTimeMillis(); // Boot time

            // Data
            this.mats = mats;
            this.rollback = rollback;
            this.cs = cs;
            this.builder = builder;
            this.lastBlock = 0;
        }
        
        public BlocksReplacer(boolean rollback, CommandSender cs, ZoneBuilder builder) {
            this(blocks.values(), rollback, cs, builder);
        }

        @Override
        public void run() {
            recursiveTask(this.lastBlock, blocks.size());
        }

        private void recursiveTask(final int n, final int total) {
            if (n < total && !stop) {
                final Object[] locs = blocks.keySet().toArray();
                final Object[] materials = mats.toArray();

                // Get the material
                final Material mat = (n >= mats.size()) ? (Material) materials[0]
                        : (Material) materials[n];
                Bukkit.getScheduler().runTaskLater(Manager.BASE.plugin(), new Runnable() {
                    @Override
                    public void run() {
                        ((Location) locs[n]).getBlock().setType(mat);
                        recursiveTask(n + 1, total);
                    }
                }, TIMEPERBLOCK);
            } else if (!stop) {
                this.starttime = System.currentTimeMillis() - this.starttime;
                this.finished = true;

                Bukkit.getServer().getPluginManager().callEvent(new BuildCompletedEvent(
                        cs, builder));
            } else {
                this.lastBlock = n;
            }
        }
        
        private void start() {
            this.stop = false;
            this.run();
        }
        
        private void stop() {
            this.stop = true;
        }
    }
    //</editor-fold>

    public boolean isFinished() {
        return run.finished;
    }

    public boolean isRollback() {
        return run.rollback;
    }

    public String zoneName() {
        return this.zone;
    }

    public long elapsedTime() {
        return run.starttime;
    }

    public void go() {
        this.run.start();
    }
    
    public void stop() {
        this.run.stop();
    }
}
