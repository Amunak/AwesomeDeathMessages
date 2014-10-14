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
import java.util.ArrayList;
import java.util.Random;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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
        if (plugin.getConfig().getBoolean("options.awesomeDeathMessages") && (killer = (Player) e.getEntity().getKiller()) instanceof Player) {
            FancyMessage m = new FancyMessage();
            m.text(e.getEntity().getDisplayName());
            m.then(" was slain by ");
            m.then(killer.getDisplayName());
            m.then(" using ");
            if (killer.getItemInHand().getType().equals(Material.AIR)) {
                m.then("only his ");
                m.then("bare hands").color(ChatColor.AQUA);
                m.then("!");
            } else {
                if (killer.getItemInHand().hasItemMeta()) {
                    if (killer.getItemInHand().getItemMeta().hasDisplayName()) {
                        m.then(killer.getItemInHand().getItemMeta().getDisplayName()).style(ChatColor.ITALIC, ChatColor.UNDERLINE).color(ChatColor.AQUA);
                    } else {
                        if (killer.getItemInHand().getItemMeta().hasEnchants()) {
                            m.then("enchanted ");
                        } else {
                            m.then(new Random().nextBoolean() ? "plain " : "ordinary ");
                        }
                        m.then(killer.getItemInHand().getType().toString().replace("_", " ").toLowerCase()).style(ChatColor.UNDERLINE).color(ChatColor.AQUA);
                    }
                    m.itemTooltip(killer.getItemInHand());
                } else {
                    ArrayList<String> words = new ArrayList<>();
                    words.add("his ");
                    words.add("good old ");
                    words.add("the precious ");
                    words.add("the only ");
                    words.add("just a ");
                    words.add("the powerful ");
                    words.add("");
                    m.then(words.get(new Random().nextInt(words.size())));

                    m.then(killer.getItemInHand().getType().toString().replace("_", " ").toLowerCase()).color(ChatColor.AQUA);
                }
            }
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                m.send(p);
            }
        }
    }

}
