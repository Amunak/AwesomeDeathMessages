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
import org.bukkit.plugin.java.JavaPlugin;

public class AwesomeDeathMessages extends JavaPlugin {

    @Override
    public void onEnable() {
        // Config shenanigans
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        reloadConfig();

        // Detect legacy config
        if (getConfig().isBoolean("options.awesomeDeathMessages")) {
            getLogger().warning("options.awesomeDeathMessages is no longer a boolean! Review your config please.");
            getLogger().info("options.awesomeDeathMessages: defaulting to 'classic'");
            getConfig().set("options.awesomeDeathMessages", "classic");
        }

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
    }

}
