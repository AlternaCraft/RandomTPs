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
package com.alternacraft.randomtps.Commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.commands.SubCommandExecutor;
import com.alternacraft.aclib.extras.NumbersUtils;
import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.randomtps.API.Events.DefineZoneEvent;
import com.alternacraft.randomtps.Langs.DefineInfo;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Langs.GeneralInfo;
import com.alternacraft.randomtps.Listeners.HandleBuild;
import static com.alternacraft.randomtps.Listeners.HandleBuild.DISABLED;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Utils.ZoneBuilder;
import com.alternacraft.randomtps.Zone.DefinedZone;
import java.util.List;
import java.util.Map;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Zone management.
 * 
 * @author AlternaCraft
 */
public class ZoneCommand implements SubCommandExecutor {

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Lang lang = Localizer.getLocale(cs);
        String cmd = args[0];

        switch (cmd) {
            case "list":
                // <editor-fold defaultstate="collapsed" desc="LIST CODE">
                cs.sendMessage("");
                MessageManager.sendPluginMessage(cs, "");
                cs.sendMessage(ChatColor.GRAY + "  ---------");
                cs.sendMessage(ChatColor.BLUE + "   Zone list");
                cs.sendMessage(ChatColor.GRAY + "  ---------");

                List<DefinedZone> locs = Manager.INSTANCE.getDefinedZones();
                for (DefinedZone loc : locs) {
                    String zonename = "  - " + loc.getZoneName();
                    if (cs instanceof Player) {
                        TextComponent tc = new TextComponent();
                        tc.setText(zonename);
                        tc.setItalic(Boolean.TRUE);
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/rtp zone go " + loc.getZoneName()));
                        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(loc.toClickString(lang)).create()));
                        ((Player) cs).spigot().sendMessage(tc);
                    } else {
                        cs.sendMessage(zonename);
                    }

                }
                break;
            // </editor-fold>
            case "toggleactive":
                //<editor-fold defaultstate="collapsed" desc="TOGGLE CODE">
                if (args.length > 1) {
                    String zone = args[1];
                    if (Manager.INSTANCE.zoneExists(zone)) {
                        Manager.INSTANCE.disableDefinedZone(zone);
                        MessageManager.sendPluginMessage(cs, 
                                GameInfo.ZONE_DISABLED.getText(lang));
                    }
                    else {
                        Manager.INSTANCE.enableDefinedZone(zone);
                        MessageManager.sendPluginMessage(cs,
                                GameInfo.ZONE_ENABLED.getText(lang));
                    }
                } else {
                    MessageManager.sendPluginMessage(cs,
                            "&cSyntax: /rtp zone toggle <name>");
                }
                break;
                //</editor-fold>                
            case "go":
                // <editor-fold defaultstate="collapsed" desc="GO CODE">
                if (cs instanceof Player) {
                    Player player = (Player) cs;

                    if (args.length > 1) {
                        DefinedZone loc = Manager.INSTANCE.getDefinedZoneByName(args[1]);

                        if (loc != null) {
                            player.setGameMode(GameMode.CREATIVE);
                            player.setFlying(Boolean.TRUE);

                            player.teleport(loc.getVisitLocation(Bukkit.getWorld(loc.getOrigin())));
                            MessageManager.sendPluginMessage(player,
                                    GameInfo.ZONE_GO_FINISHED.getText(lang));
                        } else {
                            MessageManager.sendPluginMessage(cs,
                                    GameInfo.ZONE_NOT_EXISTS.getText(lang));
                        }
                    } else {
                        MessageManager.sendPluginMessage(cs,
                                "&cSyntax: /rtp zone go <name>");
                    }
                } else {
                    MessageManager.sendPluginMessage(cs,
                            GeneralInfo.COMMAND_FORBIDDEN.getText(lang));
                }
                break;
            // </editor-fold>
            case "create":
                // <editor-fold defaultstate="collapsed" desc="DEFINE CODE">
                if (cs instanceof Player) {
                    Player player = (Player) cs;

                    if (args.length > 1) {
                        String zone = args[1];

                        boolean exists = Manager.INSTANCE.zoneExists(zone);

                        if (exists) {
                            HandleBuild.DISABLED.put(zone, null);
                            MessageManager.sendPluginMessage(cs,
                                    DefineInfo.REDEFINING.getText(lang));
                        }
                        
                        Bukkit.getServer().getPluginManager().callEvent(
                                new DefineZoneEvent(player, zone, exists));
                    } else {
                        MessageManager.sendPluginMessage(cs,
                                "&cSyntax: /rtp zone create <name>");
                    }
                } else {
                    MessageManager.sendPluginMessage(cs,
                            GeneralInfo.COMMAND_FORBIDDEN.getText(lang));
                }
                break;
            // </editor-fold>
            case "show":
                // <editor-fold defaultstate="collapsed" desc="SHOW CODE">
                if (args.length > 1) {
                    String zone = args[1];

                    if (!isAvailable(zone) || DISABLED.containsKey(zone)) {
                        if (!isAvailable(zone)) {
                            MessageManager.sendPluginMessage(cs,
                                    GameInfo.ZONE_IN_USE.getText(lang));
                        } else {
                            MessageManager.sendPluginMessage(cs,
                                    GameInfo.ZONE_ALREADY_BUILDED.getText(lang));
                        }
                        return true;
                    }

                    String material = "glowstone"; // Default
                    if (args.length > 2) {
                        material = args[2];
                    }

                    Material m = null;

                    // Get the material
                    if (material.matches("[0-9]+.*")) {
                        String[] parts = material.split(":");
                        if (NumbersUtils.isInteger(parts[0])) {
                            int typeId = Integer.parseInt(parts[0]); // Parse from the first string part
                            m = Material.getMaterial(typeId);
                        }
                    } else {
                        try {
                            m = Material.valueOf(material.toUpperCase());
                        } catch (Exception e) {
                        }
                    }

                    // Check the material
                    if (m == null) {
                        MessageManager.sendPluginMessage(cs, "&cYou have to indicate "
                                + "a valid material (materialName|materialID)");
                    } else {
                        DefinedZone l = Manager.INSTANCE.getDefinedZoneByName(zone);

                        if (l == null) {
                            MessageManager.sendPluginMessage(cs,
                                    GameInfo.ZONE_NOT_EXISTS.getText(lang));
                            return true;
                        }

                        ZoneBuilder z = new ZoneBuilder(l.getZoneName(), l.getP1(), l.getP2());
                        z.loadStructure(Bukkit.getWorld(l.getOrigin()));
                        z.show(cs, m);

                        DISABLED.put(zone, z); // Save the task
                    }
                } else {
                    MessageManager.sendPluginMessage(cs, "&cSyntax: /rtp zone show"
                            + " <name> <materialName|materialID>");
                }
                break;
            // </editor-fold>
            case "hide":
                // <editor-fold defaultstate="collapsed" desc="HIDE CODE">
                if (args.length > 1) {
                    String zone = args[1];

                    if (!isAvailable(zone)) {
                        MessageManager.sendPluginMessage(cs,
                                GameInfo.ZONE_IN_USE.getText(lang));
                        return true;
                    }

                    if (DISABLED.containsKey(zone)) {
                        DISABLED.get(zone).hide(cs);
                    } else {
                        MessageManager.sendPluginMessage(cs,
                                GameInfo.ZONE_NOT_ROLLBACK.getText(lang));
                    }
                } else {
                    MessageManager.sendPluginMessage(cs, "&cSyntax: /rtp zone hide"
                            + " <name>");
                }
                break;
            // </editor-fold>
            case "restoreall":
                //<editor-fold defaultstate="collapsed" desc="RESTORE ALL CODE">
                if (!DISABLED.isEmpty()) {
                    MessageManager.sendPluginMessage(cs, GameInfo.RESET_ZONES.getText(lang));
                    for (Map.Entry<String, ZoneBuilder> entry : DISABLED.entrySet()) {
                        ZoneBuilder zonebuilder = entry.getValue();
                        if (zonebuilder.isFinished()) {
                            zonebuilder.hide(cs);
                        } else {
                            MessageManager.sendPluginMessage(cs,
                                    GameInfo.ZONE_IN_USE.getText(lang));
                        }
                    }
                } else {
                    MessageManager.sendPluginMessage(cs,
                            GameInfo.NO_ZONES_TO_RESET.getText(lang));
                }
                break;
                //</editor-fold>
            default:
                return false;
        }

        return true;
    }

    private boolean isAvailable(String zone) {
        if (DISABLED.containsKey(zone)) {
            return DISABLED.get(zone).isFinished();
        }
        return true;
    }
}
