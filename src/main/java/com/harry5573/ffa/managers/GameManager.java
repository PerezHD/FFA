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
package com.harry5573.ffa.managers;

import com.harry5573.ffa.FreeForAll;

/**
 *
 * @author Harry5573
 */
public class GameManager {

      FreeForAll plugin;

      public GameManager(FreeForAll instance) {
            this.plugin = instance;
      }

      /**
       * Returns how many players are in FFA
       *
       * @return
       */
      public int getAmountPlayersInFFA() {
            return plugin.playerInFFA.size();
      }
}
