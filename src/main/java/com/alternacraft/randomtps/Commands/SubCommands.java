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
package com.alternacraft.randomtps.Commands;

import com.alternacraft.aclib.commands.Condition;
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.commands.registerer.SubCommandsInterface;
import com.alternacraft.aclib.extras.PluginCommands;
import com.alternacraft.randomtps.Langs.CommandInfo;
import com.alternacraft.randomtps.Main.Manager;

/**
 * List of all subcommands.
 * 
 * @author AlternaCraft
 */
public enum SubCommands implements SubCommandsInterface {
    NONE(
            "",
            "rtp",
            CommandInfo.COMMAND_EMPTY,
            new PluginCommands(Manager.INSTANCE.getMainCommand().cmdListener())
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
    );

    private final String arg, usage;
    private final Enum desc;
    private final SubCommandExecutor instance;
    private final Condition condition;
    private final String[] aliases;

    SubCommands(String argument, String usage, Enum desc, 
            SubCommandExecutor instance) {
        this(argument, usage, desc, instance, null, new String[0]);
    }

    SubCommands(String argument, String usage, Enum desc, 
            SubCommandExecutor instance, String... aliases) {
        this(argument, usage, desc, instance, null, aliases);
    }

    SubCommands(String argument, String usage, Enum desc, 
            SubCommandExecutor instance, Condition condition) {
        this(argument, usage, desc, instance, condition, new String[0]);
    }

    SubCommands(String argument, String usage, Enum desc, SubCommandExecutor instance, 
            Condition condition, String... aliases) {
        this.arg = argument;
        this.usage = usage;
        this.desc = desc;
        this.instance = instance;
        this.condition = condition;
        this.aliases = aliases;
    }

    @Override
    public String getSubCommand() {
        return arg;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public Enum getDescription() {
        return desc;
    }

    @Override
    public SubCommandExecutor getInstance() {
        return instance;
    }

    @Override
    public Condition getCustomCondition() {
        return condition;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }
}
