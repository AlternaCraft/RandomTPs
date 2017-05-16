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
import com.alternacraft.aclib.exceptions.PluginException;
import com.alternacraft.aclib.utils.PluginFile;
import com.alternacraft.randomtps.API.ZonesDB;
import com.alternacraft.randomtps.Zone.DefinedZone;
import com.alternacraft.randomtps.Zone.Zone;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.util.Vector;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * Database class.
 *
 * @author AlternaCraft
 */
public class ZonesFile implements ZonesDB {

    //<editor-fold defaultstate="collapsed" desc="FILE">
    private static PluginFile ZONESFILE;

    public static void load() {
        ZONESFILE = new PluginFile("localizations.yml") {
            @Override
            public void createConfig() {
                this.resetYamlConfiguration();

                this.yamlFile.options().header(""
                        + "###############\n"
                        + "## ZONE LIST ##\n"
                        + "###############"
                );
                this.yamlFile.options().copyHeader(true);

                this.setNode("activeZones", new ArrayList());

                this.setNode("example.customExtras.time", 15);
                this.setNode("example.origin.alias", "world");

                this.setNode("example.origin.p1.x", 0);
                this.setNode("example.origin.p1.y", 0);
                this.setNode("example.origin.p1.z", 0);

                this.setNode("example.origin.p2.x", 0);
                this.setNode("example.origin.p2.y", 0);
                this.setNode("example.origin.p2.z", 0);

                this.setNode("example.destination", Arrays.asList(new String[]{"world"}));

                this.saveConfiguration();
            }
        };
    }
    //</editor-fold>

    @Override
    public List<DefinedZone> getDefinedZones() throws PluginException {
        try {
            List<DefinedZone> zones = new ArrayList<>();
            List<String> activeZones = (List<String>) ZONESFILE.getNode("activeZones");

            for (String activeZone : activeZones) {
                if (ZONESFILE.hasNode(activeZone)) {
                    DefinedZone l = getDefinedZone(activeZone);
                    zones.add(l);
                    MessageManager.log("&eLocalization &b\"" + l.getZoneName()
                            + "\" &eloaded correctly");
                }
            }

            return zones;
        } catch (NullPointerException | YAMLException ex) {
            throw new PluginException(ex);
        }
    }

    @Override
    public DefinedZone getDefinedZone(String zoneName) throws PluginException {
        try {
            Zone zone = getZone(zoneName);
            DefinedZone l;

            String origin = (String) ZONESFILE.getNode(zoneName + ".origin.alias");
            if (ZONESFILE.getNode(zoneName + ".destination") instanceof List) {
                List<String> destinations = (List<String>) ZONESFILE.getNode(zoneName
                        + ".destination");
                l = new DefinedZone(zoneName, zone, origin, destinations);
            } else {
                Map<String, List<Zone>> subzones = getSubzones(zoneName);
                l = new DefinedZone(zoneName, zone, origin, subzones);
            }

            // Custom options
            l.setX(Manager.INSTANCE.loader().getX());
            if (ZONESFILE.hasNode(zoneName + ".customExtras.limits.x.max")
                    && ZONESFILE.hasNode(zoneName + ".customExtras.limits.x.min")) {
                l.setX(new int[]{
                    (int) ZONESFILE.getNode(zoneName + ".customExtras.limits.x.max"),
                    (int) ZONESFILE.getNode(zoneName + ".customExtras.limits.x.min")
                });
            }
            l.setY(Manager.INSTANCE.loader().getY());
            if (ZONESFILE.hasNode(zoneName + ".customExtras.height")) {
                l.setY((int) ZONESFILE.getNode(zoneName + ".customExtras.height"));
            }
            l.setZ(Manager.INSTANCE.loader().getZ());
            if (ZONESFILE.hasNode(zoneName + ".customExtras.limits.z.max")
                    && ZONESFILE.hasNode(zoneName + ".customExtras.limits.z.min")) {
                l.setZ(new int[]{
                    (int) ZONESFILE.getNode(zoneName + ".customExtras.limits.z.max"),
                    (int) ZONESFILE.getNode(zoneName + ".customExtras.limits.z.min")
                });
            }
            l.setTime(Manager.INSTANCE.loader().getTime());
            if (ZONESFILE.hasNode(zoneName + ".customExtras.time")) {
                l.setTime((int) ZONESFILE.getNode(zoneName + ".customExtras.time"));
            }
            l.setBroadcastAsEXP(Manager.INSTANCE.loader().isBroadcast_as_exp());
            if (ZONESFILE.hasNode(zoneName + ".customExtras.broadcast.as_experience")) {
                l.setBroadcastAsEXP((boolean) ZONESFILE.getNode(zoneName
                        + ".customExtras.broadcast.as_experience"));
            }
            l.setPotion_effects(Manager.INSTANCE.loader().getPotions_effects());
            if (ZONESFILE.hasNode(zoneName + ".customExtras.effects.potions")) {
                l.setPotion_effects((List<String>) ZONESFILE.getNode(zoneName
                        + ".customExtras.effects.potions"));
            }
            l.setValidations(Manager.INSTANCE.loader().getValidations());
            if (ZONESFILE.hasNode(zoneName + ".customExtras.validations")) {
                l.setValidations((List<String>) ZONESFILE.getNode(zoneName
                        + ".customExtras.validations"));
            }

            return l;
        } catch (NullPointerException | YAMLException ex) {
            throw new PluginException(ex);
        }
    }

