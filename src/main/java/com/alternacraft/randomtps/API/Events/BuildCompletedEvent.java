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
package com.alternacraft.randomtps.API.Events;

import com.alternacraft.randomtps.Utils.ZoneBuilder;
import org.bukkit.command.CommandSender;

public class BuildCompletedEvent extends BuildEvent {
    
    public BuildCompletedEvent(CommandSender cs, ZoneBuilder zb) {
        super(cs, zb);
    }
    
    /**
     * Gets time between the start and the end.
     * 
     * @return Elapsed time in milliseconds
     */
    public long getElapsedtime() {
        return this.zoneBuilder().elapsedTime();
    }
    
    /**
     * Gets elapsed time in seconds
     * 
     * @return Elapsed time in seconds
     */
    public int getElapsedtimeInSeconds() {
        return (int)(getElapsedtime() / 1000);
    }
}
