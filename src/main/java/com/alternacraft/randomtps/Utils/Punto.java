package com.alternacraft.randomtps.Utils;

/**
 *
 * @author Julián
 */
public class Punto {

    private int x;
    private int y;
    private int z;

    public Punto(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Punto(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }  

    /*
    private void setX(int x) {
        if (x < 0) {
            x--;
        }
        else {
            this.x = x;
        }
    }

    private void setY(int y) {
        if (y < 0) {
            y--;
        }
        else {        
            this.y = y;
        }
    }

    private void setZ(int z) {
        if (z < 0) {
            z--;
        }
        else {
            this.z = z;
        }
    }    
    */
}
