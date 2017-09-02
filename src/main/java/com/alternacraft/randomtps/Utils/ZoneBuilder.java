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
package com.alternacraft.randomtps.Utils;

import com.alternacraft.aclib.MessageManager;
import static com.alternacraft.aclib.PluginBase.TPS;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.API.Events.BuildCompletedEvent;
import com.alternacraft.randomtps.API.Events.BuildStartedEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Main.Manager;
import static com.alternacraft.randomtps.Main.RandomTPs.METER;
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

/**
 * Zone builder.
 *
 * @author AlternaCraft
 */
public class ZoneBuilder {

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

        METER.start("Loading zone");

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

        METER.recordTime("Loading zone");
    }

    public void show(CommandSender cs, final Material m) {
        Lang l = Localizer.getLocale(cs);

        run = new BlocksReplacer(new ArrayList() {
            {
                add(m);
            }
        }, false, cs, this);
        Bukkit.getScheduler().runTaskLater(Manager.BASE.plugin(), run, TPS);

        MessageManager.sendCommandSender(cs, GameInfo.ZONE_START_BUILD.getText(l)
                .replace("%NAME%", zone));

        Bukkit.getServer().getPluginManager().callEvent(new BuildStartedEvent(
                cs, this));
    }

    public void hide(CommandSender cs) {
        Lang l = Localizer.getLocale(cs);

        run = new BlocksReplacer(true, cs, this);
        Bukkit.getScheduler().runTaskLater(Manager.BASE.plugin(), run, TPS);

        MessageManager.sendCommandSender(cs, GameInfo.ZONE_START_ROLLBACK.getText(l)
                .replace("%NAME%", zone));

        Bukkit.getServer().getPluginManager().callEvent(new BuildStartedEvent(
                cs, this));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Build runnable">
    public class BlocksReplacer implements Runnable {

        private boolean instantly = Manager.INSTANCE.loader().doInstantly();

        private final Collection mats;
        private final boolean rollback;

        private final CommandSender cs;
        private final ZoneBuilder builder;

        private long starttime;
        private long endtime;
        
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

                // Get the material and change
                final Material mat = (n >= mats.size()) ? (Material) materials[0]
                        : (Material) materials[n];                
                ((Location) locs[n]).getBlock().setType(mat);
                
                if (!instantly) {
                    Bukkit.getScheduler().runTaskLater(Manager.BASE.plugin(), new Runnable() {
                        @Override
                        public void run() {
                            recursiveTask(n + 1, total);
                        }
                    }, 1L);                    
                } else {
                    recursiveTask(n + 1, total);
                }               
            } else if (!stop) {
                this.finished = true;
                this.endtime = System.currentTimeMillis();

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
        
        public void forceInstant() {
            this.instantly = true;
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
        return run.endtime - run.starttime;
    }

    public void go() {
        this.run.start();
    }

    public void stop() {
        this.run.stop();
    }
    
    public Runnable getRunnable() {
        return this.run;
    }
}
