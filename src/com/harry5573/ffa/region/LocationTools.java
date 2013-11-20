/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.ffa.region;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;

/**
 *
 * @author Harry5573
 */
public class LocationTools {

    public static BlockVector toVector(Block block) {
        return new BlockVector(block.getX(), block.getY(), block.getZ());
    }

    public static BlockVector toVector(Location vec) {
        if (vec == null) {
            return null;
        }
        return new BlockVector(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
    }
}
