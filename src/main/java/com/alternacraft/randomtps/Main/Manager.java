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

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.commands.registerer.SubCommandsRegisterer;
import com.alternacraft.aclib.hook.ExternalPluginRegisterer;
import com.alternacraft.aclib.hook.HookerInterface;
import com.alternacraft.aclib.langs.CommandMessages;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.listeners.HandlersRegisterer;
import com.alternacraft.randomtps.API.ZonesDB;
import com.alternacraft.randomtps.Commands.SubCommands;
import com.alternacraft.randomtps.Langs.CommandInfo;
import com.alternacraft.randomtps.Langs.DefineInfo;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Langs.GeneralInfo;
import com.alternacraft.randomtps.Listeners.Handlers;
import com.alternacraft.randomtps.Localizations.LocalizationInfo;
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
    public static final ExternalPluginRegisterer HOOKS = ExternalPluginRegisterer.INSTANCE;

    /* DBs */
    private ZonesDB zonesdb;
    
    private List<LocalizationInfo> localizations = new ArrayList();
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

        this.loadLanguages();
        this.loadExternalPlugins();
        this.loadLocalizations(); // Logic game

        return true;
    }
    
    public void registerDBs() {
        // Multiple DBs in the future
        zonesdb = new ZonesFile();
    }

    public void registerCommands() {
        mainCommand = new SubCommandsRegisterer("randomtps", "rtp");
        mainCommand.register(SubCommands.class);
    }

    public void registerListeners() {
        HandlersRegisterer.load(Handlers.class);
    }

    public void loadLanguages() {
        LangManager.setKeys(Langs.ES, Langs.EN);
        LangManager.saveMessages(CommandMessages.class, GeneralInfo.class,
                CommandInfo.class, DefineInfo.class, GameInfo.class);
        LangManager.loadMessages();
    }

    public void loadExternalPlugins() {
    }

    public void loadLocalizations() {
        ZonesFile.load();
        this.localizations = zonesdb.getLocalizations();
    }

    /* GETTERS */
    public ConfigLoader loader() {
        return this.loader;
    }

    public HookerInterface getHooker(String name) {
        return HOOKS.getHooker(name);
    }

    public SubCommandsRegisterer getMainCommand() {
        return mainCommand;
    }

    public ZonesDB getZonesDB() {
        return this.zonesdb;
    }
    
    /* LOCALIZATIONS */
    public LocalizationInfo getLocalizationByName(String zone) {
        for (LocalizationInfo loc : localizations) {
            if (loc.getZoneName().equals(zone)) {
                return loc;
            }
        }

        return null;
    }

    public List<LocalizationInfo> getLocalizations() {
        return localizations;
    }

    public boolean localizationExists(String zone) {
        return getLocalizationByName(zone) != null;
    }

    public void enableLocation(String zone) {
        zonesdb.enableLocalization(zone);
        this.addLocalization(zone);
    }

    public void disableLocation(String zone) {
        zonesdb.disableLocalization(zone);
        this.removeLocalization(zone);
    }

    public void addLocalization(String zone) {        
        localizations.add(zonesdb.getLocalization(zone));
    }

    public void removeLocalization(String zone) {
        LocalizationInfo l = getLocalizationByName(zone);
        this.localizations.remove(l);
    }
}
