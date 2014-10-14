package net.amunak.bukkit.awesomedmsgs;

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
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Amunak
 */
public class PlayerDeathListener implements Listener {

    protected AwesomeDeathMessages plugin;

    public PlayerDeathListener(AwesomeDeathMessages p) {
        plugin = p;
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onPlayerDeathLP(PlayerDeathEvent e) {
        if (plugin.getConfig().getString("options.defaultMessageMode").equalsIgnoreCase("remove")) {
            e.setDeathMessage(null);
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerDeathHP(PlayerDeathEvent e) {
        if (plugin.getConfig().getString("options.defaultMessageMode").equalsIgnoreCase("forceRemove")) {
            e.setDeathMessage(null);
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void onPlayerDeathMO(PlayerDeathEvent e) {
        Player killer;
        if (plugin.getConfig().getBoolean("options.awesomeDeathMessages") && (killer = (Player) e.getEntity().getLastDamageCause().getEntity()) instanceof Player) {
            FancyMessage m = new FancyMessage();
            m.then(e.getEntity().getDisplayName());
            m.then(" got killed by ");
            m.then(killer.getDisplayName());
            m.then(" with ");
            ItemStack i = killer.getItemInHand();
            m.text((i.hasItemMeta() && i.getItemMeta().hasDisplayName()) ? i.getItemMeta().getDisplayName() : i.getType().name().replace("_", " ").toLowerCase()).color(ChatColor.RED).itemTooltip(i);
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                m.send(p);
            }
        }
    }

}
