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
package com.alternacraft.randomtps.Utils;

import com.alternacraft.aclib.langs.Lang;
import com.alternacraft.randomtps.Listeners.HandleTeleport.CollisionEffect;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration data.
 * 
 * @author AlternaCraft
 */
public class ConfigDataStore {

    // General
    protected String pluginPrefix = "";
    
    protected boolean metrics = true;
    
    protected boolean alert = true;
    protected boolean update = true;
    
    protected short errorFormat = 2;
    
    // Langs
    protected Lang defaultLang = Lang.EN;
    
    // Selection
    protected String selection = "add";
    protected String cancel = "cancel";    
    
    // Build
    protected boolean instant = true;

    // Limits
    public static final short MAX = 0;
    public static final short MIN = 1;

    protected int[] x = {10000, -5000};
    protected int y = 150;
    protected int[] z = {10000, -5000};

    // Default extras
    protected CollisionEffect coll = CollisionEffect.SLOW_DOWN;
    protected int time = 10;
    protected boolean broadcast_as_exp = true;
    protected List<String> potions_effects = new ArrayList();
    protected List<String> validations = new ArrayList();
    
    public String getPluginPrefix() {
        return pluginPrefix;
    }

    public void setPluginPrefix(String pluginPrefix) {
        this.pluginPrefix = pluginPrefix;
    }

    public boolean isMetrics() {
        return metrics;
    }

    public void setMetrics(boolean metrics) {
        this.metrics = metrics;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public short getErrorFormat() {
        return errorFormat;
    }

    public void setErrorFormat(short errorFormat) {
        this.errorFormat = errorFormat;
    }

    public Lang getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(Lang defaultLang) {
        this.defaultLang = defaultLang;
    }
    
    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public boolean doInstantly() {
        return instant;
    }

    public void setInstant(boolean instant) {
        this.instant = instant;
    }

    public int[] getX() {
        return x;
    }

    public int getMaxX() {
        return x[MAX];
    }

    public int getMinX() {
        return x[MIN];
    }

    public void setX(int[] x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[] getZ() {
        return z;
    }

    public int getMaxZ() {
        return z[MAX];
    }

    public int getMinZ() {
        return z[MIN];
    }

    public void setZ(int[] z) {
        this.z = z;
    }

    public CollisionEffect getCollisionEffect() {
        return coll;
    }

    public void setCollisionEffect(String coll) {
        if (CollisionEffect.valueOf(coll) != null) {
            this.coll = CollisionEffect.valueOf(coll);
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isBroadcast_as_exp() {
        return broadcast_as_exp;
    }

    public void setBroadcast_as_exp(boolean broadcast_as_exp) {
        this.broadcast_as_exp = broadcast_as_exp;
    }

    public List<String> getPotions_effects() {
        return potions_effects;
    }

    public void setPotions_effects(List<String> potions_effects) {
        this.potions_effects = potions_effects;
    }

    public List<String> getValidations() {
        return validations;
    }

    public void setValidations(List<String> validations) {
        this.validations = validations;
    }    
}
