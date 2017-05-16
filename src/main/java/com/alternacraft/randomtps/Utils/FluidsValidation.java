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

import com.alternacraft.randomtps.API.ZoneValidation;
import com.alternacraft.randomtps.Zone.Zone;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 *
 * @author AlternaCraft
 */
public class FluidsValidation implements ZoneValidation {

    @Override
    public boolean isValid(Location l, Chunk[] affected_chunks) {
        for (Chunk chunk : affected_chunks) {
            int highest = ZoneUtils.getHighestMiddleBlockYAt(chunk);
            Block[] blocks = ZoneUtils.getChunkBlocks(chunk, highest, highest+1);
            for (Block b : blocks) {
                if (b.isEmpty()) {
                    // highest block returns the first air block.
                    Block highestBlock = chunk.getWorld().getHighestBlockAt(b.getLocation());
                    Block underHighest = highestBlock.getWorld().getBlockAt(
                            highestBlock.getX(), highestBlock.getY()-1, highestBlock.getZ());
                    if (underHighest.isLiquid()) return false;
                } else {
                    if (b.isLiquid()) return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isValidInsideSubzone(Location l, Chunk[] affected_chunks, Zone zone) {
        for (Chunk chunk : affected_chunks) {
            int highest = ZoneUtils.getHighestMiddleBlockYAt(chunk);
            Block[] blocks = ZoneUtils.getChunkBlocksInsideZone(chunk, 
                    highest, highest+1, zone);
            for (Block b : blocks) {
                if (b.isEmpty()) {
                    // highest block returns the first air block.
                    Block highestBlock = chunk.getWorld().getHighestBlockAt(b.getLocation());
                    Block underHighest = highestBlock.getWorld().getBlockAt(
                            highestBlock.getX(), highestBlock.getY()-1, highestBlock.getZ());
                    if (underHighest.isLiquid()) return false;
                } else {
                    if (b.isLiquid()) return false;
                }
            }
        }
        return true;
    }
}
