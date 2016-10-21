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
package com.alternacraft.randomtps.Listeners;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.Events.DefineZoneEvent;
import com.alternacraft.randomtps.Langs.DefineInfo;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Utils.PreLocalization;
import com.alternacraft.randomtps.Utils.Zone;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class HandleZoneCreation implements Listener {

    public final static Map<UUID, PreLocalization> DEFINERS = new HashMap();
    public final static Map<UUID, Location> LOCATIONS = new HashMap();

    @EventHandler
    public void saveLocation(PlayerInteractEvent ev) {
        UUID uuid = ev.getPlayer().getUniqueId();
        if (DEFINERS.containsKey(uuid)) {
            if (ev.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Location l = ev.getClickedBlock().getLocation();
                if (l.getBlockX() < 0) { // Negative values
                    l.setX(l.getBlockX() + 1);
                }
                if (l.getBlockZ() < 0) { // Negative values
                    l.setZ(l.getBlockZ() + 1);
                }

                if (LOCATIONS.containsKey(uuid)) {
                    int x = LOCATIONS.get(uuid).getBlockX();
                    int y = LOCATIONS.get(uuid).getBlockY();
                    int z = LOCATIONS.get(uuid).getBlockZ();

                    if (l.getBlockX() == x && l.getBlockY() == y && l.getBlockZ() == z) {
                        return;
                    }
                }

                LOCATIONS.put(uuid, l);
                MessageManager.sendPlayer(ev.getPlayer(),
                        DefineInfo.SELECTION.getText(Localizer.getLocale(ev.getPlayer()))
                        .replace("%BLOCK_X%", String.valueOf(l.getBlockX()))
                        .replace("%BLOCK_Y%", String.valueOf(l.getBlockY()))
                        .replace("%BLOCK_Z%", String.valueOf(l.getBlockZ()))
                );
            }
        }
    }

    @EventHandler
    public void define(DefineZoneEvent ev) {
        UUID uuid = ev.offplayer().getUniqueId();
        if (ev.offplayer().isOnline()) {
            Langs lang = Localizer.getLocale(ev.player());

            if (DEFINERS.containsKey(uuid)) {
                MessageManager.sendPlayer(ev.player(),
                        DefineInfo.ALREADY_DEFINING.getText(lang));
            } else {
                String cancel = Manager.INSTANCE.loader().getCancel();
                String selection = Manager.INSTANCE.loader().getSelection();
                
                MessageManager.sendPlayer(ev.player(), 
                        DefineInfo.HOW_TO_CANCEL.getText(lang)
                        .replace("%CANCEL%", cancel));
                MessageManager.sendPlayer(ev.player(),
                        DefineInfo.POINT_1.getText(lang)
                        .replace("%SELECT%", selection));
                
                DEFINERS.put(uuid, new PreLocalization(ev.getZoneName()));
            }
        }
    }

    @EventHandler
    public void cancel(AsyncPlayerChatEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Player player = ev.getPlayer();
        UUID uuid = player.getUniqueId();
        String answ = ev.getMessage();

        if (DEFINERS.containsKey(uuid) && answ.contains(Manager.INSTANCE.loader().getCancel())) {
            Langs lang = Localizer.getLocale(player);
            String zone = HandleZoneCreation.DEFINERS.get(uuid).getZoneName();
            
            // Bye bye
            HandleBuild.DISABLED.remove(zone);
            MessageManager.sendPlayer(player,
                    DefineInfo.STOP_DEFINING.getText(lang));    
            
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void point(AsyncPlayerChatEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Langs lang = Localizer.getLocale(ev.getPlayer());
        UUID uuid = ev.getPlayer().getUniqueId();

        if (DEFINERS.containsKey(uuid)) {
            if (ev.getMessage().contains(Manager.INSTANCE.loader().getSelection())) {
                ev.setCancelled(true);

                PreLocalization preloc = DEFINERS.get(uuid);

                if (!LOCATIONS.containsKey(uuid)) {
                    MessageManager.sendPlayer(ev.getPlayer(),
                            DefineInfo.NO_SELECTED.getText(lang));
                    return;
                }

                Vector v = LOCATIONS.get(uuid).toVector();

                if (!preloc.coord1Saved()) {
                    if (preloc.isSubzone()) {
                        preloc.setCS1(v);
                    } else {
                        preloc.setC1(v);
                    }

                    preloc.setCoord1(true);

                    String selection = Manager.INSTANCE.loader().getSelection();
                    MessageManager.sendPlayer(ev.getPlayer(),
                            DefineInfo.POINT_2.getText(lang)
                            .replace("%SELECT%", selection));
                } else if (!preloc.coord2Saved()) {
                    if (preloc.isSubzone()) {
                        preloc.addSubzone(LOCATIONS.get(uuid).getWorld().getName(),
                                new Zone(preloc.getCS1(), v));
                    } else {
                        preloc.setC2(v);
                        preloc.setOrigin(ev.getPlayer().getLocation().getWorld().getName());
                    }

                    preloc.setCoord2(true);
                    preloc.askWorld(false);
                    preloc.askSubzone(true);

                    MessageManager.sendPlayer(ev.getPlayer(),
                            DefineInfo.SUBZONE.getText(lang));
                }

                LOCATIONS.remove(uuid);
            }
        }
    }

    @EventHandler
    public void subzone(AsyncPlayerChatEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Langs lang = Localizer.getLocale(ev.getPlayer());
        UUID uuid = ev.getPlayer().getUniqueId();

        if (DEFINERS.containsKey(uuid)) {
            PreLocalization preloc = DEFINERS.get(uuid);

            if (preloc.coord2Saved() && preloc.askSubzone()) {
                ev.setCancelled(true);

                String answ = ev.getMessage();

                if (answ.contains("y")) {
                    preloc.resetValues();
                    preloc.setSubzone(true);

                    preloc.askWorld(false);
                    preloc.askSubzone(false);

                    String selection = Manager.INSTANCE.loader().getSelection();
                    MessageManager.sendPlayer(ev.getPlayer(),
                            DefineInfo.POINT_1.getText(lang)
                            .replace("%SELECT%", selection));
                } else if (answ.contains("n")) {
                    if (preloc.isSubzone()) {
                        save(preloc, ev.getPlayer());
                    } else {
                        preloc.askWorld(true);
                        preloc.askSubzone(false);

                        MessageManager.sendPlayer(ev.getPlayer(),
                                DefineInfo.WORLDS.getText(lang));
                    }
                } else {
                    MessageManager.sendPlayer(ev.getPlayer(),
                            DefineInfo.SUBZONE.getText(lang));
                }
            }
        }
    }

    @EventHandler
    public void world(AsyncPlayerChatEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Langs lang = Localizer.getLocale(ev.getPlayer());
        UUID uuid = ev.getPlayer().getUniqueId();

        if (DEFINERS.containsKey(uuid)) {
            PreLocalization preloc = DEFINERS.get(uuid);

            if (preloc.coord2Saved() && preloc.askWorld()) {
                ev.setCancelled(true);

                String[] world = ev.getMessage().split(" ");
                for (String w : world) {
                    if (Bukkit.getWorld(w) != null) {
                        preloc.addDestination(w);
                    } else {
                        MessageManager.sendPlayer(ev.getPlayer(),
                                DefineInfo.INVALID_WORLD.getText(lang)
                                .replace("%WORLD%", w));
                        return;
                    }
                }

                save(preloc, ev.getPlayer());
            }
        }
    }

    private void save(PreLocalization preloc, Player pl) {
        Langs lang = Localizer.getLocale(pl);

        Manager.INSTANCE.getZonesDB().saveLocalization(preloc.toLocalization());
        Manager.INSTANCE.getZonesDB().enableLocalization(preloc.getZoneName());        
        Manager.INSTANCE.addLocalization(preloc.getZoneName());

        DEFINERS.remove(pl.getUniqueId());
        // If redefine
        HandleBuild.DISABLED.remove(preloc.getZoneName());

        MessageManager.sendPlayer(pl,
                DefineInfo.ZONE_CREATED.getText(lang));
    }
}
