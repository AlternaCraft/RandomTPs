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

import com.alternacraft.aclib.listeners.HandlersInterface;
import com.alternacraft.randomtps.Utils.BroadcastAsExp;
import org.bukkit.event.Listener;

/**
 * List of handlers.
 * 
 * @author AlternaCraft
 */
public enum Handlers implements HandlersInterface {

    BUILD(new HandleBuild()),
    FALL(new HandleFall()),
    GODS(new HandleGods()),
    GODS_EXP_BROADCAST(new BroadcastAsExp()), // custom broadcast
    TELEPORT(new HandleTeleport()),
    DEFINE(new HandleZoneCreation());

    private final Listener listener;

    Handlers(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Listener getListener() {
        return listener;
    }
}
