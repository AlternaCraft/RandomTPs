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
import static com.alternacraft.aclib.PluginBase.TPS;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.Managers.BroadcastManager;
import com.alternacraft.randomtps.Managers.EffectManager;
import com.alternacraft.randomtps.API.Events.PlayerBecomesUselessEvent;
import com.alternacraft.randomtps.API.Events.PlayerGodModeEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Localizations.Localization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class HandleGods implements Listener {

    // God players
    private final List<UUID> gods = new ArrayList();
    // Cancel delayed task
    private final Map<UUID, Integer> overtime = new HashMap();

    //<editor-fold defaultstate="collapsed" desc="BAD EXIT">
    @EventHandler
    public void onPlayerFcking(PlayerQuitEvent e) {
        if (gods.contains(e.getPlayer().getUniqueId())) {
            clearGod(e.getPlayer());
        }
    }

    @EventHandler
    public void onAdminFcking(PlayerKickEvent e) {
        if (gods.contains(e.getPlayer().getUniqueId())) {
            clearGod(e.getPlayer());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NEW GOD">
    @EventHandler
    public void onPlayerBecomesGod(PlayerGodModeEvent ev) {
        if (!ev.player().isOnline()) {
            // Lost the opportunity :)
            return;
        }

        Player pl = (Player) ev.player();
        Langs lang = Localizer.getLocale(pl);

        // Auto fix if something fails
        if (gods.contains(pl.getUniqueId())) {
            clearGod(pl);
        }

        Localization l = ev.getLocalization();
        int time = l.getTime();

        // God
        pl.setNoDamageTicks(TPS * time);
        MessageManager.sendPlayer(pl, GameInfo.PLAYER_INVULNERABILITY.
                getText(lang).replaceAll("%TIME%", String.valueOf(time)));

        // Effects
        if (!l.getPotionEffects().isEmpty()) {
            EffectManager.addEffect(EffectManager.TYPE.POTIONS, pl, time,
                    l.getPotionEffects());
        }

        // Ending
        if (l.broadcastAsEXP()) {
            BroadcastManager.callBroadcast(BroadcastManager.TYPE.AS_EXP, pl, time);
        }

        overtime(pl, time);

        gods.add(pl.getUniqueId());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="END TASKS">
    private void overtime(final OfflinePlayer pl, final int time) {
        overtime.put(pl.getUniqueId(), Bukkit.getServer().getScheduler().runTaskLater(Manager.BASE.plugin(), new Runnable() {
            @Override
            public void run() {
                clearGod(pl);
                if (pl.isOnline()) {
                    MessageManager.sendPlayer((Player) pl, GameInfo.PLAYER_RECOVERS_PVP
                            .getText(Localizer.getLocale((Player) pl)));
                }
                Bukkit.getServer().getPluginManager().callEvent(new PlayerBecomesUselessEvent(pl));
            }
        }, TPS * time).getTaskId());
    }

    private void clearGod(OfflinePlayer player) {
        // Broadcast
        if (BroadcastManager.BROADCASTERS.containsKey(player.getUniqueId())) {
            BroadcastManager.stopBroadcasts(player);
        }

        // Effects
        if (EffectManager.EFFECTS.containsKey(player.getUniqueId())) {
            EffectManager.removeEffects(player);
        }

        // Players
        if (gods.contains(player.getUniqueId())) {
            Bukkit.getServer().getScheduler().cancelTask(overtime.get(player.getUniqueId()));
            overtime.remove(player.getUniqueId());

            if (player.isOnline()) {
                Player pl = (Player) player;
                UUID uuid = pl.getUniqueId();

                // God
                pl.setNoDamageTicks(0);
                pl.removePotionEffect(PotionEffectType.SPEED);
                gods.remove(uuid);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="HITS">
    @EventHandler
    public void onHit(EntityDamageEvent e) {
        if (e instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
            Entity damager = ev.getDamager();

            if (damager instanceof Player) {
                Player no_pvp = (Player) damager;

                for (UUID uuid : gods) {
                    Player player = Bukkit.getPlayer(uuid);
                    Langs lang = Localizer.getLocale(player);

                    if (no_pvp.getUniqueId().equals(player.getUniqueId())) {
                        MessageManager.sendPlayer(no_pvp, GameInfo.PLAYER_LOSES_PVP.getText(lang));
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }
    //</editor-fold>
}
