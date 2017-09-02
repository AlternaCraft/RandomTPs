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
package com.alternacraft.randomtps.Main;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.commands.registerer.SubCommandsRegisterer;
import com.alternacraft.aclib.exceptions.ErrorManager;
import com.alternacraft.aclib.exceptions.PluginException;
import com.alternacraft.aclib.langs.CommandMessages;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.listeners.HandlersRegisterer;
import com.alternacraft.randomtps.API.Errors.DBErrors;
import com.alternacraft.randomtps.API.ZonesDB;
import com.alternacraft.randomtps.Commands.Commands;
import com.alternacraft.randomtps.Langs.CommandInfo;
import com.alternacraft.randomtps.Langs.DefineInfo;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Langs.GeneralInfo;
import com.alternacraft.randomtps.Listeners.Handlers;
import com.alternacraft.randomtps.Managers.ZoneManager;
import com.alternacraft.randomtps.Managers.ZoneManager.DEFAULT_VALIDATIONS;
import com.alternacraft.randomtps.Utils.FallingSafeValidation;
import com.alternacraft.randomtps.Utils.FluidsValidation;
import com.alternacraft.randomtps.Utils.PlayersValidation;
import com.alternacraft.randomtps.Zone.DefinedZone;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin management class.
 * 
 * @author AlternaCraft
 */
public class Manager {

    public static Manager INSTANCE = new Manager();

    public static final PluginBase BASE = PluginBase.INSTANCE;

    /* DBs */
    private ZonesDB zonesdb;
    
    private List<DefinedZone> zones = new ArrayList();
    private SubCommandsRegisterer mainCommand = null;

    private final ConfigLoader loader;

    private Manager() {
        loader = new ConfigLoader();
    }

    public boolean setup(JavaPlugin plugin) {
        BASE.init(plugin, loader);

        this.registerDBs();
        
        this.registerCommands();
        this.registerListeners();

        this.loadTestsBenchs(); // Analyze exceptions to find possible causes
        this.loadLanguages();
        this.loadExternalPlugins();
        this.loadDefinedZones(); // Logic game

        // Zone validations
        this.registerDefaultValidations();
        
        return true;
    }
    
    public void registerDBs() {
        // Multiple DBs in the future
        zonesdb = new ZonesFile();
    }

    public void registerCommands() {
        mainCommand = new SubCommandsRegisterer("randomtps", "rtp");
        mainCommand.register(Commands.class);
    }

    public void registerListeners() {
        HandlersRegisterer.load(Handlers.class);
    }

    public void loadTestsBenchs() {
        ErrorManager.registerTestsBenchs(DBErrors.class);
    }
    
    public void loadLanguages() {
        LangManager.setKeys(Lang.ES, Lang.EN);
        LangManager.saveMessages(CommandMessages.class, GeneralInfo.class,
                CommandInfo.class, DefineInfo.class, GameInfo.class);
        LangManager.loadMessages();
    }

    public void loadExternalPlugins() {
        // In the future
    }

    public void loadDefinedZones() {
        ZonesFile.load();
        try {
            this.zones = zonesdb.getDefinedZones();
        } catch (PluginException ex) {
            MessageManager.logArrayError(ex.getCustomStacktrace());
        }
    }
    
    public void registerDefaultValidations() {
        ZoneManager.registerValidation(
                DEFAULT_VALIDATIONS.FALLING.name(), new FallingSafeValidation()
        );
        ZoneManager.registerValidation(
                DEFAULT_VALIDATIONS.FLUIDS.name(), new FluidsValidation()
        );
        ZoneManager.registerValidation(
                DEFAULT_VALIDATIONS.PLAYERS.name(), new PlayersValidation()
        );
    }

    /* GETTERS */
    public ConfigLoader loader() {
        return this.loader;
    }

    public SubCommandsRegisterer getMainCommand() {
        return mainCommand;
    }

    public ZonesDB getZonesDB() {
        return this.zonesdb;
    }
    
    /* DEFINED ZONES */
    public DefinedZone getDefinedZoneByName(String zone) {
        for (DefinedZone loc : zones) {
            if (loc.getZoneName().equals(zone)) {
                return loc;
            }
        }

        return null;
    }

    public List<DefinedZone> getDefinedZones() {
        return zones;
    }

    public boolean zoneExists(String zone) {
        return getDefinedZoneByName(zone) != null;
    }

    public void enableDefinedZone(String zone) {
        try {
            zonesdb.enableDefinedZone(zone);
            this.addZone(zone);
        } catch (PluginException ex) {
            MessageManager.logArrayError(ex.getCustomStacktrace());
        }
    }

    public void disableDefinedZone(String zone) {
        try {
            zonesdb.disableDefinedZone(zone);
            this.removeDefinedZone(zone);
        } catch (PluginException ex) {
            MessageManager.logArrayError(ex.getCustomStacktrace());
        }
    }

    public void addZone(String zone) {        
        try {
            zones.add(zonesdb.getDefinedZone(zone));
        } catch (PluginException ex) {
            MessageManager.logArrayError(ex.getCustomStacktrace());
        }
    }

    public void removeDefinedZone(String zone) {
        DefinedZone l = getDefinedZoneByName(zone);
        this.zones.remove(l);
    }
}
