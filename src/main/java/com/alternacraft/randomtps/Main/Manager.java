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
package com.alternacraft.randomtps.Main;

import com.alternacraft.randomtps.Langs.GeneralInfo;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.arguments.ArgumentsRegister;
import com.alternacraft.aclib.hook.ExternalPluginRegister;
import com.alternacraft.aclib.hook.HookerInterface;
import com.alternacraft.aclib.langs.DefaultMessages;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.listeners.HandlersRegister;
import com.alternacraft.randomtps.Commands.Arguments;
import com.alternacraft.randomtps.Files.ZonesFile;
import com.alternacraft.randomtps.Langs.CommandInfo;
import com.alternacraft.randomtps.Langs.DefineInfo;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Listeners.Handlers;
import com.alternacraft.randomtps.Utils.Localization;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public class Manager {

    public static Manager INSTANCE = new Manager();

    public static final PluginBase BASE = PluginBase.INSTANCE;
    public static final ExternalPluginRegister HOOKS = ExternalPluginRegister.INSTANCE;

    private List<Localization> localizations = new ArrayList();
    private ArgumentsRegister mainCommand = null;

    /**
     * @since 0.0.9
     */
    private final ConfigLoader loader;

    private Manager() {
        loader = new ConfigLoader();
    }

    public boolean setup(JavaPlugin plugin) {
        BASE.init(plugin, loader);

        this.registerCommands();
        this.registerListeners();

        this.loadLanguages();
        this.loadExternalPlugins();
        this.loadLocalizations(); // Logic game

        return true;
    }

    public void registerCommands() {
        mainCommand = new ArgumentsRegister("randomtps", "rt");
        mainCommand.register(Arguments.class);
    }

    public void registerListeners() {
        HandlersRegister.load(Handlers.class);
    }

    public void loadLanguages() {
        LangManager.setKeys(Langs.ES, Langs.EN);
        LangManager.saveMessages(DefaultMessages.class, GeneralInfo.class,
                CommandInfo.class, DefineInfo.class, GameInfo.class);
        LangManager.loadMessages();
    }

    public void loadExternalPlugins() {
    }

    public void loadLocalizations() {
        ZonesFile.load();
        this.localizations = ZonesFile.getLocalizations();
    }

    /* GETTERS */
    public ConfigLoader loader() {
        return this.loader;
    }

    public HookerInterface getHooker(String name) {
        return HOOKS.getHooker(name);
    }

    public ArgumentsRegister getArgRegister() {
        return mainCommand;
    }

    /* LOCALIZATIONS */
    public Localization getLocalizationByName(String zone) {
        for (Localization loc : localizations) {
            if (loc.getZoneName().equals(zone)) {
                return loc;
            }
        }

        return null;
    }

    public List<Localization> getLocalizations() {
        return localizations;
    }

    public boolean localizationExits(String zone) {
        return getLocalizationByName(zone) != null;
    }

    public void enableLocation(String zone) {
        ZonesFile.enableZone(zone);
        this.addLocalization(zone);
    }

    public void disableLocation(String zone) {
        ZonesFile.disableZone(zone);
        this.removeLocalization(zone);
    }

    public void addLocalization(String zone) {
        localizations.add(ZonesFile.getLocalization(zone));
    }

    public void removeLocalization(String zone) {
        Localization l = getLocalizationByName(zone);
        this.localizations.remove(l);
    }
}
