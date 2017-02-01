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
import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.randomtps.API.Events.BuildCompletedEvent;
import com.alternacraft.randomtps.API.Events.BuildStartedEvent;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Utils.ZoneBuilder;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Handle zone construction.
 * 
 * @author AlternaCraft
 */
public class HandleBuild implements Listener {

    public static final Map<String, ZoneBuilder> DISABLED = new HashMap();
    
    @EventHandler
    public void handleBuild(final BuildStartedEvent ev) {        
        if (!ev.isRollback()) {
            DISABLED.put(ev.zoneName(), ev.zoneBuilder());
        }
    }
    
    @EventHandler
    public void handleBuildCompleted(final BuildCompletedEvent ev) {
        String time = StringsUtils.splitToComponentTimes(ev.getElapsedtimeInSeconds());
        Langs l = Localizer.getLocale(ev.cs());
        
        if (ev.isRollback()) {
            MessageManager.sendCommandSender(ev.cs(), GameInfo.ZONE_END_ROLLBACK
                    .getText(l).replace("%TIME%", time).replace("%NAME%", ev.zoneName()));            
            DISABLED.remove(ev.zoneName());
        } else {
            MessageManager.sendCommandSender(ev.cs(), GameInfo.ZONE_END_BUILD
                    .getText(l).replace("%TIME%", time).replace("%NAME%", ev.zoneName()));
        }
    }

}
