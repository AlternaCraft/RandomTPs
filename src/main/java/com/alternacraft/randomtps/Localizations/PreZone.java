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
package com.alternacraft.randomtps.Localizations;

/**
 * Zone incomplete.
 * 
 * @author AlternaCraft
 */
public class PreZone {

    private boolean coord1 = false;
    private boolean coord2 = false;
    
    private boolean subzone = false;
    
    private boolean askWorld = false;    
    private boolean askSubzone = false;

    public boolean coord1Saved() {
        return coord1;
    }

    public void setCoord1(boolean coord1) {
        this.coord1 = coord1;
    }

    public boolean coord2Saved() {
        return coord2;
    }

    public void setCoord2(boolean coord2) {
        this.coord2 = coord2;
    }

    public void setSubzone(boolean b) {
        this.subzone = b;
    }
    
    public boolean isSubzone() {
        return this.subzone;
    }
    
    public boolean askWorld() {
        return askWorld;
    }

    public void askWorld(boolean world) {
        this.askWorld = world;
    }

    public boolean askSubzone() {
        return askSubzone;
    }

    public void askSubzone(boolean subzone) {
        this.askSubzone = subzone;
    }
    
    public void resetValues() {
        this.coord1 = false;
        this.coord2 = false;
    }
}
