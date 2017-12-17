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
import com.alternacraft.aclib.commands.SubCommand;
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
public enum Commands implements SubCommandsInterface {
    NONE(
            "",
            CommandInfo.COMMAND_EMPTY,
            new PluginCommands(Manager.INSTANCE.getMainCommand().cmdListener())
    ),
    INFO(
            "info",
            CommandInfo.COMMAND_INFO_DESC,
            new InfoCommand()
    ),
    ZONE(
            "zone",
            CommandInfo.COMMAND_ZONE_DESC,
            new ZoneCommand(),
            new SubCommand("list", CommandInfo.COMMAND_ZONE_LIST_DESC),
            new SubCommand("toggleactive", CommandInfo.COMMAND_ZONE_TOGGLEACTIVE_DESC),
            new SubCommand("go", CommandInfo.COMMAND_ZONE_GO_DESC),
            new SubCommand("create", CommandInfo.COMMAND_ZONE_CREATE_DESC),
            new SubCommand("show", CommandInfo.COMMAND_ZONE_SHOW_DESC),
            new SubCommand("hide", CommandInfo.COMMAND_ZONE_HIDE_DESC),
            new SubCommand("restoreall", CommandInfo.COMMAND_ZONE_RESTOREALL_DESC)
    ),
    PURGE(
            "purge",
            CommandInfo.COMMAND_PURGE_DESC,
            new PurgeCommand()
    ),
    RELOAD(
            "reload",
            CommandInfo.COMMAND_RELOAD_DESC,
            new ReloadCommand(),
            "rl"
    );

    private final String cmd;
    private final Enum desc;
    private final SubCommandExecutor instance;
    private String[] aliases;
    private SubCommand[] commands;

    Commands(String cmd, Enum desc, SubCommandExecutor instance, SubCommand... commands) {
        this(cmd, desc, instance);
        this.commands = commands;
    }

    Commands(String cmd, Enum desc, SubCommandExecutor instance, String... aliases) {
        this(cmd, desc, instance);
        this.aliases = aliases;
    }

    Commands(String cmd, Enum desc, SubCommandExecutor instance) {
        this.cmd = cmd;
        this.desc = desc;
        this.instance = instance;
        this.aliases = new String[0];
        this.commands = new SubCommand[0];
    }

    @Override
    public String getSubCommand() {
        return cmd;
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
        return null;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public SubCommand[] getArguments() {
        return commands;
    }
}