    @Override
    public Map<String, List<Zone>> getSubzones(String zone) throws PluginException {
        try {
            Map<String, List<Zone>> subzones = new HashMap();
            Set<String> worlds = ZONESFILE.getNodes(zone + ".destination");
            if (worlds != null) {
                for (String w : worlds) {
                    List<Zone> zones = new ArrayList();

                    Set<String> szs = ZONESFILE.getNodes(zone + ".destination." + w);
                    for (String subzone : szs) {
                        Vector v1 = new Vector(
                                (int) ZONESFILE.getNode(zone + ".destination."
                                        + w + "." + subzone + ".p1.x"),
                                0, // I dont need it
                                (int) ZONESFILE.getNode(zone + ".destination."
                                        + w + "." + subzone + ".p1.z")
                        );
                        Vector v2 = new Vector(
                                (int) ZONESFILE.getNode(zone + ".destination."
                                        + w + "." + subzone + ".p2.x"),
                                0, // I dont need it
                                (int) ZONESFILE.getNode(zone + ".destination."
                                        + w + "." + subzone + ".p2.z")
                        );

                        zones.add(new Zone(v1, v2));
                    }

                    subzones.put(w, zones);
                }
            }
            return subzones;
        } catch (Exception ex) {
            throw new PluginException(ex);
        }
    }

    @Override
    public Zone getZone(String zone) throws PluginException {
        try {
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
        } catch (Exception ex) {
            throw new PluginException(ex);
        }
    }

    @Override
    public void saveDefinedZone(DefinedZone l) throws PluginException {
        try {
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
                        ZONESFILE.setNode(zone + ".destination." + world + ".sz"
                                + i + ".p1.x", z.getP1().getBlockX());
                        ZONESFILE.setNode(zone + ".destination." + world + ".sz"
                                + i + ".p1.z", z.getP1().getBlockZ());

                        ZONESFILE.setNode(zone + ".destination." + world + ".sz"
                                + i + ".p2.x", z.getP2().getBlockX());
                        ZONESFILE.setNode(zone + ".destination." + world + ".sz"
                                + i + ".p2.z", z.getP2().getBlockZ());

                        i++;
                    }
                }
            } else {
                ZONESFILE.setNode(zone + ".destination", l.getDestinations());
            }

            ZONESFILE.saveConfiguration();
        } catch (Exception ex) {
            throw new PluginException(ex);
        }
    }

    @Override
    public void enableDefinedZone(String zone) throws PluginException {
        try {
            List<String> actives = (List<String>) ZONESFILE.getNode("activeZones");
            if (!actives.contains(zone)) {
                actives.add(zone);
                ZONESFILE.setNode("activeZones", actives);
            }
            ZONESFILE.saveConfiguration();
        } catch (Exception ex) {
            throw new PluginException(ex);
        }
    }

    @Override
    public void disableDefinedZone(String zone) throws PluginException {
        try {
            List<String> actives = (List<String>) ZONESFILE.getNode("activeZones");
            if (actives.contains(zone)) {
                actives.remove(zone);
                ZONESFILE.setNode("activeZones", actives);
            }
            ZONESFILE.saveConfiguration();
        } catch (Exception ex) {
            throw new PluginException(ex);
        }
    }

    @Override
    public int purge() throws PluginException {
        try {
            int n = 0;

            List<String> actives = ZONESFILE.yamlFile.getStringList("activeZones");
            Set<String> zones = ZONESFILE.getNodes("");

            for (String zone : zones) {
                if (!actives.contains(zone) && !zone.equals("activeZones")) {
                    ZONESFILE.setNode(zone, null);
                    n++;
                }
            }

            ZONESFILE.saveConfiguration();

            return n;
        } catch (Exception ex) {
            throw new PluginException(ex);
        }
    }
}
