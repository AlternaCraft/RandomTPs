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
import com.alternacraft.randomtps.Events.PlayerBecomesUselessEvent;
import com.alternacraft.randomtps.Events.PlayerGodModeEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Utils.Localization;
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
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HandleGods implements Listener {

    // God players
    private final List<UUID> gods = new ArrayList();
    // Cancel delayed task
    private final Map<UUID, Integer> overtime = new HashMap();

    // Old experience
    private final Map<UUID, Integer> experience = new HashMap();
    // Cancel repeat task
    private final Map<UUID, Integer> counter = new HashMap();

    //<editor-fold defaultstate="collapsed" desc="COUNTER FIX">
    @EventHandler
    public void onPlayerGetsExperience(PlayerExpChangeEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (experience.containsKey(uuid)) {
            e.setAmount(0);
        }
    }
    //</editor-fold>

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
            // Lost your opportunity :)
            return;
        }

        Player pl = (Player) ev.player();

        // Auto fix if something fails
        if (gods.contains(pl.getUniqueId())) {
            clearGod(pl);
        }

        Localization l = ev.getLocalization();
        int time = l.getTime();

        // God
        if (l.isInmortal()) {
            pl.setNoDamageTicks(TPS * time);
        }
        
        // Effects
        Map<String, Integer> potions = l.getPotionEffects();
        for (Map.Entry<String, Integer> entry : potions.entrySet()) {
            String name = entry.getKey();
            Integer effect = entry.getValue();
            
            pl.addPotionEffect(new PotionEffect(PotionEffectType.getByName(name),
                    TPS * time, effect));
        }

        gods.add(pl.getUniqueId());

        Langs lang = Localizer.getLocale(pl);
        MessageManager.sendPlayer(pl, GameInfo.PLAYER_INVULNERABILITY.
                getText(lang).replaceAll("%TIME%", String.valueOf(time)));

        // Ending
        if (l.broadcastAsEXP()) {
            counter(pl, time);
        }
        
        overtime(pl, time);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="END TASKS">
    //<editor-fold defaultstate="collapsed" desc="SECUNDARY">
    private void counter(final Player pl, int time) {
        experience.put(pl.getUniqueId(), pl.getLevel());
        pl.setLevel(time);

        counter.put(pl.getUniqueId(), Bukkit.getServer().getScheduler().runTaskTimer(Manager.BASE.plugin(), new Runnable() {
            @Override
            public void run() {
                pl.setLevel(pl.getLevel() - 1);
            }
        }, TPS, TPS).getTaskId());
    }

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

    //</editor-fold>
    private void clearGod(OfflinePlayer player) {
        // Counter
        if (counter.containsKey(player.getUniqueId())) {
            Bukkit.getServer().getScheduler().cancelTask(counter.get(player.getUniqueId()));
            counter.remove(player.getUniqueId());

            if (player.isOnline()) {
                Player pl = (Player) player;
                UUID uuid = pl.getUniqueId();

                // Experience        
                int level = experience.get(uuid);
                experience.remove(uuid);
                pl.setLevel(level);
            }
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
                        no_pvp.sendMessage(PLUGIN + GameInfo.PLAYER_LOSES_PVP.getText(lang));
                        e.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }
    //</editor-fold>
}
