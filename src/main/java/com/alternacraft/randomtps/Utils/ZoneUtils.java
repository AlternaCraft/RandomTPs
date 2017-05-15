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

import com.alternacraft.randomtps.Zone.Zone;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 *
 * @author AlternaCraft
 */
public class ZoneUtils {
     
    public static final int CHUNK = 16;
    
    public static final int MAX_HEIGHT = 100;
    public static final int MAX_DIFF = 10;
    
    /**
     * Gets the blocks of a chunk
     * 
     * @param chunk Chunk to inspect.
     * @param min Minimum height.
     * @param max Maximum height.
     * 
     * @return Blocks
     */
    public static Block[] getChunkBlocks(Chunk chunk, int min, int max) {
        Block[] blocks = new Block[CHUNK*CHUNK*(max-min)];
        
        int cx = chunk.getX();
        int cz = chunk.getZ();
        
        int counter = 0;
        
        for (int x = cx; x < cx + CHUNK; x++) {
            for (int z = cz; z < cz + CHUNK; z++) {
                for (int y = min; y < max; y++) {
                    blocks[counter] = chunk.getBlock(x, y, z);
                    counter++;
                }
            }
        }
        
        return blocks;
    }    
    
    /**
     * Returns the highest middle block of the chunk.
     * 
     * @param c Chunk
     * 
     * @return Highest middle block
     */
    public static int getHighestMiddleBlockYAt(Chunk c) {
        int cx = c.getX() + (CHUNK / 2);
        int cz = c.getZ() + (CHUNK / 2);
        return c.getWorld().getHighestBlockYAt(cx, cz);
    }
    
    /**
     * Checks if a player is inside of a zone.
     *
     * @param p Player coordinates
     * @param z Zone name
     *
     * @return True if he is; False if not
     */
    public static boolean isInsideOfZone(Vector p, Zone z) {
        Vector max = Vector.getMaximum(z.getP1(), z.getP2());
        Vector min = Vector.getMinimum(z.getP1(), z.getP2());

        int xplayer = (p.getBlockX() < 0) ? p.getBlockX() + 1 : p.getBlockX();
        int zplayer = (p.getBlockZ() < 0) ? p.getBlockZ() + 1 : p.getBlockZ();

        return ((xplayer <= max.getBlockX() && xplayer >= min.getBlockX())
                && (zplayer <= max.getBlockZ() && zplayer >= min.getBlockZ()));
    }    
    
    /**
     * Gets collidant chunks by location.
     * It returns something like this:
     *   xxx
     *   xox
     *   xxx
     * where 'o' is the location and 'x' are the collidant chunks.
     *
     * @param l Location to inspect
     *
     * @return Array of collidant chunks
     */
    public static Chunk[] getCollidantChunks(Location l) {
        Chunk[] chunks = new Chunk[9];

        Chunk origin = l.getChunk();

        int xmin = origin.getX() - 1;
        int zmin = origin.getZ() - 1;

        int diff = 3;

        int xtemp = xmin;
        int ztemp = zmin;

        for (int i = 0; i < chunks.length; i++) {
            if (ztemp < zmin + diff) {
                chunks[i] = l.getWorld().getChunkAt(xtemp, ztemp);
                ztemp++;
            } else {
                xtemp++;
                ztemp = zmin;
                i--;
            }
        }

        return chunks;
    }
}
