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
package com.alternacraft.randomtps.Files;

import static com.alternacraft.aclib.ConfigurationFile.DIRECTORY;
import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.files.PluginFile;
import com.alternacraft.randomtps.Utils.Localization;
import com.alternacraft.randomtps.Utils.Zone;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.util.Vector;

public class ZonesFile {

    //<editor-fold defaultstate="collapsed" desc="FILE">
    public static final PluginFile ZONESFILE = new PluginFile(
            DIRECTORY
            + File.separator
            + "localizations.yml");

    public static void load() {
        if (!ZONESFILE.exists()) {
            createConfig();
        }
        ZONESFILE.loadYamlConfiguration();
    }

    private static void createConfig() {
        ZONESFILE.resetYamlConfiguration();

        ZONESFILE.yamlFile.options().header(
                "###############\n"
                + "## ZONE LIST ##\n"
                + "###############"
        );
        ZONESFILE.yamlFile.options().copyHeader(true);

        ZONESFILE.setNode("activeZones", new ArrayList());

        // Example
        ZONESFILE.setNode("defaultExtras.time", 10);
        ZONESFILE.setNode("defaultExtras.inmortal", true);
        ZONESFILE.setNode("defaultExtras.broadcast.show_as_exp", true);
        ZONESFILE.setNode("defaultExtras.effects.potions", new ArrayList<String>() {
            {
                add("Speed 5");
            }
        });

        ZONESFILE.setNode("example.customExtras.time", 15);
        ZONESFILE.setNode("example.origin.alias", "world");

        ZONESFILE.setNode("example.origin.p1.x", 0);
        ZONESFILE.setNode("example.origin.p1.y", 0);
        ZONESFILE.setNode("example.origin.p1.z", 0);

        ZONESFILE.setNode("example.origin.p2.x", 0);
        ZONESFILE.setNode("example.origin.p2.y", 0);
        ZONESFILE.setNode("example.origin.p2.z", 0);

        ZONESFILE.setNode("example.destination", Arrays.asList(new String[]{"world"}));

        ZONESFILE.saveConfiguration();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UTILS">
    public static List<Localization> getLocalizations() {
        List<Localization> zones = new ArrayList<>();
        List<String> activeZones = (List<String>) ZONESFILE.getNode("activeZones");

        for (String activeZone : activeZones) {
            if (ZONESFILE.hasNode(activeZone)) {
                Localization l = getLocalization(activeZone);
                zones.add(l);
                MessageManager.log("&eLocalization &b\"" + l.getZoneName() + "\" &eloaded correctly");
            }
        }

        return zones;
    }

    public static Localization getLocalization(String zoneName) {
        Zone zone = getZone(zoneName);
        Localization l;

        String origin = (String) ZONESFILE.getNode(zoneName + ".origin.alias");
        if (ZONESFILE.getNode(zoneName + ".destination") instanceof List) {
            List<String> destinations = (List<String>) ZONESFILE.getNode(zoneName + ".destination");
            l = new Localization(zoneName, zone, origin, destinations);
        } else {
            Map<String, List<Zone>> subzones = getSubzones(zoneName);
            l = new Localization(zoneName, zone, origin, subzones);
        }

        // Custom options
        l.setTime((int) ZONESFILE.getNode("defaultExtras.time"));
        if (ZONESFILE.hasNode(zoneName + ".customExtras.time")) {
            l.setTime((int) ZONESFILE.getNode(zoneName + ".customExtras.time"));
        }
        l.setInmortal((boolean) ZONESFILE.getNode("defaultExtras.inmortal"));
        if (ZONESFILE.hasNode(zoneName + ".customExtras.inmortal")) {
            l.setInmortal((boolean) ZONESFILE.getNode(zoneName + ".customExtras.inmortal"));
        }
        l.setBroadcastAsEXP((boolean) ZONESFILE.getNode("defaultExtras.broadcast.show_as_exp"));
        if (ZONESFILE.hasNode(zoneName + ".customExtras.broadcast.show_as_exp")) {
            l.setBroadcastAsEXP((boolean) ZONESFILE.getNode(zoneName
                    + ".customExtras.broadcast.show_as_exp"));
        }
        l.setPotion_effects((List<String>) ZONESFILE.getNode("defaultExtras.effects.potions"));
        if (ZONESFILE.hasNode(zoneName + ".customExtras.effects.potions")) {
            l.setPotion_effects((List<String>) ZONESFILE.getNode(zoneName
                    + ".customExtras.effects.potions"));
        }

        return l;
    }

    public static void saveZone(Localization l) {
        String zone = l.getZoneName();

        ZONESFILE.setNode(zone + ".origin.alias", l.getOrigin());

        ZONESFILE.setNode(zone + ".origin.p1.x", l.getP1().getBlockX());
        ZONESFILE.setNode(zone + ".origin.p1.y", l.getP1().getBlockY());
        ZONESFILE.setNode(zone + ".origin.p1.z", l.getP1().getBlockZ());

        ZONESFILE.setNode(zone + ".origin.p2.x", l.getP2().getBlockX());
        ZONESFILE.setNode(zone + ".origin.p2.y", l.getP2().getBlockY());
        ZONESFILE.setNode(zone + ".origin.p2.z", l.getP2().getBlockZ());

        if (l.hasSubzones()) {
            Map<String, List<Zone>> subzones = l.getSubzones();
            for (Map.Entry<String, List<Zone>> entry : subzones.entrySet()) {
                String world = entry.getKey();
                List<Zone> zones = entry.getValue();
                int i = 1;
                for (Zone z : zones) {
                    ZONESFILE.setNode(zone + ".destination." + world + ".sz" + i + ".p1.x", z.getP1().getBlockX());
                    ZONESFILE.setNode(zone + ".destination." + world + ".sz" + i + ".p1.y", z.getP1().getBlockY());
                    ZONESFILE.setNode(zone + ".destination." + world + ".sz" + i + ".p1.z", z.getP1().getBlockZ());

                    ZONESFILE.setNode(zone + ".destination." + world + ".sz" + i + ".p2.x", z.getP2().getBlockX());
                    ZONESFILE.setNode(zone + ".destination." + world + ".sz" + i + ".p2.y", z.getP2().getBlockY());
                    ZONESFILE.setNode(zone + ".destination." + world + ".sz" + i + ".p2.z", z.getP2().getBlockZ());

                    i++;
                }
            }
        } else {
            ZONESFILE.setNode(zone + ".destination", l.getDestinations());
        }

        enableZone(zone);

        ZONESFILE.saveConfiguration();
    }

    public static void enableZone(String zone) {
        List<String> actives = (List<String>) ZONESFILE.getNode("activeZones");
        if (!actives.contains(zone)) {
            actives.add(zone);
            ZONESFILE.setNode("activeZones", actives);
        }
    }

    public static void disableZone(String zone) {
        List<String> actives = (List<String>) ZONESFILE.getNode("activeZones");
        if (actives.contains(zone)) {
            actives.remove(zone);
            ZONESFILE.setNode("activeZones", actives);
        }
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INTERNAL STUFF">
    private static Zone getZone(String zone) {
        int x1, y1, z1;
        int x2, y2, z2;

        x1 = (int) ZONESFILE.getNode(zone + ".origin.p1.x");
        y1 = (int) ZONESFILE.getNode(zone + ".origin.p1.y");
        z1 = (int) ZONESFILE.getNode(zone + ".origin.p1.z");

        x2 = (int) ZONESFILE.getNode(zone + ".origin.p2.x");
        y2 = (int) ZONESFILE.getNode(zone + ".origin.p2.y");
        z2 = (int) ZONESFILE.getNode(zone + ".origin.p2.z");

        Vector p1 = new Vector(x1, y1, z1);
        Vector p2 = new Vector(x2, y2, z2);

        return new Zone(p1, p2);
    }

    private static Map<String, List<Zone>> getSubzones(String zone) {
        Map<String, List<Zone>> subzones = new HashMap();
        Set<String> worlds = ZONESFILE.getNodes(zone + ".destination");
        if (worlds != null) {
            for (String w : worlds) {
                List<Zone> zones = new ArrayList();

                Set<String> szs = ZONESFILE.getNodes(zone + ".destination." + w);
                for (String subzone : szs) {
                    Vector v1 = new Vector(
                            (int) ZONESFILE.getNode(zone + ".destination." + w + "." + subzone + ".p1.x"),
                            (int) ZONESFILE.getNode(zone + ".destination." + w + "." + subzone + ".p1.y"),
                            (int) ZONESFILE.getNode(zone + ".destination." + w + "." + subzone + ".p1.z")
                    );
                    Vector v2 = new Vector(
                            (int) ZONESFILE.getNode(zone + ".destination." + w + "." + subzone + ".p2.x"),
                            (int) ZONESFILE.getNode(zone + ".destination." + w + "." + subzone + ".p2.y"),
                            (int) ZONESFILE.getNode(zone + ".destination." + w + "." + subzone + ".p2.z")
                    );

                    zones.add(new Zone(v1, v2));
                }

                subzones.put(w, zones);
            }
        }
        return subzones;
    }
    //</editor-fold>
}
