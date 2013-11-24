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
import com.harry5573.ffa.utilitys.MessageUtil;

/**
 *
 * @author Harry5573
 */
public class MessageManager {

    FreeForAll plugin;
    
    public MessageManager(FreeForAll instance) {
        this.plugin = instance;
    }
    
    public enum MessageType {
        PREFIX, MONEYGIVE, WARMUP, BROADCASTJOIN, BROADCASTKILLSTREAK, THANKS, PREJOIN, OLDSAVE, KITGIVE
    }
    
    /**
     * Loads all the needed messages
     */
    public void load() {
        plugin.messages.put(MessageType.PREFIX, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.prefix")));
        plugin.messages.put(MessageType.MONEYGIVE, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.moneygive")));
        plugin.messages.put(MessageType.WARMUP, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.warmup")));
        plugin.messages.put(MessageType.BROADCASTJOIN, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.broadcastjoin")));
        plugin.messages.put(MessageType.BROADCASTKILLSTREAK, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.broadcastkillstreak")));
        plugin.messages.put(MessageType.THANKS, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.thanks")));
        plugin.messages.put(MessageType.PREJOIN, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.prejoin")));
        plugin.messages.put(MessageType.OLDSAVE, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.oldsave")));
        plugin.messages.put(MessageType.KITGIVE, MessageUtil.translateToColorCode(plugin.cfManager.getMessagesConfig().getString("messages.kitgive")));
    }

    /**
     * Returns the plugins prefix from messages.yml
     *
     * @return
     */
    public String getPrefix() {
        return plugin.messages.get(MessageType.PREFIX);
    }
}
