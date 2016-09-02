/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.alternacraft.randomtps.Utils;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 *
 * @author Julian
 */
public class Zona {
    
    protected Punto p1;
    protected Punto p2;

    private ArrayList<LocMat> bloques = new ArrayList();
    
    public Zona() {}
    
    public Zona(Punto p1, Punto p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Punto getP1() {
        return p1;
    }

    public void setP1(Punto p1) {
        this.p1 = p1;
    }

    public Punto getP2() {
        return p2;
    }

    public void setP2(Punto p2) {
        this.p2 = p2;
    }
    
    public Location visitarZona(World mundo) {
        double x,y,z;

        x = (p1.getX() + p2.getX()) / 2;
        y = (p1.getY() + p2.getY()) / 2;
        
        // Uso la coord 'z' por problemas con la orientacion
        if (p1.getZ() <= p2.getZ()) {
            z = p1.getZ()-5;
        }
        else {
            z = p2.getZ()-5;
        }
        
        return new Location(mundo,x,y,z);
    }
    
    public Location locAl(World mundo) {
        int pos1, pos2, pos3;     
        
        if (p1.getX()>p2.getX()) {
            pos1 = GenAleatorio.genAl(p1.getX(), p2.getX());
        }
        else if (p1.getX()==p2.getX()) {
            pos1 = p1.getX();
        }
        else {
            pos1 = GenAleatorio.genAl(p2.getX(), p1.getX());
        }
        
        pos2 = 2;
        
        if (p1.getZ()>p2.getZ()) {
            pos3 = GenAleatorio.genAl(p1.getZ(), p2.getZ());
        }
        else if (p1.getZ()==p2.getZ()) {
            pos3 = p1.getZ();
        }        
        else {
            pos3 = GenAleatorio.genAl(p2.getZ(), p1.getZ());
        }
        
        return new Location(mundo, pos1, pos2, pos3);
    }
    
    public boolean compLoc(World mundo) {
        int x,z;
        int minx,minz;
        
        if (p1.getX()>=p2.getX()) {
            x = p1.getX();
            minx = p2.getX();
        }
        else {
            x = p2.getX();
            minx = p1.getX();
        }
        if (p1.getZ()>=p2.getZ()) {
            z = p1.getZ();
            minz = p2.getZ();
        }
        else {
            z = p2.getZ();
            minz = p1.getZ();
        }

        for (int i = minx; i <= x; i++) {
            for (int j = 0; j <= 65; j++) {
                for (int k = minz; k <= z; k++) {                                     
                    Location l = new Location(mundo, (i < 0) ? (i-1) : i, j, (k < 0) ? (k-1) : k);
                    if (l.getBlock().isLiquid()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void generaBlocks(World mundo) {
        int x,y,z;
        int minx,miny,minz;
        
        if (p1.getX()>=p2.getX()) {
            x = p1.getX();
            minx = p2.getX();
        }
        else {
            x = p2.getX();
            minx = p1.getX();
        }
        if (p1.getY()>=p2.getY()) {
            y = p1.getY();
            miny = p2.getY();
        }
        else {
            y = p2.getY();
            miny = p1.getY();
        }
        if (p1.getZ()>=p2.getZ()) {
            z = p1.getZ();
            minz = p2.getZ();
        }
        else {
            z = p2.getZ();
            minz = p1.getZ();
        }
        
        // Parte de arriba y de abajo
        for (int i = minx; i <= x; i++) {
            for (int j = miny; j <= y; j++) {
                for (int k = minz; k <= z; k++) {
                    if ((i == minx || i == x) && (j == miny || j == y) && (k == minz || k == z) || // Esquinas
                            (i == minx || i == x) && (j == miny || j == y) || // ambos
                            (k == minz || k == z) && (j == miny || j == y) || // ambos
                            (j > miny && j < y) && (i == minx || i == x) && (k == minz || k == z)) { // Laterales
                        Location l = new Location(mundo, (i < 0) ? (i-1) : i, j, (k < 0) ? (k-1) : k);
                        Material b = l.getBlock().getType();
                        bloques.add(new LocMat(l,b));
                        l.getBlock().setType(Material.GLOWSTONE);
                    }
                }   
            }
        }
    }
    
    public void devuelveBlocks() {
        for (int i = 0; i < bloques.size(); i++) {
            bloques.get(i).getL().getBlock().setType(bloques.get(i).getM());
        }
    }
}
