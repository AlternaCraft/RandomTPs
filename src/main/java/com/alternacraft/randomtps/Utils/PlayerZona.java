/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.alternacraft.randomtps.Utils;

import org.bukkit.entity.Player;

/**
 *
 * @author Julian
 */
public class PlayerZona {
    
    private String zona;
    private Player j;

    public PlayerZona(String zona, Player j) {
        this.zona = zona;
        this.j = j;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Player getJ() {
        return j;
    }

    public void setJ(Player j) {
        this.j = j;
    }    
}
