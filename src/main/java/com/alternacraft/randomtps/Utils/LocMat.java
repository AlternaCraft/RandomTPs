/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.alternacraft.randomtps.Utils;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author Julian
 */
public class LocMat {
    
    private Location l;
    private Material m;

    public LocMat(Location l, Material m) {
        this.l = l;
        this.m = m;
    }

    public Location getL() {
        return l;
    }

    public void setL(Location l) {
        this.l = l;
    }

    public Material getM() {
        return m;
    }

    public void setM(Material m) {
        this.m = m;
    }
}
