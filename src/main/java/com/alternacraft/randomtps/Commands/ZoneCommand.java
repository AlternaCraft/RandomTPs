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

import com.alternacraft.aclib.MessageManager;
import org.bukkit.command.CommandSender;
import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.NumbersUtils;
import com.alternacraft.randomtps.Events.DefineZoneEvent;
import com.alternacraft.randomtps.Langs.DefineInfo;
import com.alternacraft.randomtps.Langs.GameInfo;
import com.alternacraft.randomtps.Langs.GeneralInfo;
import com.alternacraft.randomtps.Listeners.HandleBuild;
import static com.alternacraft.randomtps.Listeners.HandleBuild.DISABLED;
import com.alternacraft.randomtps.Main.Manager;
import com.alternacraft.randomtps.Utils.Localization;
import com.alternacraft.randomtps.Utils.ZoneBuilder;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.Map;

public class ZoneCommand implements ArgumentExecutor {

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        if (args.length <= 1) {
            return false;
        }

        Langs lang = Localizer.getLocale(cs);
        String cmd = args[1];

        switch (cmd) {
            case "list":
                // <editor-fold defaultstate="collapsed" desc="LIST CODE">
                cs.sendMessage("");
                MessageManager.sendCommandSender(cs, "");
                cs.sendMessage(ChatColor.GRAY + "  ---------");
                cs.sendMessage(ChatColor.BLUE + "   Zone list");
                cs.sendMessage(ChatColor.GRAY + "  ---------");

                List<Localization> locs = Manager.INSTANCE.getLocalizations();
                for (Localization loc : locs) {
                    String zonename = "  - " + loc.getZoneName();
                    if (cs instanceof Player) {
                        TextComponent tc = new TextComponent();
                        tc.setText(zonename);
                        tc.setItalic(Boolean.TRUE);
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/rt zone go " + loc.getZoneName()));
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
                if (args.length > 2) {
                    String zone = args[2];
                    if (Manager.INSTANCE.localizationExits(zone)) {
                        Manager.INSTANCE.disableLocation(zone);
                        MessageManager.sendCommandSender(cs, 
                                GameInfo.ZONE_DISABLED.getText(lang));
                    }
                    else {
                        Manager.INSTANCE.enableLocation(zone);
                        MessageManager.sendCommandSender(cs,
                                GameInfo.ZONE_ENABLED.getText(lang));
                    }
                } else {
                    MessageManager.sendCommandSender(cs,
                            "&cSyntax: /rt zone toggle <name>");
                }
                break;
                //</editor-fold>                
            case "go":
                // <editor-fold defaultstate="collapsed" desc="GO CODE">
                if (cs instanceof Player) {
                    Player player = (Player) cs;

                    if (args.length > 2) {
                        Localization loc = Manager.INSTANCE.getLocalizationByName(args[2]);

                        if (loc != null) {
                            player.setGameMode(GameMode.CREATIVE);
                            player.setFlying(Boolean.TRUE);

                            player.teleport(loc.getVisitLocation(Bukkit.getWorld(loc.getOrigin())));
                            MessageManager.sendPlayer(player,
                                    GameInfo.ZONE_GO_FINISHED.getText(lang));
                        } else {
                            MessageManager.sendCommandSender(cs,
                                    GameInfo.ZONE_NOT_EXISTS.getText(lang));
                        }
                    } else {
                        MessageManager.sendCommandSender(cs,
                                "&cSyntax: /rt zone go <name>");
                    }
                } else {
                    MessageManager.sendCommandSender(cs,
                            GeneralInfo.COMMAND_FORBIDDEN.getText(lang));
                }
                break;
            // </editor-fold>
            case "create":
                // <editor-fold defaultstate="collapsed" desc="DEFINE CODE">
                if (cs instanceof Player) {
                    Player player = (Player) cs;

                    if (args.length > 2) {
                        String zone = args[2];

                        boolean exists = Manager.INSTANCE.localizationExits(zone);

                        if (exists) {
                            HandleBuild.DISABLED.put(zone, null);
                            MessageManager.sendCommandSender(cs,
                                    DefineInfo.REDEFINING.getText(lang));
                        }
                        
                        Bukkit.getServer().getPluginManager().callEvent(
                                new DefineZoneEvent(player, zone));
                    } else {
                        MessageManager.sendCommandSender(cs,
                                "&cSyntax: /rt zone create <name> [redefine]");
                    }
                } else {
                    MessageManager.sendCommandSender(cs,
                            GeneralInfo.COMMAND_FORBIDDEN.getText(lang));
                }
                break;
            // </editor-fold>
            case "show":
                // <editor-fold defaultstate="collapsed" desc="SHOW CODE">
                if (args.length > 2) {
                    String zone = args[2];

                    if (!isAvailable(zone) || DISABLED.containsKey(zone)) {
                        if (!isAvailable(zone)) {
                            MessageManager.sendCommandSender(cs,
                                    GameInfo.ZONE_IN_USE.getText(lang));
                        } else {
                            MessageManager.sendCommandSender(cs,
                                    GameInfo.ZONE_ALREADY_BUILDED.getText(lang));
                        }
                        return true;
                    }

                    String material = "glowstone"; // Default
                    if (args.length > 3) {
                        material = args[3];
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
                        MessageManager.sendCommandSender(cs, "&cYou have to indicate "
                                + "a valid material (materialName|materialID)");
                    } else {
                        Localization l = Manager.INSTANCE.getLocalizationByName(zone);

                        if (l == null) {
                            MessageManager.sendCommandSender(cs,
                                    GameInfo.ZONE_NOT_EXISTS.getText(lang));
                            return true;
                        }

                        ZoneBuilder z = new ZoneBuilder(l.getZoneName(), l.getP1(), l.getP2());
                        z.loadStructure(Bukkit.getWorld(l.getOrigin()));
                        z.show(cs, m);

                        DISABLED.put(zone, z); // Save the task
                    }
                } else {
                    MessageManager.sendCommandSender(cs, "&cSyntax: /rt zone show"
                            + " <name> <materialName|materialID>");
                }
                break;
            // </editor-fold>
            case "hide":
                // <editor-fold defaultstate="collapsed" desc="HIDE CODE">
                if (args.length > 2) {
                    String zone = args[2];

                    if (!isAvailable(zone)) {
                        MessageManager.sendCommandSender(cs,
                                GameInfo.ZONE_IN_USE.getText(lang));
                        return true;
                    }

                    if (DISABLED.containsKey(zone)) {
                        DISABLED.get(zone).hide(cs);
                    } else {
                        MessageManager.sendCommandSender(cs,
                                GameInfo.ZONE_NOT_ROLLBACK.getText(lang));
                    }
                } else {
                    MessageManager.sendCommandSender(cs, "&cSyntax: /rt zone hide"
                            + " <name>");
                }
                break;
            // </editor-fold>
            case "restoreall":
                //<editor-fold defaultstate="collapsed" desc="RESTORE ALL CODE">
                if (!DISABLED.isEmpty()) {
                    MessageManager.sendCommandSender(cs, GameInfo.RESET_ZONES.getText(lang));
                    for (Map.Entry<String, ZoneBuilder> entry : DISABLED.entrySet()) {
                        ZoneBuilder zonebuilder = entry.getValue();
                        if (zonebuilder.isFinished()) {
                            zonebuilder.hide(cs);
                        } else {
                            MessageManager.sendCommandSender(cs,
                                    GameInfo.ZONE_IN_USE.getText(lang));
                        }
                    }
                } else {
                    MessageManager.sendCommandSender(cs,
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
