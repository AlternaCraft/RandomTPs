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
package com.alternacraft.randomtps.Listeners;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.exceptions.PluginException;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.API.Events.DefineZoneEvent;
import com.alternacraft.randomtps.Langs.DefineInfo;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Zone.PreDefinedZone;
import com.alternacraft.randomtps.Zone.Zone;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

/**
 * Handle zone creation.
 * 
 * @author AlternaCraft
 */
public class HandleZoneCreation implements Listener {

    public final static Map<UUID, PreDefinedZone> DEFINERS = new HashMap();
    public final static Map<UUID, Location> LOCATIONS = new HashMap();

    @EventHandler
    public void saveLocation(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        UUID uuid = player.getUniqueId();
        
        if (DEFINERS.containsKey(uuid)) {
            if (ev.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (player.getInventory().getItemInMainHand() != null
                        && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    ev.setCancelled(true); // It is not the time for building
                }
                
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
                MessageManager.sendCommandSender(player,
                        DefineInfo.SELECTED.getText(Localizer.getLocale(player))
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
            Lang lang = Localizer.getLocale(ev.player());

            if (DEFINERS.containsKey(uuid)) {
                MessageManager.sendCommandSender(ev.player(),
                        DefineInfo.ALREADY_DEFINING.getText(lang));
            } else {
                String cancel = Manager.INSTANCE.loader().getCancel();
                String selection = Manager.INSTANCE.loader().getSelection();

                MessageManager.sendCommandSender(ev.player(),
                        DefineInfo.HOW_TO_CANCEL.getText(lang)
                                .replace("%CANCEL%", cancel));
                MessageManager.sendCommandSender(ev.player(),
                        DefineInfo.COORD_1.getText(lang)
                                .replace("%SELECT%", selection));

                DEFINERS.put(uuid, new PreDefinedZone(ev.getZoneName(), 
                        ev.isRedefine()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancel(AsyncPlayerChatEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Player player = ev.getPlayer();
        UUID uuid = player.getUniqueId();
        String answ = ev.getMessage();

        if (DEFINERS.containsKey(uuid) && answ.contains(Manager.INSTANCE.loader().getCancel())) {
            Lang lang = Localizer.getLocale(player);
            String zone = HandleZoneCreation.DEFINERS.get(uuid).getZoneName();

            // Bye bye
            HandleBuild.DISABLED.remove(zone);
            DEFINERS.remove(uuid);
            
            MessageManager.sendCommandSender(player,
                    DefineInfo.STOP_DEFINING.getText(lang));

            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void point(AsyncPlayerChatEvent ev) {
        if (ev.isCancelled()) {
            return;
        }

        Lang lang = Localizer.getLocale(ev.getPlayer());
        UUID uuid = ev.getPlayer().getUniqueId();

        if (DEFINERS.containsKey(uuid)) {
            if (ev.getMessage().contains(Manager.INSTANCE.loader().getSelection())) {
                ev.setCancelled(true);

                PreDefinedZone preloc = DEFINERS.get(uuid);

                if (!LOCATIONS.containsKey(uuid)) {
                    MessageManager.sendCommandSender(ev.getPlayer(),
                            DefineInfo.UNSELECTED.getText(lang));
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
                    MessageManager.sendCommandSender(ev.getPlayer(),
                            DefineInfo.COORD_2.getText(lang)
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

                    MessageManager.sendCommandSender(ev.getPlayer(),
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

        Lang lang = Localizer.getLocale(ev.getPlayer());
        UUID uuid = ev.getPlayer().getUniqueId();

        if (DEFINERS.containsKey(uuid)) {
            PreDefinedZone preloc = DEFINERS.get(uuid);

            if (preloc.coord2Saved() && preloc.askSubzone()) {
                ev.setCancelled(true);

                String answ = ev.getMessage();

                if (answ.contains("y")) {
                    preloc.resetValues();
                    preloc.setSubzone(true);

                    preloc.askWorld(false);
                    preloc.askSubzone(false);

                    String selection = Manager.INSTANCE.loader().getSelection();
                    MessageManager.sendCommandSender(ev.getPlayer(),
                            DefineInfo.COORD_1.getText(lang)
                                    .replace("%SELECT%", selection));
                } else if (answ.contains("n")) {
                    if (preloc.isSubzone()) {
                        save(preloc, ev.getPlayer());
                    } else {
                        preloc.askWorld(true);
                        preloc.askSubzone(false);

                        MessageManager.sendCommandSender(ev.getPlayer(),
                                DefineInfo.WORLDS.getText(lang));
                    }
                } else {
                    MessageManager.sendCommandSender(ev.getPlayer(),
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

        Lang lang = Localizer.getLocale(ev.getPlayer());
        UUID uuid = ev.getPlayer().getUniqueId();

        if (DEFINERS.containsKey(uuid)) {
            PreDefinedZone preloc = DEFINERS.get(uuid);

            if (preloc.coord2Saved() && preloc.askWorld()) {
                ev.setCancelled(true);

                String[] world = ev.getMessage().split(" ");
                for (String w : world) {
                    if (Bukkit.getWorld(w) != null) {
                        preloc.addDestination(w);
                    } else {
                        MessageManager.sendCommandSender(ev.getPlayer(),
                                DefineInfo.INVALID_WORLD.getText(lang)
                                        .replace("%WORLD%", w));
                        return;
                    }
                }

                save(preloc, ev.getPlayer());
            }
        }
    }

    private void save(PreDefinedZone preloc, Player pl) {
        try {
            Lang lang = Localizer.getLocale(pl);
            
            Manager.INSTANCE.getZonesDB().saveDefinedZone(preloc.toDefinedZone());
            Manager.INSTANCE.getZonesDB().enableDefinedZone(preloc.getZoneName());
            
            if (preloc.redefine()) {
                Manager.INSTANCE.removeDefinedZone(preloc.getZoneName());
                HandleBuild.DISABLED.remove(preloc.getZoneName());
            }
            Manager.INSTANCE.addZone(preloc.getZoneName());
            
            DEFINERS.remove(pl.getUniqueId());
            
            MessageManager.sendCommandSender(pl,
                    DefineInfo.ZONE_CREATED.getText(lang));
        } catch (PluginException ex) {
            MessageManager.logArrayError(ex.getCustomStacktrace());
        }
    }
}
