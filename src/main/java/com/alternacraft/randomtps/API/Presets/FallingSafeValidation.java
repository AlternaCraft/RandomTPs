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
package com.alternacraft.randomtps.API.Presets;

import com.alternacraft.randomtps.API.ZoneValidation;
import com.alternacraft.randomtps.Utils.ZoneUtils;
import static com.alternacraft.randomtps.Utils.ZoneUtils.MAX_DIFF;
import static com.alternacraft.randomtps.Utils.ZoneUtils.MAX_HEIGHT;
import com.alternacraft.randomtps.Zone.Zone;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 *
 * @author AlternaCraft
 */
public class FallingSafeValidation implements ZoneValidation {

    @Override
    public boolean isValid(Location l, Chunk[] affected_chunks) {
        for (Chunk chunk : affected_chunks) {
            // Gets block under the highest because it's air
            int highest = ZoneUtils.getHighestMiddleBlockYAt(chunk) - 1;
            if (highest <= MAX_HEIGHT) {                
                Block[] blocks = ZoneUtils.getChunkBlocks(chunk, highest, highest + 1);
                for (Block b : blocks) {
                    if (b.isEmpty()) {
                        int selected = chunk.getWorld().getHighestBlockYAt(b.getLocation()) - 1;
                        if (Math.abs(selected - highest) > MAX_DIFF) {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isValidInsideSubzone(Location l, Chunk[] affected_chunks, Zone zone) {
        for (Chunk chunk : affected_chunks) {
            int highest = ZoneUtils.getHighestMiddleBlockYAt(chunk) - 1;
            Block highestBlock = ZoneUtils.getHighestMiddleBlockAt(chunk);
            if (highest <= MAX_HEIGHT) {
                Block[] blocks = ZoneUtils.getChunkBlocksInsideZone(chunk, 
                        highest, highest + 1, zone);
                for (Block b : blocks) {
                    if (b.isEmpty()) {
                        int selected = chunk.getWorld().getHighestBlockYAt(b.getLocation()) - 1;
                        if (Math.abs(selected - highest) > MAX_DIFF) {
                            return false;
                        }
                    }
                }
            } else if (ZoneUtils.isInsideOfSubzone(
                    highestBlock.getLocation().toVector(), zone)) {
                return false;
            }
        }
        return true;
    }
}
