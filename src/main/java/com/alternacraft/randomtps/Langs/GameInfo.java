/*
 * Copyright (C) 2018 AlternaCraft
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
package com.alternacraft.randomtps.Langs;

import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.langs.LangInterface;
import java.util.HashMap;
import java.util.Map;

/**
 * Messages about game information.
 * 
 * @author AlternaCraft
 */
public enum GameInfo implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="MESSAGES">
    PLAYER_CANT_BE_TELEPORTED(
            "&cLo siento, la zona de llegada esta ocupada",
            "&cI am sorry, the destination zone is full"
    ),
    PLAYER_TELEPORTED(
            "&6¡Prepárate para la batalla!",
            "&6Prepare for the battle!"
    ),
    PLAYER_INVULNERABILITY(
            "&bEres invulnerable durante %TIME% segundos",
            "&bYou are invulnerable for %TIME% seconds"
    ),
    PLAYER_LOSES_PVP(
            "&c¡No puedes pegar a otros jugadores mientras estás protegido!",
            "&cYou can not hit other players while you are protected!"
    ),
    PLAYER_RECOVERS_PVP(
            "&cSe ha terminado tu protección",
            "&cYour protection has finished"
    ),
    PLUGIN_ERROR_ON_TP(
            "&cEl mundo de destino no existe",
            "&cThe destination world does not exist"
    ),
    ZONE_ENABLED(
            "&aZona activada",
            "&aZone enabled"
    ),
    ZONE_DISABLED(
            "&aZona desactivada",
            "&aZone disabled"
    ),
    ZONE_START_BUILD(
            "&6Costruyendo límites de la zona %NAME%...",
            "&6Building the limits of the zone %NAME%..."
    ),
    ZONE_END_BUILD(
            "&aConstrucción de %NAME% terminada en %TIME%",
            "&aBuilding of %NAME% completed in %TIME%"
    ),
    ZONE_START_ROLLBACK(
            "&6Restaurando la zona %NAME%...",
            "&6Restoring the %NAME% zone..."
    ),
    ZONE_END_ROLLBACK(
            "&aRollback de %NAME% completado en %TIME%",
            "&aRollback of %NAME% completed in %TIME%"
    ),
    ZONE_ALREADY_BUILDED(
            "&cEsa zona ya está definida",
            "&cThat zone is already defined"
    ),
    ZONE_NOT_EXISTS(
            "&cEsa zona no existe",
            "&cThat zone does not exist"
    ),
    ZONE_NOT_ROLLBACK(
            "&cEsa zona no puede ser restaurada",
            "&cThat zone can not be restored"
    ),
    ZONE_GO_TITLE(
            "&b* &lCoordenadas &bde la zona *",
            "&b* Zone &coordinates&b *"
    ),
    ZONE_GO_CLICK(
            "&5# &lClic izquierdo &5para ir #",
            "&5# Left click to go #"
    ),
    ZONE_GO_FINISHED(
            "&aHa llegado a su destino",
            "&aYou have reached your destination"
    ),
    ZONE_IN_USE(
            "&cEsa zona está en uso, ¡espera a que termine!",
            "&cThat zone is in use, wait until it ends!"
    ),
    RESET_ZONES(
            "&eRestaurando las zonas...",
            "&eRestoring zones..."
    ),
    NO_ZONES_TO_RESET(
            "&cNo hay zonas para restaurar",
            "&cThere are no zones to restore"
    ),
    PURGE_ZONES(
            "&eSe ha\\/n eliminado %QUANT% zona/s inactiva/s",
            "&e%QUANT% inactive zone/s has been removed"
    );
    // </editor-fold>

    private final HashMap<Lang, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     */
    private GameInfo(String es, String en) {
        this.locales.put(Lang.ES, es);
        this.locales.put(Lang.EN, en);
    }

    @Override
    public Map<Lang, String> getLocales() {
        return this.locales;
    }

    @Override
    public Enum getEnum() {
        return this;
    }
}
