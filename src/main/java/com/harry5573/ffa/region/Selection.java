/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.harry5573.ffa.region;

import org.bukkit.util.BlockVector;

/**
 *
 * @author Harry5573
 */
public class Selection {

      private static BlockVector p1;
      private static BlockVector p2;

      public Selection() {
      }

      public static void setP1(BlockVector loc) {
            Selection.p1 = loc;
      }

      public static void setP2(BlockVector loc) {
            Selection.p2 = loc;
      }

      public static BlockVector getP1() {
            return p1;
      }

      public static BlockVector getP2() {
            return p2;
      }
}
