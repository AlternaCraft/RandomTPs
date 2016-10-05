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
package com.alternacraft.randomtps.Commands;

import com.alternacraft.aclib.arguments.ArgumentsInterface;
import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.randomtps.Langs.CommandInfo;

public enum Arguments implements ArgumentsInterface {
    NONE(
            "",
            "rtp",
            CommandInfo.COMMAND_EMPTY,
            new Commands()
    ),
    INFO(
            "info",
            "rtp info",
            CommandInfo.COMMAND_INFO_DESC,
            new InfoCommand()
    ),
    ZONE(
            "zone",
            "rtp zone [list | toggleactive | go | create | show | hide | restoreall]",
            CommandInfo.COMMAND_ZONE_DESC,
            new ZoneCommand()
    ),
    PURGE(
            "purge",
            "rtp purge",
            CommandInfo.COMMAND_PURGE_DESC,
            new PurgeCommand()
    ),
    RELOAD(
            "reload",
            "rtp reload",
            CommandInfo.COMMAND_RELOAD_DESC,
            new ReloadCommand()
    ),
    STATS(
            "stats",
            "rtp stats",
            CommandInfo.COMMAND_STATS_DESC,
            new StatsCommand()
    );

    private final String arg, usage;
    private final Enum info;
    private final ArgumentExecutor instance;

    Arguments(String argument, String usage, Enum description, ArgumentExecutor instance) {
        this.arg = argument;
        this.usage = usage;
        this.info = description;
        this.instance = instance;
    }

    @Override
    public String getArgument() {
        return arg;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public Enum getDescription() {
        return info;
    }

    @Override
    public ArgumentExecutor getInstance() {
        return instance;
    }
}
