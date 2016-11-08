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
import com.alternacraft.randomtps.API.GMEffect;
import java.util.Map;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author AlternaCraft
 */
public class PotionEffects implements GMEffect {

    private Map<String, Integer> potions;

    @Override
    public boolean start(Player pl, int time, Object values) {
        potions = (Map<String, Integer>) values;

        if (values == null || pl == null) {
            return false;
        }

        for (Map.Entry<String, Integer> entry : potions.entrySet()) {
            String name = entry.getKey();
            Integer effect = entry.getValue();
            pl.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    PotionEffectType.getByName(name), TPS * time, effect));
        }

        return true;
    }

    @Override
    public void stop(OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline()) {
            for (Map.Entry<String, Integer> entry : potions.entrySet()) {
                String name = entry.getKey();
                ((Player) offlinePlayer).removePotionEffect(PotionEffectType.getByName(name));
            }
        }
    }

}
