/*Copyright (C) Harry5573 2013-14

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package com.harry5573.ffa.utilitys;

/**
 *
 * @author Harry5573
 */
public class SpawnData {

    private Double x;
    private Double y;
    private Double z;
    private float yaw;
    
    /**
     * Sets x to the value
     * @param value 
     */
    public void setX(Double value) {
        this.x = value;
    }

    /**
     * Sets y to the value
     * @param value 
     */
    public void setY(Double value) {
        this.y = value;
    }

    /**
     * Sets z to the value
     * @param value 
     */
    public void setZ(Double value) {
        this.z = value;
    }

    /**
     * Sets the yaw to the value
     * @param value 
     */
    public void setYaw(float value) {
        this.yaw = value;
    }
    
    /**
     * Returns the x value
     * @return 
     */
    public Double getX() {
        return this.x;
    }
    
    /**
     * Returns the y value
     * @return 
     */
    public Double getY() {
        return this.y;
    }
    
    /**
     * Returns the z value
     * @return 
     */
    public Double getZ() {
        return this.z;
    }
    
    /**
     * Returns the yaw
     * @return 
     */
    public float getYaw() {
        return this.yaw;
    }
}
