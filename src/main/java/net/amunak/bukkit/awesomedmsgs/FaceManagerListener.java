package net.amunak.bukkit.awesomedmsgs;

import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Copyright 2014 Jiří Barouš
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
public class FaceManagerListener implements Listener {

    protected final AwesomeDeathMessages plugin;

    public FaceManagerListener(AwesomeDeathMessages p) {
        plugin = p;
    }

    /**
     * Invokes the face caching mechanism
     * <p>
     * The cache request is ran asynchronously.</p>
     *
     * @param e the event
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent e) {
        // Let's have a class in a method in a classs. What could go wrong?
        class CacheTask implements Runnable {

            private final UUID playerID;

            public CacheTask(UUID playerID) {
                this.playerID = playerID;
            }

            @Override
            public void run() {
                plugin.getFaceManager().cacheFace(plugin.getServer().getPlayer(playerID));
            }
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new CacheTask(e.getPlayer().getUniqueId()));
    }
}
