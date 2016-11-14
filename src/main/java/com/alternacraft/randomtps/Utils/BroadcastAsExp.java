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

import static com.alternacraft.aclib.PluginBase.TPS;
import com.alternacraft.randomtps.API.GMBroadcast;
import com.alternacraft.randomtps.Main.Manager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

/**
 * Custom broadcast.
 * 
 * @author AlternaCraft
 */
public class BroadcastAsExp implements Listener, GMBroadcast {

    //<editor-fold defaultstate="collapsed" desc="COUNTER FIX">
    @EventHandler
    public void onPlayerGetsExperience(PlayerExpChangeEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (experience.containsKey(uuid)) {
            e.setAmount(0);
        }
    }
    //</editor-fold>

    // Old experience
    private final Map<UUID, Integer> experience = new HashMap();
    // Cancel repeat task
    private final Map<UUID, Integer> counter = new HashMap();

    @Override
    public int start(final Player pl, int time) {
        experience.put(pl.getUniqueId(), pl.getLevel());
        pl.setLevel(time);

        int task = Bukkit.getServer().getScheduler().runTaskTimer(Manager.BASE.plugin(), new Runnable() {
            @Override
            public void run() {
                pl.setLevel(pl.getLevel() - 1);
            }
        }, TPS, TPS).getTaskId();

        counter.put(pl.getUniqueId(), task);

        return task;
    }

    @Override
    public void stop(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();

        Bukkit.getServer().getScheduler().cancelTask(this.counter.get(uuid));
        counter.remove(uuid);

        if (player.isOnline()) {
            ((Player) player).setLevel(experience.get(uuid));
            experience.remove(uuid);
        }
    }
}
