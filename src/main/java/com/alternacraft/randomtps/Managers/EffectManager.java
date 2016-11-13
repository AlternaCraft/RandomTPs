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
package com.alternacraft.randomtps.Managers;

import com.alternacraft.randomtps.API.GMEffect;
import com.alternacraft.randomtps.Utils.PotionEffects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class EffectManager {

    public enum TYPE {
        POTIONS
    }

    public static final Map<UUID, List<GMEffect>> EFFECTS = new HashMap();

    public static boolean addEffect(TYPE type, Player pl, int time, Object v) {
        GMEffect b;

        switch (type) {
            case POTIONS:
                b = new PotionEffects();
                break;
            default:
                return false;
        }

        if (b.start(pl, time, v)) {
            if (!EFFECTS.containsKey(pl.getUniqueId())) {
                EFFECTS.put(pl.getUniqueId(), new ArrayList<GMEffect>());
            }
            EFFECTS.get(pl.getUniqueId()).add(b);
            return true;
        }

        return false;
    }
    
    public static void removeEffect(OfflinePlayer player, TYPE type) {
        List<GMEffect> effects = EFFECTS.get(player.getUniqueId());
        for (GMEffect effect : effects) {
            switch(type) {
                case POTIONS:
                    if (effect instanceof PotionEffects) {
                        EFFECTS.get(player.getUniqueId()).remove(effect);
                        return;
                    }
            }
            effect.stop(player);
        }
    }
    
    public static void removeEffects(OfflinePlayer player) {
        List<GMEffect> effects = EFFECTS.get(player.getUniqueId());
        for (GMEffect effect : effects) {
            effect.stop(player);
        }
        EFFECTS.remove(player.getUniqueId());
    }
}
